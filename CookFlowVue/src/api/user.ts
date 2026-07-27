import request from '@/utils/request';

export function getUserProfile() {
  return request({
    url: '/user/profile',
    method: 'get'
  });
}

export function updateUserProfile(data: any) {
  return request({
    url: '/user/profile',
    method: 'put',
    data
  });
}

export function getUserRecipes() {
  return request({
    url: '/user/recipes',
    method: 'get'
  });
}

export function getUserBooks() {
  return request({
    url: '/user/books',
    method: 'get'
  });
}

export function getUserQuestions() {
  return request({
    url: '/user/questions',
    method: 'get'
  });
}

export function getUserAnswers() {
  return request({
    url: '/user/answers',
    method: 'get'
  });
}

export function getActiveUsers(limit: number) {
  return request({
    url: '/system/user/active',
    method: 'get',
    params: { limit }
  })
}