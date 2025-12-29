<!-- src/pages/AboutPage.vue -->
<template>
    <section style="display:grid; gap:12px;">
      <h1>关于我们</h1>
  
      <p>
        这是一个演示 Vue3 路由的页面。  
        你现在看到的内容，是通过前端路由在不刷新页面的情况下加载的。
      </p>
  
      <!-- 返回首页按钮 -->
      <button @click="goHome" style="width:120px;">返回首页</button>
    </section>
  </template>
  
  <script setup lang="ts">
  import { useRouter } from 'vue-router';
  
  // useRouter() 类似 Java Web 里的 HttpServletRequest.getRequestDispatcher()
  // 你可以在前端主动跳转不同的路径
  const router = useRouter();
  
  function goHome() {
    router.push('/'); // 跳转到首页
  }
  </script>
  
  <style scoped>
  h1 {
    color: #3b82f6;
  }
  </style>
  