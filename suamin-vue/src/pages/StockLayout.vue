<!-- src/pages/StockLayout.vue -->
<template>
    <div class="stock-shell">
      <aside class="side">
        <div class="side__inner">
          <div class="side__title">股票系统</div>
  
          <nav class="side__menu">
            <RouterLink to="/stock/dashboard">总览</RouterLink>
            <RouterLink to="/stock/securities">证券</RouterLink>
            <RouterLink to="/stock/news">资讯</RouterLink>
            <RouterLink to="/stock/strategies">策略</RouterLink>
            <RouterLink to="/stock/trading">交易</RouterLink>
            <RouterLink to="/stock/ingest">采集</RouterLink>
          </nav>
  
          <div class="side__hint">
            <div class="hint__title">提示</div>
            <div class="hint__text">建议：先把证券 + 日线 + 资讯打通，再做策略回测。</div>
          </div>
        </div>
      </aside>
  
      <section class="main">
        <div class="topbar">
          <div class="crumb">
            <span class="tag">Atlas</span>
            <span class="sep">/</span>
            <span class="muted">Stock</span>
          </div>
  
          <div class="actions">
            <button class="btn" @click="goBack">返回官网</button>
          </div>
        </div>
  
        <div class="content">
          <transition name="fade" mode="out-in">
            <RouterView />
          </transition>
        </div>
      </section>
    </div>
  </template>
  
  <script setup lang="ts">
  import { useRouter } from "vue-router";
  const router = useRouter();
  const goBack = () => router.push("/");
  </script>
  
  <style scoped>
  /* ✅ 关键：继承 App.vue main-area 的高度（flex:1），让右侧能撑满 */
  .stock-shell {
    display: grid;
    grid-template-columns: 260px 1fr;
    gap: 14px;
    align-items: start;
  
    height: 100%;
    min-height: 100%;
  }
  
  /* 左侧 */
  .side__inner {
    position: sticky;
    top: 16px;
    border: 1px solid var(--border);
    border-radius: 14px;
    background: #fff;
    padding: 12px;
  }
  .side__title {
    font-weight: 800;
    font-size: 16px;
    margin-bottom: 10px;
  }
  .side__menu {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }
  .side__menu a {
    text-decoration: none;
    color: var(--muted);
    padding: 9px 10px;
    border-radius: 10px;
    border: 1px solid transparent;
    transition: background-color .15s, border-color .15s, color .15s;
  }
  .side__menu a.router-link-active {
    color: var(--primary);
    border-color: color-mix(in srgb, var(--primary) 25%, transparent);
    background: color-mix(in srgb, var(--primary) 10%, transparent);
  }
  .side__hint {
    margin-top: 12px;
    padding-top: 12px;
    border-top: 1px dashed var(--border);
  }
  .hint__title { font-weight: 700; font-size: 13px; margin-bottom: 6px; }
  .hint__text { font-size: 12px; color: var(--muted); line-height: 1.5; }
  
  /* ✅ 关键：右侧面板最小高度铺满 */
  .main {
    border: 1px solid var(--border);
    border-radius: 14px;
    background: #fff;
  
    min-height: 100%;
  }
  
  /* topbar */
  .topbar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px 14px;
    border-bottom: 1px solid var(--border);
    background: linear-gradient(180deg, #ffffff, #fbfbff);
  }
  .crumb { display: flex; align-items: center; gap: 8px; }
  .tag {
    padding: 2px 8px;
    border-radius: 999px;
    border: 1px solid var(--border);
    font-size: 12px;
  }
  .sep { color: var(--muted); }
  .muted { color: var(--muted); font-size: 13px; }
  
  .btn {
    border: 1px solid var(--border);
    background: #fafafa;
    border-radius: 10px;
    padding: 8px 10px;
    cursor: pointer;
  }
  .btn:hover { background: #f3f3f3; }
  
  /* content */
  .content {
    padding: 16px;
  }
  
  /* 动画 */
  .fade-enter-active, .fade-leave-active {
    transition: opacity .18s ease, transform .18s ease;
  }
  .fade-enter-from, .fade-leave-to {
    opacity: 0;
    transform: translateY(4px);
  }
  
  @media (max-width: 1000px) {
    .stock-shell { grid-template-columns: 1fr; }
    .side__inner { position: relative; top: 0; }
  }
  </style>
  