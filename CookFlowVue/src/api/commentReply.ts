import request from '@/utils/request'

export function getUnreadReplyCount() {
  return request({
    url: '/replies/unread/count',
    method: 'get'
  })
}

export function getReplyList(params?: { pageNum?: number; pageSize?: number }) {
  return request({
    url: '/replies/list',
    method: 'get',
    params
  })
}

export function markRepliesAsRead(data: string[]) {
  return request({
    url: '/replies/mark-read',
    method: 'put',
    data
  })
}