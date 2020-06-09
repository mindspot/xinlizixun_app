import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => ('redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard' }
    }]
  }
]
export const asyncRoutes = [
  {
    path: '/user',
    component: Layout,
    redirect: '/user/consultant',
    name: 'User',
    meta: { title: '客户管理', icon: 'example', roles: ['admin', 'supervisor'] },
    children: [
      {
        path: 'account',
        name: 'Account',
        component: () => import('@/views/user/account'),
        meta: { title: '账号管理', icon: 'tree', roles: ['admin'] }
      },
      {
        path: 'member',
        name: 'Member',
        component: () => import('@/views/user/member'),
        meta: { title: '会员管理', icon: 'tree', roles: ['admin'] }
      },
      {
        path: 'consultant',
        name: 'Consultant',
        component: () => import('@/views/user/consultant'),
        meta: { title: '咨询师管理', icon: 'table', roles: ['admin', 'supervisor'] }
      },
      {
        path: 'supervisor',
        name: 'Supervisor',
        component: () => import('@/views/user/supervisor'),
        meta: { title: '督导师管理', icon: 'table', roles: ['admin'] }
      },
      {
        path: 'consultantInfo/:id',
        component: () => import('@/views/user/consultantInfo'),
        meta: { title: '咨询师详情', icon: 'table', roles: ['admin', 'supervisor'] },
        hidden: true
      },
      {
        path: 'point-history',
        name: 'PointHistory',
        component: () => import('@/views/user/pointHistory'),
        meta: { title: '账户余额变更记录', icon: 'tree', roles: ['admin'] }
      }
    ]
  },
  {
    path: '/order',
    component: Layout,
    redirect: '/order/orderList',
    name: 'Order',
    meta: { title: '订单管理', icon: 'example', roles: ['admin', 'supervisor'] },
    children: [
      {
        path: 'order',
        name: 'Order',
        component: () => import('@/views/order/order'),
        meta: { title: '普通订单', icon: 'table', roles: ['admin', 'supervisor'] }
      },
      {
        path: 'setMealOrder',
        name: 'SetMealOrderList',
        component: () => import('@/views/order/setMealOrder'),
        meta: { title: '套餐订单', icon: 'table', roles: ['admin', 'supervisor'] }
      },
      {
        path: 'consultantSupervisorOrder',
        name: 'ConsultantSupervisorOrder',
        component: () => import('@/views/order/supervisorOrder'),
        meta: { title: '督导订单', icon: 'table', roles: ['admin', 'supervisor'] }
      },
      {
        path: 'setMealPayOrder',
        name: 'SetMealOrderPayList',
        component: () => import('@/views/order/setMealPayOrder'),
        meta: { title: '套餐卡支付订单', icon: 'table', roles: ['admin', 'supervisor'] }
      },
      {
        path: '/order/orderInfo/:id/:orderNo',
        component: () => import('@/views/order/orderInfo'),
        hidden: true
      },
      {
        path: '/order/setMealOrderInfo/:id/:orderNo',
        component: () => import('@/views/order/setMealOrderInfo'),
        hidden: true
      }
    ]
  },
  {
    path: '/common',
    component: Layout,
    redirect: '/common/orderList',
    name: 'Common',
    meta: { title: '配置管理', icon: 'example', roles: ['admin'] },
    children: [
      {
        path: 'common',
        name: 'Common',
        component: () => import('@/views/common/banner'),
        meta: { title: '轮播图', icon: 'table', roles: ['admin'] }
      },
      {
        path: 'tesQuestionst',
        name: 'TesQuestionst',
        component: () => import('@/views/common/tesQuestionst'),
        meta: { title: '新手引导', icon: 'table', roles: ['admin'] }
      },
      {
        path: 'certificate',
        name: 'Certificate',
        component: () => import('@/views/common/certificate'),
        meta: { title: '资质证书', icon: 'table', roles: ['admin'] }
      },
      {
        path: 'category',
        name: 'Category',
        component: () => import('@/views/common/category'),
        meta: { title: '咨询分类', icon: 'table', roles: ['admin'] }
      },
      {
        path: 'contention',
        name: 'Contention',
        component: () => import('@/views/common/contention'),
        meta: { title: '针对人群', icon: 'table', roles: ['admin'] }
      },
      {
        path: 'cashNotice',
        name: 'CashNotice',
        component: () => import('@/views/common/cashNotice'),
        meta: { title: '基础内容', icon: 'table', roles: ['admin'] }
      },
      {
        path: 'platformWorkingTime',
        name: 'PlatformWorkingTime',
        component: () => import('@/views/common/platformWorkingTime'),
        meta: { title: '排班时间', icon: 'table', roles: ['admin'] }
      },
      {
        path: 'customerService',
        name: 'CustomerService',
        component: () => import('@/views/common/customerService'),
        meta: { title: '客服电话', icon: 'table', roles: ['admin'] }
      }
    ]
  },
  {
    path: '/coupon',
    component: Layout,
    redirect: '/coupon/list',
    name: 'Coupon',
    meta: { title: '优惠劵管理', icon: 'example', roles: ['admin'] },
    children: [
      {
        path: 'coupon-template',
        name: 'CouponTemplate',
        component: () => import('@/views/coupon/template'),
        meta: { title: '优惠劵模板', icon: 'table', roles: ['admin'] }
      },
      {
        path: 'promotional-activities',
        name: 'PromotionalActivities',
        component: () => import('@/views/coupon/activities'),
        meta: { title: '优惠活动', icon: 'table', roles: ['admin'] }
      },
      {
        path: 'coupon',
        name: 'Coupon',
        component: () => import('@/views/coupon/coupon'),
        meta: { title: '优惠劵列表', icon: 'table', roles: ['admin'] }
      }
    ]
  },
  {
    path: '/consultantService',
    component: Layout,
    redirect: '/consultantService/list',
    name: 'ConsultantService',
    meta: { title: '服务审核', icon: 'example', roles: ['admin', 'supervisor'] },
    children: [
      {
        path: 'ConsultantService',
        name: 'consultantService',
        component: () => import('@/views/consultantService/consultantService'),
        meta: { title: '服务审核', icon: 'table', roles: ['admin', 'supervisor'] }
      }
    ]
  },
  {
    path: '/dispatch',
    component: Layout,
    redirect: '/web/dispatch/list',
    name: 'Dispatch',
    meta: { title: '派单系统', icon: 'example', roles: ['admin', 'supervisor'] },
    children: [
      {
        path: 'DispatchService',
        name: 'dispatch',
        component: () => import('@/views/dispatch/index'),
        meta: { title: '派单系统', icon: 'table', roles: ['admin', 'supervisor'] }
      }
    ]
  },
  {
    path: '/userCashWithdrawal',
    component: Layout,
    redirect: '/userCashWithdrawal/list',
    name: 'UserCashWithdrawal',
    meta: { title: '提现管理', icon: 'example', roles: ['admin'] },
    children: [
      {
        path: 'UserCashWithdrawal',
        name: 'userCashWithdrawal',
        component: () => import('@/views/userCashWithdrawal/userCashWithdrawal'),
        meta: { title: '客户提现列表', icon: 'table', roles: ['admin'] }
      },
      {
        path: 'ConsultantCashWithdrawal',
        name: 'consultantCashWithdrawal',
        component: () => import('@/views/userCashWithdrawal/consultantCashWithdrawal'),
        meta: { title: '咨询师提现列表', icon: 'table', roles: ['admin'] }
      }
    ]
  },
  {
    path: '/article-manage',
    component: Layout,
    redirect: '/article-manage/article',
    name: 'User',
    meta: { title: '内容管理', icon: 'example', roles: ['admin', 'article'] },
    children: [
      {
        path: 'category',
        name: 'Category',
        component: () => import('@/views/content/category/index'),
        meta: { title: '文章分类', icon: 'tree', roles: ['admin'] }
      },
      {
        path: 'article',
        name: 'Article',
        component: () => import('@/views/content/list'),
        meta: { title: '文章管理', icon: 'tree', roles: ['admin', 'article'] }
      },
      {
        path: 'create-article',
        name: 'ArticleCreate',
        component: () => import('@/views/content/create'),
        meta: { title: '创建文章', icon: 'tree', roles: ['admin', 'article'] },
        hidden: true
      },
      {
        path: 'edit-article/:id',
        name: 'EditArticle',
        hidden: true,
        component: () => import('@/views/content/edit'),
        meta: {
          title: '编辑文章', icon: 'function',
          roles: ['admin', 'article']
        }
      }
    ]
  },
  {
    path: '/proposal',
    component: Layout,
    redirect: '/proposal/list',
    name: 'Proposal',
    meta: { title: '建议管理', icon: 'example', roles: ['admin'] },
    children: [
      {
        path: 'Proposal',
        name: 'proposal',
        component: () => import('@/views/proposal/proposal'),
        meta: { title: '建议列表', icon: 'table', roles: ['admin'] }
      }
    ]
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]
const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
