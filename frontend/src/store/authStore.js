import { defineStore } from "pinia";
import { computed, ref } from "vue";
import { useTokenStore } from "./tokenStore";

const NICKNAME_KEY = "nickname";

export const useAuthStore = defineStore("auth", () => {
  const tokenStore = useTokenStore();

  const nickname = ref(localStorage.getItem(NICKNAME_KEY));

  const getNickname = computed(() => nickname.value);
  const isLogin = computed(() => tokenStore.hasAccessToken);

  function login(user) {
    nickname.value = user.nickname;
    localStorage.setItem(NICKNAME_KEY, user.nickname);
    tokenStore.setToken(user.token);
  }

  function logout() {
    nickname.value = null;
    localStorage.removeItem(NICKNAME_KEY);
    tokenStore.clearToken();
  }

  return { getNickname, isLogin, login, logout };
});
