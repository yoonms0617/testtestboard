<template>
  <div class="container">
    <div class="text-center mb-5 mt-5">
      <router-link to="/" class="text-decoration-none text-dark">
        <h1 class="display-4 mb-4">게시판</h1>
      </router-link>
      <h3>회원가입</h3>
    </div>
    <div class="signup-form">
      <form @submit.prevent="submitForm">
        <div class="mb-3">
          <div class="form-floating">
            <input @input="inputNickname" @blur="checkDuplicateNickname()" type="text" class="form-control" placeholder="닉네임" autocomplete="off" required />
            <label>닉네임</label>
          </div>
          <small class="message" :class="validate.nickname ? 'text-success' : 'text-danger'">
            {{ message.nickname }}
          </small>
        </div>
        <div class="mb-3">
          <div class="form-floating">
            <input v-model="input.username" @blur="checkDuplicateUsername()" type="text" class="form-control" placeholder="아이디" autocomplete="off" required />
            <label>아이디</label>
          </div>
          <small class="message" :class="validate.username ? 'text-success' : 'text-danger'">
            {{ message.username }}
          </small>
        </div>
        <div class="mb-3">
          <div class="form-floating">
            <input v-model="input.password" type="password" class="form-control" placeholder="비밀번호" autocomplete="off" required />
            <label>비밀번호</label>
          </div>
          <small class="message" :class="validate.password ? 'text-success' : 'text-danger'">
            {{ message.password }}
          </small>
        </div>
        <div class="mb-3">
          <div class="form-floating">
            <input v-model="input.confirm" type="password" class="form-control" placeholder="비밀번호 확인" autocomplete="off" required />
            <label>비밀번호 확인</label>
          </div>
          <small class="message" :class="validate.confirm ? 'text-success' : 'text-danger'">
            {{ message.confirm }}
          </small>
        </div>
        <div class="d-grid gap-2">
          <button type="submit" class="btn btn-lg btn-primary" :class="validateAll() ? '' : 'disabled'">가입하기</button>
        </div>
      </form>
      <hr />
      <div class="text-center">
        <p>
          이미 회원이신가요?
          <router-link to="/login" class="text-decoration-none"> 로그인하기 </router-link>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, watch } from "vue";
import { useRouter } from "vue-router";

import { SIGNUP_VALIDATION } from "@/util/signup-validatation.js";
import { SIGNUP } from "@/api/member";

const router = useRouter();

const input = reactive({
  nickname: "",
  username: "",
  password: "",
  confirm: "",
});

const message = reactive({
  nickname: "",
  username: "",
  password: "",
  confirm: "",
});

const validate = reactive({
  nickname: false,
  username: false,
  password: false,
  confirm: false,
});

watch(
  () => input.nickname,
  () => {
    validate.nickname = false;
    SIGNUP_VALIDATION.nickname(input, message);
  }
);

watch(
  () => input.username,
  () => {
    validate.username = false;
    SIGNUP_VALIDATION.username(input, message);
  }
);

watch(
  () => input.password,
  () => {
    SIGNUP_VALIDATION.password(input, message, validate);
    SIGNUP_VALIDATION.checkMatchPassword(input, message, validate);
  }
);

watch(
  () => input.confirm,
  () => {
    SIGNUP_VALIDATION.confirm(input, message, validate);
    SIGNUP_VALIDATION.checkMatchPassword(input, message, validate);
  }
);

function submitForm() {
  if (validateAll()) {
    const inputData = {
      nickname: input.nickname,
      username: input.username,
      password: input.password,
    };
    SIGNUP.signupRequest(inputData).then(() => {
      router.replace("/");
    });
  }
}

function inputNickname(event) {
  input.nickname = event.target.value;
}

function checkDuplicateNickname() {
  SIGNUP_VALIDATION.checkDuplicateNickname(input.nickname, message, validate);
}

function checkDuplicateUsername() {
  SIGNUP_VALIDATION.checkDuplicateUsername(input.username, message, validate);
}

function validateAll() {
  return Object.values(validate).every((value) => value === true);
}
</script>

<style scoped>
.signup-form {
  max-width: 400px;
  margin: auto;
}

.message {
  font-size: 13px;
}
</style>
