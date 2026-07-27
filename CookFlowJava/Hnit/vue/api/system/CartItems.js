import request from '@/utils/request'

// 查询购物车项列表
export function listCartItems(query) {
  return request({
    url: '/system/CartItems/list',
    method: 'get',
    params: query
  })
}

// 查询购物车项详细
export function getCartItems(id) {
  return request({
    url: '/system/CartItems/' + id,
    method: 'get'
  })
}

// 新增购物车项
export function addCartItems(data) {
  return request({
    url: '/system/CartItems',
    method: 'post',
    data: data
  })
}

// 修改购物车项
export function updateCartItems(data) {
  return request({
    url: '/system/CartItems',
    method: 'put',
    data: data
  })
}

// 删除购物车项
export function delCartItems(id) {
  return request({
    url: '/system/CartItems/' + id,
    method: 'delete'
  })
}
