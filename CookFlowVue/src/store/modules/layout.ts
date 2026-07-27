import { defineStore } from 'pinia';

// 定义导航项的类型
interface NavItem {
  id: string;
  name: string;
  path: string;
  subItems?: SubNavItem[];
}

// 定义子导航项的类型
interface SubNavItem {
  id: string;
  name: string;
  path: string;
}

// 导出布局store
export const useLayoutStore = defineStore('layout', {
  state: () => ({
    // 导航菜单数据
    navItems: [
      { id: 'home', name: '首页', path: '/' },
      { id: 'recipes', name: '菜谱区', path: '/recipes' },
      { id: 'ingredients', name: '食材区', path: '/ingredients' },
      { id: 'books', name: '书籍区', path: '/books' },
      { id: 'explore', name: '拓展区', path: '/explore' },
      {
        id: 'forum',
        name: '论坛',
        path: '/forum',
        subItems: [
          { id: 'forum-share', name: '分享区', path: '/forum/share' },
          { id: 'forum-question', name: '问答区', path: '/forum/question' },
          { id: 'forum-activity', name: '主题活动', path: '/forum/activity' }
        ]
      }
    ] as NavItem[]
  }),
  
  getters: {
    // 获取所有导航项
    getAllNavItems: (state) => state.navItems,
    
    // 获取主导航项（不包含子导航）
    getMainNavItems: (state) => state.navItems.map(item => ({
      id: item.id,
      name: item.name,
      path: item.path
    }))
  }
});