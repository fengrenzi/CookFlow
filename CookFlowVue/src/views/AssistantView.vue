<template>
  <div class="assistant-container">
    <!-- 对话区域 -->
    <div class="chat-container" ref="chatContainer">
      <!-- 系统提示 -->
      <div class="system-message" v-if="messages.length === 0">
        <p>👨‍🍳 欢迎使用烹饪助手！</p>
        <p>您可以：</p>
        <ul>
          <li>输入菜谱编号直接开始对话</li>
          <li>询问烹饪技巧和常见问题</li>
          <li>分享您的烹饪心得</li>
        </ul>
      </div>

      <!-- 当前菜谱 -->
      <div v-if="currentRecipe" class="current-recipe">
        <h3>当前查看菜谱</h3>
        <p><strong>{{ currentRecipe.name }}</strong></p>
        <p v-if="currentRecipe.isOfficial">官方菜谱</p>
        <p v-else>用户 {{ currentRecipe.author }} 分享</p>
      </div>

      <!-- 消息列表 -->
      <div class="message-list">
        <div
          v-for="(message, index) in messages"
          :key="index"
          :class="['message-item', message.role]"
        >
          <div class="message-avatar">
            {{ message.role === 'user' ? '👤' : '🤖' }}
          </div>
          <div class="message-content">
            <div v-html="formatMessageContent(message.content)"></div>
            <!-- 菜谱推荐卡片 -->
            <div v-if="message.recipes && message.recipes.length" class="recipe-cards">
              <div
                v-for="recipe in message.recipes"
                :key="recipe.id"
                class="recipe-card"
                @click="chooseRecipe(recipe.id)"
              >
                <img :src="recipe.imgUrl" alt="菜谱图片" class="recipe-img" />
                <div class="recipe-title">{{ recipe.title }}</div>
              </div>
            </div>
            <!-- 助手消息底部：时间和播放按钮 -->
            <div v-if="message.role === 'assistant'" class="message-footer">
              <span class="message-time">{{ formatTime(message.time) }}</span>
              <button
                class="voice-output-btn"
                @click="toggleSpeak(message)"
                :title="getSpeakButtonTitle(message)"
              >
                {{ getSpeakButtonIcon(message) }}
              </button>
            </div>
          </div>
          <!-- 用户消息时间单独显示在框外（右侧） -->
          <div v-if="message.role === 'user'" class="user-message-time">
            {{ formatTime(message.time) }}
          </div>
        </div>

        <!-- 打字动画 -->
        <div class="message-item assistant typing" v-if="isTyping">
          <div class="message-avatar">🤖</div>
          <div class="message-content">
            <div class="typing-indicator">
              <span></span>
              <span></span>
              <span></span>
            </div>
          </div>
        </div>
      </div>

      <!-- 弹幕区域 -->
      <div class="danmaku-container" ref="danmakuContainer">
        <div
          v-for="(danmaku, index) in danmakus"
          :key="index"
          class="danmaku"
          :style="danmaku.style"
        >
          {{ danmaku.content }}
        </div>
      </div>

      <!-- 弹幕控制按钮 -->
      <div class="danmaku-controls">
        <label class="switch">
          <input type="checkbox" :checked="isDanmakuPlaying" @change="toggleDanmaku" style="display: none;">
          <span class="slider"></span>
        </label>
        <span class="danmaku-label">弹幕</span>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="input-container">
      <div class="input-wrapper">
        <div class="input-container-with-icon">
          <input
            v-model="inputMessage"
            type="text"
            placeholder="输入您的问题或菜谱编号..."
            @keyup.enter="sendMessage"
            :disabled="isTyping"
          />
          <button
            @click="startVoiceInput"
            class="voice-btn-inside"
            :class="{ 'recording': isRecording }"
            :disabled="isTyping"
          >
            {{ isRecording ? '⏹️' : '🔊' }}
          </button>
        </div>
        <button @click="sendMessage" :disabled="isTyping || !inputMessage.trim()" class="send-btn">
          发送
        </button>
      </div>
      <div class="input-actions">
        <button @click="publishErrorToForum" class="error-btn" v-if="hasCookingError">
          📝 发布问题
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { sendMessage as sendMessageApi, selectRecipe } from '@/api/assistant';

