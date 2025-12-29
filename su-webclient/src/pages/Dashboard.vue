<template>
    <div class="dash">
      <header class="head">
        <div>
          <h1 class="title">Atlas 交易台</h1>
          <div class="sub">总览：关注标的 / 最新资讯 / 运行状态 / 风控事件</div>
        </div>
  
        <div class="head-actions">
          <button class="btn" @click="refreshAll">全部刷新</button>
        </div>
      </header>
  
      <section class="grid">
        <!-- 关注标的 -->
        <div class="card">
          <div class="card-head">
            <h2>关注标的</h2>
            <div class="toolbar">
              <input v-model="watchKeyword" placeholder="搜索代码/名称" />
              <button class="btn" @click="loadWatchlist">刷新</button>
            </div>
          </div>
  
          <div class="card-body">
            <div v-if="filteredWatchlist.length === 0" class="empty-state">
              <div class="empty-icon">📌</div>
              <div>
                <div class="empty-title">还没有关注标的</div>
                <div class="empty-desc">从「证券」页添加自选，或先导入证券列表。</div>
                <div class="empty-actions">
                  <button class="btn" @click="goTo('/stock/securities')">去添加</button>
                  <button class="btn ghost" @click="loadWatchlist">刷新</button>
                </div>
              </div>
            </div>
  
            <div v-else class="table-wrap">
              <table class="tbl">
                <thead>
                  <tr>
                    <th style="width: 92px;">代码</th>
                    <th>名称</th>
                    <th style="width: 72px;">市场</th>
                    <th class="num" style="width: 92px;">最新价</th>
                    <th class="num" style="width: 92px;">涨跌幅</th>
                    <th class="num" style="width: 110px;">成交额</th>
                    <th style="width: 110px;">更新</th>
                    <th style="width: 70px;"></th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="it in filteredWatchlist" :key="it.securityId">
                    <td class="mono">{{ it.securityCode }}</td>
                    <td class="name-cell">
                      <span class="name">{{ it.securityName }}</span>
                    </td>
                    <td class="mono">{{ it.market }}</td>
                    <td class="num">{{ fmt(it.lastClose) }}</td>
                    <td class="num" :class="pctClass(it.pctChg)">{{ fmtPct(it.pctChg) }}</td>
                    <td class="num">{{ fmtMoney(it.amount) }}</td>
                    <td class="mono">{{ it.tradeDate }}</td>
                    <td class="op">
                      <button class="link" @click="goSecurity(it.securityId)">详情</button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
  
        <!-- 最新资讯 -->
        <div class="card">
          <div class="card-head">
            <h2>最新资讯</h2>
            <div class="toolbar">
              <select v-model="newsType">
                <option value="">全部</option>
                <option value="NEWS">资讯</option>
                <option value="ANNOUNCEMENT">公告</option>
                <option value="REPORT">研报</option>
              </select>
              <button class="btn" @click="loadNews">刷新</button>
            </div>
          </div>
  
          <div class="card-body">
            <ul v-if="news.length > 0" class="list">
              <li v-for="n in news" :key="n.newsId" class="list-item" @click="goNews(n.newsId)">
                <div class="li-title">{{ n.title }}</div>
                <div class="li-meta">
                  <span class="mono">{{ n.publishTime }}</span>
                  <span class="tag">{{ n.source }}</span>
                  <span class="tag" v-if="n.newsType">{{ n.newsType }}</span>
                </div>
                <div class="li-digest" v-if="n.digest">{{ n.digest }}</div>
              </li>
            </ul>
  
            <div v-else class="empty-state slim">
              <div class="empty-icon">📰</div>
              <div>
                <div class="empty-title">暂无资讯</div>
                <div class="empty-desc">可先跑一次采集任务，或稍后刷新看看。</div>
                <div class="empty-actions">
                  <button class="btn ghost" @click="loadNews">刷新</button>
                  <button class="btn ghost" @click="goTo('/stock/ingest')">去采集</button>
                </div>
              </div>
            </div>
          </div>
        </div>
  
        <!-- 策略运行状态 -->
        <div class="card">
          <div class="card-head">
            <h2>策略运行状态</h2>
            <div class="toolbar">
              <button class="btn" @click="loadRuns">刷新</button>
            </div>
          </div>
  
          <div class="card-body">
            <div v-if="runs.length === 0" class="empty-state">
              <div class="empty-icon">⚙️</div>
              <div>
                <div class="empty-title">暂无策略运行</div>
                <div class="empty-desc">创建策略后可发起回测 / 模拟 / 实盘运行。</div>
                <div class="empty-actions">
                  <button class="btn" @click="goTo('/stock/strategies')">去策略</button>
                  <button class="btn ghost" @click="loadRuns">刷新</button>
                </div>
              </div>
            </div>
  
            <div v-else class="table-wrap">
              <table class="tbl">
                <thead>
                  <tr>
                    <th>策略</th>
                    <th style="width: 92px;">模式</th>
                    <th style="width: 92px;">状态</th>
                    <th style="width: 160px;">开始</th>
                    <th style="width: 160px;">结束</th>
                    <th style="width: 70px;"></th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="r in runs" :key="r.runId">
                    <td class="name-cell"><span class="name">{{ r.strategyName }}</span></td>
                    <td class="mono">{{ r.mode }}</td>
                    <td><span class="pill" :class="statusClass(r.status)">{{ r.status }}</span></td>
                    <td class="mono">{{ r.startTime || "-" }}</td>
                    <td class="mono">{{ r.endTime || "-" }}</td>
                    <td class="op"><button class="link" @click="goRun(r.runId)">详情</button></td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
  
        <!-- 风控事件 -->
        <div class="card">
          <div class="card-head">
            <h2>风控事件</h2>
            <div class="toolbar">
              <select v-model="accountId">
                <option :value="0">全部账户</option>
                <option v-for="a in accounts" :key="a.accountId" :value="a.accountId">
                  {{ a.broker }} / {{ a.mode }} (#{{ a.accountId }})
                </option>
              </select>
              <button class="btn" @click="loadRisk">刷新</button>
            </div>
          </div>
  
          <div class="card-body">
            <ul v-if="riskEvents.length > 0" class="list compact">
              <li v-for="e in riskEvents" :key="e.eventId" class="list-item">
                <div class="li-meta">
                  <span class="mono">{{ e.ts }}</span>
                  <span class="tag">{{ e.riskType }}</span>
                  <span class="tag">acct: {{ e.accountId }}</span>
                </div>
                <pre class="json" v-if="e.detail">{{ e.detail }}</pre>
              </li>
            </ul>
  
            <div v-else class="empty-state slim ok">
              <div class="empty-icon">✅</div>
              <div>
                <div class="empty-title">暂无风控事件</div>
                <div class="empty-desc">当前未触发止损 / 超限 / 异常等事件。</div>
                <div class="empty-actions">
                  <button class="btn ghost" @click="loadRisk">刷新</button>
                </div>
              </div>
            </div>
          </div>
        </div>
  
      </section>
    </div>
  </template>
  
  <script setup lang="ts">
  import { computed, onMounted, ref } from "vue";
  import { useRouter } from "vue-router";
  
  const router = useRouter();
  
  type WatchRow = {
    securityId: number;
    securityCode: string;
    securityName: string;
    market: string;
    tradeDate: string;
    lastClose: number | null;
    pctChg: number | null;
    amount: number | null;
  };
  
  type NewsItem = {
    newsId: string;
    title: string;
    digest?: string | null;
    source: string;
    newsType: string;
    publishTime: string;
  };
  
  type RunRow = {
    runId: string;
    strategyName: string;
    mode: string;
    status: string;
    startTime?: string | null;
    endTime?: string | null;
  };
  
  type AccountRow = { accountId: number; broker: string; mode: string };
  
  type RiskEventRow = {
    eventId: number;
    accountId: number;
    ts: string;
    riskType: string;
    detail?: string | null;
  };
  
  const watchKeyword = ref("");
  const watchlist = ref<WatchRow[]>([]);
  const newsType = ref("");
  const news = ref<NewsItem[]>([]);
  const runs = ref<RunRow[]>([]);
  const accounts = ref<AccountRow[]>([]);
  const accountId = ref<number>(0);
  const riskEvents = ref<RiskEventRow[]>([]);
  
  const filteredWatchlist = computed(() => {
    const kw = watchKeyword.value.trim().toLowerCase();
    if (!kw) return watchlist.value;
    return watchlist.value.filter(
      (x) =>
        x.securityCode.toLowerCase().includes(kw) ||
        x.securityName.toLowerCase().includes(kw)
    );
  });
  
  function goTo(path: string) {
    router.push(path);
  }
  
  function fmt(v: number | null | undefined, digits = 2) {
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
  function pctClass(v: number | null) {
    if (v === null || v === undefined) return "";
    if (v > 0) return "up";
    if (v < 0) return "down";
    return "";
  }
  function statusClass(s: string) {
    const t = (s || "").toUpperCase();
    if (t.includes("RUN")) return "running";
    if (t.includes("SUCC")) return "success";
    if (t.includes("FAIL")) return "failed";
    return "idle";
  }
  
  function goSecurity(id: number) {
    router.push(`/stock/security/${id}`);
  }
  function goRun(runId: string) {
    router.push(`/stock/strategy-runs/${runId}`);
  }
  function goNews(newsId: string) {
    router.push(`/stock/news/${newsId}`);
  }
  
  async function apiGet<T>(url: string): Promise<T> {
    const r = await fetch(url);
    if (!r.ok) throw new Error(await r.text());
    return (await r.json()) as T;
  }
  
  async function loadWatchlist() {
    watchlist.value = await apiGet<WatchRow[]>(`/api/dashboard/watchlist`);
  }
  async function loadNews() {
    const q = newsType.value ? `?newsType=${encodeURIComponent(newsType.value)}` : "";
    news.value = await apiGet<NewsItem[]>(`/api/news/latest${q}`);
  }
  async function loadRuns() {
    runs.value = await apiGet<RunRow[]>(`/api/strategy-runs/latest`);
  }
  async function loadAccounts() {
    accounts.value = await apiGet<AccountRow[]>(`/api/accounts`);
  }
  async function loadRisk() {
    const q = accountId.value ? `?accountId=${accountId.value}` : "";
    riskEvents.value = await apiGet<RiskEventRow[]>(`/api/risk-events/latest${q}`);
  }
  
  async function refreshAll() {
    await Promise.allSettled([loadWatchlist(), loadNews(), loadRuns(), loadAccounts()]);
    await loadRisk();
  }
  
  onMounted(refreshAll);
  </script>
  
  <style scoped>
  /* 页面骨架（子页面不再 max-width/margin，交给 StockLayout.content 控制） */
  .dash {
    display: flex;
    flex-direction: column;
    gap: 14px;
  }
  
  /* 顶部 */
  .head {
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
    gap: 12px;
  }
  .title {
    margin: 0;
    font-size: 30px;
    letter-spacing: 0.2px;
  }
  .sub {
    color: var(--muted);
    font-size: 13px;
    margin-top: 6px;
  }
  .head-actions {
    display: flex;
    gap: 8px;
  }
  
  /* 关键：卡片变大 + 卡片内滚动 */
  .grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 14px;
    align-items: stretch;
  }
  .card {
    border: 1px solid var(--border);
    border-radius: 14px;
    background: #fff;
    overflow: hidden;
    display: flex;
    flex-direction: column;
    min-height: 420px; /* ✅ 卡片更大 */
  }
  
  /* 卡片头 */
  .card-head {
    padding: 12px 12px 10px 12px;
    border-bottom: 1px solid var(--border);
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 10px;
  }
  .card h2 {
    margin: 0;
    font-size: 18px;
  }
  
  /* 卡片体：允许滚动 */
  .card-body {
    padding: 12px;
    flex: 1;
    overflow: auto; /* ✅ 卡片内部滚动 */
  }
  
  /* 工具条 */
  .toolbar {
    display: flex;
    gap: 8px;
    align-items: center;
    flex-wrap: wrap;
  }
  .toolbar input,
  .toolbar select {
    border: 1px solid var(--border);
    border-radius: 10px;
    padding: 8px 10px;
    outline: none;
    min-width: 180px;
    background: #fff;
  }
  
  /* 按钮 */
  .btn {
    border: 1px solid var(--border);
    background: #fafafa;
    border-radius: 10px;
    padding: 8px 10px;
    cursor: pointer;
  }
  .btn:hover {
    background: #f3f3f3;
  }
  .btn.ghost {
    background: transparent;
  }
  
  /* 表格 */
  .table-wrap {
    border: 1px solid #f2f2f2;
    border-radius: 12px;
    overflow: auto;
  }
  .tbl {
    width: 100%;
    border-collapse: collapse;
    font-size: 13px;
  }
  .tbl th,
  .tbl td {
    border-bottom: 1px solid #f2f2f2;
    padding: 10px 8px;
    white-space: nowrap;
  }
  .tbl thead th {
    position: sticky;
    top: 0;
    background: #fff;
    z-index: 1;
  }
  .tbl th {
    text-align: left;
    color: #555;
    font-weight: 800;
  }
  .num {
    text-align: right;
  }
  .op {
    text-align: right;
  }
  .mono {
    font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas,
      "Liberation Mono", "Courier New", monospace;
  }
  .name-cell {
    max-width: 220px;
  }
  .name {
    display: inline-block;
    max-width: 220px;
    overflow: hidden;
    text-overflow: ellipsis;
    vertical-align: bottom;
  }
  .link {
    border: none;
    background: transparent;
    color: var(--primary);
    cursor: pointer;
    padding: 0;
  }
  
  /* 列表 */
  .list {
    list-style: none;
    padding: 0;
    margin: 0;
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  .list.compact {
    gap: 8px;
  }
  .list-item {
    border: 1px solid #f2f2f2;
    border-radius: 12px;
    padding: 10px;
    cursor: pointer;
    transition: transform 0.12s ease, box-shadow 0.12s ease;
  }
  .list-item:hover {
    transform: translateY(-1px);
    box-shadow: 0 6px 18px rgba(0, 0, 0, 0.06);
  }
  .li-title {
    font-weight: 900;
    line-height: 1.35;
  }
  .li-meta {
    display: flex;
    gap: 8px;
    margin-top: 6px;
    color: #666;
    font-size: 12px;
    align-items: center;
    flex-wrap: wrap;
  }
  .li-digest {
    margin-top: 6px;
    color: #444;
    font-size: 13px;
    line-height: 1.5;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
  .tag {
    border: 1px solid var(--border);
    border-radius: 999px;
    padding: 2px 8px;
    background: #fafafa;
  }
  
  /* 状态 */
  .pill {
    border-radius: 999px;
    padding: 2px 8px;
    border: 1px solid var(--border);
    background: #fafafa;
    font-size: 12px;
  }
  .pill.running {
    background: #fff7e6;
    border-color: #ffd591;
  }
  .pill.success {
    background: #f6ffed;
    border-color: #b7eb8f;
  }
  .pill.failed {
    background: #fff1f0;
    border-color: #ffa39e;
  }
  .pill.idle {
    background: #f5f5f5;
    border-color: #ddd;
  }
  .up {
    color: #d32029;
  }
  .down {
    color: #2f9e44;
  }
  
  /* JSON */
  .json {
    margin: 8px 0 0;
    background: #0b1020;
    color: #cfe2ff;
    padding: 10px;
    border-radius: 10px;
    overflow: auto;
    font-size: 12px;
  }
  
  /* 空态（更舒服） */
  .empty-state {
    padding: 18px 14px;
    border: 1px dashed var(--border);
    border-radius: 14px;
    background: linear-gradient(180deg, #ffffff, #fbfbff);
    display: flex;
    align-items: center;
    gap: 12px;
  }
  .empty-state.slim {
    padding: 14px 12px;
    border-radius: 12px;
  }
  .empty-icon {
    width: 44px;
    height: 44px;
    border-radius: 14px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: color-mix(in srgb, var(--primary) 10%, #fff);
    border: 1px solid color-mix(in srgb, var(--primary) 18%, var(--border));
    font-size: 22px;
    flex: 0 0 auto;
  }
  .empty-title {
    font-weight: 900;
    font-size: 14px;
  }
  .empty-desc {
    margin-top: 4px;
    color: var(--muted);
    font-size: 12px;
    line-height: 1.5;
  }
  .empty-actions {
    margin-top: 10px;
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
  }
  .empty-state.ok .empty-icon {
    background: #f6ffed;
    border-color: #b7eb8f;
  }
  
  /* 响应式 */
  @media (max-width: 1100px) {
    .grid {
      grid-template-columns: 1fr;
    }
    .card {
      min-height: 380px;
    }
  }
  </style>
  