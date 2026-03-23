import request from '@/utils/request'

// ---- 预约 ----
export function createReservation(data: any) {
  return request.post('/reservations', data)
}

export function getMyReservations(params?: any) {
  return request.get('/reservations/my', { params })
}

export function cancelReservation(id: number | string, data?: { reason?: string }) {
  return request.put(`/reservations/${id}/cancel`, data || {})
}

// ---- 收藏 ----
export function toggleFavorite(exhibitionId: number | string) {
  return request.post(`/favorites/${exhibitionId}`)
}

export function checkFavorite(exhibitionId: number | string) {
  return request.get(`/favorites/${exhibitionId}/check`)
}

export function getMyFavorites(params?: any) {
  return request.get('/favorites', { params })
}

// ---- 评论 ----
export function postComment(data: { exhibitionId: number; content: string; rating: number }) {
  return request.post('/comments', data)
}

// ---- 个人资料 ----
export function getProfile() {
  return request.get('/profile')
}

export function updateProfile(data: any) {
  return request.put('/profile', data)
}

export function changePassword(data: { oldPassword: string; newPassword: string }) {
  return request.put('/profile/password', data)
}

// ---- 时段可用性 ----
export function getTimeSlotAvailability(id: number | string, date: string) {
  return request.get(`/time-slots/${id}/available`, { params: { date } })
}
