import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080'

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  }
})

// Request interceptor to add auth token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// Response interceptor to handle errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export const authAPI = {
  login: (data: any) => api.post('/users/login', data),
  register: (data: any) => api.post('/users/register', data),
  getProfile: () => api.get('/users/me'),
  updateProfile: (data: any) => api.put('/users/me', data),
  changePassword: (data: any) => api.patch('/users/me/password', data),
  verifyEmail: (token: string) => api.get(`/users/verify/${token}`)
}

export const productAPI = {
  getProducts: (params?: any) => api.get('/products', { params }),
  getProduct: (id: number) => api.get(`/products/${id}`)
}

export const categoryAPI = {
  getCategories: () => api.get('/categories')
}

export const cartAPI = {
  getCart: () => api.get('/carts/me'),
  addToCart: (data: any) => api.post('/carts/me', data),
  updateCartItem: (productId: number, data: any) => api.patch(`/carts/me/${productId}`, data),
  removeFromCart: (productId: number) => api.delete(`/carts/me/${productId}`),
  clearCart: () => api.delete('/carts/me')
}

export const orderAPI = {
  getOrders: () => api.get('/orders/me'),
  createOrder: (data: any) => api.post('/orders/me', data),
  getOrderItems: (orderId: number) => api.get(`/orders/${orderId}/items`)
}

export const promotionAPI = {
  getPromotions: () => api.get('/promotions')
}

export const adminAPI = {
  getUsers: () => api.get('/admin/users'),
  updateUserStatus: (userId: number, data: any) => api.patch(`/admin/users/${userId}/status`, data),
  createProduct: (data: any) => api.post('/admin/products', data),
  updateProduct: (productId: number, data: any) => api.put(`/admin/products/${productId}`, data),
  deleteProduct: (productId: number) => api.delete(`/admin/products/${productId}`),
  getOrders: () => api.get('/admin/orders'),
  updateOrderStatus: (orderId: number, data: any) => api.patch(`/admin/orders/${orderId}`, data),
  deleteOrder: (orderId: number) => api.delete(`/admin/orders/${orderId}`)
}

export default api