import { defineStore } from 'pinia'
import { ref } from 'vue'

// 定义用户基本信息接口
export interface UserInfo {
  username: string
  email: string
  phone: string
  bio: string
  registerDate: string
  avatar: string
}

// 定义菜谱接口
export interface Recipe {
  id: number
  title: string
  image: string
  likes?: number
  comments?: number
  author?: string
}

// 定义书籍接口
export interface Book {
  id: number
  title: string
  author: string
  category: string
  publishDate?: string
  collectDate?: string
  lastRead?: string
  progress?: number
}

// 定义问答接口
export interface Question {
  id: number
  title: string
  content?: string
  answers?: number
  views?: number
  date?: string
  author?: string
}

// 定义回答接口
export interface Answer {
  id: number
  questionTitle: string
  content: string
  likes: number
  comments: number
  date: string
}

// 定义活动接口
export interface Activity {
  id: number
  title: string
  date: string
  time?: string
  location?: string
  participants?: number
  status?: string
  organizer?: string
  suggestion?: string
}

export const useUserProfileStore = defineStore('userProfile', () => {
  // 用户基本信息
  const userInfo = ref<UserInfo>({
    username: '',
    email: '',
    phone: '',
    bio: '',
    registerDate: '',
    avatar: 'https://picsum.photos/200/200'
  })
  
  // 未读消息数
  const unreadMessages = ref(0)
  
  // 菜谱相关数据
  const publishedRecipes = ref<Recipe[]>([])
  const collectedRecipes = ref<Recipe[]>([])
  const likedRecipes = ref<Recipe[]>([])
  
  // 书籍相关数据
  const publishedBooks = ref<Book[]>([])
  const collectedBooks = ref<Book[]>([])
  const readingHistory = ref<Book[]>([])
  
  // 问答相关数据
  const myQuestions = ref<Question[]>([])
  const myAnswers = ref<Answer[]>([])
  const followedQuestions = ref<Question[]>([])
  const collectedQuestions = ref<Question[]>([])
  
  // 活动相关数据
  const organizedActivities = ref<Activity[]>([])
  const joinedActivities = ref<Activity[]>([])
  const suggestedActivities = ref<Activity[]>([])
  
  // 加载状态
  const isLoading = ref(false)
  const error = ref<string | null>(null)
  
  // 初始化数据，从后端获取所有用户相关数据
  const initializeData = async (_userId?: number) => {
    isLoading.value = true
    error.value = null
    
    try {
      // 实际项目中，这里应该调用API获取数据
      // 模拟API调用延迟
      await new Promise(resolve => setTimeout(resolve, 300))
      
      // 模拟数据，实际应从后端获取
      userInfo.value = {
        username: '用户名',
        email: 'user@example.com',
        phone: '138****1234',
        bio: '这个人很懒，什么都没有留下...',
        registerDate: '2024-01-01',
        avatar: 'https://picsum.photos/200/200'
      }
      
      unreadMessages.value = 5
      
      publishedRecipes.value = [
        { id: 1, title: '红烧肉', image: 'https://picsum.photos/id/1/300/200', likes: 120, comments: 35 },
        { id: 2, title: '鱼香肉丝', image: 'https://picsum.photos/id/2/300/200', likes: 98, comments: 24 },
      ]
      
      collectedRecipes.value = [
        { id: 1, title: '糖醋排骨', image: 'https://picsum.photos/id/4/300/200', author: '用户A' },
        { id: 2, title: '麻婆豆腐', image: 'https://picsum.photos/id/5/300/200', author: '用户B' },
      ]
      
      likedRecipes.value = [
        { id: 1, title: '水煮鱼', image: 'https://picsum.photos/id/6/300/200', author: '用户C' },
      ]
      
      publishedBooks.value = [
        { id: 1, title: '家常菜100例', author: '张三', category: '菜谱', publishDate: '2024-01-15' },
      ]
      
      collectedBooks.value = [
        { id: 1, title: '烘焙入门', author: '李四', category: '烘焙', collectDate: '2024-02-10' },
      ]
      
      readingHistory.value = [
        { id: 1, title: '家常菜100例', author: '张三', category: '菜谱', lastRead: '2024-03-01', progress: 65 },
      ]
      
      myQuestions.value = [
        { id: 1, title: '红烧肉怎么做才好吃？', content: '想学习红烧肉的正宗做法，请大家分享一下经验。', answers: 15, views: 230, date: '2024-02-20' },
      ]
      
      myAnswers.value = [
        { id: 1, questionTitle: '如何提高刀工技巧？', content: '多练习是关键，可以从切土豆丝开始训练。', likes: 8, comments: 2, date: '2024-02-25' },
      ]
      
      followedQuestions.value = [
        { id: 1, title: '厨房小窍门分享', answers: 32, views: 560 },
      ]
      
      collectedQuestions.value = [
        { id: 1, title: '新手必学的10道家常菜', answers: 20, author: '王五' },
      ]
      
      organizedActivities.value = [
        { id: 1, title: '美食交流会', date: '2024-03-15', time: '14:00-17:00', location: '市中心美食城', participants: 25, status: '进行中' },
      ]
      
      joinedActivities.value = [
        { id: 1, title: '厨艺大赛', date: '2024-03-20', time: '10:00-16:00', location: '市展览馆', organizer: '美食协会' },
      ]
      
      suggestedActivities.value = [
        { id: 1, title: '烘焙体验课', suggestion: '希望能举办一场关于蛋糕制作的体验课程', status: '已采纳', date: '2024-02-18' },
      ]
    } catch (err) {
      error.value = '获取用户资料失败'
      console.error('Failed to load user profile data:', err)
    } finally {
      isLoading.value = false
    }
  }
  
  // 更新用户资料
  const updateUserInfo = async (updatedInfo: Partial<UserInfo>) => {
    try {
      // 实际项目中，这里应该调用API更新数据
      await new Promise(resolve => setTimeout(resolve, 300))
      
      // 更新本地状态
      Object.assign(userInfo.value, updatedInfo)
      
      return true
    } catch (err) {
      error.value = '更新用户资料失败'
      console.error('Failed to update user info:', err)
      return false
    }
  }
  
  // 刷新特定分类数据
  const refreshCategoryData = async (category: string) => {
    try {
      // 实际项目中，这里应该根据分类调用相应的API
      await new Promise(resolve => setTimeout(resolve, 200))
      
      // 这里可以根据category参数决定刷新哪部分数据
      // 目前只是模拟成功
      return true
    } catch (err) {
      error.value = `刷新${category}数据失败`
      console.error(`Failed to refresh ${category} data:`, err)
      return false
    }
  }
  
  return {
    // 状态
    userInfo,
    unreadMessages,
    publishedRecipes,
    collectedRecipes,
    likedRecipes,
    publishedBooks,
    collectedBooks,
    readingHistory,
    myQuestions,
    myAnswers,
    followedQuestions,
    collectedQuestions,
    organizedActivities,
    joinedActivities,
    suggestedActivities,
    isLoading,
    error,
    
    // 方法
    initializeData,
    updateUserInfo,
    refreshCategoryData
  }
})