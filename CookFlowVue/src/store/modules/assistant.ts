import { defineStore } from 'pinia';
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';

// 消息接口
export interface Message {
  role: 'user' | 'assistant';
  content: string;
  time: Date;
}

// 菜谱接口
export interface Recipe {
  id: string;
  name: string;
  author?: string;
  isOfficial: boolean;
}

// 弹幕接口
export interface Danmaku {
  id: number;
  content: string;
  style: any;
}

export const useAssistantStore = defineStore('assistant', () => {
  const router = useRouter();
  
  // 状态定义
  const messages = ref<Message[]>([]);
  const currentRecipe = ref<Recipe | null>(null);
  const isTyping = ref(false);
  const hasCookingError = ref(false);
  const danmakus = ref<Danmaku[]>([]);
  const danmakuIdCounter = ref(0);
  const isDanmakuPlaying = ref(false);
  let danmakuInterval: number | null = null;

  // 模拟AI回复
  const generateAssistantResponse = async (content: string) => {
    isTyping.value = true;
    
    try {
      // 实际应用中，这里应该调用API获取AI回复
      // const response = await api.getAssistantResponse(content, currentRecipe.value?.id);
      
      // 模拟思考延迟
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      let response = '';
      
      // 检查是否为菜谱编号
      const recipeIdMatch = content.match(/^#?(\d+)$/);
      if (recipeIdMatch) {
        const recipeId = recipeIdMatch[1] || '';
        
        // 实际应用中，这里应该调用API获取菜谱信息
        // const recipeData = await api.getRecipeById(recipeId);
        
        currentRecipe.value = {
          id: recipeId,
          name: `示例菜谱 ${recipeId}`,
          isOfficial: true
        };
        response = `已为您加载菜谱 #${recipeId}。您可以询问关于这道菜的烹饪技巧、食材替换或烹饪时间等问题。`;
      } else if (content.includes('错误') || content.includes('失败') || content.includes('不对')) {
        hasCookingError.value = true;
        response = '我理解您遇到了烹饪问题。请详细描述您的情况，包括您使用的食材、步骤和具体遇到的问题，我会尽力帮助您解决。';
      } else if (content.includes('作者') || content.includes('来源')) {
        if (!currentRecipe.value) {
          response = '请先输入一个菜谱编号或告诉我您想了解的具体菜谱。';
        } else if (currentRecipe.value.isOfficial) {
          response = `${currentRecipe.value.name} 是我们的官方菜谱，由专业厨师团队精心研发。`;
        } else {
          response = `${currentRecipe.value.name} 是由用户 ${currentRecipe.value.author} 分享的精彩创意。`;
        }
      } else {
        // 一般烹饪问题回复
        response = '感谢您的提问！作为您的烹饪助手，我很乐意帮助您解决烹饪中的各种问题。如果您有具体的步骤或食材问题，请详细描述，我会提供更精准的建议。';
        
        // 随机生成一些烹饪建议
        const suggestions = [
          '小贴士：烹饪时保持食材新鲜是关键。',
          '火候控制很重要，不同的烹饪方法需要调整火力。',
          '调味品的添加顺序会影响最终的味道。',
          '刀工技巧可以提升菜品的口感和美观度。',
          '尝试不同的香料组合可以创造独特的风味。'
        ];
        const randomSuggestion = suggestions[Math.floor(Math.random() * suggestions.length)];
        response += `\n\n${randomSuggestion}`;
      }
      
      // 模拟打字延迟
      await new Promise(resolve => setTimeout(resolve, 500));
      
      messages.value.push({
        role: 'assistant',
        content: response,
        time: new Date()
      });
    } catch (error) {
      console.error('生成助手回复失败:', error);
      ElMessage.error('获取回复失败，请稍后重试');
      
      messages.value.push({
        role: 'assistant',
        content: '抱歉，我暂时无法为您提供回复。请稍后再试。',
        time: new Date()
      });
    } finally {
      isTyping.value = false;
    }
  };

  // 发送消息
  const sendMessage = async (content: string) => {
    if (!content.trim() || isTyping.value) return;
    
    // 添加用户消息
    messages.value.push({
      role: 'user',
      content: content.trim(),
      time: new Date()
    });
    
    // 生成助手回复
    await generateAssistantResponse(content.trim());
  };

  // 发布错误到问答区
  const publishErrorToForum = () => {
    router.push('/forum/question');
    ElMessage.info('正在跳转到问答区...');
    hasCookingError.value = false;
  };

  // 模拟弹幕数据
  const mockDanmakus = [
    '这道菜真的很好吃！',
    '我昨天也做了这个，很成功！',
    '请问火候怎么控制？',
    '食材切多大比较合适？',
    '有没有替代食材推荐？',
    '这个步骤太关键了！',
    '学到了，谢谢分享！',
    '下次我也要试试！'
  ];

  // 生成随机颜色
  const getRandomColor = () => {
    const colors = ['#ff6b6b', '#4ecdc4', '#45b7d1', '#96ceb4', '#ffeaa7', '#dfe6e9', '#6c5ce7', '#a29bfe'];
    return colors[Math.floor(Math.random() * colors.length)];
  };

  // 创建弹幕
  const createDanmaku = (containerWidth: number, containerHeight: number) => {
    if (!isDanmakuPlaying.value) return;
    
    const randomIndex = Math.floor(Math.random() * mockDanmakus.length);
    const content = mockDanmakus[randomIndex] || '大家好！';
    const speed = 150 + Math.random() * 150;
    const top = Math.random() * (containerHeight - 30);
    const color = getRandomColor();
    const animationDuration = containerWidth / speed;
    
    const danmaku: Danmaku = {
      id: danmakuIdCounter.value++,
      content,
      style: {
        left: '0',
        top: `${top}px`,
        color,
        animationDuration: `${animationDuration}s`,
        animationPlayState: isDanmakuPlaying.value ? 'running' : 'paused',
        animationIterationCount: '1'
      }
    };
    
    danmakus.value.push(danmaku);
    
    // 弹幕消失后移除
    setTimeout(() => {
      danmakus.value = danmakus.value.filter(d => d.id !== danmaku.id);
    }, animationDuration * 1000 + 500);
  };

  // 切换弹幕播放状态
  const toggleDanmaku = (containerWidth: number, containerHeight: number) => {
    isDanmakuPlaying.value = !isDanmakuPlaying.value;
    
    // 控制定时器
    if (isDanmakuPlaying.value && !danmakuInterval) {
      danmakuInterval = window.setInterval(() => createDanmaku(containerWidth, containerHeight), 2000);
    } else if (!isDanmakuPlaying.value && danmakuInterval) {
      clearInterval(danmakuInterval);
      danmakuInterval = null;
      
      // 立即清空所有弹幕
      danmakus.value = [];
    }
  };

  // 清除所有弹幕
  const clearDanmakus = () => {
    danmakus.value = [];
  };

  // 格式化时间
  const formatTime = (date: Date) => {
    return date.toLocaleTimeString('zh-CN', {
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  // 初始化助手
  const initializeAssistant = () => {
    // 实际应用中，这里可以从后端加载历史消息记录
    // await api.loadChatHistory();
    messages.value = [];
    currentRecipe.value = null;
    hasCookingError.value = false;
  };

  // 组件卸载时清理
  const cleanup = () => {
    if (danmakuInterval) {
      clearInterval(danmakuInterval);
      danmakuInterval = null;
    }
  };

  return {
    // 状态
    messages,
    currentRecipe,
    isTyping,
    hasCookingError,
    danmakus,
    isDanmakuPlaying,
    
    // 方法
    sendMessage,
    publishErrorToForum,
    toggleDanmaku,
    clearDanmakus,
    formatTime,
    initializeAssistant,
    cleanup
  };
});