import request from '@/utils/request'

// 查询评论（支持@/回复/状态）列表
export function listComments(query) {
  return request({
    url: '/system/Comments/list',
    method: 'get',
    params: query
  })
}

// 查询评论（支持@/回复/状态）详细
export function getComments(id) {
  return request({
    url: '/system/Comments/' + id,
    method: 'get'
  })
}

// 新增评论（支持@/回复/状态）
export function addComments(data) {
  return request({
    url: '/system/Comments',
    method: 'post',
    data: data
  })
}

// 修改评论（支持@/回复/状态）
export function updateComments(data) {
  return request({
    url: '/system/Comments',
    method: 'put',
    data: data
  })
}

// 删除评论（支持@/回复/状态）
export function delComments(id) {
  return request({
    url: '/system/Comments/' + id,
    method: 'delete'
  })
}
