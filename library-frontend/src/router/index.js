import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'
import BookManage from '../views/BookManage.vue'
import BorrowManage from '../views/BorrowManage.vue'
import UserManage from '../views/UserManage.vue'
import UserAudit from '../views/UserAudit.vue' // 新增
import BorrowStatistics from '../views/BorrowStatistics.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: Login },
  {
    path: '/home',
    name: 'Home',
    component: Home,
    children: [
      { path: 'book-manage', name: 'BookManage', component: BookManage },
      { path: 'borrow-manage', name: 'BorrowManage', component: BorrowManage },
      { path: 'user-manage', name: 'UserManage', component: UserManage },
      { path: 'user-audit', name: 'UserAudit', component: UserAudit }, // 新增
      { path: 'statistics', name: 'BorrowStatistics', component: BorrowStatistics }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (to.path !== '/login' && !user.id) {
    next('/login')
  } else if (['UserManage', 'UserAudit', 'BorrowStatistics'].includes(to.name) && user.role !== 0) {
    ElMessage.warning('无管理员权限')
    next('/home/book-manage')
  } else {
    next()
  }
})

export default router