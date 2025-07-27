<template>
  <div class="home">
    <!-- Hero Section -->
    <div class="hero-section bg-primary text-white text-center py-5 mb-5 rounded">
      <div class="container">
        <h1 class="display-4 fw-bold">歡迎來到電商平台</h1>
        <p class="lead">探索我們精選的商品，享受優質的購物體驗</p>
        <RouterLink to="/products" class="btn btn-light btn-lg">開始購物</RouterLink>
      </div>
    </div>

    <!-- Promotions Section -->
    <div class="promotions-section mb-5" v-if="promotions.length > 0">
      <h2 class="text-center mb-4">限時優惠</h2>
      <div class="row">
        <div class="col-md-6 col-lg-4 mb-4" v-for="promotion in promotions" :key="promotion.id">
          <div class="card h-100 border-warning">
            <img :src="promotion.imageUrl" class="card-img-top" alt="promotion image" style="height: 200px; object-fit: cover;">
            <div class="card-body">
              <h5 class="card-title text-warning">{{ promotion.name }}</h5>
              <p class="card-text">{{ promotion.description }}</p>
              <div class="d-flex justify-content-between align-items-center">
                <span class="badge bg-danger fs-6">{{ promotion.discountValue }}% OFF</span>
                <small class="text-muted">
                  {{ formatDate(promotion.endDate) }} 截止
                </small>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Featured Products -->
    <div class="featured-products mb-5">
      <h2 class="text-center mb-4">熱門商品</h2>
      <div class="row" v-if="products.length > 0">
        <div class="col-md-6 col-lg-3 mb-4" v-for="product in featuredProducts" :key="product.id">
          <div class="card h-100 shadow-sm">
            <img :src="product.imageURL" class="card-img-top" alt="product image" style="height: 200px; object-fit: cover;">
            <div class="card-body d-flex flex-column">
              <h5 class="card-title">{{ product.name }}</h5>
              <p class="card-text text-muted small">{{ product.shortDescription }}</p>
              <div class="mt-auto">
                <div class="d-flex justify-content-between align-items-center mb-2">
                  <span class="h5 text-primary mb-0">NT$ {{ product.price }}</span>
                  <div class="rating">
                    <span class="text-warning">★ {{ product.rating }}</span>
                    <small class="text-muted">({{ product.soldCount }})</small>
                  </div>
                </div>
                <div class="d-flex gap-2">
                  <RouterLink :to="`/products/${product.id}`" class="btn btn-outline-primary btn-sm flex-fill">
                    查看詳情
                  </RouterLink>
                  <button @click="addToCart(product)" class="btn btn-primary btn-sm" v-if="authStore.isAuthenticated">
                    加入購物車
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="text-center" v-else>
        <div class="spinner-border" role="status" v-if="loading">
          <span class="visually-hidden">Loading...</span>
        </div>
        <p v-else class="text-muted">暫無商品</p>
      </div>
    </div>

    <!-- Categories -->
    <div class="categories-section">
      <h2 class="text-center mb-4">商品分類</h2>
      <div class="row" v-if="categories.length > 0">
        <div class="col-md-4 col-lg-3 mb-3" v-for="category in categories" :key="category.id">
          <RouterLink :to="`/products?categoryId=${category.id}`" class="text-decoration-none">
            <div class="card text-center h-100 border-secondary">
              <div class="card-body">
                <h5 class="card-title">{{ category.name }}</h5>
                <p class="card-text small text-muted">{{ category.description }}</p>
              </div>
            </div>
          </RouterLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { useProductStore } from '@/stores/product'
import { useCartStore } from '@/stores/cart'
import { useAuthStore } from '@/stores/auth'

const productStore = useProductStore()
const cartStore = useCartStore()
const authStore = useAuthStore()

const products = computed(() => productStore.products)
const promotions = computed(() => productStore.promotions)
const categories = computed(() => productStore.categories)
const loading = computed(() => productStore.loading)

const featuredProducts = computed(() => products.value.slice(0, 8))

onMounted(async () => {
  await Promise.all([
    productStore.fetchProducts(),
    productStore.fetchPromotions(),
    productStore.fetchCategories()
  ])
})

const addToCart = async (product: any) => {
  const result = await cartStore.addToCart(product)
  if (result.success) {
    alert('商品已加入購物車！')
  } else {
    alert(result.error)
  }
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('zh-TW')
}
</script>

<style scoped>
.hero-section {
  background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
}

.card:hover {
  transform: translateY(-2px);
  transition: transform 0.2s ease-in-out;
}

.rating {
  font-size: 0.9rem;
}
</style>
