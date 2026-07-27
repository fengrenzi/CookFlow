import request from '@/utils/request'

// 获取购物车列表
export function getCartList() {
  return request({
    url: '/cart/list',
    method: 'get'
  })
}

// 添加项目到购物车
export function addToCart(data: { itemType: string; itemId: string; quantity: number }) {
  return request({
    url: '/cart/add',
    method: 'post',
    data
  })
}

// 更新购物车项数量
export function updateCartItem(cartId: string, data: { quantity: number }) {
  return request({
    url: `/cart/${cartId}`,
    method: 'put',
    data
  })
}

// 删除购物车项
export function removeCartItem(cartId: string) {
  return request({
    url: `/cart/${cartId}`,
    method: 'delete'
  })
}

// 生成购物清单文本（选中项ID列表）
export function generateShoppingListText(selectedCartIds?: string[]) {
  return request({
    url: '/cart/generate-text',
    method: 'post',
    data: selectedCartIds || []
  })
}

// 生成购物清单图片（后端直接返回图片URL，或前端生成，此处仅示意）
export function generateShoppingListImage(selectedCartIds?: string[]) {
  return request({
    url: '/cart/generate-image',
    method: 'post',
    data: selectedCartIds || [],
    responseType: 'blob'  // 如果后端返回图片二进制
  })
}