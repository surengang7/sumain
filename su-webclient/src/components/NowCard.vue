<template>
    <div style="padding:12px; border:1px solid #e5e7eb; border-radius:8px;">
      <!-- 标题 -->
      <strong>{{ title }}</strong>
  
      <!-- 当前时间文本 -->
      <div style="margin-top:6px;">
        <span>{{ formattedNow }}</span>
        <!-- 手动刷新按钮 -->
        <button @click="refreshNow" style="margin-left:8px;">刷新时间</button>
      </div>
  
      <!-- 小提示 -->
      <p style="margin-top:8px; color:#64748b;">
        （演示 props + emits：外部可传入标题/格式/是否自动计时；每次刷新会 emit “refreshed” 事件）
      </p>
    </div>
  </template>
  
  <script setup lang="ts">
  /**
   *【知识点】
   * 1) defineProps：声明“输入参数”（从父组件传进来的配置/数据）
   * 2) defineEmits：声明“输出事件”（往父组件发通知/回调）
   * 3) ref/computed：响应式变量与基于它的派生值
   * 4) onMounted/onUnmounted：生命周期（挂载/卸载）
   */
  
  import { ref, computed, onMounted, onUnmounted } from 'vue';
  
  /** 1) 声明 props（输入） */
  const props = defineProps<{
    title?: string;       // 卡片标题（可选），默认“当前时间”
    format?: string;      // 时间格式（这里演示用途，不严格解析，实际项目建议用 dayjs）
    autoTick?: boolean;   // 是否每秒自动刷新当前时间（默认 false）
  }>();
  
  /** 2) 声明 emits（输出事件） */
  const emit = defineEmits<{
    // 当组件内部刷新时间时，告诉父组件（可以传一些信息上去，这里仅演示不传参）
    (e: 'refreshed'): void;
  }>();
  
  /** 3) 内部状态：当前时间对象 */
  const now = ref(new Date());
  
  /** 4) 计算属性：格式化显示（演示 format；不依赖第三方库） */
  const formattedNow = computed(() => {
    // 简化处理：如果传了 format=“date” 就显示日期；format=“time” 就显示时间；否则本地化完整字符串
    if (props.format === 'date') return now.value.toLocaleDateString();
    if (props.format === 'time') return now.value.toLocaleTimeString();
    return now.value.toLocaleString();
  });
  
  /** 刷新时间，并向父组件发事件 */
  function refreshNow() {
    now.value = new Date();
    emit('refreshed');
  }
  
  /** 5) 自动计时（如果 autoTick=true，则每秒刷新一次） */
  let timer: number | undefined;
  
  onMounted(() => {
    if (props.autoTick) {
      timer = window.setInterval(() => {
        now.value = new Date();
        // 自动刷新不 emit 事件，避免父组件过于频繁收到通知（需要的话也可以 emit）
      }, 1000);
    }
  });
  
  onUnmounted(() => {
    if (timer) window.clearInterval(timer);
  });
  
  /** 补充：提供默认标题（模板里直接用 title，不传则显示这个默认值） */
  const title = props.title ?? '当前时间';
  </script>
  