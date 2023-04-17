import { defineStore } from "pinia";
import { computed, ref } from "vue";

const ACCESS_TOKEN_KEY = process.env.VUE_APP_ACCESS_TOKEN_KEY;
const REFRESH_TOKEN_KEY = process.env.VUE_APP_REFRESH_TOKEN_KEY;

export const useTokenStore = defineStore("token", () => {
  const accessToken = ref(localStorage.getItem(ACCESS_TOKEN_KEY));
  const refreshToken = ref(localStorage.getItem(REFRESH_TOKEN_KEY));

  const getAccessToken = computed(() => accessToken.value);
  const getRefreshToken = computed(() => refreshToken.value);
  const hasAccessToken = computed(() => accessToken.value != null);

  function setToken(token) {
    accessToken.value = token.accessToken;
    refreshToken.value = token.refreshToken;
    localStorage.setItem(ACCESS_TOKEN_KEY, token.accessToken);
    localStorage.setItem(REFRESH_TOKEN_KEY, token.refreshToken);
  }

  function clearToken() {
    accessToken.value = null;
    refreshToken.value = null;
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
  }

  return { getAccessToken, getRefreshToken, hasAccessToken, setToken, clearToken };
});
