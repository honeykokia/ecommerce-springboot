<template>
  <div class="checkout-view">
    <div class="row">
      <!-- Order Summary -->
      <div class="col-lg-4 order-lg-2 mb-4">
        <div class="card sticky-top" style="top: 1rem;">
          <div class="card-header bg-primary text-white">
            <h5 class="mb-0">訂單摘要</h5>
          </div>
          <div class="card-body">
            <div v-if="cartItems.length > 0">
              <div 
                v-for="item in cartItems" 
                :key="item.id"
                class="d-flex justify-content-between align-items-center mb-2"
              >
                <div class="flex-grow-1">
                  <small>商品 ID: {{ item.productId }}</small>
                  <div class="text-muted small">{{ item.quantity }} x NT$ {{ item.unitPrice.toLocaleString() }}</div>
                </div>
                <span class="fw-bold">NT$ {{ (item.quantity * item.unitPrice).toLocaleString() }}</span>
              </div>
              
              <hr>
              
              <div class="d-flex justify-content-between mb-2">
                <span>商品小計</span>
                <span>NT$ {{ totalPrice.toLocaleString() }}</span>
              </div>
              <div class="d-flex justify-content-between mb-2">
                <span>運費</span>
                <span>NT$ 0</span>
              </div>
              <hr>
              <div class="d-flex justify-content-between fw-bold fs-5">
                <span>總計</span>
                <span class="text-primary">NT$ {{ totalPrice.toLocaleString() }}</span>
              </div>
            </div>
            <div v-else class="text-center text-muted">
              購物車是空的
            </div>
          </div>
        </div>
      </div>

      <!-- Checkout Form -->
      <div class="col-lg-8 order-lg-1">
        <div class="card shadow">
          <div class="card-header bg-light">
            <h4 class="mb-0">結帳資訊</h4>
          </div>
          <div class="card-body p-4">
            <div v-if="errorMessage" class="alert alert-danger" role="alert">
              {{ errorMessage }}
            </div>

            <form @submit.prevent="submitOrder">
              <!-- Personal Information -->
              <div class="section-header mb-3">
                <h5 class="text-primary">個人資訊</h5>
                <hr>
              </div>
              
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="name" class="form-label">姓名</label>
                  <input 
                    type="text" 
                    class="form-control" 
                    id="name" 
                    v-model="checkoutData.name"
                    required
                  >
                </div>
                <div class="col-md-6 mb-3">
                  <label for="email" class="form-label">電子郵件</label>
                  <input 
                    type="email" 
                    class="form-control" 
                    id="email" 
                    v-model="checkoutData.email"
                    required
                  >
                </div>
              </div>
              
              <div class="mb-3">
                <label for="phone" class="form-label">聯絡電話</label>
                <input 
                  type="tel" 
                  class="form-control" 
                  id="phone" 
                  v-model="checkoutData.phone"
                  required
                >
              </div>

              <!-- Shipping Information -->
              <div class="section-header mb-3 mt-4">
                <h5 class="text-primary">配送資訊</h5>
                <hr>
              </div>
              
              <div class="mb-3">
                <label for="shippingMethod" class="form-label">配送方式</label>
                <select 
                  class="form-select" 
                  id="shippingMethod" 
                  v-model="checkoutData.shippingMethod"
                  required
                >
                  <option value="">請選擇配送方式</option>
                  <option value="STANDARD">標準配送 (免費)</option>
                  <option value="EXPRESS">快速配送 (+NT$ 100)</option>
                  <option value="PICKUP">自取</option>
                </select>
              </div>
              
              <div class="mb-3">
                <label for="shippingAddress" class="form-label">配送地址</label>
                <textarea 
                  class="form-control" 
                  id="shippingAddress" 
                  rows="3"
                  v-model="checkoutData.shippingAddress"
                  placeholder="請輸入完整的配送地址"
                  required
                ></textarea>
              </div>

              <!-- Payment Information -->
              <div class="section-header mb-3 mt-4">
                <h5 class="text-primary">付款資訊</h5>
                <hr>
              </div>
              
              <div class="mb-3">
                <label for="paymentMethod" class="form-label">付款方式</label>
                <select 
                  class="form-select" 
                  id="paymentMethod" 
                  v-model="checkoutData.paymentMethod"
                  required
                >
                  <option value="">請選擇付款方式</option>
                  <option value="CREDIT_CARD">信用卡</option>
                  <option value="BANK_TRANSFER">銀行轉帳</option>
                  <option value="CASH_ON_DELIVERY">貨到付款</option>
                </select>
              </div>

              <!-- Credit Card Information (show only if credit card is selected) -->
              <div v-if="checkoutData.paymentMethod === 'CREDIT_CARD'" class="card bg-light p-3 mb-3">
                <h6 class="mb-3">信用卡資訊</h6>
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <label for="cardNumber" class="form-label">卡號</label>
                    <input 
                      type="text" 
                      class="form-control" 
                      id="cardNumber" 
                      v-model="checkoutData.cardNumber"
                      placeholder="1234 5678 9012 3456"
                      :required="checkoutData.paymentMethod === 'CREDIT_CARD'"
                    >
                  </div>
                  <div class="col-md-3 mb-3">
                    <label for="expiryDate" class="form-label">有效期限</label>
                    <input 
                      type="text" 
                      class="form-control" 
                      id="expiryDate" 
                      v-model="checkoutData.expiryDate"
                      placeholder="MM/YY"
                      :required="checkoutData.paymentMethod === 'CREDIT_CARD'"
                    >
                  </div>
                  <div class="col-md-3 mb-3">
                    <label for="cvv" class="form-label">CVV</label>
                    <input 
                      type="text" 
                      class="form-control" 
                      id="cvv" 
                      v-model="checkoutData.cvv"
                      placeholder="123"
                      :required="checkoutData.paymentMethod === 'CREDIT_CARD'"
                    >
                  </div>
                </div>
                <div class="mb-3">
                  <label for="cardholderName" class="form-label">持卡人姓名</label>
                  <input 
                    type="text" 
                    class="form-control" 
                    id="cardholderName" 
                    v-model="checkoutData.cardholderName"
                    :required="checkoutData.paymentMethod === 'CREDIT_CARD'"
                  >
                </div>
              </div>

              <!-- Special Instructions -->
              <div class="mb-4">
                <label for="notes" class="form-label">特殊需求 (選填)</label>
                <textarea 
                  class="form-control" 
                  id="notes" 
                  rows="3"
                  v-model="checkoutData.notes"
                  placeholder="如有特殊配送需求或備註，請在此說明"
                ></textarea>
              </div>

              <!-- Terms and Conditions -->
              <div class="mb-4">
                <div class="form-check">
                  <input 
                    class="form-check-input" 
                    type="checkbox" 
                    id="agreeTerms"
                    v-model="agreeTerms"
                    required
                  >
                  <label class="form-check-label" for="agreeTerms">
                    我同意 <a href="#" class="text-decoration-none">服務條款</a> 和 <a href="#" class="text-decoration-none">隱私政策</a>
                  </label>
                </div>
              </div>

              <!-- Submit Button -->
              <div class="text-end">
                <RouterLink to="/cart" class="btn btn-outline-secondary me-3">
                  返回購物車
                </RouterLink>
                <button 
                  type="submit" 
                  class="btn btn-primary btn-lg"
                  :disabled="submitting || cartItems.length === 0 || !agreeTerms"
                >
                  <span v-if="submitting" class="spinner-border spinner-border-sm me-2" role="status"></span>
                  確認訂單 (NT$ {{ totalPrice.toLocaleString() }})
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import { useAuthStore } from '@/stores/auth'
import { orderAPI } from '@/services/api'

