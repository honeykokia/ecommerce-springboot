<template>
  <div class="products-view">
    <div class="row">
      <!-- Filters Sidebar -->
      <div class="col-lg-3 mb-4">
        <div class="card">
          <div class="card-header bg-light">
            <h5 class="mb-0">篩選條件</h5>
          </div>
          <div class="card-body">
            <!-- Search -->
            <div class="mb-3">
              <label for="search" class="form-label">商品搜尋</label>
              <input 
                type="text" 
                class="form-control" 
                id="search" 
                v-model="filters.name"
                placeholder="輸入商品名稱"
                @input="applyFilters"
              >
            </div>

            <!-- Category Filter -->
            <div class="mb-3">
              <label for="category" class="form-label">商品分類</label>
              <select 
                class="form-select" 
                id="category" 
                v-model="filters.categoryId"
                @change="applyFilters"
              >
                <option value="">全部分類</option>
                <option 
                  v-for="category in categories" 
                  :key="category.id" 
                  :value="category.id"
                >
                  {{ category.name }}
                </option>
              </select>
            </div>

            <!-- Reset Filters -->
            <button @click="resetFilters" class="btn btn-outline-secondary w-100">
              重置篩選
            </button>
          </div>
        </div>
      </div>

      <!-- Products Grid -->
      <div class="col-lg-9">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h2>商品列表</h2>
          <div class="text-muted">
            共找到 {{ products.length }} 件商品
          </div>
        </div>

        <div v-if="loading" class="text-center py-5">
          <div class="spinner-border" role="status">
            <span class="visually-hidden">Loading...</span>
          </div>
        </div>

        <div v-else-if="products.length === 0" class="text-center py-5">
          <div class="text-muted">
            <i class="bi bi-search fs-1"></i>
            <h4>找不到符合條件的商品</h4>
            <p>請嘗試調整篩選條件或搜尋關鍵字</p>
          </div>
        </div>

        <div v-else class="row">
          <div class="col-md-6 col-xl-4 mb-4" v-for="product in products" :key="product.id">
            <div class="card h-100 shadow-sm product-card">
              <div class="position-relative">
                <img 
                  :src="product.imageURL" 
                  class="card-img-top" 
                  alt="product image" 
                  style="height: 250px; object-fit: cover;"
                >
                <div class="position-absolute top-0 end-0 m-2">
                  <span v-for="tag in product.tags" :key="tag.id" class="badge bg-danger me-1">
                    {{ tag.name }}
                  </span>
                </div>
              </div>
              
              <div class="card-body d-flex flex-column">
                <h5 class="card-title">{{ product.name }}</h5>
                <p class="card-text text-muted small flex-grow-1">
                  {{ product.shortDescription }}
                </p>
                
                <div class="product-info">
                  <div class="d-flex justify-content-between align-items-center mb-2">
                    <span class="h5 text-primary mb-0">NT$ {{ product.price.toLocaleString() }}</span>
                    <div class="rating">
                      <span class="text-warning">★ {{ product.rating }}</span>
                      <small class="text-muted">({{ product.soldCount }})</small>
                    </div>
                  </div>
                  
                  <div class="mb-2">
                    <small class="text-muted">
                      庫存: {{ product.inStock }} 件
                    </small>
                  </div>

                  <div class="d-flex gap-2">
                    <RouterLink 
                      :to="`/products/${product.id}`" 
                      class="btn btn-outline-primary btn-sm flex-fill"
                    >
                      查看詳情
                    </RouterLink>
                    <button 
                      @click="addToCart(product)" 
                      class="btn btn-primary btn-sm"
                      :disabled="product.inStock === 0"
                      v-if="authStore.isAuthenticated"
                    >
                      {{ product.inStock === 0 ? '缺貨' : '加入購物車' }}
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import { useProductStore } from '@/stores/product'
import { useCartStore } from '@/stores/cart'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const productStore = useProductStore()
const cartStore = useCartStore()
const authStore = useAuthStore()

const products = computed(() => productStore.products)
const categories = computed(() => productStore.categories)
const loading = computed(() => productStore.loading)

const filters = reactive({
  name: '',
  categoryId: ''
})

onMounted(async () => {
  // Get categories first
  await productStore.fetchCategories()
  
  // Check for URL parameters
  if (route.query.categoryId) {
    filters.categoryId = route.query.categoryId as string
  }
  
  // Load products with initial filters
  await applyFilters()
})

// Watch for route changes
watch(() => route.query, (newQuery) => {
  if (newQuery.categoryId && newQuery.categoryId !== filters.categoryId) {
    filters.categoryId = newQuery.categoryId as string
    applyFilters()
  }
})

const applyFilters = async () => {
  const params: any = {}
  
  if (filters.name) {
    params.name = filters.name
  }
  
  if (filters.categoryId) {
    params.categoryId = parseInt(filters.categoryId)
  }
  
  await productStore.fetchProducts(params)
}

const resetFilters = async () => {
  filters.name = ''
  filters.categoryId = ''
  await applyFilters()
}

const addToCart = async (product: any) => {
  const result = await cartStore.addToCart(product)
  if (result.success) {
    alert('商品已加入購物車！')
  } else {
    alert(result.error)
  }
}
</script>

<style scoped>
.product-card:hover {
  transform: translateY(-2px);
  transition: transform 0.2s ease-in-out;
}

.product-card:hover .card-img-top {
  transform: scale(1.05);
  transition: transform 0.2s ease-in-out;
}

.card-img-top {
  transition: transform 0.2s ease-in-out;
}

.rating {
  font-size: 0.9rem;
}

.product-info {
  border-top: 1px solid #eee;
  padding-top: 1rem;
}
</style>