<!-- src/pages/HomePage.vue -->
<template>
    <!-- ===== 1) 顶部横幅（Hero） ===== -->
    <section class="hero">
      <div class="hero__content">
        <p class="eyebrow">相遇是美好，生活是态度，努力是姿态</p>
        <h1 class="hero__title">
          发现日常里的小确幸，
          <span class="grad">好好生活，从今天开始</span>
        </h1>
        <p class="hero__subtitle">
          We doing...
        </p>
  
        <!-- 搜索条（示意，暂不接后台） -->
        <!-- <div class="searchbar">
          <input v-model="q" type="text" placeholder="搜一搜：咖啡店 / 徒步 / 胶片相机 / 收纳技巧…" />
          <button class="btn" @click="onSearch">搜索</button>
        </div> -->
  
        <!-- 生活标签 -->
        <div class="tags">
          <button class="tag" @click="quickFill('城市咖啡地图')"># 城市咖啡地图</button>
          <button class="tag" @click="quickFill('周末徒步路线')"># 周末徒步路线</button>
          <button class="tag" @click="quickFill('小厨房快手菜')"># 小厨房快手菜</button>
          <button class="tag" @click="quickFill('极简收纳指南')"># 极简收纳指南</button>
        </div>
      </div>
      <!-- 右侧配图（可换成你自己的图片地址） -->
      <div class="hero__image">
        <img
          src="https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?q=80&w=1600&auto=format&fit=crop"
          alt="Morning coffee"
        />
      </div>
    </section>
  
    <!-- ===== 2) 生活分类（三列栅格） ===== -->
    <section class="container" style="margin-top: 28px;">
      <div class="grid">
        <article class="card cat">
          <img class="cat__img" src="https://images.unsplash.com/photo-1504754524776-8f4f37790ca0?q=80&w=800&auto=format&fit=crop" alt="简易料理">
          <div class="cat__body">
            <h3>简易料理</h3>
            <p class="muted">十分钟快手菜、周末家宴、便当灵感</p>
            <button class="btn-outline" @click="go('/about')">看看食谱</button>
          </div>
        </article>
  
        <article class="card cat">
          <img class="cat__img" src="https://images.unsplash.com/photo-1519710164239-da123dc03ef4?q=80&w=800&auto=format&fit=crop" alt="城市出游">
          <div class="cat__body">
            <h3>城市出游</h3>
            <p class="muted">2 小时微旅行、步行路线、街区口袋地图</p>
            <button class="btn-outline" @click="go('/about/history')">规划路线</button>
          </div>
        </article>
  
        <article class="card cat">
          <img class="cat__img" src="https://images.unsplash.com/photo-1493666438817-866a91353ca9?q=80&w=800&auto=format&fit=crop" alt="家居整理">
          <div class="cat__body">
            <h3>家居整理</h3>
            <p class="muted">极简收纳、居家氛围、提升幸福感的小物件</p>
            <button class="btn-outline" @click="go('/contact')">获取清单</button>
          </div>
        </article>
      </div>
    </section>
  
    <!-- ===== 3) 精选内容（文章卡片） ===== -->
    <section class="container" style="margin-top: 28px;">
      <h2 style="margin:0 0 12px;">本周精选</h2>
      <div class="grid">
        <article class="card post">
          <img class="post__cover" src="https://images.unsplash.com/photo-1478147427282-58a87a120781?q=80&w=1200&auto=format&fit=crop" alt="">
          <div class="post__body">
            <h3>把清晨还给自己：30 分钟晨间例行</h3>
            <p class="muted">从一杯热饮开始，到短暂冥想与拉伸，留一点时间跟自己相处。</p>
            <div class="row">
              <span class="pill">身心</span><span class="pill">早晨</span>
            </div>
          </div>
        </article>
  
        <article class="card post">
          <img class="post__cover" src="https://images.unsplash.com/photo-1469474968028-56623f02e42e?q=80&w=1200&auto=format&fit=crop" alt="">
          <div class="post__body">
            <h3>周末 8km 城市徒步路线 · 河岸版</h3>
            <p class="muted">把手机揣兜里，沿河慢走，记录一路的风与水。</p>
            <div class="row">
              <span class="pill">城市</span><span class="pill">徒步</span>
            </div>
          </div>
        </article>
  
        <article class="card post">
          <img class="post__cover" src="https://images.unsplash.com/photo-1520607162513-77705c0f0d4a?q=80&w=1200&auto=format&fit=crop" alt="">
          <div class="post__body">
            <h3>小厨房也能开大餐：三道快手家常菜</h3>
            <p class="muted">番茄炖牛腩 / 蒜香时蔬 / 黑椒鸡胸，简单也能很美味。</p>
            <div class="row">
              <span class="pill">料理</span><span class="pill">家常</span>
            </div>
          </div>
        </article>
      </div>
    </section>
  
    <!-- ===== 4) 本周计划（可勾选清单） ===== -->
    <section class="container" style="margin-top: 28px;">
      <div class="card checklist">
        <div class="row" style="justify-content: space-between;">
          <h2 style="margin:0;">本周小目标</h2>
          <button class="btn-outline" @click="resetPlan">重置</button>
        </div>
        <ul>
          <li v-for="(item, i) in plan" :key="i" @click="toggle(i)">
            <input type="checkbox" :checked="item.done" @change.stop="toggle(i)" />
            <span :class="{ done: item.done }">{{ item.text }}</span>
          </li>
        </ul>
      </div>
    </section>
  
    <!-- ===== 5) 订阅区（Newsletter） ===== -->
    <section class="container" style="margin: 28px 0 40px;">
      <div class="card subscribe">
        <div class="subscribe__left">
          <h2 style="margin:0 0 6px;">订阅周报</h2>
          <p class="muted" style="margin:0;">每周一封邮件，送达生活灵感与路线攻略。</p>
        </div>
        <div class="subscribe__right">
          <input v-model="email" type="email" placeholder="输入你的邮箱" />
          <button class="btn" @click="subscribe">订阅</button>
        </div>
      </div>
    </section>
  </template>
  
  <script setup lang="ts">
  /**
   * 脚本仅做最小交互：
   * - 搜索条：把输入填入控制台（后续可接真实接口）
   * - 本周计划：简单的勾选/重置交互
   * - 订阅：校验邮箱后 toast（alert）
   */
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';
  
  const router = useRouter();
  
  // 搜索框与快捷填充
  const q = ref('');
  function onSearch() {
    if (!q.value.trim()) return alert('请输入关键字再搜索～');
    console.log('search:', q.value);
    alert(`搜索：${q.value}（此处可接你的后端接口）`);
  }
  function quickFill(text: string) {
    q.value = text;
  }
  
  // 本周计划清单
  const plan = ref([
    { text: '做一道没做过的菜', done: false },
    { text: '完成 30 分钟步行/慢跑', done: false },
    { text: '整理书桌或阳台角落', done: false },
    { text: '阅读 30 页喜欢的书', done: false },
  ]);
  function toggle(i: number) { plan.value[i].done = !plan.value[i].done; }
  function resetPlan() { plan.value.forEach(it => (it.done = false)); }
  
  // 订阅
  const email = ref('');
  function subscribe() {
    const re = /\S+@\S+\.\S+/;
    if (!re.test(email.value)) return alert('请输入有效邮箱');
    alert(`订阅成功：${email.value}`);
    email.value = '';
  }
  
  // 跳转（示例：链接到你已有的路由）
  function go(path: string) { router.push(path); }
  </script>
  
  <style scoped>
  /* ===== Hero 区块样式 ===== */
  .hero {
    display: grid;
    grid-template-columns: 1.2fr 1fr;
    gap: 16px;
    align-items: center;
    max-width: 1080px;
    margin: 12px auto 0;
    padding: 0 16px;
  }
  .hero__content {
     padding: 24px 24px; 
    }
  .eyebrow { 
    color: var(--muted); 
    font-size: 28px;
    margin: 0 0 12px; 
    letter-spacing: .8px; 
  }
  .hero__title { 
    margin: 0 0 8px; 
    font-size: 34px; 
    line-height: 1.2; 
  }
  .grad { 
    background: linear-gradient(90deg, #60a5fa, #a78bfa); 
    -webkit-background-clip: text; 
    background-clip: text; 
    color: transparent; 
  }
  .hero__subtitle { margin: 0 0 12px; color: var(--muted); }
  
  .searchbar {
    display: flex; gap: 8px; margin-top: 8px;
  }
  .searchbar input {
    flex: 1; padding: 10px 12px; border-radius: 10px; border: 1px solid var(--border); outline: none;
    background: var(--surface); color: var(--text);
  }
  .tags { display: flex; flex-wrap: wrap; gap: 8px; margin-top: 12px; }
  .tag {
    border: 1px solid var(--border); background: transparent; color: var(--text);
    padding: 6px 10px; border-radius: 999px; cursor: pointer;
  }
  .tag:hover { border-color: var(--primary); color: var(--primary); }
  
  .hero__image img {
    width: 100%; height: 100%; object-fit: cover; border-radius: var(--radius); box-shadow: var(--shadow);
  }
  
  /* ===== 分类卡片 ===== */
  .cat { padding: 0; overflow: hidden; }
  .cat__img { width: 100%; height: 160px; object-fit: cover; display: block; }
  .cat__body { padding: 14px; }
  
  /* ===== 精选文章卡片 ===== */
  .post { padding: 0; overflow: hidden; }
  .post__cover { width: 100%; height: 160px; object-fit: cover; display: block; }
  .post__body { padding: 14px; }
  .pill {
    display: inline-block; font-size: 12px; padding: 4px 8px; border-radius: 999px;
    background: color-mix(in srgb, var(--primary) 12%, transparent); color: var(--primary);
  }
  
  /* ===== 清单卡片 ===== */
  .checklist ul { list-style: none; padding: 0; margin: 10px 0 0; }
  .checklist li {
    display: flex; align-items: center; gap: 10px; padding: 8px 0; border-bottom: 1px dashed var(--border); cursor: pointer;
  }
  .checklist li:last-child { border-bottom: 0; }
  .checklist .done { text-decoration: line-through; color: var(--muted); }
  
  /* ===== 订阅区 ===== */
  .subscribe {
    display: grid; grid-template-columns: 1fr auto; gap: 12px; align-items: center;
  }
  .subscribe__right { display: flex; gap: 8px; }
  .subscribe__right input {
    width: 260px; padding: 10px 12px; border-radius: 10px; border: 1px solid var(--border); background: var(--surface); color: var(--text);
  }
  
  /* 响应式：小屏 Hero 改为一列 */
  @media (max-width: 900px) {
    .hero { grid-template-columns: 1fr; }
  }
  </style>
  