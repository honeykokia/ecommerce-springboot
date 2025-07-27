<template>
  <div class="admin-view">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2>管理後台</h2>
    </div>

    <!-- Navigation Tabs -->
    <ul class="nav nav-tabs mb-4" id="adminTabs" role="tablist">
      <li class="nav-item" role="presentation">
        <button 
          class="nav-link active" 
          id="users-tab" 
          data-bs-toggle="tab" 
          data-bs-target="#users" 
          type="button" 
          role="tab"
          @click="activeTab = 'users'"
        >
          用戶管理
        </button>
      </li>
      <li class="nav-item" role="presentation">
        <button 
          class="nav-link" 
          id="products-tab" 
          data-bs-toggle="tab" 
          data-bs-target="#products" 
          type="button" 
          role="tab"
          @click="activeTab = 'products'"
        >
          商品管理
        </button>
      </li>
      <li class="nav-item" role="presentation">
        <button 
          class="nav-link" 
          id="orders-tab" 
          data-bs-toggle="tab" 
          data-bs-target="#orders" 
          type="button" 
          role="tab"
          @click="activeTab = 'orders'"
        >
          訂單管理
        </button>
      </li>
    </ul>

    <!-- Tab Content -->
    <div class="tab-content" id="adminTabsContent">
      <!-- Users Management -->
      <div 
        class="tab-pane fade show active" 
        id="users" 
        role="tabpanel" 
        v-show="activeTab === 'users'"
      >
        <div class="card">
          <div class="card-header bg-primary text-white">
            <h5 class="mb-0">用戶列表</h5>
          </div>
          <div class="card-body p-0">
            <div v-if="loadingUsers" class="text-center py-4">
              <div class="spinner-border" role="status">
                <span class="visually-hidden">Loading...</span>
              </div>
            </div>
            
            <div v-else-if="users.length === 0" class="text-center py-4 text-muted">
              沒有用戶資料
            </div>
            
            <div v-else class="table-responsive">
              <table class="table table-hover mb-0">
                <thead class="bg-light">
                  <tr>
                    <th>姓名</th>
                    <th>電子郵件</th>
                    <th>註冊時間</th>
                    <th>狀態</th>
                    <th>操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="user in users" :key="user.id">
                    <td>{{ user.name }}</td>
                    <td>{{ user.email }}</td>
                    <td>{{ formatDate(user.createdAt) }}</td>
                    <td>
                      <span class="badge bg-success">正常</span>
                    </td>
                    <td>
                      <button class="btn btn-sm btn-outline-warning me-2">
                        停用
                      </button>
                      <button class="btn btn-sm btn-outline-danger">
                        刪除
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <!-- Products Management -->
      <div 
        class="tab-pane fade" 
        id="products" 
        role="tabpanel" 
        v-show="activeTab === 'products'"
      >
        <div class="d-flex justify-content-between align-items-center mb-3">
          <h5>商品管理</h5>
          <button class="btn btn-primary" @click="showProductModal()">
            新增商品
          </button>
        </div>
        
        <div class="card">
          <div class="card-body p-0">
            <div v-if="loadingProducts" class="text-center py-4">
              <div class="spinner-border" role="status">
                <span class="visually-hidden">Loading...</span>
              </div>
            </div>
            
            <div v-else-if="adminProducts.length === 0" class="text-center py-4 text-muted">
              沒有商品資料
            </div>
            
            <div v-else class="table-responsive">
              <table class="table table-hover mb-0">
                <thead class="bg-light">
                  <tr>
                    <th>圖片</th>
                    <th>商品名稱</th>
                    <th>價格</th>
                    <th>庫存</th>
                    <th>分類</th>
                    <th>狀態</th>
                    <th>操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="product in adminProducts" :key="product.id">
                    <td>
                      <img 
                        :src="product.imageURL" 
                        alt="product" 
                        style="width: 50px; height: 50px; object-fit: cover; border-radius: 4px;"
                      >
                    </td>
                    <td>{{ product.name }}</td>
                    <td>NT$ {{ product.price.toLocaleString() }}</td>
                    <td>{{ product.inStock }}</td>
                    <td>{{ getCategoryName(product.categoryId) }}</td>
                    <td>
                      <span class="badge bg-success">上架中</span>
                    </td>
                    <td>
                      <button class="btn btn-sm btn-outline-primary me-2" @click="editProduct(product)">
                        編輯
                      </button>
                      <button class="btn btn-sm btn-outline-danger" @click="deleteProduct(product.id)">
                        刪除
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <!-- Orders Management -->
      <div 
        class="tab-pane fade" 
        id="orders" 
        role="tabpanel" 
        v-show="activeTab === 'orders'"
      >
        <div class="card">
          <div class="card-header bg-primary text-white">
            <h5 class="mb-0">訂單列表</h5>
          </div>
          <div class="card-body p-0">
            <div v-if="loadingOrders" class="text-center py-4">
              <div class="spinner-border" role="status">
                <span class="visually-hidden">Loading...</span>
              </div>
            </div>
            
            <div v-else-if="adminOrders.length === 0" class="text-center py-4 text-muted">
              沒有訂單資料
            </div>
            
            <div v-else class="table-responsive">
              <table class="table table-hover mb-0">
                <thead class="bg-light">
                  <tr>
                    <th>訂單編號</th>
                    <th>用戶</th>
                    <th>金額</th>
                    <th>付款狀態</th>
                    <th>訂單狀態</th>
                    <th>建立時間</th>
                    <th>操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="order in adminOrders" :key="order.id">
                    <td>{{ order.orderNumber }}</td>
                    <td>用戶 ID: {{ order.userId }}</td>
                    <td>NT$ {{ order.totalPrice.toLocaleString() }}</td>
                    <td>
                      <span :class="order.isPaid ? 'badge bg-success' : 'badge bg-warning'">
                        {{ order.isPaid ? '已付款' : '待付款' }}
                      </span>
                    </td>
                    <td>
                      <span :class="getOrderStatusClass(order.status)" class="badge">
                        {{ getOrderStatusText(order.status) }}
                      </span>
                    </td>
                    <td>{{ formatDate(order.createdAt) }}</td>
                    <td>
                      <select 
                        class="form-select form-select-sm" 
                        @change="updateOrderStatus(order.id, $event.target.value)"
                        :value="order.status"
                      >
                        <option value="PENDING">處理中</option>
                        <option value="CONFIRMED">已確認</option>
                        <option value="SHIPPING">配送中</option>
                        <option value="DELIVERED">已送達</option>
                        <option value="CANCELLED">已取消</option>
                      </select>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Product Modal -->
    <div class="modal fade" id="productModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ editingProduct ? '編輯商品' : '新增商品' }}</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveProduct">
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="productName" class="form-label">商品名稱</label>
                  <input 
                    type="text" 
                    class="form-control" 
                    id="productName" 
                    v-model="productForm.name"
                    required
                  >
                </div>
                <div class="col-md-6 mb-3">
                  <label for="productPrice" class="form-label">價格</label>
                  <input 
                    type="number" 
                    class="form-control" 
                    id="productPrice" 
                    v-model.number="productForm.price"
                    required
                  >
                </div>
              </div>
              
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="productStock" class="form-label">庫存</label>
                  <input 
                    type="number" 
                    class="form-control" 
                    id="productStock" 
                    v-model.number="productForm.inStock"
                    required
                  >
                </div>
                <div class="col-md-6 mb-3">
                  <label for="productCategory" class="form-label">分類</label>
                  <select 
                    class="form-select" 
                    id="productCategory" 
                    v-model="productForm.categoryId"
                    required
                  >
                    <option value="">請選擇分類</option>
                    <option 
                      v-for="category in categories" 
                      :key="category.id" 
                      :value="category.id"
                    >
                      {{ category.name }}
                    </option>
                  </select>
                </div>
              </div>
              
              <div class="mb-3">
                <label for="productImage" class="form-label">圖片網址</label>
                <input 
                  type="url" 
                  class="form-control" 
                  id="productImage" 
                  v-model="productForm.imageURL"
                  required
                >
              </div>
              
              <div class="mb-3">
                <label for="productDescription" class="form-label">商品描述</label>
                <textarea 
                  class="form-control" 
                  id="productDescription" 
                  rows="3"
                  v-model="productForm.shortDescription"
                  required
                ></textarea>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
            <button type="button" class="btn btn-primary" @click="saveProduct">
              {{ editingProduct ? '更新' : '新增' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { adminAPI } from '@/services/api'
import { useProductStore } from '@/stores/product'

const productStore = useProductStore()

const activeTab = ref('users')
const users = ref([])
const adminProducts = ref([])
const adminOrders = ref([])
const categories = ref([])

const loadingUsers = ref(false)
const loadingProducts = ref(false)
const loadingOrders = ref(false)

const editingProduct = ref(false)
const productForm = reactive({
  id: null,
  name: '',
  price: 0,
  inStock: 0,
  categoryId: '',
  imageURL: '',
  shortDescription: ''
})

onMounted(async () => {
  await Promise.all([
    fetchUsers(),
    fetchProducts(),
    fetchOrders(),
    fetchCategories()
  ])
})

const fetchUsers = async () => {
  loadingUsers.value = true
  try {
    const response = await adminAPI.getUsers()
    users.value = response.data.data.users || []
  } catch (error) {
    console.error('Failed to fetch users:', error)
  } finally {
    loadingUsers.value = false
  }
}

const fetchProducts = async () => {
  loadingProducts.value = true
  try {
    await productStore.fetchProducts()
    adminProducts.value = productStore.products
  } catch (error) {
    console.error('Failed to fetch products:', error)
  } finally {
    loadingProducts.value = false
  }
}

const fetchOrders = async () => {
  loadingOrders.value = true
  try {
    const response = await adminAPI.getOrders()
    adminOrders.value = response.data.data.orders || []
  } catch (error) {
    console.error('Failed to fetch orders:', error)
  } finally {
    loadingOrders.value = false
  }
}

const fetchCategories = async () => {
  await productStore.fetchCategories()
  categories.value = productStore.categories
}

const showProductModal = (product?: any) => {
  if (product) {
    editingProduct.value = true
    Object.assign(productForm, product)
  } else {
    editingProduct.value = false
    Object.assign(productForm, {
      id: null,
      name: '',
      price: 0,
      inStock: 0,
      categoryId: '',
      imageURL: '',
      shortDescription: ''
    })
  }
  
  const modal = new (window as any).bootstrap.Modal(document.getElementById('productModal'))
  modal.show()
}

const editProduct = (product: any) => {
  showProductModal(product)
}

const saveProduct = async () => {
  try {
    if (editingProduct.value && productForm.id) {
      await adminAPI.updateProduct(productForm.id, productForm)
      alert('商品更新成功！')
    } else {
      await adminAPI.createProduct(productForm)
      alert('商品新增成功！')
    }
    
    // Close modal and refresh products
    const modal = (window as any).bootstrap.Modal.getInstance(document.getElementById('productModal'))
    modal.hide()
    await fetchProducts()
  } catch (error) {
    alert('操作失敗！')
  }
}

const deleteProduct = async (productId: number) => {
  if (confirm('確定要刪除此商品嗎？')) {
    try {
      await adminAPI.deleteProduct(productId)
      alert('商品刪除成功！')
      await fetchProducts()
    } catch (error) {
      alert('刪除失敗！')
    }
  }
}

const updateOrderStatus = async (orderId: number, status: string) => {
  try {
    await adminAPI.updateOrderStatus(orderId, { status })
    alert('訂單狀態更新成功！')
    await fetchOrders()
  } catch (error) {
    alert('更新失敗！')
  }
}

const getCategoryName = (categoryId: number) => {
  const category = categories.value.find(c => c.id === categoryId)
  return category?.name || '未知分類'
}

const getOrderStatusText = (status: string) => {
  const statusMap: { [key: string]: string } = {
    'PENDING': '處理中',
    'CONFIRMED': '已確認',
    'SHIPPING': '配送中',
    'DELIVERED': '已送達',
    'CANCELLED': '已取消'
  }
  return statusMap[status] || status
}

const getOrderStatusClass = (status: string) => {
  const classMap: { [key: string]: string } = {
    'PENDING': 'bg-warning',
    'CONFIRMED': 'bg-info',
    'SHIPPING': 'bg-primary',
    'DELIVERED': 'bg-success',
    'CANCELLED': 'bg-danger'
  }
  return classMap[status] || 'bg-secondary'
}

const formatDate = (dateString?: string) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-TW')
}
</script>

<style scoped>
.admin-view {
  margin-top: 1rem;
}

.nav-tabs .nav-link {
  color: #666;
  border: none;
  border-bottom: 3px solid transparent;
}

.nav-tabs .nav-link.active {
  color: #007bff;
  border-bottom-color: #007bff;
  background-color: transparent;
}

.table th {
  border-top: none;
  font-weight: 600;
  color: #495057;
}

.card {
  border: none;
  box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
}

.badge {
  font-size: 0.75rem;
}

.form-select-sm {
  font-size: 0.875rem;
}

@media (max-width: 768px) {
  .table-responsive {
    font-size: 0.875rem;
  }
  
  .btn-sm {
    padding: 0.25rem 0.5rem;
    font-size: 0.75rem;
  }
}
</style>