import request from '@/utils/request'

// 查询媒体资源（图片/视频/音频）列表
export function listMedia(query) {
  return request({
    url: '/system/Media/list',
    method: 'get',
    params: query
  })
}

// 查询媒体资源（图片/视频/音频）详细
export function getMedia(id) {
  return request({
    url: '/system/Media/' + id,
    method: 'get'
  })
}

// 新增媒体资源（图片/视频/音频）
export function addMedia(data) {
  return request({
    url: '/system/Media',
    method: 'post',
    data: data
  })
}

// 修改媒体资源（图片/视频/音频）
export function updateMedia(data) {
  return request({
    url: '/system/Media',
    method: 'put',
    data: data
  })
}

// 删除媒体资源（图片/视频/音频）
export function delMedia(id) {
  return request({
    url: '/system/Media/' + id,
    method: 'delete'
  })
}
