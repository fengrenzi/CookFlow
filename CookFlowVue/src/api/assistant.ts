import request from '@/utils/request';

// 发送对话消息
export function sendMessage(data: { content: string; sessionKey?: string; recipeId?: string }) {
  return request({
    url: '/dialogue/send',
    method: 'post',
    data
  });
}

// 获取历史消息（可选）
export function getConversation(sessionKey: string) {
  return request({
    url: `/dialogue/conversations/${sessionKey}`,
    method: 'get'
  });
}

// 语音识别（直接发送音频二进制）
export function recognizeVoice(audioBlob: Blob) {
  return request({
    url: '/voice/recognize',
    method: 'post',
    data: audioBlob,
    headers: {
      'Content-Type': 'audio/wav'   // 关键：告诉后端这是 wav 音频
    },
    timeout: 60000  // 60秒超时
  });
}

// 选择菜谱
export function selectRecipe(sessionId: string, recipeId: number) {
  return request({
    url: '/dialogue/select-recipe',
    method: 'post',
    params: { sessionId, recipeId }
  });
}