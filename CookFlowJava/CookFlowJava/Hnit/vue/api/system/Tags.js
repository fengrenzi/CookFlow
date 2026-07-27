import request from '@/utils/request'

// 查询标签列表
export function listTags(query) {
  return request({
    url: '/system/Tags/list',
    method: 'get',
    params: query
  })
}

// 查询标签详细
export function getTags(id) {
  return request({
    url: '/system/Tags/' + id,
    method: 'get'
  })
}

// 新增标签
export function addTags(data) {
  return request({
    url: '/system/Tags',
    method: 'post',
    data: data
  })
}

// 修改标签
export function updateTags(data) {
  return request({
    url: '/system/Tags',
    method: 'put',
    data: data
  })
}

// 删除标签
export function delTags(id) {
  return request({
    url: '/system/Tags/' + id,
    method: 'delete'
  })
}
