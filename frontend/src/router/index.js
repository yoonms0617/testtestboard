import { createRouter, createWebHistory } from "vue-router";
import memberRoutes from "@/router/member/member-router";
import postRoutes from "@/router/post/post-router";

const routes = [
  {
    path: "/",
    component: () => import("@/views/HomeView.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes: [...routes, ...memberRoutes, ...postRoutes],
});

export default router;
