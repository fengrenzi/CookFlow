import request from '@/utils/request'

// 查询敏感词库，用于内容审核/替换列表
export function listSensitiveWords(query) {
  return request({
    url: '/system/SensitiveWords/list',
    method: 'get',
    params: query
  })
}

// 查询敏感词库，用于内容审核/替换详细
export function getSensitiveWords(id) {
  return request({
    url: '/system/SensitiveWords/' + id,
    method: 'get'
  })
}

// 新增敏感词库，用于内容审核/替换
export function addSensitiveWords(data) {
  return request({
    url: '/system/SensitiveWords',
    method: 'post',
    data: data
  })
}

// 修改敏感词库，用于内容审核/替换
export function updateSensitiveWords(data) {
  return request({
    url: '/system/SensitiveWords',
    method: 'put',
    data: data
  })
}

// 删除敏感词库，用于内容审核/替换
export function delSensitiveWords(id) {
  return request({
    url: '/system/SensitiveWords/' + id,
    method: 'delete'
  })
}
