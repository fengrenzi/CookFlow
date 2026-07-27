import request from '@/utils/request'

// 查询用户收藏列表
export function listFavorites(query) {
  return request({
    url: '/system/Favorites/list',
    method: 'get',
    params: query
  })
}

// 查询用户收藏详细
export function getFavorites(userId) {
  return request({
    url: '/system/Favorites/' + userId,
    method: 'get'
  })
}

// 新增用户收藏
export function addFavorites(data) {
  return request({
    url: '/system/Favorites',
    method: 'post',
    data: data
  })
}

// 修改用户收藏
export function updateFavorites(data) {
  return request({
    url: '/system/Favorites',
    method: 'put',
    data: data
  })
}

// 删除用户收藏
export function delFavorites(userId) {
  return request({
    url: '/system/Favorites/' + userId,
    method: 'delete'
  })
}
