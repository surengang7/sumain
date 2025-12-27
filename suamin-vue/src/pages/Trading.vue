<template>
    <div class="page">
      <header class="page-header">
        <h1>交易台</h1>
        <div class="sub">account / position_snapshot / cash_snapshot / orders / trade_fill / risk_event</div>
      </header>
  
      <div class="toolbar card">
        <select v-model="accountId">
          <option v-for="a in accounts" :key="a.accountId" :value="a.accountId">
            {{ a.broker }} / {{ a.mode }} (#{{ a.accountId }})
          </option>
        </select>
        <button @click="reload">刷新</button>
        <span class="hint">建议后端做“最新快照聚合接口”，避免前端多次拉取。</span>
      </div>
  
      <section class="grid">
        <div class="card">
          <h2>资金快照</h2>
          <div class="kv" v-if="cash">
            <div><span>时间</span><b class="mono">{{ cash.ts }}</b></div>
            <div><span>可用现金</span><b>{{ cash.cash }}</b></div>
            <div><span>冻结</span><b>{{ cash.frozenCash }}</b></div>
            <div><span>总权益</span><b>{{ cash.equity }}</b></div>
            <div><span>回撤</span><b>{{ cash.drawdown ?? '-' }}</b></div>
          </div>
          <div v-else class="empty">暂无</div>
  
          <h3>风控事件</h3>
          <ul class="list compact">
            <li v-for="e in risk" :key="e.eventId" class="row">
              <span class="mono">{{ e.ts }}</span>
              <span class="tag">{{ e.riskType }}</span>
            </li>
            <li v-if="risk.length===0" class="empty">暂无</li>
          </ul>
        </div>
  
        <div class="card">
          <h2>持仓（最新快照）</h2>
          <div class="table-wrap">
            <table class="tbl">
              <thead>
                <tr><th>标的</th><th class="num">数量</th><th class="num">成本</th><th class="num">市价</th><th class="num">市值</th><th class="num">浮盈</th></tr>
              </thead>
              <tbody>
                <tr v-for="p in positions" :key="p.securityId">
                  <td><button class="link" @click="goSecurity(p.securityId)">{{ p.securityName }}</button></td>
                  <td class="num">{{ p.qty }}</td>
                  <td class="num">{{ p.avgCost ?? '-' }}</td>
                  <td class="num">{{ p.marketPrice ?? '-' }}</td>
                  <td class="num">{{ p.marketValue ?? '-' }}</td>
                  <td class="num" :class="pnlClass(p.unrealizedPnl)">{{ p.unrealizedPnl ?? '-' }}</td>
                </tr>
                <tr v-if="positions.length===0"><td colspan="6" class="empty">暂无持仓</td></tr>
              </tbody>
            </table>
          </div>
  
          <h3>订单（最近）</h3>
          <div class="table-wrap">
            <table class="tbl">
              <thead>
                <tr><th>时间</th><th>标的</th><th>方向</th><th>类型</th><th class="num">价</th><th class="num">量</th><th>状态</th><th class="mono">order_id</th></tr>
              </thead>
              <tbody>
                <tr v-for="o in orders" :key="o.orderId" @click="selectOrder(o.orderId)" :class="{ active: o.orderId===activeOrderId }">
                  <td class="mono">{{ o.ts }}</td>
                  <td>{{ o.securityName }}</td>
                  <td><span class="tag">{{ o.side }}</span></td>
                  <td class="mono">{{ o.orderType }}</td>
                  <td class="num">{{ o.price ?? '-' }}</td>
                  <td class="num">{{ o.qty }}</td>
                  <td class="mono">{{ o.status }}</td>
                  <td class="mono ellipsis" :title="o.orderId">{{ o.orderId }}</td>
                </tr>
                <tr v-if="orders.length===0"><td colspan="8" class="empty">暂无订单</td></tr>
              </tbody>
            </table>
          </div>
  
          <h3>成交（选中订单）</h3>
          <div class="table-wrap">
            <table class="tbl">
              <thead>
                <tr><th>时间</th><th class="num">价</th><th class="num">量</th><th class="num">fee</th><th class="num">tax</th><th>exchange_trade_id</th></tr>
              </thead>
              <tbody>
                <tr v-for="f in fills" :key="f.fillId">
                  <td class="mono">{{ f.ts }}</td>
                  <td class="num">{{ f.price }}</td>
                  <td class="num">{{ f.qty }}</td>
                  <td class="num">{{ f.fee }}</td>
                  <td class="num">{{ f.tax }}</td>
                  <td class="mono">{{ f.exchangeTradeId || '-' }}</td>
                </tr>
                <tr v-if="fills.length===0"><td colspan="6" class="empty">选择上方订单查看成交</td></tr>
              </tbody>
            </table>
          </div>
  
        </div>
      </section>
    </div>
  </template>
  
  <script setup lang="ts">
  import { onMounted, ref, watch } from "vue";
  
  type AccountRow = { accountId: number; broker: string; mode: string };
  type CashRow = { ts: string; cash: string; frozenCash: string; equity: string; drawdown?: string | null };
  type RiskRow = { eventId: number; ts: string; riskType: string };
  
  type PosRow = {
    securityId: number;
    securityName: string;
    qty: number;
    avgCost?: string | null;
    marketPrice?: string | null;
    marketValue?: string | null;
    unrealizedPnl?: string | null;
  };
  
  type OrderRow = {
    orderId: string;
    ts: string;
    securityName: string;
    side: string;
    orderType: string;
    price?: number | null;
    qty: number;
    status: string;
  };
  
  type FillRow = { fillId: string; ts: string; price: number; qty: number; fee: number; tax: number; exchangeTradeId?: string | null };
  
  const accounts = ref<AccountRow[]>([]);
  const accountId = ref<number>(0);
  
  const cash = ref<CashRow | null>(null);
  const risk = ref<RiskRow[]>([]);
  const positions = ref<PosRow[]>([]);
  const orders = ref<OrderRow[]>([]);
  const fills = ref<FillRow[]>([]);
  const activeOrderId = ref("");
  
  async function apiGet<T>(url: string): Promise<T> {
    const r = await fetch(url);
    if (!r.ok) throw new Error(await r.text());
    return (await r.json()) as T;
  }
  
  function goSecurity(id: number) {
    window.location.hash = `#/security/${id}`;
  }
  
  function pnlClass(v?: string | null) {
    if (!v) return "";
    const n = Number(v);
    if (Number.isNaN(n)) return "";
    if (n > 0) return "up";
    if (n < 0) return "down";
    return "";
  }
  
  async function loadAccounts() {
    accounts.value = await apiGet<AccountRow[]>(`/api/accounts`);
    if (!accountId.value && accounts.value.length) accountId.value = accounts.value[0].accountId;
  }
  
  async function reload() {
    if (!accountId.value) return;
    // 这里建议后端提供 /api/accounts/{id}/overview 聚合接口
    cash.value = await apiGet<CashRow>(`/api/accounts/${accountId.value}/cash/latest`);
    risk.value = await apiGet<RiskRow[]>(`/api/accounts/${accountId.value}/risk/latest?limit=20`);
    positions.value = await apiGet<PosRow[]>(`/api/accounts/${accountId.value}/positions/latest`);
    orders.value = await apiGet<OrderRow[]>(`/api/accounts/${accountId.value}/orders?limit=50`);
    activeOrderId.value = "";
    fills.value = [];
  }
  
  async function selectOrder(orderId: string) {
    activeOrderId.value = orderId;
    fills.value = await apiGet<FillRow[]>(`/api/orders/${orderId}/fills`);
  }
  
  watch(accountId, () => reload());
  
  onMounted(async () => {
    await loadAccounts();
    await reload();
  });
  </script>
  
  <style scoped>
  .page{padding:16px;max-width:1400px;margin:0 auto;}
  .page-header{display:flex;flex-direction:column;gap:6px;margin-bottom:14px;}
  .sub{color:#666;font-size:13px;}
  .grid{display:grid;grid-template-columns:1fr 2fr;gap:14px;align-items:start;}
  .card{background:#fff;border:1px solid #eee;border-radius:12px;padding:12px;box-shadow:0 1px 6px rgba(0,0,0,.04);}
  .toolbar{display:flex;gap:8px;align-items:center;flex-wrap:wrap;}
  select{border:1px solid #ddd;border-radius:10px;padding:8px 10px;outline:none;min-width:260px;}
  button{border:1px solid #ddd;background:#fafafa;border-radius:10px;padding:8px 10px;cursor:pointer;}
  button.link{border:none;background:transparent;color:#0b66ff;padding:0;cursor:pointer;}
  .hint{color:#777;font-size:12px;}
  .kv{display:grid;grid-template-columns:1fr 1fr;gap:8px;margin-top:10px;}
  .kv div{display:flex;justify-content:space-between;border:1px solid #f2f2f2;border-radius:10px;padding:8px 10px;}
  .kv span{color:#666;}
  .table-wrap{overflow:auto;margin-top:8px;}
  .tbl{width:100%;border-collapse:collapse;font-size:13px;}
  .tbl th,.tbl td{border-bottom:1px solid #f0f0f0;padding:8px 6px;white-space:nowrap;}
  .tbl th{text-align:left;color:#555;font-weight:600;}
  .num{text-align:right;}
  .tag{border:1px solid #eee;border-radius:999px;padding:2px 8px;background:#fafafa;font-size:12px;}
  .list{list-style:none;padding:0;margin:10px 0 0;display:flex;flex-direction:column;gap:8px;}
  .row{display:flex;gap:8px;align-items:center;flex-wrap:wrap;}
  .empty{padding:14px;text-align:center;color:#888;}
  .mono{font-family:ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;}
  .ellipsis{max-width:240px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;}
  tr.active{background:#f6f9ff;}
  .up{color:#d32029;}
  .down{color:#2f9e44;}
  @media (max-width: 1100px){.grid{grid-template-columns:1fr;}}
  </style>
  