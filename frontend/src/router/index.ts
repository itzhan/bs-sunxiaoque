import { createRouter, createWebHistory } from 'vue-router'
import DefaultLayout from '@/layouts/DefaultLayout.vue'

const router = createRouter({
  history: createWebHistory(),
  scrollBehavior: () => ({ top: 0 }),
  routes: [
    {
      path: '/',
      component: DefaultLayout,
      children: [
        {
          path: '',
          name: 'Home',
          component: () => import('@/views/Home.vue'),
          meta: { title: '首页' }
        },
        {
          path: 'exhibitions',
          name: 'ExhibitionList',
          component: () => import('@/views/ExhibitionList.vue'),
          meta: { title: '展览' }
        },
        {
          path: 'exhibitions/:id',
          name: 'ExhibitionDetail',
          component: () => import('@/views/ExhibitionDetail.vue'),
          meta: { title: '展览详情' }
        },
        {
          path: 'announcements',
          name: 'AnnouncementList',
          component: () => import('@/views/AnnouncementList.vue'),
          meta: { title: '公告' }
        },
        {
          path: 'announcements/:id',
          name: 'AnnouncementDetail',
          component: () => import('@/views/AnnouncementDetail.vue'),
          meta: { title: '公告详情' }
        },
        {
          path: 'profile',
          name: 'Profile',
          component: () => import('@/views/Profile.vue'),
          meta: { title: '个人中心', requiresAuth: true }
        }
      ]
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/Login.vue'),
      meta: { title: '登录' }
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('@/views/NotFound.vue'),
      meta: { title: '404' }
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = `${to.meta.title || ''} - 光影美术馆`
  
  // 需要认证的页面
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('token')
    if (!token) {
      next({ name: 'Login', query: { redirect: to.fullPath } })
      return
    }
  }
  next()
})

export default router
