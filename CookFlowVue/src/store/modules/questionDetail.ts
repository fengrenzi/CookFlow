import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { ElMessage } from 'element-plus';

// 问题数据接口
export interface Question {
  id: string;
  title: string;
  description: string;
  username: string;
  publishTime: string;
  tags: string[];
  followerCount: number;
  viewCount: number;
  avatarUrl: string;
}

// 回答数据接口
export interface Answer {
  id: string;
  username: string;
  answerTime: string;
  content: string;
  likeCount: number;
  commentCount: number;
  isLiked: boolean;
  avatarUrl: string;
}

// 评论数据接口
export interface Comment {
  id: string;
  username: string;
  avatarUrl: string;
  content: string;
  commentTime: string;
  likeCount: number;
  isLiked: boolean;
}

// 相关问题接口
export interface RelatedQuestion {
  id: string;
  title: string;
  answerCount: number;
  viewCount: number;
}

export const useQuestionDetailStore = defineStore('questionDetail', () => {
  // 状态定义
  const question = ref<Question>({
    id: '',
    title: '',
    description: '',
    username: '',
    publishTime: '',
    tags: [],
    followerCount: 0,
    viewCount: 0,
    avatarUrl: ''
  });

  const answers = ref<Answer[]>([]);
  const relatedQuestions = ref<RelatedQuestion[]>([]);
  const comments = ref<Record<string, Comment[]>>({});
  const isFollowingQuestion = ref(false);
  const isGoodQuestion = ref(false);
  const goodQuestionCount = ref(0);
  const currentSort = ref('time'); // time, likes, comments
  const showCommentsForAnswer = ref<Record<string, boolean>>({});
  const loading = ref(false);
  const error = ref<string | null>(null);

  // 计算属性
  const currentSortText = computed(() => {
    switch (currentSort.value) {
      case 'time': return '按时间排序';
      case 'likes': return '按点赞排序';
      case 'comments': return '按评论排序';
      default: return '按时间排序';
    }
  });

  const sortedAnswers = computed(() => {
    const answersCopy = [...answers.value];
    switch (currentSort.value) {
      case 'likes':
        return answersCopy.sort((a, b) => b.likeCount - a.likeCount);
      case 'comments':
        return answersCopy.sort((a, b) => b.commentCount - a.commentCount);
      case 'time':
      default:
        return answersCopy.sort((a, b) =>
          new Date(b.answerTime).getTime() - new Date(a.answerTime).getTime()
        );
    }
  });

  // 异步方法
  const initializeData = async (questionId: string) => {
    loading.value = true;
    error.value = null;
    
    try {
      // 实际应用中，这里应该调用API获取数据
      // const response = await api.getQuestionDetail(questionId);
      // 模拟API调用延迟
      await new Promise(resolve => setTimeout(resolve, 500));
      
      // 模拟数据，实际应用中应该使用API返回的数据
      question.value = {
        id: questionId,
        title: '如何正确炖煮鸡汤才能让汤更鲜美？',
        description: '我每次煮的鸡汤都感觉不够鲜美，肉质也容易老。想请教一下各位烹饪达人，有没有什么秘诀可以让鸡汤更鲜美，肉质更嫩滑？我一般是用土鸡，加入一些常见的调料，但效果总是不如预期。希望能得到一些具体的建议，比如炖煮时间、火候控制、调料搭配等方面的技巧。',
        username: '美食爱好者小王',
        publishTime: '2024-06-15',
        tags: ['汤品', '鸡肉', '炖煮', '家常菜'],
        followerCount: 42,
        viewCount: 1568,
        avatarUrl: ''
      };

      answers.value = [
        {
          id: '1',
          username: '资深厨师老李',
          answerTime: '2024-06-15 10:30',
          content: '炖鸡汤的关键在于以下几点：1. 焯水时用冷水下锅，慢慢升温，可以更好地去除血水；2. 炖煮时用小火慢炖，避免大火沸腾导致肉质变老；3. 可以加入一小勺白醋，帮助释放钙质和鲜味；4. 最后10分钟再加入盐，过早加盐会使肉质收缩；5. 可以放一些红枣、枸杞增加鲜味和营养。',
          likeCount: 89,
          commentCount: 12,
          isLiked: false,
          avatarUrl: ''
        },
        {
          id: '2',
          username: '家庭主妇张阿姨',
          answerTime: '2024-06-15 11:15',
          content: '我炖鸡汤有个小秘诀，就是先将鸡肉用少量油稍微煎一下，这样炖出来的汤会更浓郁。另外，炖的时候加一些香菇或者竹荪，味道会特别鲜美。记住不要频繁揭开锅盖，保持温度稳定很重要。',
          likeCount: 56,
          commentCount: 8,
          isLiked: true,
          avatarUrl: ''
        },
        {
          id: '3',
          username: '美食博主小林',
          answerTime: '2024-06-15 14:20',
          content: '除了大家提到的方法，我还建议使用砂锅来炖煮，保温效果更好。另外，鸡肉最好选择当年的土鸡，老母鸡虽然营养好，但肉质可能会比较柴。如果想要汤更清澈，可以将浮沫彻底撇干净，并且在炖煮过程中不要搅动太多。',
          likeCount: 34,
          commentCount: 5,
          isLiked: false,
          avatarUrl: ''
        }
      ];

      relatedQuestions.value = [
        {
          id: '2',
          title: '鸡汤和鸭汤哪个更有营养？',
          answerCount: 18,
          viewCount: 2341
        },
        {
          id: '3',
          title: '如何去除鸡汤中的腥味？',
          answerCount: 25,
          viewCount: 3456
        },
        {
          id: '4',
          title: '炖鸡汤放哪些中药材比较好？',
          answerCount: 12,
          viewCount: 1890
        },
        {
          id: '5',
          title: '电压力锅炖鸡汤和传统砂锅炖有什么区别？',
          answerCount: 21,
          viewCount: 2105
        }
      ];

      goodQuestionCount.value = 17;
    } catch (err) {
      error.value = '获取问题详情失败';
      console.error('获取问题详情失败:', err);
      ElMessage.error('获取问题详情失败');
    } finally {
      loading.value = false;
    }
  };

  // 加载评论
  const loadComments = async (answerId: string) => {
    try {
      // 实际应用中，这里应该调用API获取评论数据
      // const response = await api.getComments(answerId);
      
      // 模拟API调用延迟
      await new Promise(resolve => setTimeout(resolve, 300));
      
      // 模拟评论数据
      const mockComments: Comment[] = [
        {
          id: `${answerId}-comment-1`,
          username: '美食爱好者',
          avatarUrl: '',
          content: '非常实用的建议！我之前一直都是直接加盐，难怪肉质会变老。下次一定按照这个方法试试。',
          commentTime: '2小时前',
          likeCount: 5,
          isLiked: false
        },
        {
          id: `${answerId}-comment-2`,
          username: '家庭主厨',
          avatarUrl: '',
          content: '补充一点，我一般会在炖煮过程中加入一些姜片和葱段，可以更好地去除腥味，提升汤的鲜美度。另外，炖煮时间也很重要，一般建议小火慢炖1.5-2小时。',
          commentTime: '1小时前',
          likeCount: 8,
          isLiked: true
        },
        {
          id: `${answerId}-comment-3`,
          username: '新手学做饭',
          avatarUrl: '',
          content: '请问用什么锅具炖煮比较好呢？我家有砂锅和不锈钢锅，不知道哪个效果更好？',
          commentTime: '30分钟前',
          likeCount: 2,
          isLiked: false
        }
      ];

      comments.value[answerId] = mockComments;
    } catch (err) {
      console.error('获取评论失败:', err);
      ElMessage.error('获取评论失败');
    }
  };

  // 切换评论区显示状态
  const toggleComments = (answerId: string) => {
    showCommentsForAnswer.value[answerId] = !showCommentsForAnswer.value[answerId];

    // 如果是第一次打开评论区，加载评论数据
    if (showCommentsForAnswer.value[answerId] && !comments.value[answerId]) {
      loadComments(answerId);
    }
  };

  // 关注/取消关注问题
  const toggleFollowQuestion = async () => {
    try {
      // 实际应用中，这里应该调用API更新关注状态
      // await api.toggleFollowQuestion(question.value.id);
      
      isFollowingQuestion.value = !isFollowingQuestion.value;
      question.value.followerCount += isFollowingQuestion.value ? 1 : -1;
      ElMessage.success(isFollowingQuestion.value ? '关注问题成功' : '取消关注成功');
    } catch (err) {
      console.error('更新关注状态失败:', err);
      ElMessage.error('操作失败，请重试');
    }
  };

  // 点赞/取消点赞问题
  const toggleGoodQuestion = async () => {
    try {
      // 实际应用中，这里应该调用API更新点赞状态
      // await api.toggleGoodQuestion(question.value.id);
      
      isGoodQuestion.value = !isGoodQuestion.value;
      goodQuestionCount.value += isGoodQuestion.value ? 1 : -1;
    } catch (err) {
      console.error('更新点赞状态失败:', err);
      ElMessage.error('操作失败，请重试');
    }
  };

  // 点赞/取消点赞回答
  const toggleAnswerLike = async (answerId: string) => {
    try {
      // 实际应用中，这里应该调用API更新回答点赞状态
      // await api.toggleAnswerLike(answerId);
      
      const answer = answers.value.find(a => a.id === answerId);
      if (answer) {
        answer.isLiked = !answer.isLiked;
        answer.likeCount += answer.isLiked ? 1 : -1;
      }
    } catch (err) {
      console.error('更新回答点赞状态失败:', err);
      ElMessage.error('操作失败，请重试');
    }
  };

  // 提交回答
  const submitAnswer = async (content: string) => {
    if (!content.trim()) {
      ElMessage.error('请输入回答内容');
      return false;
    }

    try {
      loading.value = true;
      // 实际应用中，这里应该调用API提交回答
      // const response = await api.submitAnswer(question.value.id, content);
      
      // 模拟API调用延迟
      await new Promise(resolve => setTimeout(resolve, 500));
      
      const newAnswer: Answer = {
        id: Date.now().toString(),
        username: '当前用户',
        answerTime: new Date().toLocaleString('zh-CN'),
        content: content,
        likeCount: 0,
        commentCount: 0,
        isLiked: false,
        avatarUrl: ''
      };

      answers.value.unshift(newAnswer);
      ElMessage.success('回答提交成功！');
      return true;
    } catch (err) {
      console.error('提交回答失败:', err);
      ElMessage.error('提交回答失败，请重试');
      return false;
    } finally {
      loading.value = false;
    }
  };

  // 提交评论
  const submitComment = async (answerId: string, content: string) => {
    if (!content.trim()) {
      ElMessage.warning('请输入评论内容');
      return false;
    }

    try {
      // 实际应用中，这里应该调用API提交评论
      // const response = await api.submitComment(answerId, content);
      
      // 模拟API调用延迟
      await new Promise(resolve => setTimeout(resolve, 300));
      
      const newComment: Comment = {
        id: `${answerId}-comment-${Date.now()}`,
        username: '当前用户',
        avatarUrl: '',
        content: content,
        commentTime: '刚刚',
        likeCount: 0,
        isLiked: false
      };

      if (!comments.value[answerId]) {
        comments.value[answerId] = [];
      }

      comments.value[answerId].unshift(newComment);

      // 更新回答的评论数
      const answer = answers.value.find(a => a.id === answerId);
      if (answer) {
        answer.commentCount++;
      }

      ElMessage.success('评论发表成功！');
      return true;
    } catch (err) {
      console.error('提交评论失败:', err);
      ElMessage.error('提交评论失败，请重试');
      return false;
    }
  };

  // 点赞/取消点赞评论
  const toggleCommentLike = async (answerId: string, commentId: string) => {
    try {
      // 实际应用中，这里应该调用API更新评论点赞状态
      // await api.toggleCommentLike(commentId);
      
      const comment = comments.value[answerId]?.find(c => c.id === commentId);
      if (comment) {
        comment.isLiked = !comment.isLiked;
        comment.likeCount += comment.isLiked ? 1 : -1;
      }
    } catch (err) {
      console.error('更新评论点赞状态失败:', err);
      ElMessage.error('操作失败，请重试');
    }
  };

  // 更新排序方式
  const handleSortChange = (sortType: string) => {
    currentSort.value = sortType;
  };

  return {
    // 状态
    question,
    answers,
    relatedQuestions,
    comments,
    isFollowingQuestion,
    isGoodQuestion,
    goodQuestionCount,
    currentSort,
    showCommentsForAnswer,
    loading,
    error,
    
    // 计算属性
    currentSortText,
    sortedAnswers,
    
    // 方法
    initializeData,
    loadComments,
    toggleComments,
    toggleFollowQuestion,
    toggleGoodQuestion,
    toggleAnswerLike,
    submitAnswer,
    submitComment,
    toggleCommentLike,
    handleSortChange
  };
});