const router = useRouter()
const cartStore = useCartStore()
const authStore = useAuthStore()

const errorMessage = ref('')
const submitting = ref(false)
const agreeTerms = ref(false)

const cartItems = computed(() => cartStore.cartItems)
const totalPrice = computed(() => cartStore.totalPrice)

const checkoutData = reactive({
  name: '',
  email: '',
  phone: '',
  shippingMethod: '',
  shippingAddress: '',
  paymentMethod: '',
  cardNumber: '',
  expiryDate: '',
  cvv: '',
  cardholderName: '',
  notes: ''
})

onMounted(async () => {
  // Load cart items
  await cartStore.fetchCart()
  
  // Pre-fill user information if available
  if (authStore.user) {
    checkoutData.name = authStore.user.name || ''
    checkoutData.email = authStore.user.email || ''
  }
  
  // Redirect if cart is empty
  if (cartItems.value.length === 0) {
    router.push('/cart')
  }
})

const submitOrder = async () => {
  if (cartItems.value.length === 0) {
    errorMessage.value = '購物車是空的'
    return
  }

  submitting.value = true
  errorMessage.value = ''

  try {
    const orderData = {
      shippingAddress: checkoutData.shippingAddress,
      shippingMethod: checkoutData.shippingMethod,
      paymentMethod: checkoutData.paymentMethod,
      notes: checkoutData.notes,
      customerInfo: {
        name: checkoutData.name,
        email: checkoutData.email,
        phone: checkoutData.phone
      }
    }

    const response = await orderAPI.createOrder(orderData)
    
    if (response.status === 200) {
      // Clear cart after successful order
      await cartStore.clearCart()
      
      // Show success message and redirect
      alert('訂單建立成功！')
      router.push('/orders')
    }
  } catch (error: any) {
    errorMessage.value = error.response?.data?.message || '訂單建立失敗'
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.checkout-view {
  margin-top: 1rem;
}

.section-header h5 {
  margin-bottom: 0.5rem;
}

.card {
  border: none;
  border-radius: 10px;
}

.card-header {
  border-radius: 10px 10px 0 0;
}

.form-label {
  font-weight: 600;
  color: #495057;
}

.bg-light {
  background-color: #f8f9fa !important;
  border-radius: 8px;
}

@media (max-width: 991px) {
  .sticky-top {
    position: static !important;
  }
}
</style>