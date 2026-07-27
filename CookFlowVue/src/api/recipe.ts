import request from '@/utils/request'

export function getRecipeList(params) {
  return request({
    url: '/recipes/list',
    method: 'get',
    params
  })
}

export function getRecipeDetail(id) {
  return request({
    url: `/recipes/${id}`,
    method: 'get'
  })
}

export function createRecipe(data) {
  return request({
    url: '/recipes',
    method: 'post',
    data
  })
}

export function updateRecipe(id, data) {
  return request({
    url: `/recipes/${id}`,
    method: 'put',
    data
  })
}

export function deleteRecipe(id) {
  return request({
    url: `/recipes/${id}`,
    method: 'delete'
  })
}

export function toggleFavorite(recipeId) {
  return request({
    url: `/recipes/${recipeId}/favorite`,
    method: 'post'
  })
}

// 获取用户发布的菜谱
export function getUserPublishedRecipes() {
  return request({
    url: '/user/recipes/published',
    method: 'get'
  })
}

// 获取用户收藏的菜谱
export function getUserCollectedRecipes() {
  return request({
    url: '/user/recipes/collected',
    method: 'get'
  })
}

// 获取用户点赞的菜谱
export function getUserLikedRecipes() {
  return request({
    url: '/user/recipes/liked',
    method: 'get'
  })
}