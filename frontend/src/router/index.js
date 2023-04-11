import { createRouter, createWebHistory } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import LoginView from "@/views/member/LoginView.vue";
import SignupView from "@/views/member/SignupView.vue";
import WriteView from "@/views/post/WriteView.vue";
import DetailView from "@/views/post/DetailView.vue";
import { CLIENT_MESSAGE } from "@/constants/message";

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

function requireLogin(to, from, next) {
  const accessToken = localStorage.getItem(ACCESS_TOKEN_KEY);
  if (!accessToken) {
    alert(CLIENT_MESSAGE.REQUIRE_LOGIN);
    next("/login");
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
  {
    path: "/post/write",
    name: "write",
    component: WriteView,
    beforeEnter: requireLogin,
  },
  {
    path: "/post/detail/:postNum",
    name: "detail",
    component: DetailView,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
