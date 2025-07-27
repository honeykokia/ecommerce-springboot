<template>
  <div class="login-view">
    <div class="row justify-content-center">
      <div class="col-md-6 col-lg-4">
        <div class="card shadow">
          <div class="card-body p-4">
            <h2 class="card-title text-center mb-4">用戶登入</h2>
            
            <div v-if="errorMessage" class="alert alert-danger" role="alert">
              {{ errorMessage }}
            </div>

            <form @submit.prevent="handleLogin">
              <div class="mb-3">
                <label for="email" class="form-label">電子郵件</label>
                <input 
                  type="email" 
                  class="form-control" 
                  id="email" 
                  v-model="formData.email"
                  required
                >
              </div>

              <div class="mb-3">
                <label for="password" class="form-label">密碼</label>
                <input 
                  type="password" 
                  class="form-control" 
                  id="password" 
                  v-model="formData.password"
                  required
                >
              </div>

              <div class="mb-3 form-check">
                <input type="checkbox" class="form-check-input" id="remember" v-model="rememberMe">
                <label class="form-check-label" for="remember">
                  記住我
                </label>
              </div>

              <button type="submit" class="btn btn-primary w-100" :disabled="loading">
                <span v-if="loading" class="spinner-border spinner-border-sm me-2" role="status"></span>
                登入
              </button>
            </form>

            <div class="text-center mt-3">
              <p class="mb-0">還沒有帳號？ 
                <RouterLink to="/register" class="text-decoration-none">立即註冊</RouterLink>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const loading = ref(false)
const errorMessage = ref('')
const rememberMe = ref(false)

const formData = reactive({
  email: '',
  password: ''
})

const handleLogin = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const result = await authStore.login(formData)
    
    if (result.success) {
      // Login successful, redirect to home or previous page
      router.push('/')
    } else {
      errorMessage.value = result.error
    }
  } catch (error) {
    errorMessage.value = '登入過程中發生錯誤'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-view {
  margin-top: 2rem;
}

.card {
  border: none;
  border-radius: 10px;
}

.btn-primary {
  border-radius: 8px;
}
</style>