import request from '@/utils/request'

// 查询菜谱与食材关联（含数量）列表
export function listRecipeIngredients(query) {
  return request({
    url: '/system/RecipeIngredients/list',
    method: 'get',
    params: query
  })
}

// 查询菜谱与食材关联（含数量）详细
export function getRecipeIngredients(recipeId) {
  return request({
    url: '/system/RecipeIngredients/' + recipeId,
    method: 'get'
  })
}

// 新增菜谱与食材关联（含数量）
export function addRecipeIngredients(data) {
  return request({
    url: '/system/RecipeIngredients',
    method: 'post',
    data: data
  })
}

// 修改菜谱与食材关联（含数量）
export function updateRecipeIngredients(data) {
  return request({
    url: '/system/RecipeIngredients',
    method: 'put',
    data: data
  })
}

// 删除菜谱与食材关联（含数量）
export function delRecipeIngredients(recipeId) {
  return request({
    url: '/system/RecipeIngredients/' + recipeId,
    method: 'delete'
  })
}
