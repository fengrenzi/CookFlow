import request from '@/utils/request'

// 获取书籍列表（分页、筛选、排序）
export function getBookList(params?: any) {
  return request({
    url: '/books/list',
    method: 'get',
    params
  });
}

// 获取书籍详情
export function getBookDetail(id: string | number) {
  return request({
    url: `/books/${id}`,
    method: 'get'
  });
}

// 获取热销书籍
export function getHotBooks(params?: any) {
  return request({
    url: '/books/hot',
    method: 'get',
    params
  });
}

// 获取阅读热榜
export function getReadingRank(params?: any) {
  return request({
    url: '/books/ranking',
    method: 'get',
    params
  });
}

// 获取推荐书籍
export function getRecommendBooks(params?: any) {
  return request({
    url: '/books/recommend',
    method: 'get',
    params
  });
}

// 获取书籍分类（用于左侧导航）
export function getBookCategories() {
  return request({
    url: '/books/categories',
    method: 'get'
  });
}

// 收藏/取消收藏书籍
export function toggleBookFavorite(bookId: string) {
  return request({
    url: `/books/${bookId}/favorite`,
    method: 'post'
  });
}

// 获取用户发布的书籍
export function getUserPublishedBooks() {
  return request({
    url: '/user/books/published',
    method: 'get'
  })
}

// 获取用户收藏的书籍
export function getUserCollectedBooks() {
  return request({
    url: '/user/books/collected',
    method: 'get'
  })
}