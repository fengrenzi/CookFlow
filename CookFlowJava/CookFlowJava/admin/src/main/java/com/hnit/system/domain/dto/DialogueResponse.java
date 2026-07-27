package com.hnit.system.domain.dto;

import lombok.Data;
import java.util.List;

@Data
public class DialogueResponse {
    private String reply; // 助手的文本回复
    private List<RecipeOption> recipes; // 如果有推荐菜谱，填充此列表
    private Boolean stepIncremented; // 是否推进了步骤（用于前端反馈）
    // 可扩展其他动作
}