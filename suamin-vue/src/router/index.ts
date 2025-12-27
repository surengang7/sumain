import { createRouter, createWebHistory } from 'vue-router';
import { stockRoutes } from './stock.routes';

// 定义路由表：path -> component
const routes = [
  // 首页
  { path: '/', component: () => import('../pages/HomePage.vue') },

  // ✅ 股票模块（单独维护）
  ...stockRoutes,

  // 关于我们
  {
    path: '/about',
    component: () => import('../pages/AboutLayout.vue'),
    children: [
      { path: '', redirect: '/about/team' },
      { path: 'team', component: () => import('../pages/AboutTeamPage.vue') },
      { path: 'history', component: () => import('../pages/AboutHistoryPage.vue') },
    ],
  },

  // 联系我们
  { path: '/contact', component: () => import('../pages/ContactPage.vue') },

  // 兜底
  { path: '/:pathMatch(.*)*', redirect: '/' },
];

export default createRouter({
  history: createWebHistory(),
  routes,
});
