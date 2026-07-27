import request from '@/utils/request';

export function getActivityDetail(id: number) {
  return request({
    url: `/activities/${id}`,
    method: 'get'
  });
}

export function getActivityComments(id: number, params?: any) {
  return request({
    url: `/activities/${id}/comments`,
    method: 'get',
    params
  });
}

export function getActivityShares(id: number, params?: any) {
  return request({
    url: `/activities/${id}/shares`,
    method: 'get',
    params
  });
}

export function submitActivityComment(id: number, data: { content: string }) {
  return request({
    url: `/activities/${id}/comments`,
    method: 'post',
    data
  });
}

export function submitActivityShare(id: number, data: { content: string; imageUrl?: string }) {
  return request({
    url: `/activities/${id}/shares`,
    method: 'post',
    data
  });
}

export function joinActivity(id: number) {
  return request({
    url: `/activities/${id}/join`,
    method: 'post'
  });
}

export function cancelJoinActivity(id: number) {
  return request({
    url: `/activities/${id}/join`,
    method: 'delete'
  });
}