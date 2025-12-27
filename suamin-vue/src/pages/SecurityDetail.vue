<template>
    <div class="page">
      <header class="page-header">
        <div class="row">
          <button @click="back">← 返回</button>
          <h1>{{ sec?.securityName || "证券详情" }}</h1>
          <span class="mono" v-if="sec">{{ sec.market }} / {{ sec.securityCode }}</span>
        </div>
        <div class="sub">证券工作台：主数据 + 行情 + 复权/公司行为 + 资讯 + 策略信号</div>
      </header>
  
      <section class="grid">
        <div class="card">
          <h2>基本信息</h2>
          <div v-if="sec" class="kv">
            <div><span>证券ID</span><b class="mono">{{ sec.securityId }}</b></div>
            <div><span>代码</span><b class="mono">{{ sec.securityCode }}</b></div>
            <div><span>市场</span><b class="mono">{{ sec.market }}</b></div>
            <div><span>类型</span><b class="mono">{{ sec.securityType }}</b></div>
            <div><span>交易所</span><b class="mono">{{ sec.exchangeCode || sec.exchangeId }}</b></div>
            <div><span>币种</span><b class="mono">{{ sec.currency }}</b></div>
            <div><span>最小单位</span><b class="mono">{{ sec.tradeMinSize }}</b></div>
            <div><span>上市</span><b class="mono">{{ sec.listingDate || "-" }}</b></div>
            <div><span>状态</span><b class="mono">{{ sec.securityStatus }}</b></div>
          </div>
          <div v-else class="empty">加载中…</div>
  
          <h3>标签</h3>
          <div class="tags">
            <span v-for="t in tags" :key="t.tagId" class="tag">
              {{ t.taxonomy }}/{{ t.tagType }}: {{ t.tagName }}
            </span>
            <span v-if="tags.length===0" class="empty-inline">暂无标签</span>
          </div>
        </div>
  
        <div class="card">
          <h2>最近日线</h2>
          <div class="toolbar">
            <label>区间：</label>
            <input type="date" v-model="from" />
            <span>—</span>
            <input type="date" v-model="to" />
            <button @click="loadDaily">刷新</button>
          </div>
  
          <div class="table-wrap">
            <table class="tbl">
              <thead>
                <tr>
                  <th>日期</th><th class="num">开</th><th class="num">高</th><th class="num">低</th><th class="num">收</th>
                  <th class="num">涨跌幅</th><th class="num">成交量</th><th class="num">成交额</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="d in daily" :key="d.tradeDate">
                  <td class="mono">{{ d.tradeDate }}</td>
                  <td class="num">{{ fmt(d.open) }}</td>
                  <td class="num">{{ fmt(d.high) }}</td>
                  <td class="num">{{ fmt(d.low) }}</td>
                  <td class="num">{{ fmt(d.close) }}</td>
                  <td class="num" :class="pctClass(d.pctChg)">{{ fmtPct(d.pctChg) }}</td>
                  <td class="num">{{ d.volume }}</td>
                  <td class="num">{{ fmtMoney(d.amount) }}</td>
                </tr>
                <tr v-if="daily.length===0"><td colspan="8" class="empty">暂无数据</td></tr>
              </tbody>
            </table>
          </div>
  
          <div class="split">
            <div class="mini">
              <h3>复权因子</h3>
              <ul class="list compact">
                <li v-for="a in adj" :key="a.tradeDate" class="row">
                  <span class="mono">{{ a.tradeDate }}</span>
                  <span class="mono">factor={{ a.factor }}</span>
                </li>
                <li v-if="adj.length===0" class="empty">暂无</li>
              </ul>
            </div>
            <div class="mini">
              <h3>公司行为</h3>
              <ul class="list compact">
                <li v-for="c in actions" :key="c.actionId" class="row">
                  <span class="mono">{{ c.actionDate }}</span>
                  <span class="tag">{{ c.actionType }}</span>
                  <span class="mono" v-if="c.cashPerShare">cash={{ c.cashPerShare }}</span>
                  <span class="mono" v-if="c.ratio">ratio={{ c.ratio }}</span>
                </li>
                <li v-if="actions.length===0" class="empty">暂无</li>
              </ul>
            </div>
          </div>
        </div>
  
        <div class="card">
          <h2>关联资讯</h2>
          <div class="toolbar">
            <select v-model="newsFilter">
              <option value="">全部</option>
              <option value="NEWS">资讯</option>
              <option value="ANNOUNCEMENT">公告</option>
              <option value="REPORT">研报</option>
            </select>
            <button @click="loadNews">刷新</button>
          </div>
  
          <ul class="list">
            <li v-for="n in relatedNews" :key="n.newsId" class="list-item">
              <div class="title" @click="goNews(n.newsId)">{{ n.title }}</div>
              <div class="meta">
                <span class="mono">{{ n.publishTime }}</span>
                <span class="tag">{{ n.source }}</span>
                <span class="tag">{{ n.newsType }}</span>
                <span class="tag" v-if="n.sentiment !== null && n.sentiment !== undefined">情绪 {{ n.sentiment }}</span>
                <span class="tag" v-if="n.importance !== null && n.importance !== undefined">重要度 {{ n.importance }}</span>
              </div>
              <div class="digest" v-if="n.digest">{{ n.digest }}</div>
            </li>
            <li v-if="relatedNews.length===0" class="empty">暂无关联资讯</li>
          </ul>
        </div>
  
        <div class="card">
          <h2>策略信号</h2>
          <div class="toolbar">
            <input v-model="runId" class="mono" placeholder="可选：run_id 过滤" />
            <button @click="loadSignals">刷新</button>
          </div>
  
          <div class="table-wrap">
            <table class="tbl">
              <thead>
                <tr>
                  <th>时间</th><th>类型</th><th class="num">强度</th><th class="num">参考价</th><th>原因</th><th class="mono">run_id</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="s in signals" :key="s.signalId">
                  <td class="mono">{{ s.ts }}</td>
                  <td><span class="tag">{{ s.signalType }}</span></td>
                  <td class="num">{{ s.strength }}</td>
                  <td class="num">{{ fmt(s.priceRef) }}</td>
                  <td class="ellipsis" :title="s.reason || ''">{{ s.reason || '-' }}</td>
                  <td class="mono ellipsis" :title="s.runId">{{ s.runId }}</td>
                </tr>
                <tr v-if="signals.length===0"><td colspan="6" class="empty">暂无信号</td></tr>
              </tbody>
            </table>
          </div>
  
          <div class="hint">
            说明：这里建议后端聚合 signal + strategy_run（补 strategyName/mode/status），前端展示会更完整。
          </div>
        </div>
      </section>
    </div>
  </template>
  
  <script setup lang="ts">
  import { onMounted, ref } from "vue";
  
  function getIdFromHash(): number {
    const m = window.location.hash.match(/security\/(\d+)/);
    return m ? Number(m[1]) : 0;
  }
  const securityId = ref<number>(getIdFromHash());
  
  type SecurityDetail = {
    securityId: number;
    securityCode: string;
    securityName: string;
    market: string;
    securityType: string;
    currency: string;
    tradeMinSize: number;
    exchangeId: number;
    exchangeCode?: string | null;
    listingDate?: string | null;
    securityStatus: string;
  };
  
  type Tag = { tagId: number; taxonomy: string; tagType: string; tagName: string };
  
  type DailyBar = {
    tradeDate: string;
    open: number;
    high: number;
    low: number;
    close: number;
    volume: number;
    amount?: number | null;
    pctChg?: number | null;
  };
  
  type Adj = { tradeDate: string; factor: string };
  type Action = { actionId: number; actionDate: string; actionType: string; cashPerShare?: string | null; ratio?: string | null };
  
  type RelatedNews = {
    newsId: string;
    title: string;
    digest?: string | null;
    source: string;
    newsType: string;
    publishTime: string;
    sentiment?: number | null;
    importance?: number | null;
  };
  
  type SignalRow = {
    signalId: number;
    runId: string;
    ts: string;
    signalType: string;
    strength: number;
    priceRef?: number | null;
    reason?: string | null;
  };
  
  const sec = ref<SecurityDetail | null>(null);
  const tags = ref<Tag[]>([]);
  const from = ref("");
  const to = ref("");
  const daily = ref<DailyBar[]>([]);
  const adj = ref<Adj[]>([]);
  const actions = ref<Action[]>([]);
  const newsFilter = ref("");
  const relatedNews = ref<RelatedNews[]>([]);
  const runId = ref("");
  const signals = ref<SignalRow[]>([]);
  
  async function apiGet<T>(url: string): Promise<T> {
    const r = await fetch(url);
    if (!r.ok) throw new Error(await r.text());
    return (await r.json()) as T;
  }
  
  function fmt(v: number | null | undefined, digits=2) {
    if (v === null || v === undefined) return "-";
    return Number(v).toFixed(digits);
  }
  function fmtPct(v: number | null | undefined) {
    if (v === null || v === undefined) return "-";
    return (Number(v) * 100).toFixed(2) + "%";
  }
  function fmtMoney(v: number | null | undefined) {
    if (v === null || v === undefined) return "-";
    const n = Number(v);
    if (n >= 1e8) return (n / 1e8).toFixed(2) + "亿";
    if (n >= 1e4) return (n / 1e4).toFixed(2) + "万";
    return n.toFixed(0);
  }
  function pctClass(v: number | null | undefined) {
    if (v === null || v === undefined) return "";
    if (v > 0) return "up";
    if (v < 0) return "down";
    return "";
  }
  
  function back() {
    window.history.back();
  }
  function goNews(newsId: string) {
    window.location.hash = `#/news/${newsId}`;
  }
  
  async function loadBase() {
    // 聚合：security + exchange
    sec.value = await apiGet<SecurityDetail>(`/api/securities/${securityId.value}`);
    // tags：security_tag_map + security_tag
    tags.value = await apiGet<Tag[]>(`/api/securities/${securityId.value}/tags`);
  }
  
  async function loadDaily() {
    const qs = new URLSearchParams();
    if (from.value) qs.set("from", from.value);
    if (to.value) qs.set("to", to.value);
    daily.value = await apiGet<DailyBar[]>(`/api/securities/${securityId.value}/daily?${qs.toString()}`);
    // 同区间加载复权因子（可选）
    adj.value = await apiGet<Adj[]>(`/api/securities/${securityId.value}/adj-factor?${qs.toString()}`);
    // 公司行为通常按最近 N 条
    actions.value = await apiGet<Action[]>(`/api/securities/${securityId.value}/corporate-actions?limit=50`);
  }
  
  async function loadNews() {
    const qs = new URLSearchParams();
    if (newsFilter.value) qs.set("newsType", newsFilter.value);
    relatedNews.value = await apiGet<RelatedNews[]>(`/api/securities/${securityId.value}/news?${qs.toString()}`);
  }
  
  async function loadSignals() {
    const qs = new URLSearchParams();
    if (runId.value.trim()) qs.set("runId", runId.value.trim());
    signals.value = await apiGet<SignalRow[]>(`/api/securities/${securityId.value}/signals?${qs.toString()}`);
  }
  
  onMounted(async () => {
    // 默认日期区间：最近 60 天（后端也可以不传 date，按默认）
    const now = new Date();
    const d2 = now.toISOString().slice(0, 10);
    const d1 = new Date(now.getTime() - 60 * 24 * 3600 * 1000).toISOString().slice(0, 10);
    from.value = d1;
    to.value = d2;
  
    await loadBase();
    await Promise.allSettled([loadDaily(), loadNews(), loadSignals()]);
  });
  </script>
  
  <style scoped>
  .page{padding:16px;max-width:1400px;margin:0 auto;}
  .page-header{display:flex;flex-direction:column;gap:6px;margin-bottom:14px;}
  .row{display:flex;gap:10px;align-items:center;flex-wrap:wrap;}
  .sub{color:#666;font-size:13px;}
  .grid{display:grid;grid-template-columns:1.15fr 2fr;gap:14px;align-items:start;}
  .card{background:#fff;border:1px solid #eee;border-radius:12px;padding:12px;box-shadow:0 1px 6px rgba(0,0,0,.04);}
  .toolbar{display:flex;gap:8px;align-items:center;margin:10px 0;flex-wrap:wrap;}
  .toolbar input,.toolbar select,.toolbar input[type="date"]{border:1px solid #ddd;border-radius:10px;padding:8px 10px;outline:none;}
  button{border:1px solid #ddd;background:#fafafa;border-radius:10px;padding:8px 10px;cursor:pointer;}
  .table-wrap{overflow:auto;}
  .tbl{width:100%;border-collapse:collapse;font-size:13px;}
  .tbl th,.tbl td{border-bottom:1px solid #f0f0f0;padding:8px 6px;white-space:nowrap;}
  .tbl th{text-align:left;color:#555;font-weight:600;}
  .num{text-align:right;}
  .mono{font-family:ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;}
  .kv{display:grid;grid-template-columns:1fr 1fr;gap:8px;margin-top:8px;}
  .kv div{display:flex;justify-content:space-between;border:1px solid #f2f2f2;border-radius:10px;padding:8px 10px;}
  .kv span{color:#666;}
  .tags{display:flex;gap:8px;flex-wrap:wrap;margin-top:10px;}
  .tag{border:1px solid #eee;border-radius:999px;padding:2px 8px;background:#fafafa;font-size:12px;}
  .empty{padding:14px;text-align:center;color:#888;}
  .empty-inline{color:#888;font-size:12px;}
  .list{list-style:none;padding:0;margin:0;display:flex;flex-direction:column;gap:10px;}
  .list.compact{gap:8px;}
  .list-item{border:1px solid #f2f2f2;border-radius:12px;padding:10px;}
  .title{font-weight:700;cursor:pointer;}
  .meta{display:flex;gap:8px;margin-top:6px;color:#666;font-size:12px;flex-wrap:wrap;}
  .digest{margin-top:6px;color:#444;font-size:13px;line-height:1.5;}
  .split{display:grid;grid-template-columns:1fr 1fr;gap:12px;margin-top:10px;}
  .mini{border:1px solid #f2f2f2;border-radius:12px;padding:10px;}
  .up{color:#d32029;}
  .down{color:#2f9e44;}
  .ellipsis{max-width:260px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;}
  .hint{margin-top:10px;color:#777;font-size:12px;}
  @media (max-width: 1200px){.grid{grid-template-columns:1fr;}.ellipsis{max-width:220px;}}
  </style>
  