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
          <input v-model="username" ref="username" type="text" class="form-control" placeholder="아이디" autocomplete="off" required />
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

<script>
import { LOGIN } from "@/api/member";
import { SERVER_MESSAGE } from "@/constants/message";
import { mapActions } from "vuex";

export default {
  name: "LoginView",
  data() {
    return {
      username: "",
      password: "",
      message: "",
    };
  },
  methods: {
    ...mapActions(["saveToken"]),
    login() {
      const formData = new FormData();
      formData.append("username", this.username);
      formData.append("password", this.password);
      LOGIN.loginRequest(formData)
        .then((res) => {
          const token = {
            accessToken: res.data.accessToken,
            refreshToken: res.data.refreshToken,
          };
          this.saveToken(token);
          this.$router.replace("/");
        })
        .catch((err) => {
          this.username = "";
          this.password = "";
          this.$refs.username.focus();
          const errCode = err.response.data.code;
          this.message = SERVER_MESSAGE[errCode];
        });
    },
  },
};
</script>

<style scoped>
.login-form {
  max-width: 400px;
  margin: auto;
}
</style>
