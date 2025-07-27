import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { cartAPI } from '@/services/api'

export const useCartStore = defineStore('cart', () => {
  const cartItems = ref([])
  const loading = ref(false)

  const totalItems = computed(() => 
    cartItems.value.reduce((sum: number, item: any) => sum + item.quantity, 0)
  )

  const totalPrice = computed(() => 
    cartItems.value.reduce((sum: number, item: any) => sum + (item.unitPrice * item.quantity), 0)
  )

  async function fetchCart() {
    loading.value = true
    try {
      const response = await cartAPI.getCart()
      cartItems.value = response.data.data.cart || []
      return { success: true }
    } catch (error: any) {
      return { success: false, error: error.response?.data?.message || '獲取購物車失敗' }
    } finally {
      loading.value = false
    }
  }

  async function addToCart(product: any) {
    try {
      await cartAPI.addToCart({
        productId: product.id,
        quantity: 1,
        unitPrice: product.price
      })
      await fetchCart()
      return { success: true }
    } catch (error: any) {
      return { success: false, error: error.response?.data?.message || '加入購物車失敗' }
    }
  }

  async function updateCartItem(productId: number, quantity: number) {
    try {
      await cartAPI.updateCartItem(productId, { quantity })
      await fetchCart()
      return { success: true }
    } catch (error: any) {
      return { success: false, error: error.response?.data?.message || '更新購物車失敗' }
    }
  }

  async function removeFromCart(productId: number) {
    try {
      await cartAPI.removeFromCart(productId)
      await fetchCart()
      return { success: true }
    } catch (error: any) {
      return { success: false, error: error.response?.data?.message || '移除商品失敗' }
    }
  }

  async function clearCart() {
    try {
      await cartAPI.clearCart()
      cartItems.value = []
      return { success: true }
    } catch (error: any) {
      return { success: false, error: error.response?.data?.message || '清空購物車失敗' }
    }
  }

  return {
    cartItems,
    loading,
    totalItems,
    totalPrice,
    fetchCart,
    addToCart,
    updateCartItem,
    removeFromCart,
    clearCart
  }
})