<!-- src/pages/AboutLayout.vue -->
<template>
  <section style="display:grid; gap:12px;">
    <h1>关于我们（父级布局）</h1>

    <!-- 子导航：在 /about 域内切换子页面 -->
    <nav style="display:flex; gap:12px;">
      <!-- RouterLink 默认会按当前路由自动加高亮类（.router-link-active / .router-link-exact-active） -->
      <RouterLink to="/about/team">团队</RouterLink>
      <RouterLink to="/about/history">发展历程</RouterLink>
    </nav>

    <!-- 简易面包屑：关于我们 / [当前子页标题] -->
    <div class="breadcrumb">
      <RouterLink to="/about">关于我们</RouterLink>
      <span>/</span>
      <span>{{ crumbTitle }}</span>
    </div>

    <hr />
    <!-- 子路由出口：/about 的 children 会渲染到这里 -->
    <RouterView />
  </section>
</template>

<script setup lang="ts">
/**
 * 目标：
 * 1) 在父布局里根据当前子路由显示面包屑末级标题（团队 / 发展历程）
 * 2) 子导航的高亮交给 RouterLink 内置类名处理（也可自定义样式）
 */
import { useRoute } from 'vue-router';
import { computed } from 'vue';

const route = useRoute();

// 根据当前路径末尾决定面包屑标题
const crumbTitle = computed(() => {
  if (route.path.endsWith('/team')) return '团队';
  if (route.path.endsWith('/history')) return '发展历程';
  return ''; // /about 刚进来会跳转到 /about/team
});
</script>

<style scoped>
/* 子菜单高亮样式（使用 RouterLink 的内置类名） */
.router-link-active {
  font-weight: 600;
  color: #3b82f6;
}

/* 面包屑简单样式 */
.breadcrumb {
  display: flex;
  gap: 6px;
  align-items: center;
  color: #64748b;
  font-size: 14px;
}
.breadcrumb a {
  text-decoration: none;
}
</style>
