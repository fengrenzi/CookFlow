import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

// 阅读进度接口
interface ReadingProgress {
  bookId: string;
  currentPage: number;
  lastReadTime: Date;
}

// 用户偏好设置接口
interface UserPreferences {
  fontSize: number;
  isDarkMode: boolean;
  fontFamily: string;
}

// 书架中的书籍接口
interface BookshelfBook {
  bookId: string;
  addedTime: Date;
}

export const useBookReadingStore = defineStore('bookReading', () => {
  // 状态定义
  const readingProgressList = ref<ReadingProgress[]>([]);
  const userPreferences = ref<UserPreferences>({
    fontSize: 16,
    isDarkMode: false,
    fontFamily: 'system'
  });
  const bookshelf = ref<BookshelfBook[]>([]);
  
  // 模拟从localStorage加载数据
  const loadFromLocalStorage = () => {
    try {
      // 加载阅读进度
      const savedProgress = localStorage.getItem('reading_progress');
      if (savedProgress) {
        readingProgressList.value = JSON.parse(savedProgress);
      }
      
      // 加载用户偏好
      const savedPreferences = localStorage.getItem('user_preferences');
      if (savedPreferences) {
        userPreferences.value = JSON.parse(savedPreferences);
      }
      
      // 加载书架数据
      const savedBookshelf = localStorage.getItem('bookshelf');
      if (savedBookshelf) {
        bookshelf.value = JSON.parse(savedBookshelf);
      }
    } catch (error) {
      console.error('加载本地数据失败:', error);
    }
  };
  
  // 保存数据到localStorage
  const saveToLocalStorage = () => {
    try {
      localStorage.setItem('reading_progress', JSON.stringify(readingProgressList.value));
      localStorage.setItem('user_preferences', JSON.stringify(userPreferences.value));
      localStorage.setItem('bookshelf', JSON.stringify(bookshelf.value));
    } catch (error) {
      console.error('保存数据到本地失败:', error);
    }
  };
  
  // 初始化时加载数据
  loadFromLocalStorage();
  
  // 更新阅读进度
  const updateReadingProgress = (bookId: string, currentPage: number) => {
    const existingProgressIndex = readingProgressList.value.findIndex(p => p.bookId === bookId);
    
    if (existingProgressIndex >= 0) {
      readingProgressList.value[existingProgressIndex] = {
        bookId,
        currentPage,
        lastReadTime: new Date()
      };
    } else {
      readingProgressList.value.push({
        bookId,
        currentPage,
        lastReadTime: new Date()
      });
    }
    
    saveToLocalStorage();
  };
  
  // 获取阅读进度
  const getReadingProgress = (bookId: string): number | null => {
    const progress = readingProgressList.value.find(p => p.bookId === bookId);
    return progress ? progress.currentPage : null;
  };
  
  // 保存主题偏好
  const saveThemePreference = (isDarkMode: boolean) => {
    userPreferences.value.isDarkMode = isDarkMode;
    saveToLocalStorage();
  };
  
  // 获取主题偏好
  const getThemePreference = (): boolean | null => {
    return userPreferences.value.isDarkMode;
  };
  
  // 保存字号偏好
  const saveFontSizePreference = (fontSize: number) => {
    userPreferences.value.fontSize = fontSize;
    saveToLocalStorage();
  };
  
  // 获取字号偏好
  const getFontSizePreference = (): number => {
    return userPreferences.value.fontSize;
  };
  
  // 保存字体偏好
  const saveFontPreference = (fontFamily: string) => {
    userPreferences.value.fontFamily = fontFamily;
    saveToLocalStorage();
  };
  
  // 获取字体偏好
  const getFontPreference = (): string => {
    return userPreferences.value.fontFamily;
  };
  
  // 添加到书架
  const addToBookshelf = (bookId: string): boolean => {
    if (bookshelf.value.some(book => book.bookId === bookId)) {
      return false; // 已经在书架中
    }
    
    bookshelf.value.push({
      bookId,
      addedTime: new Date()
    });
    
    saveToLocalStorage();
    return true;
  };
  
  // 从书架移除
  const removeFromBookshelf = (bookId: string): boolean => {
    const initialLength = bookshelf.value.length;
    bookshelf.value = bookshelf.value.filter(book => book.bookId !== bookId);
    
    if (bookshelf.value.length < initialLength) {
      saveToLocalStorage();
      return true;
    }
    
    return false;
  };
  
  // 检查是否在书架中
  const isInBookshelf = (bookId: string): boolean => {
    return bookshelf.value.some(book => book.bookId === bookId);
  };
  
  // 获取书架中的所有书籍ID
  const getBookshelfBookIds = computed(() => {
    return bookshelf.value.map(book => book.bookId);
  });
  
  // 获取最近阅读的书籍
  const getRecentReadBooks = computed(() => {
    return [...readingProgressList.value]
      .sort((a, b) => new Date(b.lastReadTime).getTime() - new Date(a.lastReadTime).getTime())
      .slice(0, 5);
  });
  
  return {
    // 状态
    readingProgressList,
    userPreferences,
    bookshelf,
    
    // 计算属性
    getBookshelfBookIds,
    getRecentReadBooks,
    
    // 方法
    updateReadingProgress,
    getReadingProgress,
    saveThemePreference,
    getThemePreference,
    saveFontSizePreference,
    getFontSizePreference,
    saveFontPreference,
    getFontPreference,
    addToBookshelf,
    removeFromBookshelf,
    isInBookshelf
  };
});