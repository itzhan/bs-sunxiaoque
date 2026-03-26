import { AppRouteRecord } from '@/types/router'

// 展览管理
export const galleryRoutes: AppRouteRecord = {
  path: '/gallery',
  name: 'Gallery',
  component: '/index/index',
  meta: {
    title: 'menus.gallery.title',
    icon: 'ri:gallery-line',
    roles: ['R_SUPER', 'R_ADMIN']
  },
  children: [
    {
      path: 'category',
      name: 'CategoryManage',
      component: '/gallery/category',
      meta: {
        title: 'menus.gallery.category',
        keepAlive: true
      }
    },
    {
      path: 'exhibition',
      name: 'ExhibitionManage',
      component: '/gallery/exhibition',
      meta: {
        title: 'menus.gallery.exhibition',
        keepAlive: true
      }
    },
    {
      path: 'exhibit-item',
      name: 'ExhibitItemManage',
      component: '/gallery/exhibit-item',
      meta: {
        title: 'menus.gallery.exhibitItem',
        keepAlive: true
      }
    },
    {
      path: 'virtual-tour',
      name: 'VirtualTourManage',
      component: '/gallery/virtual-tour',
      meta: {
        title: 'menus.gallery.virtualTour',
        keepAlive: true
      }
    }
  ]
}

// 预约服务
export const bookingRoutes: AppRouteRecord = {
  path: '/booking',
  name: 'Booking',
  component: '/index/index',
  meta: {
    title: 'menus.booking.title',
    icon: 'ri:calendar-check-line',
    roles: ['R_SUPER', 'R_ADMIN']
  },
  children: [
    {
      path: 'reservation',
      name: 'ReservationManage',
      component: '/gallery/reservation',
      meta: {
        title: 'menus.booking.reservation',
        keepAlive: true
      }
    }
  ]
}

// 内容运营
export const contentRoutes: AppRouteRecord = {
  path: '/content',
  name: 'Content',
  component: '/index/index',
  meta: {
    title: 'menus.content.title',
    icon: 'ri:article-line',
    roles: ['R_SUPER', 'R_ADMIN']
  },
  children: [
    {
      path: 'announcement',
      name: 'AnnouncementManage',
      component: '/gallery/announcement',
      meta: {
        title: 'menus.content.announcement',
        keepAlive: true
      }
    },
    {
      path: 'comment',
      name: 'CommentManage',
      component: '/gallery/comment',
      meta: {
        title: 'menus.content.comment',
        keepAlive: true
      }
    },
    {
      path: 'favorite',
      name: 'FavoriteManage',
      component: '/gallery/favorite',
      meta: {
        title: 'menus.content.favorite',
        keepAlive: true
      }
    }
  ]
}