const router = useRouter();

interface Message {
  role: 'user' | 'assistant';
  content: string;
  time: Date;
  recipes?: Array<{ id: number; title: string; imgUrl: string }>;
  isPlaying?: boolean;
  utterance?: SpeechSynthesisUtterance;
}

interface Danmaku {
  id: number;
  content: string;
  style: any;
}

const sessionId = ref(generateUUID());
const userId = ref(1); // 临时用户ID，实际应从登录获取

const messages = ref<Message[]>([]);
const currentRecipe = ref<{ id: string; name: string; author?: string; isOfficial: boolean } | null>(null);
const isTyping = ref(false);
const hasCookingError = ref(false);

// 弹幕状态
const danmakus = ref<Danmaku[]>([]);
const isDanmakuPlaying = ref(false);
let danmakuInterval: number | null = null;
let danmakuIdCounter = 0;
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

const chatContainer = ref<HTMLElement>();
const danmakuContainer = ref<HTMLElement>();
const inputMessage = ref('');

// 语音识别状态（Web Speech API）
const isRecording = ref(false);
let webSpeechRecognition: any = null;

// ========== 辅助函数 ==========
function generateUUID() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    const r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}

function formatTime(date: Date) {
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
}

function scrollToBottom() {
  nextTick(() => {
    if (chatContainer.value) {
      chatContainer.value.scrollTop = chatContainer.value.scrollHeight;
    }
  });
}

