<template>
  <div class="orders-view">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2>我的訂單</h2>
    </div>

    <div v-if="loading" class="text-center py-5">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>

    <div v-else-if="orders.length === 0" class="text-center py-5">
      <div class="text-muted">
        <i class="bi bi-bag-x fs-1 mb-3"></i>
        <h4>還沒有任何訂單</h4>
        <p class="mb-4">快去選購喜歡的商品吧！</p>
        <RouterLink to="/products" class="btn btn-primary">開始購物</RouterLink>
      </div>
    </div>

    <div v-else>
      <div class="row">
        <div class="col-12">
          <div 
            v-for="order in orders" 
            :key="order.id"
            class="card mb-4 shadow-sm"
          >
            <div class="card-header bg-light">
              <div class="row align-items-center">
                <div class="col-md-6">
                  <h6 class="mb-0">訂單編號: {{ order.orderNumber }}</h6>
                  <small class="text-muted">{{ formatDate(order.createdAt) }}</small>
                </div>
                <div class="col-md-6 text-md-end">
                  <span :class="getStatusBadgeClass(order.status)" class="badge">
                    {{ getStatusText(order.status) }}
                  </span>
                </div>
              </div>
            </div>
            
            <div class="card-body">
              <div class="row">
                <!-- Order Details -->
                <div class="col-md-8">
                  <div class="order-details">
                    <div class="row mb-3">
                      <div class="col-sm-6">
                        <strong>付款方式:</strong> {{ getPaymentMethodText(order.paymentMethod) }}
                      </div>
                      <div class="col-sm-6">
                        <strong>付款狀態:</strong> 
                        <span :class="order.isPaid ? 'text-success' : 'text-warning'">
                          {{ order.isPaid ? '已付款' : '待付款' }}
                        </span>
                      </div>
                    </div>
                    
                    <div class="row mb-3">
                      <div class="col-sm-6">
                        <strong>配送方式:</strong> {{ getShippingMethodText(order.shippingMethod) }}
                      </div>
                      <div class="col-sm-6">
                        <strong>配送狀態:</strong> {{ getShippingStatusText(order.shippingStatus) }}
                      </div>
                    </div>
                    
                    <div class="mb-3">
                      <strong>配送地址:</strong> {{ order.shippingAddress }}
                    </div>
                  </div>
                </div>
                
                <!-- Order Total -->
                <div class="col-md-4 text-md-end">
                  <div class="order-total">
                    <h5 class="text-primary mb-3">
                      總金額: NT$ {{ order.totalPrice.toLocaleString() }}
                    </h5>
                    
                    <div class="btn-group-vertical w-100">
                      <button 
                        @click="viewOrderItems(order.id)"
                        class="btn btn-outline-primary btn-sm mb-2"
                      >
                        查看訂單詳情
                      </button>
                      
                      <button 
                        v-if="order.status === 'PENDING' && !order.isPaid"
                        @click="payOrder(order)"
                        class="btn btn-success btn-sm mb-2"
                      >
                        立即付款
                      </button>
                      
                      <button 
                        v-if="order.status === 'PENDING'"
                        @click="cancelOrder(order)"
                        class="btn btn-outline-danger btn-sm"
                      >
                        取消訂單
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

    <!-- Order Items Modal -->
    <div 
      class="modal fade" 
      id="orderItemsModal" 
      tabindex="-1" 
      aria-labelledby="orderItemsModalLabel" 
      aria-hidden="true"
    >
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="orderItemsModalLabel">訂單詳情</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <div v-if="loadingItems" class="text-center py-3">
              <div class="spinner-border" role="status">
                <span class="visually-hidden">Loading...</span>
              </div>
            </div>
            
            <div v-else-if="orderItems.length > 0">
              <div 
                v-for="item in orderItems" 
                :key="item.productId"
                class="border-bottom pb-3 mb-3"
              >
                <div class="row align-items-center">
                  <div class="col-md-6">
                    <h6>{{ item.productName }}</h6>
                    <small class="text-muted">單價: NT$ {{ item.price.toLocaleString() }}</small>
                  </div>
                  <div class="col-md-3 text-center">
                    數量: {{ item.quantity }}
                  </div>
                  <div class="col-md-3 text-end">
                    <strong>NT$ {{ item.totalPrice.toLocaleString() }}</strong>
                  </div>
                </div>
              </div>
            </div>
            
            <div v-else>
              <p class="text-muted text-center">無法載入訂單詳情</p>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">關閉</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { orderAPI } from '@/services/api'

