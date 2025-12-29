// src/main.ts
import { createApp } from 'vue';
import App from './App.vue';
import router from './router';       // 引入我们刚才创建的路由实例
import './styles/base.css';

const app = createApp(App);
app.use(router);                     // 注册路由（类似把 Router 注入应用容器）
app.mount('#app');                   // 挂载到 index.html 的 #app 节点

