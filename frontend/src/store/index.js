import { createStore } from "vuex";

const ACCESS_TOKEN_KEY = process.env.VUE_APP_ACCESS_TOKEN_KEY;
const REFRESH_TOKEN_KEY = process.env.VUE_APP_REFRESH_TOKEN_KEY;

export default createStore({
  state: {
    accessToken: localStorage.getItem(ACCESS_TOKEN_KEY),
    refreshToken: localStorage.getItem(REFRESH_TOKEN_KEY),
  },
  getters: {
    isLogin(state) {
      return state.accessToken != null;
    },
    accessToken(state) {
      return state.accessToken;
    },
    refreshToken(state) {
      return state.refreshToken;
    },
  },
  mutations: {
    setAccessToken(state, accessToken) {
      state.accessToken = accessToken;
      localStorage.setItem(ACCESS_TOKEN_KEY, accessToken);
    },
    setRefreshToken(state, refreshToken) {
      state.refreshToken = refreshToken;
      localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken);
    },
    clearToken(state) {
      state.accessToken = null;
      state.refreshToken = null;
      localStorage.removeItem(ACCESS_TOKEN_KEY);
      localStorage.removeItem(REFRESH_TOKEN_KEY);
    },
  },
  actions: {
    saveToken({ commit }, token) {
      commit("setAccessToken", token.accessToken);
      commit("setRefreshToken", token.refreshToken);
    },
    clearToken({ commit }) {
      commit("clearToken");
    },
  },
});
