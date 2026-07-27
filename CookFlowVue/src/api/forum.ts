import request from '@/utils/request'

// ==================== 分享相关 ====================
export function getShareList(params: any) {
  return request({ url: '/forum', method: 'get', params })
}

export function getShareDetail(id: string) {
  return request({ url: `/forum/${id}`, method: 'get' })
}

export function createShare(data: any) {
  return request({ url: '/forum', method: 'post', data })
}

export function toggleShareLike(id: string) {
  return request({ url: `/forum/${id}/like`, method: 'post' })
}

export function toggleShareFavorite(id: string) {
  return request({ url: `/forum/${id}/favorite`, method: 'post' })
}

export function deleteShare(id: string) {
  return request({ url: `/forum/${id}`, method: 'delete' })
}

// ==================== 标签 ====================
export function getHotTags(type: string, limit = 10) {
  return request({ url: '/tags/hot', method: 'get', params: { type, limit } })
}

// ==================== 问答相关 ====================
export function getQuestionList(params: any) {
  return request({ url: '/forum/questions', method: 'get', params })
}

export function getQuestionDetail(id: string) {
  return request({ url: `/forum/questions/${id}`, method: 'get' })
}

export function createQuestion(data: any) {
  return request({ url: '/forum/questions', method: 'post', data })
}

export function toggleQuestionFavorite(id: string) {
  return request({ url: `/forum/questions/${id}/favorite`, method: 'post' })
}

export function toggleQuestionFollow(id: string) {
  return request({ url: `/forum/questions/${id}/follow`, method: 'post' })
}

export function deleteQuestion(id: string) {
  return request({ url: `/forum/questions/${id}`, method: 'delete' })
}

export function submitAnswer(questionId: string, data: any) {
  return request({ url: `/forum/questions/${questionId}/answers`, method: 'post', data })
}

// ==================== 回答相关 ====================
export function getAnswers(questionId: string) {
  return request({ url: `/forum/answers/question/${questionId}`, method: 'get' })
}

export function createAnswer(questionId: string, data: any) {
  return request({ url: `/forum/answers/question/${questionId}`, method: 'post', data })
}

export function toggleAnswerLike(id: string) {
  return request({ url: `/forum/answers/${id}/like`, method: 'post' })
}

export function acceptAnswer(id: string) {
  return request({ url: `/forum/answers/${id}/accept`, method: 'post' })
}

export function deleteAnswer(id: string) {
  return request({ url: `/forum/answers/${id}`, method: 'delete' })
}

// ==================== 活动相关 ====================
export function getActivityList(params: any) {
  return request({ url: '/forum/activity', method: 'get', params })
}

export function getActivityDetail(id: string) {
  return request({ url: `/forum/activity/${id}`, method: 'get' })
}

export function createActivity(data: any) {
  return request({ url: '/forum/activity', method: 'post', data })
}

export function joinActivity(id: string) {
  return request({ url: `/forum/activity/${id}/join`, method: 'post' })
}

export function cancelJoinActivity(id: string) {
  return request({ url: `/forum/activity/${id}/join`, method: 'delete' })
}

export function deleteActivity(id: string) {
  return request({ url: `/forum/activity/${id}`, method: 'delete' })
}

// 问答辅助
export function likeAnswer(answerId: string) {
  return request({ url: `/forum/answers/${answerId}/like`, method: 'post' })
}

export function getComments(resourceId: string) {
  return request({ url: `/comments?resourceId=${resourceId}`, method: 'get' })
}

export function submitComment(resourceId: string, data: any) {
  return request({ url: `/comments`, method: 'post', data: { ...data, resourceId } })
}

export function likeComment(commentId: string) {
  return request({ url: `/comments/${commentId}/like`, method: 'post' })
}

// 活动扩展
export function getActivityComments(activityId: string) {
  return request({ url: `/comments?resourceId=${activityId}&resourceType=activity`, method: 'get' })
}

export function getActivityShares(activityId: string) {
  return request({ url: `/forum/activity/${activityId}`, method: 'get' })
}

export function submitActivityComment(activityId: string, data: any) {
  return request({ url: `/comments`, method: 'post', data: { ...data, resourceId: activityId, resourceType: 'activity' } })
}

export function submitActivityShare(activityId: string, data: any) {
  return request({ url: `/forum/activity/${activityId}`, method: 'post', data })
}

// ==================== 用户相关（问答） ====================
export function getUserQuestions() {
  return request({ url: '/user/questions', method: 'get' })
}

export function getUserAnswers() {
  return request({ url: '/user/answers', method: 'get' })
}

export function getUserFollowedQuestions() {
  return request({ url: '/user/questions/followed', method: 'get' })
}

export function getUserCollectedQuestions() {
  return request({ url: '/user/questions/collected', method: 'get' })
}