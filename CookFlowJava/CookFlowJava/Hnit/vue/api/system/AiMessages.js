import request from '@/utils/request'

// 查询AI 会话消息（按消息存储）列表
export function listAiMessages(query) {
  return request({
    url: '/system/AiMessages/list',
    method: 'get',
    params: query
  })
}

// 查询AI 会话消息（按消息存储）详细
export function getAiMessages(id) {
  return request({
    url: '/system/AiMessages/' + id,
    method: 'get'
  })
}

// 新增AI 会话消息（按消息存储）
export function addAiMessages(data) {
  return request({
    url: '/system/AiMessages',
    method: 'post',
    data: data
  })
}

// 修改AI 会话消息（按消息存储）
export function updateAiMessages(data) {
  return request({
    url: '/system/AiMessages',
    method: 'put',
    data: data
  })
}

// 删除AI 会话消息（按消息存储）
export function delAiMessages(id) {
  return request({
    url: '/system/AiMessages/' + id,
    method: 'delete'
  })
}
