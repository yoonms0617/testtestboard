const ACCESS_TOKEN_KEY = process.env.VUE_APP_ACCESS_TOKEN_KEY;

function checkLogin(to, from, next) {
  const accessToken = localStorage.getItem(ACCESS_TOKEN_KEY);
  if (accessToken) {
    next("/");
  } else {
    next();
  }
}

const memberRoutes = [
  {
    path: "/login",
    component: () => import("@/views/member/LoginView.vue"),
    beforeEnter: checkLogin,
  },
  {
    path: "/signup",
    component: () => import("@/views/member/SignupView.vue"),
    beforeEnter: checkLogin,
  },
];

export default memberRoutes;
