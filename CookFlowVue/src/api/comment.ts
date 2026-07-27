import request from '@/utils/request'

// 获取评论列表
export function getComments(params: any) {
  return request({
    url: '/comments',
    method: 'get',
    params
  })
}

// 发布评论
export function addComment(data: any) {
  return request({
    url: '/comments',
    method: 'post',
    data
  })
}

// 点赞评论
export function likeComment(commentId: string) {
  return request({
    url: `/comments/${commentId}/like`,
    method: 'post'
  })
}

// 删除评论
export function deleteComment(commentId: string) {
  return request({
    url: `/comments/${commentId}`,
    method: 'delete'
  })
}