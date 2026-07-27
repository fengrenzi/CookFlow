import request from '@/utils/request'

// 查询食材详情静态内容列表
export function listIngredientDetails(query) {
  return request({
    url: '/system/IngredientDetails/list',
    method: 'get',
    params: query
  })
}

// 查询食材详情静态内容详细
export function getIngredientDetails(ingredientId) {
  return request({
    url: '/system/IngredientDetails/' + ingredientId,
    method: 'get'
  })
}

// 新增食材详情静态内容
export function addIngredientDetails(data) {
  return request({
    url: '/system/IngredientDetails',
    method: 'post',
    data: data
  })
}

// 修改食材详情静态内容
export function updateIngredientDetails(data) {
  return request({
    url: '/system/IngredientDetails',
    method: 'put',
    data: data
  })
}

// 删除食材详情静态内容
export function delIngredientDetails(ingredientId) {
  return request({
    url: '/system/IngredientDetails/' + ingredientId,
    method: 'delete'
  })
}
