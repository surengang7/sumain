<template>
    <div style="padding:12px; border:1px solid #e5e7eb; border-radius:8px;">
      <strong>计数器</strong>
  
      <div style="margin-top:8px;">
        <button @click="decrease">-1</button>
        <span style="display:inline-block; width:60px; text-align:center;">{{ internal }}</span>
        <button @click="increase">+1</button>
      </div>
  
      <p style="margin-top:8px; color:#64748b;">
  （演示 v-model：父组件用 &lt;CounterCard v-model:count="someRef" /&gt; 与我同步值）
</p>

    </div>
  </template>
  
  <script setup lang="ts">
  /**
   *【知识点】
   * 1) v-model 在子组件里的实现规则：
   *    - prop 名：count
   *    - 事件名：update:count
   * 2) 父组件使用：<CounterCard v-model:count="xxx" />
   * 3) 达到上限时，发出业务事件 reachMax
   */
  
  import { ref, watch } from 'vue';
  
  /** 声明 props：父组件传入的状态（受控）与可选上限 */
  const props = defineProps<{
    count: number;   // 与 v-model:count 对应的 prop
    max?: number;    // 可选上限，默认 5
  }>();
  
  /** 声明 emits：v-model 更新事件 + 业务事件 */
  const emit = defineEmits<{
    (e: 'update:count', v: number): void;            // v-model 对应的更新事件
    (e: 'reachMax', payload: { max: number }): void; // 业务事件：达到上限
  }>();
  
  /** 内部可编辑副本：保持受控组件的良好实践（避免直接改 props） */
  const internal = ref(props.count);
  const limit = props.max ?? 5;
  
  /** 监听外部传入的 count 变化，保持同步（父改我也要跟） */
  watch(() => props.count, (v) => {
    internal.value = v;
  });
  
  /** 业务操作 */
  function increase() {
    if (internal.value >= limit) {
      // 达到上限 → 通知父组件
      emit('reachMax', { max: limit });
      return;
    }
    internal.value++;
    emit('update:count', internal.value); // 通知父组件更新 v-model 绑定的值
  }
  
  function decrease() {
    internal.value--;
    emit('update:count', internal.value);
  }
  </script>
  