import request from '@/utils/request'


export function getHotRecipes() {
  return request({
    url: '/recipes/hot',
    method: 'get'
  })
}

export function getHotCategories() {
  return request({
    url: '/category/hot',
    method: 'get'
  })
}

export function getRecommendedRecipes() {
  return request({
    url: '/recipes/recommended',
    method: 'get'
  })
}

export function getTodayRecommends() {
  return request({
    url: '/recipes/today',
    method: 'get'
  })
}