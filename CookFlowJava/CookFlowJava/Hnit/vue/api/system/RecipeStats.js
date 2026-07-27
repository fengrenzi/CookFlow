import request from '@/utils/request'

// 查询菜谱聚合统计，用于排行榜列表
export function listRecipeStats(query) {
  return request({
    url: '/system/RecipeStats/list',
    method: 'get',
    params: query
  })
}

// 查询菜谱聚合统计，用于排行榜详细
export function getRecipeStats(recipeId) {
  return request({
    url: '/system/RecipeStats/' + recipeId,
    method: 'get'
  })
}

// 新增菜谱聚合统计，用于排行榜
export function addRecipeStats(data) {
  return request({
    url: '/system/RecipeStats',
    method: 'post',
    data: data
  })
}

// 修改菜谱聚合统计，用于排行榜
export function updateRecipeStats(data) {
  return request({
    url: '/system/RecipeStats',
    method: 'put',
    data: data
  })
}

// 删除菜谱聚合统计，用于排行榜
export function delRecipeStats(recipeId) {
  return request({
    url: '/system/RecipeStats/' + recipeId,
    method: 'delete'
  })
}
