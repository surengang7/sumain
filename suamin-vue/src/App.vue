<!-- src/App.vue -->
<template>
  <div class="container">
    <!-- ===== 顶部导航 ===== -->
    <header class="navbar fancy">
      <div class="brand" @click="goHome">
        <span class="brand__icon">SU</span>
        <span class="brand__text">ATLAS</span>
      </div>

      <nav class="menu">
        <RouterLink to="/">首页</RouterLink>
        <RouterLink to="/about">关于我们</RouterLink>
        <RouterLink to="/contact">联系我们</RouterLink>
        <RouterLink to="/stock/dashboard">股票</RouterLink>
      </nav>
    </header>

    <!-- ✅ 关键：main 作为 flex:1 撑满剩余高度 -->
    <main class="main-area">
      <transition name="fade" mode="out-in">
        <RouterView />
      </transition>
    </main>

    <footer class="footer">
      <p>© 2025 Sunline Spring · 生活不止眼前的代码，还有未来和远方</p>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
const router = useRouter();
const goHome = () => router.push("/");
</script>

<style scoped>
/* ✅ 关键：容器要撑满整个视口，并纵向 flex */
.container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* ✅ 关键：中间内容区撑满剩余高度 */
.main-area {
  flex: 1;
  padding: 20px 0;
  /* 不要 overflow hidden，否则内部 sticky/滚动会怪 */
  overflow: visible;
}

/* ========== 导航整体样式 ========== */
.navbar.fancy {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 0;
  border-bottom: 1px solid var(--border);
}

/* 品牌 Logo 区 */
.brand {
  display: flex;
  align-items: baseline;
  gap: 8px;
  font-family: "Poppins", "Segoe UI", sans-serif;
  cursor: pointer;
  user-select: none;
  transition: transform 0.15s ease;
}
.brand:hover {
  transform: translateY(-2px);
}

.brand__icon {
  display: inline-block;
  font-weight: 700;
  font-size: 26px;
  line-height: 1;
  background: linear-gradient(135deg, #3b82f6 0%, #a78bfa 100%);
  -webkit-background-clip: text;
  color: transparent;
  letter-spacing: 1px;
  animation: shine 3s linear infinite;
}
@keyframes shine {
  0% { background-position: 0% 50%; }
  100% { background-position: 200% 50%; }
}

.brand__text {
  font-size: 26px;
  font-weight: 500;
  letter-spacing: 0.5px;
  color: var(--muted);
}

/* 导航菜单 */
.menu {
  display: flex;
  gap: 16px;
}
.menu a {
  text-decoration: none;
  color: var(--muted);
  font-size: 15px;
  padding: 6px 10px;
  border-radius: 8px;
  transition: color 0.2s, background-color 0.2s;
}
.menu a.router-link-active {
  color: var(--primary);
  background: color-mix(in srgb, var(--primary) 12%, transparent);
}

/* 淡入淡出动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.18s ease, transform 0.18s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(4px);
}

/* 页脚 */
.footer {
  border-top: 1px solid var(--border);
  padding: 12px 0;
  margin-top: 20px;
  text-align: center;
  font-size: 13px;
  color: var(--muted);
}
</style>
