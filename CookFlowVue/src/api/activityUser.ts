import request from '@/utils/request'

// 获取用户组织的活动
export function getOrganizedActivities() {
  return request({
    url: '/activity/organized',
    method: 'get'
  })
}

// 获取用户报名的活动
export function getJoinedActivities() {
  return request({
    url: '/activity/joined',
    method: 'get'
  })
}

// 获取用户建议的活动
export function getSuggestedActivities() {
  return request({
    url: '/activity/suggestions/my',
    method: 'get'
  })
}

// 提交活动建议
export function submitActivitySuggestion(data: { title: string; content: string }) {
  return request({
    url: '/activity/suggestions',
    method: 'post',
    data
  })
}