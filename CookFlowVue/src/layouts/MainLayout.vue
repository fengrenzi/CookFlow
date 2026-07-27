<template>
  <div class="main-layout">
    <!-- 顶部导航栏 -->
    <header class="navbar">
      <div class="container">
        <div class="navbar-brand">
          <router-link to="/" class="logo">
            <img src="/images/logo.png" class="logo-image">
            <span class="logo-text">FlowCook</span>
          </router-link>
        </div>

        <!-- 桌面导航菜单 -->
        <nav class="navbar-nav">
          <ul class="nav-menu">
            <li v-for="item in navItems" :key="item.id" class="nav-item">
              <template v-if="item.id === 'forum' && item.subItems">
                <div class="dropdown" @mouseleave="closeDropdown('forum')">
                  <a href="#" class="nav-link dropdown-toggle" :class="{ active: isActiveRoute(item.path) }"
                    @click.prevent="toggleDropdown('forum')" @mouseenter="openDropdown('forum')">
                    {{ item.name }}
                    <span class="dropdown-arrow">▼</span>
                  </a>
                  <ul class="dropdown-menu" :class="{ show: openDropdowns.forum }">
                    <li v-for="subItem in item.subItems" :key="subItem.id" class="dropdown-item">
                      <router-link :to="subItem.path" class="dropdown-link"
                        :class="{ active: isActiveRoute(subItem.path) }">
                        {{ subItem.name }}
                      </router-link>
                    </li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <router-link :to="item.path" class="nav-link" :class="{ active: isActiveRoute(item.path) }">
                  {{ item.name }}
                </router-link>
              </template>
            </li>
          </ul>
        </nav>

        <!-- 用户操作区 -->
        <div class="navbar-actions">
          <template v-if="isLoggedIn">
            <div class="user-info">
              <span class="username">{{ username }}</span>
              <button @click="handleLogout" class="btn btn-outline logout-btn">退出</button>
            </div>
          </template>
          <template v-else>
            <router-link to="/login" class="btn btn-outline">登录</router-link>
            <router-link to="/register" class="btn btn-primary">注册</router-link>
          </template>
        </div>

      </div>
    </header>

    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 显示路由视图内容 -->
      <slot></slot>
    </main>

    <div class="fixed-sidebar">
      <div class="sidebar-container" :class="{ 'show': showSidebar }">
        <!-- 侧边栏控制按钮 -->
        <button class="sidebar-toggle" @click="toggleSidebar" :class="{ 'sidebar-toggle-show': showSidebar }">
          <div class="toggle-icon">{{ showSidebar ? '▶' : '◀' }}</div>
        </button>
        <!-- 右侧固定定位模块 -->
        <div class="sidebar-content">
          <router-link to="/user" class="sidebar-item">
            <div class="sidebar-icon">👤</div>
          </router-link>
          <router-link to="/assistant" class="sidebar-item">
            <div class="sidebar-icon">🤖</div>
          </router-link>
          <router-link to="/cart" class="sidebar-item">
            <div class="sidebar-icon">🛒</div>
          </router-link>
        </div>
      </div>
    </div>

    <!-- 回到顶部按钮 - 固定在右下角 -->
    <button class="back-to-top" :class="{ 'show': showBackToTop }" @click="scrollToTop">
      <div class="top-icon">↑</div>
    </button>

    <!-- 页脚 - 个人中心页面和AI助手页面不显示 -->
    <footer class="footer" v-if="!isUserProfilePage && !isAssistantView">
      <div class="container">
        <div class="footer-bottom">
          <p>&copy; 2025 烹饪计划. 保留所有权利.</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { useMainLayoutLogic } from '@/scripts/mainLayoutLogic';

// 使用MainLayout逻辑
const {
  isLoggedIn,
  username,
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
} = useMainLayoutLogic();
</script>

<style scoped>
.main-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 导航栏样式 */
.navbar {
  background: white;
  position: sticky;
  top: 0;
  z-index: 1000;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.navbar .container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 20px;
}

.navbar-brand .logo {
  display: flex;
  align-items: center;
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--primary-color);
}

.navbar-brand .logo-image {
  height: 40px;
  width: auto;
  margin-right: 10px;
}

.navbar-brand .logo-text {
  white-space: nowrap;
}

.nav-menu {
  display: flex;
  list-style: none;
  gap: 20px;
}

