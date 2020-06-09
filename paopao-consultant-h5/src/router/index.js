import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)
const router = new Router({
  routes: [
    {
      path: '/',
      redirect: '/login' /* app */
      // redirect: '/index' /* web */
    },
    {
      path: '/register/:id',
      name: 'register',
      component: () => import('@/views/consulant/register')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/consulant/login')
    },
    // {
    //   path: '/download',
    //   name: 'Download',
    //   component: () => import('@/views/common/download')
    // },
    {
      path: '/consulant',
      name: 'consulant',
      component: () => import('@/views/consulant/index')
    },
    {
      path: '/article/:id',
      name: 'Article',
      component: () => import('@/views/article/index')
    },
    {
      path: '/consulant-service/:token',
      name: 'ConsulantService',
      component: () => import('@/views/consulant/service')
    }
  ]
})
export default router
