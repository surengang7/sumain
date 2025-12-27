<template>
    <div class="page">
      <header class="page-header">
        <h1>证券列表</h1>
        <div class="sub">管理证券主数据（security）与标签（security_tag / security_tag_map）</div>
      </header>
  
      <div class="card">
        <div class="toolbar">
          <input v-model="kw" placeholder="搜索：代码/名称" />
          <select v-model="market">
            <option value="">全部市场</option>
            <option value="CN_A">CN_A</option>
            <option value="HK">HK</option>
            <option value="US">US</option>
          </select>
          <select v-model="tagId">
            <option :value="0">全部标签</option>
            <option v-for="t in tags" :key="t.tagId" :value="t.tagId">
              {{ t.taxonomy }} / {{ t.tagType }} / {{ t.tagName }}
            </option>
          </select>
          <button @click="load">查询</button>
        </div>
  
        <div class="table-wrap">
          <table class="tbl">
            <thead>
              <tr>
                <th>代码</th>
                <th>名称</th>
                <th>市场</th>
                <th>类型</th>
                <th>交易所</th>
                <th>币种</th>
                <th>上市</th>
                <th>状态</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="s in rows" :key="s.securityId">
                <td class="mono">{{ s.securityCode }}</td>
                <td>{{ s.securityName }}</td>
                <td class="mono">{{ s.market }}</td>
                <td class="mono">{{ s.securityType }}</td>
                <td class="mono">{{ s.exchangeCode || '-' }}</td>
                <td class="mono">{{ s.currency }}</td>
                <td class="mono">{{ s.listingDate || '-' }}</td>
                <td class="mono">{{ s.securityStatus }}</td>
                <td><button class="link" @click="go(s.securityId)">详情</button></td>
              </tr>
              <tr v-if="rows.length===0">
                <td colspan="9" class="empty">暂无数据</td>
              </tr>
            </tbody>
          </table>
        </div>
  
        <div class="hint">
          说明：这个页面的查询建议后端聚合（security + exchange + tag_map），减少前端多次请求。
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { onMounted, ref } from "vue";
  
  type Tag = { tagId: number; taxonomy: string; tagType: string; tagName: string };
  type SecurityRow = {
    securityId: number;
    securityCode: string;
    securityName: string;
    market: string;
    securityType: string;
    exchangeId: number;
    exchangeCode?: string | null;
    currency: string;
    listingDate?: string | null;
    securityStatus: string;
  };
  
  const kw = ref("");
  const market = ref("");
  const tagId = ref<number>(0);
  const tags = ref<Tag[]>([]);
  const rows = ref<SecurityRow[]>([]);
  
  async function apiGet<T>(url: string): Promise<T> {
    const r = await fetch(url);
    if (!r.ok) throw new Error(await r.text());
    return (await r.json()) as T;
  }
  
  async function loadTags() {
    tags.value = await apiGet<Tag[]>("/api/tags");
  }
  
  async function load() {
    const qs = new URLSearchParams();
    if (kw.value.trim()) qs.set("kw", kw.value.trim());
    if (market.value) qs.set("market", market.value);
    if (tagId.value) qs.set("tagId", String(tagId.value));
    rows.value = await apiGet<SecurityRow[]>(`/api/securities?${qs.toString()}`);
  }
  
  function go(securityId: number) {
    window.location.hash = `#/security/${securityId}`;
  }
  
  onMounted(async () => {
    await Promise.allSettled([loadTags()]);
    await load();
  });
  </script>
  
  <style scoped>
  .page{padding:16px;max-width:1400px;margin:0 auto;}
  .page-header{display:flex;flex-direction:column;gap:6px;margin-bottom:14px;}
  .sub{color:#666;font-size:13px;}
  .card{background:#fff;border:1px solid #eee;border-radius:12px;padding:12px;box-shadow:0 1px 6px rgba(0,0,0,.04);}
  .toolbar{display:flex;gap:8px;align-items:center;margin:10px 0;flex-wrap:wrap;}
  .toolbar input,.toolbar select{border:1px solid #ddd;border-radius:10px;padding:8px 10px;outline:none;min-width:180px;}
  button{border:1px solid #ddd;background:#fafafa;border-radius:10px;padding:8px 10px;cursor:pointer;}
  button.link{border:none;background:transparent;color:#0b66ff;padding:0;}
  .table-wrap{overflow:auto;}
  .tbl{width:100%;border-collapse:collapse;font-size:13px;}
  .tbl th,.tbl td{border-bottom:1px solid #f0f0f0;padding:8px 6px;white-space:nowrap;}
  .tbl th{text-align:left;color:#555;font-weight:600;}
  .mono{font-family:ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;}
  .empty{padding:14px;text-align:center;color:#888;}
  .hint{margin-top:10px;color:#777;font-size:12px;}
  </style>
  