.nav-link {
  display: block;
  padding: 8px 12px;
  color: var(--text-color);
  text-decoration: none;
  border-radius: var(--border-radius);
  transition: var(--transition);
  cursor: pointer;
}

.nav-link:hover {
  color: var(--primary-color);
  background-color: rgba(255, 107, 107, 0.1);
}

.nav-link.active {
  color: var(--primary-color);
  font-weight: 600;
  background-color: rgba(255, 107, 107, 0.1);
  position: relative;
}

.nav-link.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 20px;
  height: 3px;
  background-color: var(--primary-color);
  border-radius: 3px;
}

/* 下拉菜单样式 */
.dropdown {
  position: relative;
}

.dropdown-toggle {
  display: flex;
  align-items: center;
  gap: 4px;
}

.dropdown-arrow {
  font-size: 10px;
  transition: transform 0.2s ease;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  left: 0;
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: var(--border-radius);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  list-style: none;
  padding: 8px 0;
  margin: 0;
  min-width: 120px;
  opacity: 0;
  visibility: hidden;
  transform: translateY(-5px);
  transition: all 0.2s ease;
  z-index: 999;
}

.dropdown-menu.show {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.dropdown-item {
  padding: 0;
}

.dropdown-item a {
  display: block;
  padding: 8px 16px;
  color: var(--text-color);
  text-decoration: none;
  transition: var(--transition);
  white-space: nowrap;
}

.dropdown-item a:hover {
  background-color: rgba(255, 107, 107, 0.1);
  color: var(--primary-color);
}

.dropdown-item a.active {
  background-color: rgba(255, 107, 107, 0.1);
  color: var(--primary-color);
  font-weight: 500;
}

.navbar-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.username {
  font-weight: 500;
  color: var(--text-color);
}

.logout-btn {
  padding: 6px 12px;
  font-size: 14px;
}

/* 主内容区 */
.main-content {
  flex: 1;
}

/* 页脚 */
.footer {
  background-color: var(--text-color);
  color: white;
  padding: 20px 0;
}

.footer-bottom {
  text-align: center;
  color: rgba(255, 255, 255, 0.6);
}

/* 侧边栏控制按钮 */
.sidebar-toggle {
  position: absolute;
  top: 50%;
  left: -36px;
  transform: translateY(-50%);
  background: white;
  border-radius: 6px 0 0 6px;
  box-shadow: var(--box-shadow);
  padding: 6px;
  cursor: pointer;
  z-index: 950;
  transition: all 0.3s ease;
  min-width: 0;
  height: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.sidebar-toggle-show {
  left: -21px;
}

.sidebar-toggle:hover {
  background: var(--primary-color);
  color: white;
}

.toggle-icon {
  font-size: 10px;
  font-weight: bold;
}

/* 右侧固定定位模块 */
.fixed-sidebar {
  position: fixed;
  top: 50%;
  right: 0;
  transform: translateY(-50%);
  z-index: 900;
  transition: right 0.5s ease;
}

.sidebar-container.show {
  right: 15px;
}

.sidebar-container {
  position: relative;
  right: -50px;
}

.sidebar-content {
  display: flex;
  flex-direction: column;
  gap: 10px;
  background: white;
  border-radius: 6px 0 0 6px;
  box-shadow: var(--box-shadow);
  padding: 8px;
  border-right: none;
  margin: 0;
  /* 移除任何可能导致间隙的样式 */
}

.sidebar-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 3px;
  color: var(--text-color);
  text-decoration: none;
  border-radius: var(--border-radius);
  transition: var(--transition);
  cursor: pointer;
  font-size: 10px;
  text-align: center;
  min-width: 35px;
  padding: 6px 0;
}

.sidebar-item:hover {
  background-color: rgba(255, 107, 107, 0.1);
  color: var(--primary-color);
}

.sidebar-icon {
  font-size: 16px;
  margin-bottom: 2px;
}

.sidebar-text {
  font-weight: 500;
}

/* 回到顶部按钮 */
.back-to-top {
  position: fixed;
  bottom: 11%;
  right: 1%;
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: var(--box-shadow);
  width: 40px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  z-index: 800;
  transition: all 0.3s ease;
  opacity: 0;
  visibility: hidden;
  transform: translateY(10px);
  margin: 0;
}

.back-to-top.show {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.back-to-top:hover {
  background: var(--primary-color);
  color: white;
  transform: translateY(-2px);
}

.top-icon {
  font-size: 16px;
  font-weight: bold;
}
</style>