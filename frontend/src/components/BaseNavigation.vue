<template>
  <div class="container">
    <header class="d-flex justify-content-between py-4 border-bottom">
      <router-link to="/" class="d-flex align-items-center text-decoration-none">
        <span class="fs-4 text-black">
          <i class="bi bi-pencil-square"></i>
          &nbsp;게시판
        </span>
      </router-link>
      <ul class="nav nav-pills d-flex align-items-center" v-if="authStore.isLogin">
        <li class="nav-item">
          <span>{{ authStore.getNickname }}</span>
        </li>
        <li class="nav-item">
          <button type="button" class="btn btn-link text-decoration-none" @click="logout">로그아웃</button>
        </li>
      </ul>
      <ul class="nav nav-pills" v-else>
        <li class="nav-item">
          <router-link to="/login" class="nav-link">로그인</router-link>
        </li>
        <li class="nav-item">
          <router-link to="/signup" class="nav-link">회원가입</router-link>
        </li>
      </ul>
    </header>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { useAuthStore } from "@/store/authStore";

const router = useRouter();
const authStore = useAuthStore();

function logout() {
  authStore.logout();
  router.push("/");
}
</script>
