import request from '@/utils/request'

// 查询菜谱步骤（有序）列表
export function listRecipeSteps(query) {
  return request({
    url: '/system/RecipeSteps/list',
    method: 'get',
    params: query
  })
}

// 查询菜谱步骤（有序）详细
export function getRecipeSteps(id) {
  return request({
    url: '/system/RecipeSteps/' + id,
    method: 'get'
  })
}

// 新增菜谱步骤（有序）
export function addRecipeSteps(data) {
  return request({
    url: '/system/RecipeSteps',
    method: 'post',
    data: data
  })
}

// 修改菜谱步骤（有序）
export function updateRecipeSteps(data) {
  return request({
    url: '/system/RecipeSteps',
    method: 'put',
    data: data
  })
}

// 删除菜谱步骤（有序）
export function delRecipeSteps(id) {
  return request({
    url: '/system/RecipeSteps/' + id,
    method: 'delete'
  })
}
