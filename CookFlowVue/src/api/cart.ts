import request from '@/utils/request';

export function getCart() {
  return request({
    url: '/cart',
    method: 'get'
  });
}

export function addCartItem(data: any) {
  return request({
    url: '/cart/items',
    method: 'post',
    data
  });
}

export function updateCartItem(id: string, data: { quantity: number }) {
  return request({
    url: `/cart/items/${id}`,
    method: 'put',
    data
  });
}

export function deleteCartItem(id: string) {
  return request({
    url: `/cart/items/${id}`,
    method: 'delete'
  });
}

export function checkout() {
  return request({
    url: '/cart/checkout',
    method: 'post'
  });
}