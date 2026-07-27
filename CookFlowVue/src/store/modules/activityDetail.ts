import { defineStore } from 'pinia';
import { ref } from 'vue';

// 定义活动数据接口
export interface ActivityData {
  id: number;
  title: string;
  bannerImage: string;
  description: string;
  startTime: string;
  endTime: string;
  organizer: string;
  participants: number;
}

// 定义评论接口
export interface Comment {
  id: string;
  username: string;
  userAvatar: string;
  content: string;
  commentTime: string;
  likeCount: number;
  isLiked: boolean;
}

// 定义分享接口
export interface ParticipantShare {
  id: string;
  username: string;
  userAvatar: string;
  content: string;
  imageUrl: string;
  likeCount: number;
  isLiked: boolean;
  shareTime: string;
  rank?: number;
}

export const useActivityDetailStore = defineStore('activityDetail', () => {
  // 状态定义
  const activityData = ref<ActivityData>({
    id: 1,
    title: '',
    bannerImage: '',
    description: '',
    startTime: '',
    endTime: '',
    organizer: '',
    participants: 0
  });

  const comments = ref<Comment[]>([]);
  const participantShares = ref<ParticipantShare[]>([]);
  const isRegistered = ref(false);
  const isLoading = ref(false);

  // 日期格式化函数：只显示到日期
  function formatDateToDay(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}/${month}/${day}`;
  }

  // 模拟数据（在实际应用中，这些数据应该从API获取）
  const mockActivityData = [
    {
      id: 1,
      title: '夏季清凉甜品大赛',
      bannerImage: '/hots/1.jpg',
      description: '分享你的夏季清凉甜品食谱，赢取精美厨具套装！本次活动邀请专业甜点师担任评委，参赛作品将根据创意性、口感和外观进行评分。\n\n活动亮点：\n1. 前三名将获得精美厨具套装\n2. 人气奖可获得限定款甜品模具\n3. 所有参赛作品将在平台展示\n\n期待你的清凉创意！',
      startTime: formatDateToDay(new Date(Date.now() + 2 * 24 * 60 * 60 * 1000)),
      endTime: formatDateToDay(new Date(Date.now() + 7 * 24 * 60 * 60 * 1000)),
      organizer: 'FlowCook官方',
      participants: 256
    },
    {
      id: 2,
      title: '新手学做饭',
      bannerImage: '/hots/2.jpg',
      description: '新手如何开始学做饭？这里有一些入门技巧和基础食谱。本活动专为厨房新手设计，提供详细的步骤指导和实用技巧。\n\n活动内容：\n1. 基础刀工教学视频\n2. 10道简单易学的家常菜食谱\n3. 常见烹饪问题解答\n\n让我们一起开启烹饪之旅！',
      startTime: formatDateToDay(new Date(Date.now() - 3 * 24 * 60 * 60 * 1000)),
      endTime: formatDateToDay(new Date(Date.now() + 3 * 24 * 60 * 60 * 1000)),
      organizer: '厨艺学院',
      participants: 432
    },
    {
      id: 3,
      title: '家庭烘焙日',
      bannerImage: '/hots/3.jpg',
      description: '周末和家人一起做烘焙，享受美食的同时增进感情。本活动鼓励家庭成员一起参与烘焙过程，体验亲手制作甜点的乐趣。\n\n活动特色：\n1. 适合全家参与的简单烘焙食谱\n2. 亲子互动烘焙技巧\n3. 成品分享与评比\n\n让烘焙成为家庭美好回忆的一部分！',
      startTime: formatDateToDay(new Date(Date.now() - 10 * 24 * 60 * 60 * 1000)),
      endTime: formatDateToDay(new Date(Date.now() - 2 * 24 * 60 * 60 * 1000)),
      organizer: '烘焙爱好者协会',
      participants: 189
    }
  ];

  const mockComments = [
    {
      id: '1',
      username: '烘焙爱好者',
      userAvatar: '👩‍🍳',
      content: '希望能够提供一些传统月饼皮的制作教程，对于新手来说会很有帮助！',
      commentTime: '2天前',
      likeCount: 25,
      isLiked: false
    },
    {
      id: '2',
      username: '甜点师小王',
      userAvatar: '👨',
      content: '建议增加最佳创意奖，鼓励大家做出更多创新口味的月饼。',
      commentTime: '1天前',
      likeCount: 18,
      isLiked: false
    },
    {
      id: '3',
      username: '美食达人',
      userAvatar: '👩',
      content: '希望比赛结果能有直播公布环节，增加互动性。',
      commentTime: '12小时前',
      likeCount: 12,
      isLiked: false
    }
  ];

  const mockShares = [
    {
      id: '1',
      username: '月饼大师',
      userAvatar: '👨‍🍳',
      content: '这次做了流心奶黄月饼，第一次尝试，效果还不错！',
      imageUrl: '/hots/2.jpg',
      likeCount: 89,
      isLiked: false,
      shareTime: '1小时前'
    },
    {
      id: '2',
      username: '创意烘焙师',
      userAvatar: '👩',
      content: '创新了抹茶红豆口味，颜色搭配很好看！',
      imageUrl: '/hots/3.jpg',
      likeCount: 76,
      isLiked: false,
      shareTime: '2小时前'
    },
    {
      id: '3',
      username: '传统美食家',
      userAvatar: '👨',
      content: '坚持传统工艺，五仁月饼才是经典！',
      imageUrl: '/hots/1.jpg',
      likeCount: 65,
      isLiked: false,
      shareTime: '3小时前'
    }
  ];

  // 异步方法：获取活动详情
  async function fetchActivityDetail(activityId: number) {
    isLoading.value = true;
    try {
      // 在实际应用中，这里应该是API调用
      // const response = await api.get(`/activities/${activityId}`);
      // activityData.value = response.data;
      
      // 使用模拟数据
      const mockData = mockActivityData.find(activity => activity.id === activityId);
      if (mockData) {
        activityData.value = { ...mockData };
      } else {
        // 默认活动数据
        activityData.value = {
          id: activityId,
          title: '中秋月饼制作大赛',
          bannerImage: '/hots/1.jpg',
          description: '欢迎参加我们的中秋月饼制作大赛！在这个传统节日里，展示你制作月饼的独特技艺，分享你的创意配方和制作心得。无论是传统口味还是创新款式，只要你有热情，就来参加吧！\n\n参赛要求：\n1. 提交至少3张制作过程的照片\n2. 详细的配方和步骤说明\n3. 创新点说明（如有）\n\n奖项设置：\n- 金奖（1名）：专业烘焙套装\n- 银奖（2名）：高级月饼模具\n- 铜奖（3名）：精美包装礼盒\n\n期待你的参与！',
          startTime: '2024/09/20',
          endTime: '2024/09/29',
          organizer: 'FlowCook官方',
          participants: 128
        };
      }
    } catch (error) {
      console.error('获取活动详情失败:', error);
    } finally {
      isLoading.value = false;
    }
  }

  // 异步方法：获取活动评论
  async function fetchActivityComments(_activityId: number) {
    try {
      // 在实际应用中，这里应该是API调用
      // const response = await api.get(`/activities/${activityId}/comments`);
      // comments.value = response.data;
      
      // 使用模拟数据
      comments.value = [...mockComments];
    } catch (error) {
      console.error('获取活动评论失败:', error);
    }
  }

  // 异步方法：获取参与者分享
  async function fetchParticipantShares(_activityId: number) {
    try {
      // 在实际应用中，这里应该是API调用
      // const response = await api.get(`/activities/${activityId}/shares`);
      // participantShares.value = response.data;
      
      // 使用模拟数据
      participantShares.value = [...mockShares];
    } catch (error) {
      console.error('获取参与者分享失败:', error);
    }
  }

  // 异步方法：提交评论
  async function submitComment(content: string) {
    try {
      // 在实际应用中，这里应该是API调用
      // const response = await api.post(`/activities/${activityData.value.id}/comments`, { content });
      // comments.value.unshift(response.data);
      
      // 模拟提交评论
      const newComment: Comment = {
        id: Date.now().toString(),
        username: '当前用户',
        userAvatar: '👤',
        content,
        commentTime: '刚刚',
        likeCount: 0,
        isLiked: false
      };
      comments.value.unshift(newComment);
      return true;
    } catch (error) {
      console.error('提交评论失败:', error);
      return false;
    }
  }

  // 异步方法：提交分享
  async function submitShare(content: string, imageUrl?: string) {
    try {
      // 在实际应用中，这里应该是API调用
      // const response = await api.post(`/activities/${activityData.value.id}/shares`, { content, imageUrl });
      // participantShares.value.unshift(response.data);
      
      // 模拟提交分享
      const newShare: ParticipantShare = {
        id: Date.now().toString(),
        username: '当前用户',
        userAvatar: '👤',
        content,
        imageUrl: imageUrl || '',
        likeCount: 0,
        isLiked: false,
        shareTime: '刚刚'
      };
      participantShares.value.unshift(newShare);
      return true;
    } catch (error) {
      console.error('提交分享失败:', error);
      return false;
    }
  }

  // 异步方法：切换评论点赞状态
  async function toggleCommentLike(commentId: string) {
    try {
      const comment = comments.value.find(c => c.id === commentId);
      if (comment) {
        // 在实际应用中，这里应该是API调用
        // await api.post(`/comments/${commentId}/like`);
        
        // 模拟点赞操作
        comment.isLiked = !comment.isLiked;
        comment.likeCount += comment.isLiked ? 1 : -1;
      }
    } catch (error) {
      console.error('切换评论点赞状态失败:', error);
    }
  }

  // 异步方法：切换分享点赞状态
  async function toggleShareLike(shareId: string) {
    try {
      const share = participantShares.value.find(s => s.id === shareId);
      if (share) {
        // 在实际应用中，这里应该是API调用
        // await api.post(`/shares/${shareId}/like`);
        
        // 模拟点赞操作
        share.isLiked = !share.isLiked;
        share.likeCount += share.isLiked ? 1 : -1;
      }
    } catch (error) {
      console.error('切换分享点赞状态失败:', error);
    }
  }

  // 异步方法：切换报名状态
  async function toggleRegistration() {
    try {
      if (isRegistered.value) {
        // 取消报名
        // 在实际应用中，这里应该是API调用
        // await api.delete(`/activities/${activityData.value.id}/registration`);
        
        // 模拟取消报名
        isRegistered.value = false;
        activityData.value.participants -= 1;
      } else {
        // 报名
        // 在实际应用中，这里应该是API调用
        // await api.post(`/activities/${activityData.value.id}/registration`);
        
        // 模拟报名
        isRegistered.value = true;
        activityData.value.participants += 1;
      }
      return true;
    } catch (error) {
      console.error('切换报名状态失败:', error);
      return false;
    }
  }

  // 初始化数据
  async function initializeData(activityId: number) {
    await Promise.all([
      fetchActivityDetail(activityId),
      fetchActivityComments(activityId),
      fetchParticipantShares(activityId)
    ]);
  }

  return {
    // 状态
    activityData,
    comments,
    participantShares,
    isRegistered,
    isLoading,
    
    // 方法
    fetchActivityDetail,
    fetchActivityComments,
    fetchParticipantShares,
    submitComment,
    submitShare,
    toggleCommentLike,
    toggleShareLike,
    toggleRegistration,
    initializeData
  };
});