const orders = ref([])
const orderItems = ref([])
const loading = ref(false)
const loadingItems = ref(false)

onMounted(async () => {
  await fetchOrders()
})

const fetchOrders = async () => {
  loading.value = true
  try {
    const response = await orderAPI.getOrders()
    orders.value = response.data.data.order || []
  } catch (error) {
    console.error('Failed to fetch orders:', error)
  } finally {
    loading.value = false
  }
}

const viewOrderItems = async (orderId: number) => {
  loadingItems.value = true
  orderItems.value = []
  
  try {
    const response = await orderAPI.getOrderItems(orderId)
    orderItems.value = response.data.data.items || []
  } catch (error) {
    console.error('Failed to fetch order items:', error)
  } finally {
    loadingItems.value = false
  }
  
  // Show modal
  const modal = new (window as any).bootstrap.Modal(document.getElementById('orderItemsModal'))
  modal.show()
}

const payOrder = (order: any) => {
  // Implement payment logic
  alert(`支付訂單 ${order.orderNumber}`)
}

const cancelOrder = (order: any) => {
  if (confirm(`確定要取消訂單 ${order.orderNumber} 嗎？`)) {
    alert('訂單取消功能尚未實現')
  }
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('zh-TW', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const getStatusText = (status: string) => {
  const statusMap: { [key: string]: string } = {
    'PENDING': '處理中',
    'CONFIRMED': '已確認',
    'SHIPPING': '配送中',
    'DELIVERED': '已送達',
    'CANCELLED': '已取消'
  }
  return statusMap[status] || status
}

const getStatusBadgeClass = (status: string) => {
  const classMap: { [key: string]: string } = {
    'PENDING': 'bg-warning',
    'CONFIRMED': 'bg-info',
    'SHIPPING': 'bg-primary',
    'DELIVERED': 'bg-success',
    'CANCELLED': 'bg-danger'
  }
  return classMap[status] || 'bg-secondary'
}

const getPaymentMethodText = (method: string) => {
  const methodMap: { [key: string]: string } = {
    'CREDIT_CARD': '信用卡',
    'BANK_TRANSFER': '銀行轉帳',
    'CASH_ON_DELIVERY': '貨到付款'
  }
  return methodMap[method] || method
}

const getShippingMethodText = (method: string) => {
  const methodMap: { [key: string]: string } = {
    'STANDARD': '標準配送',
    'EXPRESS': '快速配送',
    'PICKUP': '自取'
  }
  return methodMap[method] || method
}

const getShippingStatusText = (status: string) => {
  const statusMap: { [key: string]: string } = {
    'PENDING': '準備中',
    'SHIPPED': '已發貨',
    'DELIVERED': '已送達'
  }
  return statusMap[status] || status
}
</script>

<style scoped>
.orders-view {
  margin-top: 1rem;
}

.card {
  border: 1px solid #e0e0e0;
  border-radius: 10px;
}

.card-header {
  border-bottom: 1px solid #e0e0e0;
}

.order-details strong {
  color: #495057;
}

.order-total {
  border-left: 2px solid #007bff;
  padding-left: 1rem;
}

@media (max-width: 768px) {
  .order-total {
    border-left: none;
    border-top: 2px solid #007bff;
    padding-left: 0;
    padding-top: 1rem;
    margin-top: 1rem;
  }
}
</style>