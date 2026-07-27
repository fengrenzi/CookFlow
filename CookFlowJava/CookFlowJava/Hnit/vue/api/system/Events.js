import request from '@/utils/request'

// 查询用户行为事件（事件流）列表
export function listEvents(query) {
  return request({
    url: '/system/Events/list',
    method: 'get',
    params: query
  })
}

// 查询用户行为事件（事件流）详细
export function getEvents(id) {
  return request({
    url: '/system/Events/' + id,
    method: 'get'
  })
}

// 新增用户行为事件（事件流）
export function addEvents(data) {
  return request({
    url: '/system/Events',
    method: 'post',
    data: data
  })
}

// 修改用户行为事件（事件流）
export function updateEvents(data) {
  return request({
    url: '/system/Events',
    method: 'put',
    data: data
  })
}

// 删除用户行为事件（事件流）
export function delEvents(id) {
  return request({
    url: '/system/Events/' + id,
    method: 'delete'
  })
}
