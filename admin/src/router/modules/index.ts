import { AppRouteRecord } from '@/types/router'
import { dashboardRoutes } from './dashboard'
import { systemRoutes } from './system'
import { galleryRoutes, bookingRoutes, contentRoutes } from './gallery'

/**
 * 导出所有模块化路由
 */
export const routeModules: AppRouteRecord[] = [
  dashboardRoutes,
  galleryRoutes,
  bookingRoutes,
  contentRoutes,
  systemRoutes
]
