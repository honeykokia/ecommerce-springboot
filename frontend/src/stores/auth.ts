import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { authAPI } from '@/services/api'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('token'))
  const isAuthenticated = computed(() => !!token.value)

  async function login(credentials: any) {
    try {
      const response = await authAPI.login(credentials)
      const { data } = response.data
      token.value = data.token
      user.value = data
      localStorage.setItem('token', data.token)
      return { success: true, data }
    } catch (error: any) {
      return { success: false, error: error.response?.data?.message || '登入失敗' }
    }
  }

  async function register(userData: any) {
    try {
      const response = await authAPI.register(userData)
      const { data } = response.data
      return { success: true, data }
    } catch (error: any) {
      return { success: false, error: error.response?.data?.message || '註冊失敗' }
    }
  }

  async function logout() {
    user.value = null
    token.value = null
    localStorage.removeItem('token')
  }

  async function getProfile() {
    try {
      const response = await authAPI.getProfile()
      const { data } = response.data
      user.value = data
      return { success: true, data }
    } catch (error: any) {
      return { success: false, error: error.response?.data?.message || '獲取用戶資料失敗' }
    }
  }

  return { 
    user, 
    token, 
    isAuthenticated, 
    login, 
    register, 
    logout, 
    getProfile 
  }
})
