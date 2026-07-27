import request from '@/utils/request'

// 查询用户通知列表
export function listNotifications(query) {
  return request({
    url: '/system/Notifications/list',
    method: 'get',
    params: query
  })
}

// 查询用户通知详细
export function getNotifications(id) {
  return request({
    url: '/system/Notifications/' + id,
    method: 'get'
  })
}

// 新增用户通知
export function addNotifications(data) {
  return request({
    url: '/system/Notifications',
    method: 'post',
    data: data
  })
}

// 修改用户通知
export function updateNotifications(data) {
  return request({
    url: '/system/Notifications',
    method: 'put',
    data: data
  })
}

// 删除用户通知
export function delNotifications(id) {
  return request({
    url: '/system/Notifications/' + id,
    method: 'delete'
  })
}
