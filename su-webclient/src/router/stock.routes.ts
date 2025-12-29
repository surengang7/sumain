// src/router/stock.routes.ts
export const stockRoutes = [
    {
      path: "/stock",
      component: () => import("../pages/StockLayout.vue"),
      children: [
        { path: "", redirect: "/stock/dashboard" },
  
        { path: "dashboard", component: () => import("../pages/Dashboard.vue") },
        { path: "securities", component: () => import("../pages/Securities.vue") },
        { path: "security/:id", component: () => import("../pages/SecurityDetail.vue") },
  
        { path: "news", component: () => import("../pages/News.vue") },
        { path: "news/:id", component: () => import("../pages/NewsDetail.vue") },
  
        { path: "strategies", component: () => import("../pages/Strategies.vue") },
        { path: "strategy-runs/:id", component: () => import("../pages/StrategyRunDetail.vue") },
  
        { path: "trading", component: () => import("../pages/Trading.vue") },
        { path: "ingest", component: () => import("../pages/Ingest.vue") },
      ],
    },
  ];
  