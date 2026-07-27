import { defineStore } from 'pinia';

interface UserState {
  isLoggedIn: boolean;
  username: string;
  token: string | null;
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    isLoggedIn: false,
    username: '',
    token: null
  }),
  
  actions: {
    login(username: string, token: string) {
      this.isLoggedIn = true;
      this.username = username;
      this.token = token;
      // 保存到localStorage
      localStorage.setItem('userToken', token);
      localStorage.setItem('username', username);
    },
    
    logout() {
      this.isLoggedIn = false;
      this.username = '';
      this.token = null;
      // 清除localStorage
      localStorage.removeItem('userToken');
      localStorage.removeItem('username');
    },
    
    checkAuth() {
      const token = localStorage.getItem('userToken');
      const username = localStorage.getItem('username');
      if (token && username) {
        this.isLoggedIn = true;
        this.username = username;
        this.token = token;
        return true;
      }
      return false;
    }
  }
});