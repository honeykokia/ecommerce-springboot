<template>
  <div class="product-detail-view">
    <div v-if="loading" class="text-center py-5">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>

    <div v-else-if="product" class="row">
      <!-- Product Image -->
      <div class="col-lg-6 mb-4">
        <div class="product-image-container">
          <img 
            :src="product.imageURL" 
            :alt="product.name"
            class="img-fluid rounded shadow-lg"
            style="width: 100%; height: 400px; object-fit: cover;"
          >
        </div>
      </div>

      <!-- Product Details -->
      <div class="col-lg-6">
        <div class="product-details">
          <!-- Tags -->
          <div class="mb-3" v-if="product.tags && product.tags.length > 0">
            <span v-for="tag in product.tags" :key="tag.id" class="badge bg-danger me-2">
              {{ tag.name }}
            </span>
          </div>

          <!-- Product Name -->
          <h1 class="product-name mb-3">{{ product.name }}</h1>

          <!-- Rating and Sales -->
          <div class="d-flex align-items-center mb-3">
            <div class="rating me-3">
              <span class="text-warning fs-5">
                <span v-for="i in 5" :key="i">
                  {{ i <= Math.floor(product.rating) ? '★' : '☆' }}
                </span>
              </span>
              <span class="ms-2 text-muted">{{ product.rating }}/5</span>
            </div>
            <div class="text-muted">
              已售 {{ product.soldCount }} 件
            </div>
          </div>

          <!-- Price -->
          <div class="price-section mb-4">
            <div class="current-price">
              <span class="h2 text-primary fw-bold">NT$ {{ product.price.toLocaleString() }}</span>
            </div>
          </div>

          <!-- Stock -->
          <div class="stock-info mb-4">
            <div class="d-flex align-items-center">
              <span class="text-muted me-2">庫存:</span>
              <span :class="stockStatusClass">
                {{ product.inStock > 0 ? `${product.inStock} 件` : '缺貨' }}
              </span>
            </div>
          </div>

          <!-- Description -->
          <div class="description mb-4">
            <h5>商品描述</h5>
            <p class="text-muted">{{ product.shortDescription }}</p>
          </div>

          <!-- Quantity and Add to Cart -->
          <div class="purchase-section" v-if="authStore.isAuthenticated">
            <div class="row align-items-end">
              <div class="col-md-4 mb-3">
                <label for="quantity" class="form-label">數量</label>
                <div class="input-group">
                  <button 
                    class="btn btn-outline-secondary" 
                    type="button"
                    @click="decreaseQuantity"
                    :disabled="quantity <= 1"
                  >
                    -
                  </button>
                  <input 
                    type="number" 
                    class="form-control text-center" 
                    id="quantity"
                    v-model.number="quantity"
                    min="1"
                    :max="product.inStock"
                  >
                  <button 
                    class="btn btn-outline-secondary" 
                    type="button"
                    @click="increaseQuantity"
                    :disabled="quantity >= product.inStock"
                  >
                    +
                  </button>
                </div>
              </div>
              <div class="col-md-8 mb-3">
                <button 
                  @click="addToCart"
                  class="btn btn-primary btn-lg w-100"
                  :disabled="product.inStock === 0 || addingToCart"
                >
                  <span v-if="addingToCart" class="spinner-border spinner-border-sm me-2" role="status"></span>
                  {{ product.inStock === 0 ? '缺貨' : '加入購物車' }}
                </button>
              </div>
            </div>
          </div>

          <!-- Login Prompt -->
          <div v-else class="alert alert-info">
            <p class="mb-2">請先登入以購買商品</p>
            <RouterLink to="/login" class="btn btn-primary">立即登入</RouterLink>
          </div>
        </div>
      </div>
    </div>

    <!-- Error State -->
    <div v-else class="text-center py-5">
      <div class="text-muted">
        <h4>找不到該商品</h4>
        <p>商品可能已下架或不存在</p>
        <RouterLink to="/products" class="btn btn-primary">返回商品頁面</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'
import { useProductStore } from '@/stores/product'
import { useCartStore } from '@/stores/cart'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const productStore = useProductStore()
const cartStore = useCartStore()
const authStore = useAuthStore()

const quantity = ref(1)
const addingToCart = ref(false)

const product = computed(() => productStore.currentProduct)
const loading = computed(() => productStore.loading)

const stockStatusClass = computed(() => {
  if (!product.value) return 'text-muted'
  return product.value.inStock > 0 ? 'text-success' : 'text-danger'
})

onMounted(async () => {
  const productId = parseInt(route.params.id as string)
  if (isNaN(productId)) {
    router.push('/products')
    return
  }
  
  const result = await productStore.fetchProduct(productId)
  if (!result.success) {
    // Handle error
    console.error('Failed to load product:', result.error)
  }
})

const increaseQuantity = () => {
  if (product.value && quantity.value < product.value.inStock) {
    quantity.value++
  }
}

const decreaseQuantity = () => {
  if (quantity.value > 1) {
    quantity.value--
  }
}

const addToCart = async () => {
  if (!product.value) return
  
  addingToCart.value = true
  
  try {
    // Add the product multiple times for the quantity
    for (let i = 0; i < quantity.value; i++) {
      const result = await cartStore.addToCart(product.value)
      if (!result.success) {
        alert(result.error)
        return
      }
    }
    
    alert(`已將 ${quantity.value} 件商品加入購物車！`)
    quantity.value = 1
  } catch (error) {
    alert('加入購物車失敗')
  } finally {
    addingToCart.value = false
  }
}
</script>

<style scoped>
.product-detail-view {
  margin-top: 1rem;
}

.product-name {
  color: #333;
  font-weight: 600;
}

.price-section {
  background-color: #f8f9fa;
  padding: 1rem;
  border-radius: 8px;
  border-left: 4px solid #007bff;
}

.rating {
  font-size: 1.1rem;
}

.stock-info {
  font-size: 1.1rem;
}

.purchase-section {
  background-color: #f8f9fa;
  padding: 1.5rem;
  border-radius: 8px;
  border: 1px solid #dee2e6;
}

.input-group input[type="number"] {
  max-width: 80px;
}

.product-image-container {
  position: relative;
  overflow: hidden;
  border-radius: 8px;
}

.product-image-container img:hover {
  transform: scale(1.05);
  transition: transform 0.3s ease-in-out;
}
</style>