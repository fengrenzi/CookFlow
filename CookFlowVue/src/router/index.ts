import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/HomeView.vue'),
    meta: {
      title: 'FlowCook',
      layout: 'MainLayout'
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/LoginView.vue'),
    meta: {
      title: '登录-FlowCook'
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/RegisterView.vue'),
    meta: {
      title: '注册-FlowCook'
    }
  },
  {
    path: '/recipes',
    name: 'Recipes',
    component: () => import('@/views/RecipesView.vue'),
    meta: {
      title: '菜谱区-FlowCook',
      layout: 'MainLayout'
    }
  },
  {
    path: '/recipe/:id',
    name: 'RecipeDetail',
    component: () => import('@/views/RecipeDetailView.vue'),
    meta: {
      title: '菜谱详情-FlowCook',
      layout: 'MainLayout'
    }
  },
  {
    path: '/ingredients',
    name: 'Ingredients',
    component: () => import('@/views/IngredientsView.vue'),
    meta: {
      title: '食材区-FlowCook',
      layout: 'MainLayout'
    }
  },
  {
    path: '/ingredients/:id',
    name: 'IngredientDetail',
    component: () => import('@/views/IngredientDetailView.vue'),
    props: true,
    meta: {
      title: '食材详情-FlowCook',
      layout: 'MainLayout'
    }
  },
  {
    path: '/books',
    name: 'Books',
    component: () => import('@/views/BooksView.vue'),
    meta: {
      title: '书籍区-FlowCook',
      layout: 'MainLayout'
    }
  },
  {
    path: '/books/:id',
    name: 'BookDetail',
    component: () => import('@/views/BookDetailView.vue'),
    meta: {
      title: '书籍详情-FlowCook',
      layout: 'MainLayout'
    }
  },
  {
    path: '/books/:id/read',
    name: 'BookReading',
    component: () => import('@/views/BookReadingView.vue'),
    meta: {
      title: '阅读-FlowCook'
    }
  },
  {
    path: '/explore',
    name: 'Explore',
    component: () => import('@/views/ExploreView.vue'),
    meta: {
      title: '拓展区-FlowCook',
      layout: 'MainLayout'
    }
  },
  {
    path: '/forum',
    name: 'Forum',
    component: () => import('@/views/ForumView.vue'),
    meta: {
      title: '论坛-FlowCook',
      layout: 'MainLayout'
    }
  },
  {
    path: '/forum/share',
    name: 'ForumShare',
    component: () => import('@/views/ForumView.vue'),
    props: { type: 'share' },
    meta: {
      title: '分享区-FlowCook',
      layout: 'MainLayout'
    }
  },
  {
    path: '/forum/share/:id',
    name: 'ForumShareDetail',
    component: () => import('@/views/UnofficialRecipeDetailView.vue'),
    meta: {
      title: '分享详情-FlowCook',
      layout: 'MainLayout'
    }
  },
  {
    path: '/forum/question',
    name: 'ForumQuestion',
    component: () => import('@/views/ForumView.vue'),
    props: { type: 'question' },
    meta: {
      title: '问答区-FlowCook',
      layout: 'MainLayout'
    }
  },
  {
    path: '/forum/question/:id',
    name: 'QuestionDetail',
    component: () => import('@/views/QuestionDetailView.vue'),
    meta: {
      title: '问答详情-FlowCook',
      layout: 'MainLayout'
    }
  },
  {
    path: '/forum/activity',
    name: 'ForumActivity',
    component: () => import('@/views/ForumView.vue'),
    props: { type: 'activity' },
    meta: {
      title: '主题活动-FlowCook',
      layout: 'MainLayout'
    }
  },
  {
    path: '/forum/activity/:id',
    name: 'activity-detail',
    component: () => import('@/views/ActivityDetailView.vue'),
    props: true,
    meta: {
      title: '活动详情-FlowCook',
      layout: 'MainLayout'
    }
  },
  {
    path: '/assistant',
    name: 'Assistant',
    component: () => import('@/views/AssistantView.vue'),
    meta: {
      title: '烹饪助手-FlowCook',
      layout: 'MainLayout'
    }
  },
  {
    path: '/cart',
    name: 'Cart',
    component: () => import('@/views/CartView.vue'),
    meta: {
      title: '购物车-FlowCook',
      layout: 'MainLayout'
    }
  },
  {
    path: '/user',
    name: 'UserProfile',
    component: () => import('@/views/UserProfileView.vue'),
    meta: {
      title: '个人中心-FlowCook',
      layout: 'MainLayout'
    }
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

// 全局前置守卫：未登录跳转登录页
// router.beforeEach((to, from, next) => {
//   const token = localStorage.getItem('token');
//   const publicPages = ['/login', '/register']; // 公开页面列表
//   const authRequired = !publicPages.includes(to.path);

//   if (authRequired && !token) {
//     // 未登录且访问需要认证的页面，重定向到登录页
//     next('/login');
//   } else {
//     // 已登录或访问公开页面，放行
//     next();
//   }
// });

router.beforeEach((to, _from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title as string;
  }
  next();
});

export default router;