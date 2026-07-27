<template>
  <div class="login-view">
    <div class="login-container">
      <div class="login-card">
        <h2>登录</h2>
        <form @submit.prevent="handleLogin">
          <div class="form-group">
            <label for="username">用户名</label>
            <input 
              id="username" 
              v-model="username" 
              type="text" 
              placeholder="请输入用户名" 
              required
            >
          </div>
          <div class="form-group">
            <label for="password">密码</label>
            <input 
              id="password" 
              v-model="password" 
              type="password" 
              placeholder="请输入密码" 
              required
            >
          </div>
          <button type="submit" class="btn btn-primary w-full">登录</button>
        </form>
        <div class="login-footer">
          <p>还没有账号？<router-link to="/register">立即注册</router-link></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../store/modules/user';
import { ElMessage } from 'element-plus';

const router = useRouter();
const userStore = useUserStore();

const username = ref('');
const password = ref('');

const handleLogin = async () => {
  try {
    // 这里是模拟登录，实际项目中应该调用API
    if (username.value && password.value) {
      // 模拟生成token
      const token = `mock-token-${Date.now()}`;
      
      // 保存用户信息到store
      userStore.login(username.value, token);
      
      ElMessage.success('登录成功');
      
      // 跳转到首页
      router.push('/');
    }
  } catch (error) {
    ElMessage.error('登录失败，请检查用户名和密码');
    console.error('Login error:', error);
  }
};
</script>

<style scoped>
.login-view {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 20px;
}

.login-container {
  width: 100%;
  max-width: 400px;
}

.login-card {
  background: white;
  border-radius: var(--border-radius);
  box-shadow: var(--box-shadow);
  padding: 40px;
}

.login-card h2 {
  text-align: center;
  margin-bottom: 30px;
  color: var(--primary-color);
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--text-color);
}

.btn-primary.w-full {
  width: 100%;
  margin-top: 10px;
}

.login-footer {
  text-align: center;
  margin-top: 20px;
}

.login-footer a {
  color: var(--primary-color);
  font-weight: 500;
}

.login-footer a:hover {
  text-decoration: underline;
}
</style>