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
            <input @input="inputNickname" @blur="checkNicknameDuplicate" :value="form.nickname" type="text" class="form-control" placeholder="닉네임" autocomplete="off" required />
            <label>닉네임</label>
          </div>
          <small :class="valid.check.nickname ? 'text-success' : 'text-danger'">
            {{ valid.message.nickname }}
          </small>
        </div>
        <div class="mb-3">
          <div class="form-floating">
            <input v-model="form.username" @blur="checkUsernameDuplicate" type="text" class="form-control" placeholder="아이디" autocomplete="off" required />
            <label>아이디</label>
          </div>
          <small :class="valid.check.username ? 'text-success' : 'text-danger'">
            {{ valid.message.username }}
          </small>
        </div>
        <div class="mb-3">
          <div class="form-floating">
            <input v-model="form.password.password" type="password" class="form-control" placeholder="비밀번호" autocomplete="off" required />
            <label>비밀번호</label>
          </div>
          <small :class="valid.check.password.password ? 'text-success' : 'text-danger'">
            {{ valid.message.password.password }}
          </small>
        </div>
        <div class="mb-3">
          <div class="form-floating">
            <input v-model="form.password.confirm" type="password" class="form-control" placeholder="비밀번호 확인" autocomplete="off" required />
            <label>비밀번호 확인</label>
          </div>
          <small :class="valid.check.password.confirm ? 'text-success' : 'text-danger'">
            {{ valid.message.password.confirm }}
          </small>
        </div>
        <div class="d-grid gap-2">
          <button type="submit" class="btn btn-lg btn-primary" :class="!validateAll() ? 'disabled' : ''">가입하기</button>
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

<script>
import { SIGNUP } from "@/api/member";
import { REGEXP } from "@/constants/regexp";
import { CLIENT_MESSAGE, SERVER_MESSAGE } from "@/constants/message";

export default {
  name: "SignupView",
  data() {
    return {
      form: {
        nickname: "",
        username: "",
        password: {
          password: "",
          confirm: "",
        },
      },
      valid: {
        message: {
          nickname: "",
          username: "",
          password: {
            password: "",
            confirm: "",
          },
        },
        check: {
          nickname: false,
          username: false,
          password: {
            password: false,
            confirm: false,
          },
        },
      },
    };
  },
  watch: {
    "form.nickname"(nickname) {
      this.valid.check.nickname = false;
      this.validateNickname(nickname);
    },
    "form.username"(username) {
      this.valid.check.username = false;
      this.validateUsername(username);
    },
    "form.password.password"(password) {
      this.validatePassword(password);
      this.checkPasswordMatch(password, this.form.password.confirm);
    },
    "form.password.confirm"(confirm) {
      this.validatePasswordConfirm(confirm);
      this.checkPasswordMatch(this.form.password.password, confirm);
    },
  },
  methods: {
    submitForm() {
      if (this.validateAll()) {
        const data = {
          nickname: this.form.nickname,
          username: this.form.username,
          password: this.form.password.password,
        };
        SIGNUP.signupRequest(data).then(() => {
          this.$router.replace("/");
        });
      }
    },
    inputNickname(event) {
      this.form.nickname = event.target.value;
    },
    validateNickname(nickname) {
      const regExp = REGEXP.NICKNAME;
      if (regExp.test(nickname)) {
        this.valid.message.nickname = "";
      } else {
        this.valid.message.nickname = CLIENT_MESSAGE.REGEXP_NICKNAME;
      }
    },
    validateUsername(username) {
      const regExp = REGEXP.USERNAME;
      if (regExp.test(username)) {
        this.valid.message.username = "";
      } else {
        this.valid.message.username = CLIENT_MESSAGE.REGEXP_USERNAME;
      }
    },
    validatePassword(password) {
      const regExp = REGEXP.PASSWORD;
      if (regExp.test(password)) {
        this.valid.check.password.password = true;
        this.valid.message.password.password = CLIENT_MESSAGE.GOOD_PASSWORD;
      } else {
        this.valid.check.password.password = false;
        this.valid.message.password.password = CLIENT_MESSAGE.REGEXP_PASSWORD;
      }
    },
    validatePasswordConfirm(confirm) {
      const inputPassword = this.form.password.password;
      if (inputPassword === confirm) {
        this.valid.check.password.confirm = true;
        this.valid.message.password.confirm = CLIENT_MESSAGE.GOOD_CONFIRM;
      } else {
        this.valid.check.password.confirm = false;
        this.valid.message.password.confirm = CLIENT_MESSAGE.NOT_MATCH_PASSWORD;
      }
    },
    checkPasswordMatch(password, confirm) {
      if (confirm !== "") {
        if (password !== confirm) {
          this.valid.check.password.confirm = false;
          this.valid.message.password.confirm = CLIENT_MESSAGE.NOT_MATCH_PASSWORD;
        } else {
          this.valid.check.password.confirm = true;
          this.valid.message.password.confirm = CLIENT_MESSAGE.GOOD_CONFIRM;
        }
      }
    },
    checkNicknameDuplicate() {
      const nickname = this.form.nickname;
      const regExp = REGEXP.NICKNAME;
      if (!regExp.test(nickname)) {
        return;
      }
      let check = this.valid.check.nickname;
      if (!check) {
        SIGNUP.existsNicknameRequest(nickname)
          .then(() => {
            this.valid.check.nickname = true;
            this.valid.message.nickname = CLIENT_MESSAGE.GOOD_NICKNAME;
          })
          .catch((err) => {
            const code = err.response.data.code;
            const message = SERVER_MESSAGE[code];
            this.valid.check.nickname = false;
            this.valid.message.nickname = message;
          });
      }
    },
    checkUsernameDuplicate() {
      const username = this.form.username;
      const regExp = REGEXP.USERNAME;
      if (!regExp.test(username)) {
        return;
      }
      let check = this.valid.check.username;
      if (!check) {
        SIGNUP.existsUsernameRequest(username)
          .then(() => {
            this.valid.check.username = true;
            this.valid.message.username = CLIENT_MESSAGE.GOOD_USERNAME;
          })
          .catch((err) => {
            const code = err.response.data.code;
            const message = SERVER_MESSAGE[code];
            this.valid.check.username = false;
            this.valid.message.username = message;
          });
      }
    },
    validateAll() {
      return this.valid.check.nickname && this.valid.check.username && this.valid.check.password.password && this.valid.check.password.confirm;
    },
  },
};
</script>

<style scoped>
.signup-form {
  max-width: 400px;
  margin: auto;
}
</style>
