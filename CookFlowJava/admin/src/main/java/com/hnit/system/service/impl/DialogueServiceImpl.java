package com.hnit.system.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.hnit.system.domain.ConversationMessage;
import com.hnit.system.domain.ConversationSession;
import com.hnit.system.domain.RecipeStep;
import com.hnit.system.domain.Recipes;
import com.hnit.system.domain.dto.DialogueRequest;
import com.hnit.system.domain.dto.DialogueResponse;
import com.hnit.system.domain.dto.RecipeOption;
import com.hnit.system.service.IDialogueService;
import com.hnit.system.service.IConversationStateService;
import com.hnit.system.service.IRecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DialogueServiceImpl implements IDialogueService {

    private static final Logger log = LoggerFactory.getLogger(DialogueServiceImpl.class);

    @Value("${dashscope.api-key}")
    private String apiKey;

    @Value("${dashscope.model}")
    private String model;

    @Value("${dashscope.timeout:30000}")
    private Integer timeout;

    @Resource
    private IConversationStateService stateService;

    @Resource
    private IRecipeService recipeService;

    @Override
    public DialogueResponse chat(DialogueRequest request) {
        // 1. 获取会话
        ConversationSession session = stateService.getOrCreateSession(request.getSessionId(), request.getUserId());

        // 2. 构建消息历史（过滤空内容）
        List<Map<String, String>> messagesMap = buildMessages(session, request.getText());
        if (messagesMap == null || messagesMap.isEmpty()) {
            log.error("构建的消息列表为空，sessionId: {}", session.getId());
            DialogueResponse errorResp = new DialogueResponse();
            errorResp.setReply("抱歉，系统内部错误，无法处理您的消息。");
            errorResp.setRecipes(new ArrayList<>());
            return errorResp;
        }

        // 3. 转换为 DashScope 消息格式
        List<Message> messages = convertToDashScopeMessages(messagesMap);
        if (messages == null || messages.isEmpty()) {
            log.error("转换后的 DashScope 消息列表为空");
            DialogueResponse errorResp = new DialogueResponse();
            errorResp.setReply("抱歉，系统内部错误，无法处理您的消息。");
            errorResp.setRecipes(new ArrayList<>());
            return errorResp;
        }

        // 4. 调用阿里云千问
        Generation gen = new Generation();
        GenerationParam param = GenerationParam.builder()
                .model(model)
                .messages(messages)
                .apiKey(apiKey)
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .temperature(0.7f)
                .build();

        String assistantReply;
        try {
            log.debug("调用千问API，模型: {}, 消息数量: {}", model, messages.size());
            GenerationResult result = gen.call(param);
            assistantReply = result.getOutput().getChoices().get(0).getMessage().getContent();
            log.debug("千问API调用成功，回复长度: {}", assistantReply.length());
        } catch (InputRequiredException e) {
            log.error("调用千问API失败：缺少必要参数", e);
            assistantReply = "AI服务参数配置错误，请联系管理员。";
        } catch (ApiException | NoApiKeyException e) {
            log.error("调用千问API失败", e);
            assistantReply = "抱歉，AI服务暂时不可用，请稍后再试。";
        }

        // 5. 解析动作（步骤推进/推荐菜谱）
        DialogueResponse dialogueRes = new DialogueResponse();
        dialogueRes.setReply(assistantReply);
        dialogueRes.setRecipes(new ArrayList<>());

        // 处理步骤推进
        if (assistantReply.contains("[ACTION:step_increment]")) {
            if (session.getCookingMode() && session.getCurrentRecipeId() != null) {
                List<RecipeStep> steps = recipeService.getSteps(session.getCurrentRecipeId());
                int nextIndex = session.getCurrentStepIndex() + 1;
                if (nextIndex < steps.size()) {
                    session.setCurrentStepIndex(nextIndex);
                    stateService.updateSession(session);
                    dialogueRes.setStepIncremented(true);
                } else {
                    // 完成所有步骤，退出烹饪模式
                    session.setCookingMode(false);
                    session.setCurrentRecipeId(null);
                    session.setCurrentStepIndex(0);
                    stateService.updateSession(session);
                }
            }
        }

        // 处理菜谱推荐
        if (assistantReply.contains("[ACTION:recommend:")) {
            int start = assistantReply.indexOf("[ACTION:recommend:") + 17;
            int end = assistantReply.indexOf("]", start);
            if (start > 0 && end > start) {
                String keywordsStr = assistantReply.substring(start, end);
                List<String> keywords = Arrays.asList(keywordsStr.split(","));
                List<RecipeOption> options = recipeService.searchRecipesByKeywords(keywords);
                dialogueRes.setRecipes(options);
            }
        }

        // 6. 保存消息
        stateService.saveMessages(session.getId(), request.getText(), assistantReply);

        return dialogueRes;
    }

    @Override
    public DialogueResponse selectRecipe(Long sessionId, Long recipeId) {
        // 注意：这里需要传入数据库会话ID，实际项目中应增加按ID查询会话的方法
        // 此处简化处理，通过 userId=null 方式获取会话，实际应根据业务调整
        ConversationSession session = stateService.getOrCreateSession(String.valueOf(sessionId), null);
        if (session == null) {
            DialogueResponse errorResp = new DialogueResponse();
            errorResp.setReply("会话不存在，请重新开始对话。");
            return errorResp;
        }
        session.setCurrentRecipeId(recipeId);
        session.setCookingMode(true);
        session.setCurrentStepIndex(0);
        stateService.updateSession(session);

        Recipes recipe = recipeService.getById(recipeId);
        if (recipe == null) {
            DialogueResponse errorResp = new DialogueResponse();
            errorResp.setReply("未找到该菜谱信息。");
            return errorResp;
        }
        String reply = "好的，我们来做 " + recipe.getTitle() + "。首先，请准备食材：" + recipe.getDescription() + "。接下来开始第一步。";
        DialogueResponse response = new DialogueResponse();
        response.setReply(reply);
        return response;
    }

    /**
     * 构建消息历史（系统提示 + 历史消息 + 当前用户消息）
     */
    private List<Map<String, String>> buildMessages(ConversationSession session, String userText) {
        List<Map<String, String>> messages = new ArrayList<>();

        // 系统消息
        Map<String, String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", buildSystemPrompt(session));
        messages.add(systemMsg);

        // 历史消息（最近10条，按时间正序）
        List<ConversationMessage> history = stateService.getRecentMessages(session.getId(), 10);
        // 由于查询是倒序，反转
        Collections.reverse(history);
        for (ConversationMessage msg : history) {
            if (msg.getContent() != null && !msg.getContent().trim().isEmpty()) {
                Map<String, String> historyMsg = new HashMap<>();
                historyMsg.put("role", msg.getRole());
                historyMsg.put("content", msg.getContent());
                messages.add(historyMsg);
            }
        }

        // 当前用户消息
        if (userText != null && !userText.trim().isEmpty()) {
            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", userText);
            messages.add(userMsg);
        }

        return messages;
    }

    /**
     * 构建系统提示词（包含当前菜谱和步骤上下文）
     */
    private String buildSystemPrompt(ConversationSession session) {
        StringBuilder sb = new StringBuilder();
        sb.append("你是烹饪助手，名字叫CookFlow。你的回答应该简洁、友好。");
        if (session.getCookingMode() && session.getCurrentRecipeId() != null) {
            Recipes recipe = recipeService.getById(session.getCurrentRecipeId());
            if (recipe != null) {
                sb.append("当前菜谱：").append(recipe.getTitle()).append("。");
            }
            List<RecipeStep> steps = recipeService.getSteps(session.getCurrentRecipeId());
            if (steps != null && !steps.isEmpty()) {
                sb.append("步骤列表：");
                for (int i = 0; i < steps.size(); i++) {
                    sb.append(i + 1).append("、").append(steps.get(i).getDescription()).append("；");
                }
                sb.append("当前进行到第").append(session.getCurrentStepIndex() + 1).append("步。");
                sb.append("如果用户说“下一步”，请在回答末尾添加 [ACTION:step_increment] 并推进步骤；如果用户提问，请解答后再继续当前步骤。");
                sb.append("当所有步骤完成后，恭喜用户并询问是否还需要帮助，然后退出烹饪模式。");
            } else {
                sb.append("当前菜谱没有详细步骤，请直接提供指导。");
            }
        } else {
            sb.append("你帮助用户选择菜谱，可以询问口味、食材、时间等，然后根据数据库推荐合适的菜谱。");
            sb.append("如果需要推荐菜谱，请在回答末尾添加 [ACTION:recommend:关键词1,关键词2] 格式。");
        }
        return sb.toString();
    }

    /**
     * 将内部消息格式转换为 DashScope 要求的 Message 列表
     */
    private List<Message> convertToDashScopeMessages(List<Map<String, String>> messagesMap) {
        List<Message> messages = new ArrayList<>();
        for (Map<String, String> m : messagesMap) {
            String role = m.get("role");
            String content = m.get("content");
            if (content == null || content.trim().isEmpty()) {
                continue; // 跳过空内容
            }
            if ("user".equals(role)) {
                messages.add(Message.builder().role(Role.USER.getValue()).content(content).build());
            } else if ("assistant".equals(role)) {
                messages.add(Message.builder().role(Role.ASSISTANT.getValue()).content(content).build());
            } else if ("system".equals(role)) {
                messages.add(Message.builder().role(Role.SYSTEM.getValue()).content(content).build());
            }
        }
        return messages;
    }
}