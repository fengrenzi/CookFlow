import request from '@/utils/request'

// 查询书籍列表
export function listBooks(query) {
  return request({
    url: '/system/Books/list',
    method: 'get',
    params: query
  })
}

// 查询书籍详细
export function getBooks(id) {
  return request({
    url: '/system/Books/' + id,
    method: 'get'
  })
}

// 新增书籍
export function addBooks(data) {
  return request({
    url: '/system/Books',
    method: 'post',
    data: data
  })
}

// 修改书籍
export function updateBooks(data) {
  return request({
    url: '/system/Books',
    method: 'put',
    data: data
  })
}

// 删除书籍
export function delBooks(id) {
  return request({
    url: '/system/Books/' + id,
    method: 'delete'
  })
}
