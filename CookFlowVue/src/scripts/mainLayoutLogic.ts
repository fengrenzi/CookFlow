import { ref, onMounted, onUnmounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { useUserStore } from '@/store/modules/user';
import { useLayoutStore } from '../store/modules/layout';

// 定义MainLayout的逻辑组合式函数
export function useMainLayoutLogic() {
  // 获取用户store和布局store实例
  const userStore = useUserStore();
  const layoutStore = useLayoutStore();
  const route = useRoute();
  
  // 从store获取导航菜单数据
  const navItems = layoutStore.navItems;
  
  // 响应式数据
  const openDropdowns = ref({
    forum: false
  });
  
  const showSidebar = ref(true);
  const showBackToTop = ref(false);
  
  // 计算属性
  const isUserProfilePage = computed(() => {
    return route.path === '/user' || route.path.startsWith('/user/');
  });
  
  // 判断是否为AI助手页面
  const isAssistantView = computed(() => {
    return route.path === '/assistant';
  });
  
  // 方法
  
  // 检测路由是否激活
  const isActiveRoute = (path: string) => {
    // 精确匹配根路径
    if (path === '/') {
      return route.path === '/';
    }
    // 对于其他路径，检查是否完全匹配或者是其子路径的前缀
    return route.path === path || route.path.startsWith(path + '/');
  };
  
  // 切换下拉菜单
  const toggleDropdown = (dropdownId: string) => {
    openDropdowns.value[dropdownId as keyof typeof openDropdowns.value] = 
      !openDropdowns.value[dropdownId as keyof typeof openDropdowns.value];
  };
  
  // 打开下拉菜单
  const openDropdown = (dropdownId: string) => {
    openDropdowns.value[dropdownId as keyof typeof openDropdowns.value] = true;
  };
  
  // 关闭下拉菜单
  const closeDropdown = (dropdownId: string) => {
    // 添加延迟，以便用户可以移动到子菜单
    setTimeout(() => {
      openDropdowns.value[dropdownId as keyof typeof openDropdowns.value] = false;
    }, 200);
  };
  
  // 处理退出登录
  const handleLogout = () => {
    userStore.logout();
    ElMessage.success('退出成功');
  };
  
  // 回到顶部功能
  const scrollToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
  };
  
  // 切换侧边栏显示/隐藏
  const toggleSidebar = () => {
    showSidebar.value = !showSidebar.value;
  };
  
  // 监听滚动事件
  const handleScroll = () => {
    // 当页面向下滚动超过300px时显示回到顶部按钮
    showBackToTop.value = window.scrollY > 300;
  };
  
  // 生命周期钩子
  onMounted(() => {
    window.addEventListener('scroll', handleScroll);
  });
  
  onUnmounted(() => {
    window.removeEventListener('scroll', handleScroll);
  });
  
  // 返回需要暴露给组件的数据和方法
  return {
    // 只返回需要的用户相关属性，而不是整个userStore
    isLoggedIn: computed(() => userStore.isLoggedIn),
    username: computed(() => userStore.username),
    navItems,
    openDropdowns,
    showSidebar,
    showBackToTop,
    isUserProfilePage,
    isAssistantView,
    isActiveRoute,
    toggleDropdown,
    openDropdown,
    closeDropdown,
    handleLogout,
    scrollToTop,
    toggleSidebar
  };
}