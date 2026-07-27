import request from '@/utils/request'

// 查询AI 会话元信息列表
export function listAiConversations(query) {
  return request({
    url: '/system/AiConversations/list',
    method: 'get',
    params: query
  })
}

// 查询AI 会话元信息详细
export function getAiConversations(id) {
  return request({
    url: '/system/AiConversations/' + id,
    method: 'get'
  })
}

// 新增AI 会话元信息
export function addAiConversations(data) {
  return request({
    url: '/system/AiConversations',
    method: 'post',
    data: data
  })
}

// 修改AI 会话元信息
export function updateAiConversations(data) {
  return request({
    url: '/system/AiConversations',
    method: 'put',
    data: data
  })
}

// 删除AI 会话元信息
export function delAiConversations(id) {
  return request({
    url: '/system/AiConversations/' + id,
    method: 'delete'
  })
}
