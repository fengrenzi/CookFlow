import request from '@/utils/request'

// 查询书籍中页码到菜谱的映射列表
export function listBookRecipes(query) {
  return request({
    url: '/system/BookRecipes/list',
    method: 'get',
    params: query
  })
}

// 查询书籍中页码到菜谱的映射详细
export function getBookRecipes(bookId) {
  return request({
    url: '/system/BookRecipes/' + bookId,
    method: 'get'
  })
}

// 新增书籍中页码到菜谱的映射
export function addBookRecipes(data) {
  return request({
    url: '/system/BookRecipes',
    method: 'post',
    data: data
  })
}

// 修改书籍中页码到菜谱的映射
export function updateBookRecipes(data) {
  return request({
    url: '/system/BookRecipes',
    method: 'put',
    data: data
  })
}

// 删除书籍中页码到菜谱的映射
export function delBookRecipes(bookId) {
  return request({
    url: '/system/BookRecipes/' + bookId,
    method: 'delete'
  })
}
