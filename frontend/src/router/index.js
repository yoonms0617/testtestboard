import { createRouter, createWebHistory } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import LoginView from "@/views/member/LoginView.vue";
import SignupView from "@/views/member/SignupView.vue";

const ACCESS_TOKEN_KEY = process.env.VUE_APP_ACCESS_TOKEN_KEY;

function isLogin(to, from, next) {
  const accessToken = localStorage.getItem(ACCESS_TOKEN_KEY);
  if (accessToken) {
    next({
      name: "home",
    });
  } else {
    next();
  }
}

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/login",
    name: "login",
    component: LoginView,
    beforeEnter: isLogin,
  },
  {
    path: "/signup",
    name: "signup",
    component: SignupView,
    beforeEnter: isLogin,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
