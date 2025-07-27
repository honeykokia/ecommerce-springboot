<template>
  <div id="app">
    <header>
      <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
          <RouterLink class="navbar-brand fw-bold" to="/">🛒 電商平台</RouterLink>
          
          <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
          </button>
          
          <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
              <li class="nav-item">
                <RouterLink class="nav-link" to="/">首頁</RouterLink>
              </li>
              <li class="nav-item">
                <RouterLink class="nav-link" to="/products">商品</RouterLink>
              </li>
            </ul>
            
            <ul class="navbar-nav">
              <li class="nav-item" v-if="authStore.isAuthenticated">
                <RouterLink class="nav-link position-relative" to="/cart">
                  🛒 購物車
                  <span v-if="cartStore.totalItems > 0" class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                    {{ cartStore.totalItems }}
                  </span>
                </RouterLink>
              </li>
              <li class="nav-item dropdown" v-if="authStore.isAuthenticated">
                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                  {{ authStore.user?.name || '用戶' }}
                </a>
                <ul class="dropdown-menu">
                  <li><RouterLink class="dropdown-item" to="/profile">個人資料</RouterLink></li>
                  <li><RouterLink class="dropdown-item" to="/orders">訂單記錄</RouterLink></li>
                  <li><hr class="dropdown-divider"></li>
                  <li><a class="dropdown-item" href="#" @click="logout">登出</a></li>
                </ul>
              </li>
              <li class="nav-item" v-else>
                <RouterLink class="nav-link" to="/login">登入</RouterLink>
              </li>
              <li class="nav-item" v-if="!authStore.isAuthenticated">
                <RouterLink class="nav-link" to="/register">註冊</RouterLink>
              </li>
            </ul>
          </div>
        </div>
      </nav>
    </header>

    <main class="container mt-4">
      <RouterView />
    </main>

    <footer class="bg-dark text-light text-center py-4 mt-5">
      <div class="container">
        <p>&copy; 2025 電商平台. All rights reserved.</p>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { RouterLink, RouterView, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useCartStore } from '@/stores/cart'

const authStore = useAuthStore()
const cartStore = useCartStore()
const router = useRouter()

onMounted(async () => {
  if (authStore.isAuthenticated) {
    await authStore.getProfile()
    await cartStore.fetchCart()
  }
})

const logout = async () => {
  await authStore.logout()
  router.push('/')
}
</script>

<style scoped>
#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

main {
  flex: 1;
}
</style>
