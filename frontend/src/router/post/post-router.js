import { CLIENT_MESSAGE } from "@/constants/message";

const ACCESS_TOKEN_KEY = process.env.VUE_APP_ACCESS_TOKEN_KEY;

function requiresAuth(to, from, next) {
  const accessToken = localStorage.getItem(ACCESS_TOKEN_KEY);
  if (!accessToken) {
    const flag = confirm(CLIENT_MESSAGE.REQUIRE_LOGIN);
    if (flag) {
      next("/login");
    } else {
      next(from.path);
    }
  } else {
    next();
  }
}

const postRoutes = [
  {
    path: "/post/write",
    component: () => import("@/views/post/WriteView.vue"),
    beforeEnter: requiresAuth,
  },
  {
    path: "/post/detail/:postNum",
    component: () => import("@/views/post/DetailView.vue"),
    beforeEnter: requiresAuth,
  },
  {
    path: "/post/update/:postNum",
    component: () => import("@/views/post/UpdateView.vue"),
    beforeEnter: requiresAuth,
  },
];

export default postRoutes;
