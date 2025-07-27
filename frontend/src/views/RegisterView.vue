<template>
  <div class="register-view">
    <div class="row justify-content-center">
      <div class="col-md-6 col-lg-5">
        <div class="card shadow">
          <div class="card-body p-4">
            <h2 class="card-title text-center mb-4">用戶註冊</h2>
            
            <div v-if="errorMessage" class="alert alert-danger" role="alert">
              {{ errorMessage }}
            </div>

            <div v-if="successMessage" class="alert alert-success" role="alert">
              {{ successMessage }}
            </div>

            <form @submit.prevent="handleRegister">
              <div class="mb-3">
                <label for="name" class="form-label">姓名</label>
                <input 
                  type="text" 
                  class="form-control" 
                  id="name" 
                  v-model="formData.name"
                  required
                >
              </div>

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
                  minlength="6"
                >
                <div class="form-text">密碼長度至少6個字符</div>
              </div>

              <div class="mb-3">
                <label for="confirmPassword" class="form-label">確認密碼</label>
                <input 
                  type="password" 
                  class="form-control" 
                  id="confirmPassword" 
                  v-model="confirmPassword"
                  required
                  :class="{ 'is-invalid': confirmPassword && formData.password !== confirmPassword }"
                >
                <div v-if="confirmPassword && formData.password !== confirmPassword" class="invalid-feedback">
                  密碼不匹配
                </div>
              </div>

              <div class="mb-3 form-check">
                <input type="checkbox" class="form-check-input" id="agree" v-model="agreeToTerms" required>
                <label class="form-check-label" for="agree">
                  我同意 <a href="#" class="text-decoration-none">服務條款</a> 和 <a href="#" class="text-decoration-none">隱私政策</a>
                </label>
              </div>

              <button 
                type="submit" 
                class="btn btn-primary w-100" 
                :disabled="loading || formData.password !== confirmPassword || !agreeToTerms"
              >
                <span v-if="loading" class="spinner-border spinner-border-sm me-2" role="status"></span>
                註冊
              </button>
            </form>

            <div class="text-center mt-3">
              <p class="mb-0">已經有帳號？ 
                <RouterLink to="/login" class="text-decoration-none">立即登入</RouterLink>
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
const successMessage = ref('')
const confirmPassword = ref('')
const agreeToTerms = ref(false)

const formData = reactive({
  name: '',
  email: '',
  password: ''
})

const handleRegister = async () => {
  if (formData.password !== confirmPassword.value) {
    errorMessage.value = '密碼不匹配'
    return
  }

  loading.value = true
  errorMessage.value = ''
  successMessage.value = ''

  try {
    const result = await authStore.register(formData)
    
    if (result.success) {
      successMessage.value = '註冊成功！請查收電子郵件進行驗證。'
      // Reset form
      Object.assign(formData, { name: '', email: '', password: '' })
      confirmPassword.value = ''
      agreeToTerms.value = false
      
      // Redirect to login after a short delay
      setTimeout(() => {
        router.push('/login')
      }, 2000)
    } else {
      errorMessage.value = result.error
    }
  } catch (error) {
    errorMessage.value = '註冊過程中發生錯誤'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-view {
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