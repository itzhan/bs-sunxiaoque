import request from '@/utils/request'

// 展览列表（已发布）
export function getExhibitions(params?: any) {
  return request.get('/public/exhibitions', { params })
}

// 展览详情（含展品、导览、时段）
export function getExhibitionDetail(id: number | string) {
  return request.get(`/public/exhibitions/${id}`)
}

// 公告列表（已发布）
export function getAnnouncements(params?: any) {
  return request.get('/public/announcements', { params })
}

// 全部分类（已启用）
export function getCategories() {
  return request.get('/public/categories')
}

// 展览评论（已审核通过）
export function getExhibitionComments(id: number | string, params?: any) {
  return request.get(`/public/exhibitions/${id}/comments`, { params })
}
