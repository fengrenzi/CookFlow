import request from '@/utils/request'

// 查询食材列表
export function listIngredients(query) {
  return request({
    url: '/system/Ingredients/list',
    method: 'get',
    params: query
  })
}

// 查询食材详细
export function getIngredients(id) {
  return request({
    url: '/system/Ingredients/' + id,
    method: 'get'
  })
}

// 新增食材
export function addIngredients(data) {
  return request({
    url: '/system/Ingredients',
    method: 'post',
    data: data
  })
}

// 修改食材
export function updateIngredients(data) {
  return request({
    url: '/system/Ingredients',
    method: 'put',
    data: data
  })
}

// 删除食材
export function delIngredients(id) {
  return request({
    url: '/system/Ingredients/' + id,
    method: 'delete'
  })
}
