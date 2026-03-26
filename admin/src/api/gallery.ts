import request from '@/utils/http'

// Dashboard
export function fetchDashboard() {
  return request.get<Api.Gallery.Dashboard>({ url: '/api/dashboard' })
}

// Exhibition Categories
export function fetchCategoryList(params: { page: number; size: number; keyword?: string }) {
  return request.get<Api.Common.PaginatedResponse<Api.Gallery.ExhibitionCategory>>({
    url: '/api/categories',
    params
  })
}
export function fetchAllCategories() {
  return request.get<Api.Gallery.ExhibitionCategory[]>({ url: '/api/categories/all' })
}
export function createCategory(params: Partial<Api.Gallery.ExhibitionCategory>) {
  return request.post({ url: '/api/categories', params })
}
export function updateCategory(id: number, params: Partial<Api.Gallery.ExhibitionCategory>) {
  return request.put({ url: `/api/categories/${id}`, params })
}
export function deleteCategory(id: number) {
  return request.del({ url: `/api/categories/${id}` })
}

// Exhibitions
export function fetchExhibitionList(params: {
  page: number
  size: number
  title?: string
  categoryId?: number
  status?: number
}) {
  return request.get<Api.Common.PaginatedResponse<Api.Gallery.Exhibition>>({
    url: '/api/exhibitions',
    params
  })
}
export function fetchExhibitionDetail(id: number) {
  return request.get<Api.Gallery.Exhibition>({ url: `/api/exhibitions/${id}` })
}
export function createExhibition(params: Partial<Api.Gallery.Exhibition>) {
  return request.post({ url: '/api/exhibitions', params })
}
export function updateExhibition(id: number, params: Partial<Api.Gallery.Exhibition>) {
  return request.put({ url: `/api/exhibitions/${id}`, params })
}
export function deleteExhibition(id: number) {
  return request.del({ url: `/api/exhibitions/${id}` })
}

// Exhibit Items
export function fetchExhibitItemList(params: { page: number; size: number; exhibitionId: number }) {
  return request.get<Api.Common.PaginatedResponse<Api.Gallery.ExhibitItem>>({
    url: '/api/exhibit-items',
    params
  })
}
export function createExhibitItem(params: Partial<Api.Gallery.ExhibitItem>) {
  return request.post({ url: '/api/exhibit-items', params })
}
export function updateExhibitItem(id: number, params: Partial<Api.Gallery.ExhibitItem>) {
  return request.put({ url: `/api/exhibit-items/${id}`, params })
}
export function deleteExhibitItem(id: number) {
  return request.del({ url: `/api/exhibit-items/${id}` })
}

// Virtual Tours
export function fetchVirtualTourList(params: { page: number; size: number; exhibitionId: number }) {
  return request.get<Api.Common.PaginatedResponse<Api.Gallery.VirtualTour>>({
    url: '/api/virtual-tours',
    params
  })
}
export function createVirtualTour(params: Partial<Api.Gallery.VirtualTour>) {
  return request.post({ url: '/api/virtual-tours', params })
}
export function updateVirtualTour(id: number, params: Partial<Api.Gallery.VirtualTour>) {
  return request.put({ url: `/api/virtual-tours/${id}`, params })
}
export function deleteVirtualTour(id: number) {
  return request.del({ url: `/api/virtual-tours/${id}` })
}

// Time Slots
export function fetchTimeSlotList(params: { exhibitionId: number }) {
  return request.get<Api.Gallery.TimeSlot[]>({ url: '/api/time-slots', params })
}
export function createTimeSlot(params: Partial<Api.Gallery.TimeSlot>) {
  return request.post({ url: '/api/time-slots', params })
}
export function updateTimeSlot(id: number, params: Partial<Api.Gallery.TimeSlot>) {
  return request.put({ url: `/api/time-slots/${id}`, params })
}
export function deleteTimeSlot(id: number) {
  return request.del({ url: `/api/time-slots/${id}` })
}

// Reservations
export function fetchReservationList(params: {
  page: number
  size: number
  exhibitionId?: number
  status?: number
  username?: string
}) {
  return request.get<Api.Common.PaginatedResponse<Api.Gallery.Reservation>>({
    url: '/api/reservations',
    params
  })
}
export function confirmReservation(id: number) {
  return request.put({ url: `/api/reservations/${id}/confirm` })
}
export function completeReservation(id: number) {
  return request.put({ url: `/api/reservations/${id}/complete` })
}

// Announcements
export function fetchAnnouncementList(params: { page: number; size: number; status?: number }) {
  return request.get<Api.Common.PaginatedResponse<Api.Gallery.Announcement>>({
    url: '/api/announcements',
    params
  })
}
export function fetchAnnouncementDetail(id: number) {
  return request.get<Api.Gallery.Announcement>({ url: `/api/announcements/${id}` })
}
export function createAnnouncement(params: Partial<Api.Gallery.Announcement>) {
  return request.post({ url: '/api/announcements', params })
}
export function updateAnnouncement(id: number, params: Partial<Api.Gallery.Announcement>) {
  return request.put({ url: `/api/announcements/${id}`, params })
}
export function deleteAnnouncement(id: number) {
  return request.del({ url: `/api/announcements/${id}` })
}

// Comments
export function fetchCommentList(params: {
  page: number
  size: number
  exhibitionId?: number
  status?: number
}) {
  return request.get<Api.Common.PaginatedResponse<Api.Gallery.Comment>>({
    url: '/api/comments',
    params
  })
}
export function approveComment(id: number) {
  return request.put({ url: `/api/comments/${id}/approve` })
}
export function rejectComment(id: number) {
  return request.put({ url: `/api/comments/${id}/reject` })
}
export function deleteComment(id: number) {
  return request.del({ url: `/api/comments/${id}` })
}

// Users (admin management)
export function fetchUserList(params: {
  page: number
  size: number
  keyword?: string
  status?: number
}) {
  return request.get<Api.Common.PaginatedResponse<Api.SystemManage.UserListItem>>({
    url: '/api/users',
    params
  })
}
export function updateUserStatus(id: number, status: number) {
  return request.put({ url: `/api/users/${id}/status`, params: { status } })
}
export function resetUserPassword(id: number) {
  return request.put({ url: `/api/users/${id}/reset-password` })
}

// Favorites (admin)
export function fetchFavoriteList(params: { page: number; size: number }) {
  return request.get<Api.Common.PaginatedResponse<any>>({
    url: '/api/favorites/all',
    params
  })
}

// File upload
export function uploadFile(file: File) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post<string>({
    url: '/api/files/upload',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
