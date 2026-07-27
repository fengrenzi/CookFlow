import request from '@/utils/request'

/**
 * 获取分类树（用于筛选器）
 * @returns {Promise<Array<{tableName: string, options: string[]}>>}
 */
export function getCategoryTree(tableName?: string) {
  return request({
    url: '/category/tree',
    method: 'get',
    params: {
      tableName
    }
  })
}