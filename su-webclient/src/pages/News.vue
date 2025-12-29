<template>
    <div class="page">
      <header class="page-header">
        <h1>资讯中心</h1>
        <div class="sub">news_item + news_feature + news_security_map（按证券关联/情绪/重要度筛选）</div>
      </header>
  
      <div class="card">
        <div class="toolbar">
          <input v-model="kw" placeholder="关键词：标题/摘要" />
          <select v-model="newsType">
            <option value="">全部类型</option>
            <option value="NEWS">NEWS</option>
            <option value="ANNOUNCEMENT">ANNOUNCEMENT</option>
            <option value="REPORT">REPORT</option>
          </select>
          <input v-model="source" placeholder="source：eastmoney/cls/ths..." />
          <input v-model="securityId" class="mono" placeholder="可选：security_id" />
          <button @click="load">查询</button>
        </div>
  
        <ul class="list">
          <li v-for="n in rows" :key="n.newsId" class="list-item">
            <div class="title" @click="go(n.newsId)">{{ n.title }}</div>
            <div class="meta">
              <span class="mono">{{ n.publishTime }}</span>
              <span class="tag">{{ n.source }}</span>
              <span class="tag">{{ n.newsType }}</span>
              <span class="tag" v-if="n.sentiment !== null && n.sentiment !== undefined">情绪 {{ n.sentiment }}</span>
              <span class="tag" v-if="n.importance !== null && n.importance !== undefined">重要度 {{ n.importance }}</span>
            </div>
            <div class="digest" v-if="n.digest">{{ n.digest }}</div>
          </li>
          <li v-if="rows.length===0" class="empty">暂无数据</li>
        </ul>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { onMounted, ref } from "vue";
  
  type NewsRow = {
    newsId: string;
    title: string;
    digest?: string | null;
    source: string;
    newsType: string;
    publishTime: string;
    sentiment?: number | null;
    importance?: number | null;
  };
  
  const kw = ref("");
  const newsType = ref("");
  const source = ref("");
  const securityId = ref("");
  const rows = ref<NewsRow[]>([]);
  
  async function apiGet<T>(url: string): Promise<T> {
    const r = await fetch(url);
    if (!r.ok) throw new Error(await r.text());
    return (await r.json()) as T;
  }
  
  async function load() {
    const qs = new URLSearchParams();
    if (kw.value.trim()) qs.set("kw", kw.value.trim());
    if (newsType.value) qs.set("newsType", newsType.value);
    if (source.value.trim()) qs.set("source", source.value.trim());
    if (securityId.value.trim()) qs.set("securityId", securityId.value.trim());
    rows.value = await apiGet<NewsRow[]>(`/api/news?${qs.toString()}`);
  }
  
  function go(newsId: string) {
    window.location.hash = `#/news/${newsId}`;
  }
  
  onMounted(load);
  </script>
  
  <style scoped>
  .page{padding:16px;max-width:1200px;margin:0 auto;}
  .page-header{display:flex;flex-direction:column;gap:6px;margin-bottom:14px;}
  .sub{color:#666;font-size:13px;}
  .card{background:#fff;border:1px solid #eee;border-radius:12px;padding:12px;box-shadow:0 1px 6px rgba(0,0,0,.04);}
  .toolbar{display:flex;gap:8px;align-items:center;margin:10px 0;flex-wrap:wrap;}
  .toolbar input,.toolbar select{border:1px solid #ddd;border-radius:10px;padding:8px 10px;outline:none;min-width:180px;}
  button{border:1px solid #ddd;background:#fafafa;border-radius:10px;padding:8px 10px;cursor:pointer;}
  .list{list-style:none;padding:0;margin:0;display:flex;flex-direction:column;gap:10px;}
  .list-item{border:1px solid #f2f2f2;border-radius:12px;padding:10px;}
  .title{font-weight:700;cursor:pointer;}
  .meta{display:flex;gap:8px;margin-top:6px;color:#666;font-size:12px;flex-wrap:wrap;}
  .digest{margin-top:6px;color:#444;font-size:13px;line-height:1.5;}
  .tag{border:1px solid #eee;border-radius:999px;padding:2px 8px;background:#fafafa;font-size:12px;}
  .empty{padding:14px;text-align:center;color:#888;}
  .mono{font-family:ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;}
  </style>
  