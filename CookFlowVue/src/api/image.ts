import request from '@/utils/request'

/**
 * 上传图片
 * @param file 图片文件
 * @returns 图片信息（包含 id）
 */
export function uploadImage(file: File) {
  const formData = new FormData()
  formData.append('file', file)
  return request<{ id: string; originalName: string; storedName: string; storagePath: string; createdAt: string }>({
    url: '/common/upload',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 根据图片ID获取图片信息
 * @param id 图片ID
 * @returns 图片信息（包含 storagePath 和 storedName）
 */
export function getImageById(id: string) {
  return request<{ id: string; originalName: string; storedName: string; storagePath: string; createdAt: string }>({
    url: `/image/${id}`,
    method: 'get'
  })
}

/**
 * 批量根据图片ID获取图片信息
 * @param ids 图片ID数组
 * @returns 图片信息数组
 */
export function getImagesByIds(ids: string[]) {
  return request<Array<{ id: string; originalName: string; storedName: string; storagePath: string; createdAt: string }>>({
    url: '/image/list',
    method: 'get',
    params: { ids: ids.join(',') }
  })
}