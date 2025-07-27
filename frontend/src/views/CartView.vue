<template>
  <div class="cart-view">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2>購物車</h2>
      <button 
        v-if="cartItems.length > 0" 
        @click="clearCart" 
        class="btn btn-outline-danger"
      >
        清空購物車
      </button>
    </div>

    <div v-if="loading" class="text-center py-5">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>

    <div v-else-if="cartItems.length === 0" class="text-center py-5">
      <div class="text-muted">
        <i class="bi bi-cart-x fs-1 mb-3"></i>
        <h4>購物車是空的</h4>
        <p class="mb-4">快去選購喜歡的商品吧！</p>
        <RouterLink to="/products" class="btn btn-primary">開始購物</RouterLink>
      </div>
    </div>

    <div v-else>
      <div class="row">
        <!-- Cart Items -->
        <div class="col-lg-8">
          <div class="card">
            <div class="card-body p-4">
              <div 
                v-for="item in cartItems" 
                :key="item.id"
                class="cart-item border-bottom pb-3 mb-3"
              >
                <div class="row align-items-center">
                  <div class="col-md-6">
                    <div class="d-flex align-items-center">
                      <img 
                        :src="getProductImage(item)" 
                        alt="product"
                        class="cart-item-image me-3"
                        style="width: 80px; height: 80px; object-fit: cover; border-radius: 8px;"
                      >
                      <div>
                        <h6 class="mb-1">{{ getProductName(item) }}</h6>
                        <p class="text-muted small mb-0">單價: NT$ {{ item.unitPrice.toLocaleString() }}</p>
                      </div>
                    </div>
                  </div>
                  
                  <div class="col-md-3">
                    <div class="quantity-controls">
                      <div class="input-group">
                        <button 
                          class="btn btn-outline-secondary btn-sm" 
                          @click="updateQuantity(item, item.quantity - 1)"
                          :disabled="item.quantity <= 1"
                        >
                          -
                        </button>
                        <input 
                          type="number" 
                          class="form-control form-control-sm text-center" 
                          :value="item.quantity"
                          @change="updateQuantity(item, $event.target.value)"
                          min="1"
                          style="max-width: 60px;"
                        >
                        <button 
                          class="btn btn-outline-secondary btn-sm" 
                          @click="updateQuantity(item, item.quantity + 1)"
                        >
                          +
                        </button>
                      </div>
                    </div>
                  </div>
                  
                  <div class="col-md-2 text-end">
                    <div class="fw-bold text-primary">
                      NT$ {{ (item.unitPrice * item.quantity).toLocaleString() }}
                    </div>
                  </div>
                  
                  <div class="col-md-1 text-end">
                    <button 
                      @click="removeItem(item)"
                      class="btn btn-outline-danger btn-sm"
                      title="移除商品"
                    >
                      ✕
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Order Summary -->
        <div class="col-lg-4">
          <div class="card sticky-top" style="top: 1rem;">
            <div class="card-header bg-light">
              <h5 class="mb-0">訂單摘要</h5>
            </div>
            <div class="card-body">
              <div class="d-flex justify-content-between mb-2">
                <span>商品小計</span>
                <span>NT$ {{ totalPrice.toLocaleString() }}</span>
              </div>
              <div class="d-flex justify-content-between mb-2">
                <span>運費</span>
                <span>NT$ 0</span>
              </div>
              <hr>
              <div class="d-flex justify-content-between mb-3 fw-bold fs-5">
                <span>總計</span>
                <span class="text-primary">NT$ {{ totalPrice.toLocaleString() }}</span>
              </div>
              
              <div class="mb-3">
                <div class="text-muted small">
                  共 {{ totalItems }} 件商品
                </div>
              </div>

              <RouterLink 
                to="/checkout" 
                class="btn btn-primary w-100 btn-lg"
              >
                前往結帳
              </RouterLink>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { useCartStore } from '@/stores/cart'

const cartStore = useCartStore()

const cartItems = computed(() => cartStore.cartItems)
const totalItems = computed(() => cartStore.totalItems)
const totalPrice = computed(() => cartStore.totalPrice)
const loading = computed(() => cartStore.loading)

onMounted(async () => {
  await cartStore.fetchCart()
})

const updateQuantity = async (item: any, newQuantity: number) => {
  const quantity = parseInt(newQuantity.toString())
  if (quantity < 1) return
  
  const result = await cartStore.updateCartItem(item.productId, quantity)
  if (!result.success) {
    alert(result.error)
  }
}

const removeItem = async (item: any) => {
  if (confirm('確定要移除此商品嗎？')) {
    const result = await cartStore.removeFromCart(item.productId)
    if (!result.success) {
      alert(result.error)
    }
  }
}

const clearCart = async () => {
  if (confirm('確定要清空購物車嗎？')) {
    const result = await cartStore.clearCart()
    if (!result.success) {
      alert(result.error)
    }
  }
}

// Helper functions to get product details
// Since the API doesn't return full product details in cart items,
// we'll use placeholder data for now
const getProductName = (item: any) => {
  return `商品 ID: ${item.productId || item.id}`
}

const getProductImage = (item: any) => {
  return '/upload/defaultProduct.jpg' // Placeholder image
}
</script>

<style scoped>
.cart-item:last-child {
  border-bottom: none !important;
  margin-bottom: 0 !important;
  padding-bottom: 0 !important;
}

.quantity-controls input[type="number"] {
  -webkit-appearance: textfield;
  -moz-appearance: textfield;
  appearance: textfield;
}

.quantity-controls input[type="number"]::-webkit-outer-spin-button,
.quantity-controls input[type="number"]::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.cart-item-image {
  border: 1px solid #ddd;
}

.btn-outline-danger:hover {
  transform: scale(1.1);
  transition: transform 0.2s ease-in-out;
}

@media (max-width: 768px) {
  .cart-item .row > div {
    margin-bottom: 0.5rem;
  }
}
</style>