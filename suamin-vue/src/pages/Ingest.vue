<template>
    <div class="page">
      <header class="page-header">
        <h1>采集与溯源</h1>
        <div class="sub">data_source / ingest_job / ingest_log / raw_payload：用于补数、监控、回放解析</div>
      </header>
  
      <section class="grid">
        <div class="card">
          <h2>数据源</h2>
          <div class="toolbar">
            <button @click="loadSources">刷新</button>
          </div>
          <ul class="list">
            <li v-for="s in sources" :key="s.sourceId" class="list-item" @click="selectSource(s)">
              <div class="title">{{ s.name }}</div>
              <div class="meta">
                <span class="tag">{{ s.sourceType }}</span>
                <span class="tag" v-if="s.authType">{{ s.authType }}</span>
                <span class="tag" v-if="s.rateLimit">rate {{ s.rateLimit }}</span>
                <span class="mono">#{{ s.sourceId }}</span>
              </div>
              <div class="desc" v-if="s.notes">{{ s.notes }}</div>
            </li>
            <li v-if="sources.length===0" class="empty">暂无</li>
          </ul>
        </div>
  
        <div class="card">
          <h2>任务 / 日志 / 原始载荷</h2>
          <div v-if="currentSource" class="kv">
            <div><span>source</span><b>{{ currentSource.name }}</b></div>
            <div><span>id</span><b class="mono">{{ currentSource.sourceId }}</b></div>
          </div>
          <div v-else class="empty">选择左侧数据源</div>
  
          <h3>任务（ingest_job）</h3>
          <div class="table-wrap">
            <table class="tbl">
              <thead><tr><th>job_type</th><th>cron</th><th>status</th><th>last_run</th><th>last_success</th><th></th></tr></thead>
              <tbody>
                <tr v-for="j in jobs" :key="j.jobId" @click="selectJob(j.jobId)" :class="{ active: j.jobId===jobId }">
                  <td class="mono">{{ j.jobType }}</td>
                  <td class="mono">{{ j.scheduleCron || '-' }}</td>
                  <td class="mono">{{ j.status }}</td>
                  <td class="mono">{{ j.lastRunTime || '-' }}</td>
                  <td class="mono">{{ j.lastSuccessTime || '-' }}</td>
                  <td><button class="link" @click.stop="selectJob(j.jobId)">日志</button></td>
                </tr>
                <tr v-if="jobs.length===0"><td colspan="6" class="empty">暂无</td></tr>
              </tbody>
            </table>
          </div>
  
          <h3>运行日志（ingest_log）</h3>
          <div class="table-wrap">
            <table class="tbl">
              <thead><tr><th>start</th><th>end</th><th>status</th><th class="num">in</th><th class="num">out</th><th>error</th></tr></thead>
              <tbody>
                <tr v-for="l in logs" :key="l.logId">
                  <td class="mono">{{ l.startTime }}</td>
                  <td class="mono">{{ l.endTime || '-' }}</td>
                  <td class="mono">{{ l.status }}</td>
                  <td class="num">{{ l.recordsIn ?? '-' }}</td>
                  <td class="num">{{ l.recordsOut ?? '-' }}</td>
                  <td class="ellipsis" :title="l.errorMsg || ''">{{ l.errorMsg || '-' }}</td>
                </tr>
                <tr v-if="logs.length===0"><td colspan="6" class="empty">暂无</td></tr>
              </tbody>
            </table>
          </div>
  
          <h3>原始载荷（raw_payload）</h3>
          <div class="toolbar">
            <input v-model="entityType" placeholder="entity_type: market_daily/NEWS..." />
            <input v-model="entityKey" placeholder="entity_key: securityId|date ..." />
            <button @click="loadPayloads">查询</button>
          </div>
  
          <div class="table-wrap">
            <table class="tbl">
              <thead><tr><th>fetch_time</th><th>entity_type</th><th>entity_key</th><th class="mono">payload_id</th></tr></thead>
              <tbody>
                <tr v-for="p in payloads" :key="p.payloadId" @click="activePayload = p">
                  <td class="mono">{{ p.fetchTime }}</td>
                  <td class="mono">{{ p.entityType }}</td>
                  <td class="mono">{{ p.entityKey }}</td>
                  <td class="mono ellipsis" :title="p.payloadId">{{ p.payloadId }}</td>
                </tr>
                <tr v-if="payloads.length===0"><td colspan="4" class="empty">暂无</td></tr>
              </tbody>
            </table>
          </div>
  
          <pre class="json" v-if="activePayload?.payload">{{ activePayload.payload }}</pre>
        </div>
      </section>
    </div>
  </template>
  
  <script setup lang="ts">
  import { onMounted, ref } from "vue";
  
  type DataSource = { sourceId: number; name: string; sourceType: string; authType?: string | null; rateLimit?: number | null; notes?: string | null };
  type Job = { jobId: number; jobType: string; scheduleCron?: string | null; status: string; lastRunTime?: string | null; lastSuccessTime?: string | null };
  type Log = { logId: number; startTime: string; endTime?: string | null; status: string; recordsIn?: number | null; recordsOut?: number | null; errorMsg?: string | null };
  type Payload = { payloadId: string; fetchTime: string; entityType: string; entityKey: string; payload?: string | null };
  
  const sources = ref<DataSource[]>([]);
  const currentSource = ref<DataSource | null>(null);
  
  const jobs = ref<Job[]>([]);
  const jobId = ref<number>(0);
  const logs = ref<Log[]>([]);
  
  const entityType = ref("");
  const entityKey = ref("");
  const payloads = ref<Payload[]>([]);
  const activePayload = ref<Payload | null>(null);
  
  async function apiGet<T>(url: string): Promise<T> {
    const r = await fetch(url);
    if (!r.ok) throw new Error(await r.text());
    return (await r.json()) as T;
  }
  
  async function loadSources() {
    sources.value = await apiGet<DataSource[]>(`/api/data-sources`);
  }
  
  async function selectSource(s: DataSource) {
    currentSource.value = s;
    jobs.value = await apiGet<Job[]>(`/api/data-sources/${s.sourceId}/jobs`);
    jobId.value = 0;
    logs.value = [];
    payloads.value = [];
    activePayload.value = null;
  }
  
  async function selectJob(id: number) {
    jobId.value = id;
    logs.value = await apiGet<Log[]>(`/api/ingest-jobs/${id}/logs?limit=50`);
  }
  
  async function loadPayloads() {
    if (!currentSource.value) return;
    const qs = new URLSearchParams();
    if (entityType.value.trim()) qs.set("entityType", entityType.value.trim());
    if (entityKey.value.trim()) qs.set("entityKey", entityKey.value.trim());
    payloads.value = await apiGet<Payload[]>(`/api/data-sources/${currentSource.value.sourceId}/payloads?${qs.toString()}`);
    activePayload.value = payloads.value[0] || null;
  }
  
  onMounted(loadSources);
  </script>
  
  <style scoped>
  .page{padding:16px;max-width:1400px;margin:0 auto;}
  .page-header{display:flex;flex-direction:column;gap:6px;margin-bottom:14px;}
  .sub{color:#666;font-size:13px;}
  .grid{display:grid;grid-template-columns:1fr 1.7fr;gap:14px;align-items:start;}
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
  .num{text-align:right;}
  .kv{display:grid;grid-template-columns:1fr 1fr;gap:8px;margin-top:8px;}
  .kv div{display:flex;justify-content:space-between;border:1px solid #f2f2f2;border-radius:10px;padding:8px 10px;}
  .kv span{color:#666;}
  .json{margin-top:10px;background:#0b1020;color:#cfe2ff;padding:12px;border-radius:12px;overflow:auto;white-space:pre-wrap;}
  .empty{padding:14px;text-align:center;color:#888;}
  .ellipsis{max-width:260px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;}
  .mono{font-family:ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;}
  tr.active{background:#f6f9ff;}
  @media (max-width: 1200px){.grid{grid-template-columns:1fr;}}
  </style>
  