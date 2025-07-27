<template>
  <div class="profile-view">
    <div class="row justify-content-center">
      <div class="col-lg-8">
        <div class="card shadow">
          <div class="card-header bg-primary text-white">
            <h4 class="mb-0">個人資料</h4>
          </div>
          <div class="card-body p-4">
            <div v-if="successMessage" class="alert alert-success" role="alert">
              {{ successMessage }}
            </div>

            <div v-if="errorMessage" class="alert alert-danger" role="alert">
              {{ errorMessage }}
            </div>

            <!-- Profile Information -->
            <form @submit.prevent="updateProfile">
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="name" class="form-label">姓名</label>
                  <input 
                    type="text" 
                    class="form-control" 
                    id="name" 
                    v-model="profileData.name"
                    required
                  >
                </div>
                <div class="col-md-6 mb-3">
                  <label for="email" class="form-label">電子郵件</label>
                  <input 
                    type="email" 
                    class="form-control" 
                    id="email" 
                    v-model="profileData.email"
                    required
                  >
                </div>
              </div>

              <div class="text-end">
                <button type="submit" class="btn btn-primary" :disabled="updatingProfile">
                  <span v-if="updatingProfile" class="spinner-border spinner-border-sm me-2" role="status"></span>
                  更新資料
                </button>
              </div>
            </form>

            <hr class="my-4">

            <!-- Change Password -->
            <div class="password-section">
              <h5 class="mb-3">修改密碼</h5>
              <form @submit.prevent="changePassword">
                <div class="row">
                  <div class="col-md-4 mb-3">
                    <label for="currentPassword" class="form-label">目前密碼</label>
                    <input 
                      type="password" 
                      class="form-control" 
                      id="currentPassword" 
                      v-model="passwordData.currentPassword"
                      required
                    >
                  </div>
                  <div class="col-md-4 mb-3">
                    <label for="newPassword" class="form-label">新密碼</label>
                    <input 
                      type="password" 
                      class="form-control" 
                      id="newPassword" 
                      v-model="passwordData.newPassword"
                      required
                      minlength="6"
                    >
                  </div>
                  <div class="col-md-4 mb-3">
                    <label for="confirmNewPassword" class="form-label">確認新密碼</label>
                    <input 
                      type="password" 
                      class="form-control" 
                      id="confirmNewPassword" 
                      v-model="passwordData.confirmNewPassword"
                      required
                      :class="{ 'is-invalid': passwordData.confirmNewPassword && passwordData.newPassword !== passwordData.confirmNewPassword }"
                    >
                    <div v-if="passwordData.confirmNewPassword && passwordData.newPassword !== passwordData.confirmNewPassword" class="invalid-feedback">
                      密碼不匹配
                    </div>
                  </div>
                </div>

                <div class="text-end">
                  <button 
                    type="submit" 
                    class="btn btn-warning"
                    :disabled="changingPassword || passwordData.newPassword !== passwordData.confirmNewPassword"
                  >
                    <span v-if="changingPassword" class="spinner-border spinner-border-sm me-2" role="status"></span>
                    修改密碼
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { authAPI } from '@/services/api'

const authStore = useAuthStore()

const successMessage = ref('')
const errorMessage = ref('')
const updatingProfile = ref(false)
const changingPassword = ref(false)

const profileData = reactive({
  name: '',
  email: ''
})

const passwordData = reactive({
  currentPassword: '',
  newPassword: '',
  confirmNewPassword: ''
})

onMounted(async () => {
  await loadProfile()
})

const loadProfile = async () => {
  const result = await authStore.getProfile()
  if (result.success && result.data) {
    profileData.name = result.data.name || ''
    profileData.email = result.data.email || ''
  }
}

const updateProfile = async () => {
  updatingProfile.value = true
  errorMessage.value = ''
  successMessage.value = ''

  try {
    const response = await authAPI.updateProfile(profileData)
    if (response.status === 200) {
      successMessage.value = '個人資料更新成功！'
      await authStore.getProfile() // Refresh store data
    }
  } catch (error: any) {
    errorMessage.value = error.response?.data?.message || '更新失敗'
  } finally {
    updatingProfile.value = false
  }
}

const changePassword = async () => {
  if (passwordData.newPassword !== passwordData.confirmNewPassword) {
    errorMessage.value = '新密碼不匹配'
    return
  }

  changingPassword.value = true
  errorMessage.value = ''
  successMessage.value = ''

  try {
    const response = await authAPI.changePassword({
      currentPassword: passwordData.currentPassword,
      newPassword: passwordData.newPassword
    })
    
    if (response.status === 200) {
      successMessage.value = '密碼修改成功！'
      // Reset password form
      Object.assign(passwordData, {
        currentPassword: '',
        newPassword: '',
        confirmNewPassword: ''
      })
    }
  } catch (error: any) {
    errorMessage.value = error.response?.data?.message || '密碼修改失敗'
  } finally {
    changingPassword.value = false
  }
}
</script>

<style scoped>
.profile-view {
  margin-top: 2rem;
}

.password-section {
  background-color: #f8f9fa;
  padding: 1.5rem;
  border-radius: 8px;
  border: 1px solid #dee2e6;
}

.card {
  border: none;
  border-radius: 10px;
}
</style>