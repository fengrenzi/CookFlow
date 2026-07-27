import request from '@/utils/request'

// 获取阅读历史列表
export function getReadHistoryList() {
  return request({
    url: '/read-history',
    method: 'get'
  })
}

// 保存阅读进度
export function saveReadHistory(data: { bookId: string; progress: number }) {
  return request({
    url: '/read-history',
    method: 'post',
    data
  })
}