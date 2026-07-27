import request from '@/utils/request'

// 查询菜谱列表
export function listRecipes(query) {
  return request({
    url: '/system/Recipes/list',
    method: 'get',
    params: query
  })
}

// 查询菜谱详细
export function getRecipes(id) {
  return request({
    url: '/system/Recipes/' + id,
    method: 'get'
  })
}

// 新增菜谱
export function addRecipes(data) {
  return request({
    url: '/system/Recipes',
    method: 'post',
    data: data
  })
}

// 修改菜谱
export function updateRecipes(data) {
  return request({
    url: '/system/Recipes',
    method: 'put',
    data: data
  })
}

// 删除菜谱
export function delRecipes(id) {
  return request({
    url: '/system/Recipes/' + id,
    method: 'delete'
  })
}
