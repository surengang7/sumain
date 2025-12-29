<template>
    <div class="page">
      <header class="page-header">
        <div class="row">
          <button @click="back">← 返回</button>
          <h1>资讯详情</h1>
        </div>
      </header>
  
      <div class="card" v-if="item">
        <div class="title">{{ item.title }}</div>
        <div class="meta">
          <span class="mono">{{ item.publishTime }}</span>
          <span class="tag">{{ item.source }}</span>
          <span class="tag">{{ item.newsType }}</span>
          <a v-if="item.url" :href="item.url" target="_blank" rel="noreferrer">原文</a>
        </div>
  
        <div class="digest" v-if="item.digest">{{ item.digest }}</div>
        <pre class="content" v-if="item.contentText">{{ item.contentText }}</pre>
        <div v-else class="empty">正文为空（可能 content_ref 外置）</div>
  
        <div class="split">
          <div class="mini">
            <h3>关联证券</h3>
            <ul class="list compact">
              <li v-for="s in securities" :key="s.securityId" class="row">
                <button class="link" @click="goSecurity(s.securityId)">
                  {{ s.market }}/{{ s.securityCode }} {{ s.securityName }}
                </button>
                <span class="tag">{{ s.matchType }}</span>
                <span class="mono">conf={{ s.confidence }}</span>
              </li>
              <li v-if="securities.length===0" class="empty">暂无关联</li>
            </ul>
          </div>
          <div class="mini">
            <h3>特征（news_feature）</h3>
            <div class="kv">
              <div><span>情绪</span><b>{{ feature?.sentiment ?? '-' }}</b></div>
              <div><span>重要度</span><b>{{ feature?.importance ?? '-' }}</b></div>
              <div><span>主题</span><b>{{ feature?.topic ?? '-' }}</b></div>
              <div><span>模型</span><b class="mono">{{ feature?.modelVersion ?? '-' }}</b></div>
            </div>
            <div class="tags" v-if="feature?.keywords?.length">
              <span class="tag" v-for="k in feature!.keywords" :key="k">{{ k }}</span>
            </div>
          </div>
        </div>
      </div>
  
      <div v-else class="card empty">加载中…</div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { onMounted, ref } from "vue";
  
  function getIdFromHash(): string {
    const m = window.location.hash.match(/news\/([0-9a-fA-F-]+)/);
    return m ? m[1] : "";
  }
  const newsId = ref(getIdFromHash());
  
  type NewsItem = {
    newsId: string;
    source: string;
    newsType: string;
    title: string;
    digest?: string | null;
    url?: string | null;
    publishTime: string;
    contentText?: string | null;
    contentRef?: string | null;
  };
  
  type NewsFeature = {
    sentiment?: number | null;
    topic?: string | null;
    importance?: number | null;
    keywords?: string[] | null;
    modelVersion?: string | null;
  };
  
  type RelatedSecurity = {
    securityId: number;
    market: string;
    securityCode: string;
    securityName: string;
    matchType: string;
    confidence: string;
  };
  
  const item = ref<NewsItem | null>(null);
  const feature = ref<NewsFeature | null>(null);
  const securities = ref<RelatedSecurity[]>([]);
  
  async function apiGet<T>(url: string): Promise<T> {
    const r = await fetch(url);
    if (!r.ok) throw new Error(await r.text());
    return (await r.json()) as T;
  }
  
  function back() { window.history.back(); }
  function goSecurity(id: number) { window.location.hash = `#/security/${id}`; }
  
  onMounted(async () => {
    item.value = await apiGet<NewsItem>(`/api/news/${newsId.value}`);
    feature.value = await apiGet<NewsFeature>(`/api/news/${newsId.value}/feature`);
    securities.value = await apiGet<RelatedSecurity[]>(`/api/news/${newsId.value}/securities`);
  });
  </script>
  
  <style scoped>
  .page{padding:16px;max-width:1200px;margin:0 auto;}
  .page-header{margin-bottom:14px;}
  .row{display:flex;gap:10px;align-items:center;}
  .card{background:#fff;border:1px solid #eee;border-radius:12px;padding:12px;box-shadow:0 1px 6px rgba(0,0,0,.04);}
  .title{font-weight:800;font-size:20px;}
  .meta{display:flex;gap:8px;margin-top:8px;color:#666;font-size:12px;flex-wrap:wrap;align-items:center;}
  .tag{border:1px solid #eee;border-radius:999px;padding:2px 8px;background:#fafafa;font-size:12px;}
  .digest{margin-top:10px;color:#444;font-size:13px;line-height:1.6;}
  .content{margin-top:12px;background:#0b1020;color:#cfe2ff;padding:12px;border-radius:12px;overflow:auto;white-space:pre-wrap;}
  .split{display:grid;grid-template-columns:1fr 1fr;gap:12px;margin-top:14px;}
  .mini{border:1px solid #f2f2f2;border-radius:12px;padding:10px;}
  .list{list-style:none;padding:0;margin:0;display:flex;flex-direction:column;gap:10px;}
  .list.compact{gap:8px;}
  .empty{padding:14px;text-align:center;color:#888;}
  button{border:1px solid #ddd;background:#fafafa;border-radius:10px;padding:8px 10px;cursor:pointer;}
  button.link{border:none;background:transparent;color:#0b66ff;padding:0;cursor:pointer;text-align:left;}
  .kv{display:grid;grid-template-columns:1fr 1fr;gap:8px;margin-top:8px;}
  .kv div{display:flex;justify-content:space-between;border:1px solid #f2f2f2;border-radius:10px;padding:8px 10px;}
  .kv span{color:#666;}
  .tags{display:flex;gap:8px;flex-wrap:wrap;margin-top:10px;}
  .mono{font-family:ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;}
  @media (max-width: 1000px){.split{grid-template-columns:1fr;}}
  </style>
  