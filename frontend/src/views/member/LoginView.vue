<template>
  <div class="container">
    <div class="text-center mb-5 mt-5">
      <router-link to="/" class="text-decoration-none text-dark">
        <h1 class="display-4 mb-4">게시판</h1>
      </router-link>
      <h3>로그인</h3>
    </div>
    <div class="login-form">
      <form @submit.prevent="login">
        <div class="form-floating mb-3">
          <input v-model="username" type="text" class="form-control" placeholder="아이디" autocomplete="off" required />
          <label>아이디</label>
        </div>
        <div class="form-floating mb-3">
          <input v-model="password" type="password" class="form-control" placeholder="비밀번호" autocomplete="off" required />
          <label>비밀번호</label>
        </div>
        <div class="d-grid gap-2">
          <button type="submit" class="btn btn-lg btn-primary">로그인</button>
        </div>
      </form>
      <div class="mb-3 mt-3">
        <p class="text-danger">
          {{ message }}
        </p>
      </div>
      <hr />
      <div class="text-center">
        <p>
          아직 회원이 아니신가요?
          <router-link to="/signup" class="ms-1 text-decoration-none"> 회원가입하기 </router-link>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/store/authStore";
import { LOGIN } from "@/api/member";
import { SERVER_MESSAGE } from "@/constants/message";

const router = useRouter();
const authStore = useAuthStore();

const username = ref("");
const password = ref("");
const message = ref("");

function login() {
  const formData = new FormData();
  formData.append("username", username.value);
  formData.append("password", password.value);
  LOGIN.loginRequest(formData)
    .then((res) => {
      authStore.login(res.data);
      router.push("/");
    })
    .catch((err) => {
      username.value = "";
      password.value = "";
      const errCode = err.response.data.code;
      message.value = SERVER_MESSAGE[errCode];
    });
}
</script>

<style scoped>
.login-form {
  max-width: 400px;
  margin: auto;
}
</style>
