<!-- src/pages/AboutPage.vue -->
<template>
    <section style="display:grid; gap:12px;">
      <h1>联系我们</h1>
  
      <p>
        邮箱：surengang@zohomail.com
        电话:17674787438
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
  