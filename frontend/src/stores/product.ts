import { ref } from 'vue'
import { defineStore } from 'pinia'
import { productAPI, categoryAPI, promotionAPI } from '@/services/api'

export const useProductStore = defineStore('product', () => {
  const products = ref([])
  const categories = ref([])
  const promotions = ref([])
  const currentProduct = ref(null)
  const loading = ref(false)

  async function fetchProducts(params?: any) {
    loading.value = true
    try {
      const response = await productAPI.getProducts(params)
      products.value = response.data.data.products || []
      return { success: true, data: products.value }
    } catch (error: any) {
      return { success: false, error: error.response?.data?.message || '獲取產品列表失敗' }
    } finally {
      loading.value = false
    }
  }

  async function fetchProduct(id: number) {
    loading.value = true
    try {
      const response = await productAPI.getProduct(id)
      currentProduct.value = response.data.data
      return { success: true, data: currentProduct.value }
    } catch (error: any) {
      return { success: false, error: error.response?.data?.message || '獲取產品詳情失敗' }
    } finally {
      loading.value = false
    }
  }

  async function fetchCategories() {
    try {
      const response = await categoryAPI.getCategories()
      categories.value = response.data.data.categories || []
      return { success: true, data: categories.value }
    } catch (error: any) {
      return { success: false, error: error.response?.data?.message || '獲取分類失敗' }
    }
  }

  async function fetchPromotions() {
    try {
      const response = await promotionAPI.getPromotions()
      promotions.value = response.data.data.promotions || []
      return { success: true, data: promotions.value }
    } catch (error: any) {
      return { success: false, error: error.response?.data?.message || '獲取促銷活動失敗' }
    }
  }

  return {
    products,
    categories,
    promotions,
    currentProduct,
    loading,
    fetchProducts,
    fetchProduct,
    fetchCategories,
    fetchPromotions
  }
})