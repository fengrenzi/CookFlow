import request from '@/utils/request'

// 查询标签映射列表
export function listTagMap(query) {
  return request({
    url: '/system/TagMap/list',
    method: 'get',
    params: query
  })
}

// 查询标签映射详细
export function getTagMap(tagId) {
  return request({
    url: '/system/TagMap/' + tagId,
    method: 'get'
  })
}

// 新增标签映射
export function addTagMap(data) {
  return request({
    url: '/system/TagMap',
    method: 'post',
    data: data
  })
}

// 修改标签映射
export function updateTagMap(data) {
  return request({
    url: '/system/TagMap',
    method: 'put',
    data: data
  })
}

// 删除标签映射
export function delTagMap(tagId) {
  return request({
    url: '/system/TagMap/' + tagId,
    method: 'delete'
  })
}
