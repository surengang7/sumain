<template>
    <div class="page">
      <header class="page-header">
        <h1>策略库</h1>
        <div class="sub">strategy + strategy_run + position_target + signal（策略定义、运行、输出、信号）</div>
      </header>
  
      <div class="grid">
        <div class="card">
          <h2>策略列表</h2>
          <div class="toolbar">
            <input v-model="kw" placeholder="策略名搜索" />
            <button @click="loadStrategies">查询</button>
          </div>
  
          <ul class="list">
            <li v-for="s in strategies" :key="s.strategyId" class="list-item" @click="selectStrategy(s)">
              <div class="title">{{ s.strategyName }}</div>
              <div class="meta">
                <span class="tag">{{ s.universe }}</span>
                <span class="tag">{{ s.barType }}</span>
                <span class="mono">#{{ s.strategyId }}</span>
              </div>
              <div class="desc" v-if="s.strategyDescription">{{ s.strategyDescription }}</div>
            </li>
            <li v-if="strategies.length===0" class="empty">暂无策略</li>
          </ul>
        </div>
  
        <div class="card">
          <h2>策略详情</h2>
          <div v-if="current">
            <div class="kv">
              <div><span>ID</span><b class="mono">{{ current.strategyId }}</b></div>
              <div><span>名称</span><b>{{ current.strategyName }}</b></div>
              <div><span>范围</span><b class="mono">{{ current.universe }}</b></div>
              <div><span>粒度</span><b class="mono">{{ current.barType }}</b></div>
            </div>
  
            <h3>最近运行</h3>
            <div class="table-wrap">
              <table class="tbl">
                <thead>
                  <tr><th>run</th><th>mode</th><th>status</th><th>start</th><th>end</th><th></th></tr>
                </thead>
                <tbody>
                  <tr v-for="r in runs" :key="r.runId">
                    <td class="mono ellipsis" :title="r.runId">{{ r.runId }}</td>
                    <td class="mono">{{ r.mode }}</td>
                    <td><span class="pill">{{ r.status }}</span></td>
                    <td class="mono">{{ r.startTime || "-" }}</td>
                    <td class="mono">{{ r.endTime || "-" }}</td>
                    <td><button class="link" @click.stop="goRun(r.runId)">进入</button></td>
                  </tr>
                  <tr v-if="runs.length===0"><td colspan="6" class="empty">暂无</td></tr>
                </tbody>
              </table>
            </div>
  
            <div class="hint">
              说明：策略的 params_schema / code_ref 可以在此扩展展示与版本对比（便于回测可复现）。
            </div>
          </div>
          <div v-else class="empty">选择左侧策略查看详情</div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { onMounted, ref } from "vue";
  
  type Strategy = {
    strategyId: number;
    strategyName: string;
    strategyDescription?: string | null;
    universe: string;
    barType: string;
  };
  
  type StrategyRun = {
    runId: string;
    mode: string;
    status: string;
    startTime?: string | null;
    endTime?: string | null;
  };
  
  const kw = ref("");
  const strategies = ref<Strategy[]>([]);
  const current = ref<Strategy | null>(null);
  const runs = ref<StrategyRun[]>([]);
  
  async function apiGet<T>(url: string): Promise<T> {
    const r = await fetch(url);
    if (!r.ok) throw new Error(await r.text());
    return (await r.json()) as T;
  }
  
  async function loadStrategies() {
    const qs = new URLSearchParams();
    if (kw.value.trim()) qs.set("kw", kw.value.trim());
    strategies.value = await apiGet<Strategy[]>(`/api/strategies?${qs.toString()}`);
  }
  
  async function selectStrategy(s: Strategy) {
    current.value = s;
    runs.value = await apiGet<StrategyRun[]>(`/api/strategies/${s.strategyId}/runs?limit=20`);
  }
  
  function goRun(runId: string) {
    window.location.hash = `#/strategy-runs/${runId}`;
  }
  
  onMounted(loadStrategies);
  </script>
  
  <style scoped>
  .page{padding:16px;max-width:1400px;margin:0 auto;}
  .page-header{display:flex;flex-direction:column;gap:6px;margin-bottom:14px;}
  .sub{color:#666;font-size:13px;}
  .grid{display:grid;grid-template-columns:1fr 1.4fr;gap:14px;align-items:start;}
  .card{background:#fff;border:1px solid #eee;border-radius:12px;padding:12px;box-shadow:0 1px 6px rgba(0,0,0,.04);}
  .toolbar{display:flex;gap:8px;align-items:center;margin:10px 0;flex-wrap:wrap;}
  .toolbar input{border:1px solid #ddd;border-radius:10px;padding:8px 10px;outline:none;min-width:220px;}
  button{border:1px solid #ddd;background:#fafafa;border-radius:10px;padding:8px 10px;cursor:pointer;}
  button.link{border:none;background:transparent;color:#0b66ff;padding:0;}
  .list{list-style:none;padding:0;margin:0;display:flex;flex-direction:column;gap:10px;}
  .list-item{border:1px solid #f2f2f2;border-radius:12px;padding:10px;cursor:pointer;}
  .title{font-weight:800;}
  .meta{display:flex;gap:8px;margin-top:6px;color:#666;font-size:12px;flex-wrap:wrap;align-items:center;}
  .tag{border:1px solid #eee;border-radius:999px;padding:2px 8px;background:#fafafa;font-size:12px;}
  .desc{margin-top:6px;color:#444;font-size:13px;line-height:1.5;}
  .table-wrap{overflow:auto;margin-top:8px;}
  .tbl{width:100%;border-collapse:collapse;font-size:13px;}
  .tbl th,.tbl td{border-bottom:1px solid #f0f0f0;padding:8px 6px;white-space:nowrap;}
  .tbl th{text-align:left;color:#555;font-weight:600;}
  .pill{border-radius:999px;padding:2px 8px;border:1px solid #eee;background:#fafafa;font-size:12px;}
  .empty{padding:14px;text-align:center;color:#888;}
  .kv{display:grid;grid-template-columns:1fr 1fr;gap:8px;margin-top:8px;}
  .kv div{display:flex;justify-content:space-between;border:1px solid #f2f2f2;border-radius:10px;padding:8px 10px;}
  .kv span{color:#666;}
  .mono{font-family:ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;}
  .ellipsis{max-width:280px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;}
  .hint{margin-top:10px;color:#777;font-size:12px;}
  @media (max-width: 1100px){.grid{grid-template-columns:1fr;}}
  </style>
  