// 清理 Markdown 标记（仅用于显示和语音输出）
function cleanMarkdown(content: string): string {
  if (!content) return '';
  let cleaned = content
    .replace(/\*\*(.*?)\*\*/g, '$1')
    .replace(/__(.*?)__/g, '$1')
    .replace(/\*(.*?)\*/g, '$1')
    .replace(/_(.*?)_/g, '$1')
    .replace(/^###?\s*/gm, '')
    .replace(/^[\*\-]\s+/gm, '');
  return cleaned;
}

function formatMessageContent(content: string): string {
  if (!content) return '';
  const cleaned = cleanMarkdown(content);
  let escaped = cleaned
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;');
  escaped = escaped.replace(/\n/g, '<br>');
  return escaped;
}

// ========== 语音输出（支持播放/暂停） ==========
let currentPlayingMsg: Message | null = null;

function getSpeakButtonIcon(message: Message): string {
  return message.isPlaying ? '⏸️' : '🔊';
}

function getSpeakButtonTitle(message: Message): string {
  return message.isPlaying ? '暂停朗读' : '朗读';
}

function toggleSpeak(message: Message) {
  if (message.isPlaying) {
    if (message.utterance) {
      window.speechSynthesis.cancel();
      message.isPlaying = false;
    }
  } else {
    if (currentPlayingMsg && currentPlayingMsg !== message) {
      if (currentPlayingMsg.utterance) {
        window.speechSynthesis.cancel();
        currentPlayingMsg.isPlaying = false;
      }
    }
    const cleanText = cleanMarkdown(message.content);
    const utterance = new SpeechSynthesisUtterance(cleanText);
    utterance.lang = 'zh-CN';
    utterance.rate = 0.9;
    utterance.pitch = 1;
    utterance.onstart = () => {
      message.isPlaying = true;
      currentPlayingMsg = message;
    };
    utterance.onend = () => {
      message.isPlaying = false;
      if (currentPlayingMsg === message) currentPlayingMsg = null;
    };
    utterance.onerror = () => {
      console.warn('语音播放失败', message.content);
      message.isPlaying = false;
      if (currentPlayingMsg === message) currentPlayingMsg = null;
    };
    message.utterance = utterance;
    window.speechSynthesis.speak(utterance);
  }
}

// ========== 语音输入（Web Speech API） ==========
function startWebSpeech() {
  if (!('webkitSpeechRecognition' in window) && !('SpeechRecognition' in window)) {
    ElMessage.warning('您的浏览器不支持语音识别');
    return false;
  }
  const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
  webSpeechRecognition = new SpeechRecognition();
  webSpeechRecognition.lang = 'zh-CN';
  webSpeechRecognition.interimResults = true;
  webSpeechRecognition.continuous = true;

  webSpeechRecognition.onstart = () => {
    isRecording.value = true;
    console.log('[Web Speech] 录音开始');
  };

  webSpeechRecognition.onresult = (event: any) => {
    let interimTranscript = '';
    let finalTranscript = '';
    for (let i = event.resultIndex; i < event.results.length; i++) {
      const transcript = event.results[i][0].transcript;
      if (event.results[i].isFinal) {
        finalTranscript += transcript;
        console.log('[Web Speech] 最终结果:', transcript);
      } else {
        interimTranscript += transcript;
        console.log('[Web Speech] 中间结果:', transcript);
      }
    }
    const recognizedText = finalTranscript || interimTranscript;
    if (recognizedText) {
      inputMessage.value = recognizedText;
    }
  };

  webSpeechRecognition.onerror = (event: any) => {
    console.error('[Web Speech] 错误:', event.error);
    if (event.error === 'network') {
      ElMessage.warning('网络连接不稳定，请直接输入文字');
    } else if (event.error === 'not-allowed') {
      ElMessage.warning('未获取麦克风权限，请允许麦克风访问');
    } else if (event.error === 'no-speech') {
      ElMessage.warning('未检测到语音，请重试');
    } else {
      ElMessage.error(`语音识别失败：${event.error}`);
    }
    isRecording.value = false;
    if (webSpeechRecognition) {
      webSpeechRecognition.abort();
      webSpeechRecognition = null;
    }
  };

  webSpeechRecognition.onend = () => {
    console.log('[Web Speech] 录音结束');
    isRecording.value = false;
    webSpeechRecognition = null;
  };

  webSpeechRecognition.start();
  return true;
}

function startVoiceInput() {
  if (isRecording.value) {
    if (webSpeechRecognition) {
      webSpeechRecognition.stop();
    }
    return;
  }
  startWebSpeech();
}

// ========== 对话相关 ==========
async function sendMessage() {
  const text = inputMessage.value.trim();
  if (!text || isTyping.value) return;

  messages.value.push({
    role: 'user',
    content: text,
    time: new Date()
  });
  inputMessage.value = '';
  scrollToBottom();

  if (text.includes('错误') || text.includes('失败') || text.includes('不对')) {
    hasCookingError.value = true;
  }

  isTyping.value = true;

  try {
    const res = await sendMessageApi({
      sessionId: sessionId.value,
      userId: userId.value,
      text: text
    });

    const assistantMsg: Message = {
      role: 'assistant',
      content: res.reply,
      time: new Date(),
      isPlaying: false
    };
    if (res.recipes && res.recipes.length) {
      assistantMsg.recipes = res.recipes;
    }
    messages.value.push(assistantMsg);
    scrollToBottom();
  } catch (error) {
    console.error('发送消息失败', error);
    ElMessage.error('获取回复失败，请稍后重试');
    messages.value.push({
      role: 'assistant',
      content: '抱歉，我暂时无法为您提供回复。请稍后再试。',
      time: new Date(),
      isPlaying: false
    });
    scrollToBottom();
  } finally {
    isTyping.value = false;
  }
}

async function chooseRecipe(recipeId: number) {
  try {
    const res = await selectRecipe(sessionId.value, recipeId);
    messages.value.push({
      role: 'assistant',
      content: res.reply,
      time: new Date(),
      isPlaying: false
    });
    scrollToBottom();
  } catch (error) {
    console.error('选择菜谱失败', error);
    ElMessage.error('选择菜谱失败，请重试');
  }
}

function publishErrorToForum() {
  router.push('/forum/question');
  ElMessage.info('正在跳转到问答区...');
  hasCookingError.value = false;
}

// ========== 弹幕相关 ==========
function getRandomColor() {
  const colors = ['#ff6b6b', '#4ecdc4', '#45b7d1', '#96ceb4', '#ffeaa7', '#dfe6e9', '#6c5ce7', '#a29bfe'];
  return colors[Math.floor(Math.random() * colors.length)];
}

function createDanmaku() {
  if (!isDanmakuPlaying.value || !danmakuContainer.value) return;
  const containerWidth = danmakuContainer.value.offsetWidth;
  const containerHeight = danmakuContainer.value.offsetHeight;
  const randomIndex = Math.floor(Math.random() * mockDanmakus.length);
  const content = mockDanmakus[randomIndex];
  const speed = 150 + Math.random() * 150;
  const top = Math.random() * (containerHeight - 30);
  const color = getRandomColor();
  const animationDuration = containerWidth / speed;
  const danmaku: Danmaku = {
    id: danmakuIdCounter++,
    content,
    style: {
      left: '0',
      top: `${top}px`,
      color,
      animationDuration: `${animationDuration}s`,
      animationPlayState: 'running',
      animationIterationCount: '1'
    }
  };
  danmakus.value.push(danmaku);
  setTimeout(() => {
    danmakus.value = danmakus.value.filter(d => d.id !== danmaku.id);
  }, animationDuration * 1000 + 500);
}

function toggleDanmaku() {
  isDanmakuPlaying.value = !isDanmakuPlaying.value;
  if (isDanmakuPlaying.value && !danmakuInterval) {
    danmakuInterval = window.setInterval(createDanmaku, 2000);
  } else if (!isDanmakuPlaying.value && danmakuInterval) {
    clearInterval(danmakuInterval);
    danmakuInterval = null;
    danmakus.value = [];
  }
}

function clearDanmakus() {
  danmakus.value = [];
}

// ========== 生命周期 ==========
function handleResize() {
  clearDanmakus();
}

onMounted(() => {
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  if (danmakuInterval) {
    clearInterval(danmakuInterval);
    danmakuInterval = null;
  }
  if (webSpeechRecognition) {
    webSpeechRecognition.abort();
  }
  window.speechSynthesis.cancel();
  window.removeEventListener('resize', handleResize);
});
</script>

<style scoped>
@import '@/styles/components/AssistantView.css';

/* 自定义滚动条 */
.chat-container::-webkit-scrollbar {
  width: 6px;
  background-color: #f5f5f5;
}
.chat-container::-webkit-scrollbar-thumb {
  background-color: #c1c1c1;
  border-radius: 3px;
}
.chat-container::-webkit-scrollbar-thumb:hover {
  background-color: #a8a8a8;
}
.chat-container::-webkit-scrollbar-track {
  background-color: #f5f5f5;
  border-radius: 3px;
}

/* 消息内容 */
.message-content :deep(br) {
  display: block;
  content: '';
  margin: 0.2em 0;
}

/* 助手消息底部栏 */
.message-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 6px;
  font-size: 12px;
  color: #999;
}
.voice-output-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 14px;
  padding: 4px 8px;
  border-radius: 16px;
  transition: background 0.2s;
  color: #666;
}
.voice-output-btn:hover {
  background-color: rgba(0,0,0,0.05);
  color: #409eff;
}

/* 用户消息时间 */
.user-message-time {
  text-align: right;
  font-size: 11px;
  color: #999;
  margin-top: 4px;
  margin-right: 8px;
}

/* 语音按钮录音状态 */
.voice-btn-inside.recording {
  background-color: #ff4d4f;
  color: white;
  animation: pulse 1s infinite;
}
@keyframes pulse {
  0% { opacity: 1; }
  50% { opacity: 0.6; }
  100% { opacity: 1; }
}

/* 菜谱卡片 */
.recipe-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 12px;
}
.recipe-card {
  width: 160px;
  cursor: pointer;
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.2s;
}
.recipe-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
.recipe-img {
  width: 100%;
  height: 120px;
  object-fit: cover;
}
.recipe-title {
  padding: 8px;
  text-align: center;
  font-size: 14px;
  font-weight: 500;
}
</style>