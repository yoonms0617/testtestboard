import Axios from "axios";

const BASE_URL = process.env.VUE_APP_BASE_URL;

const axios = Axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
});

const SIGNUP = {
  signupRequest(data) {
    return axios.post("/api/member/signup", data, {
      headers: {
        "Content-Type": "application/json",
      },
    });
  },
  existsNicknameRequest(nickname) {
    return axios.get("/api/member/nickname/exists", {
      params: {
        nickname: nickname,
      },
    });
  },
  existsUsernameRequest(username) {
    return axios.get("/api/member/username/exists", {
      params: {
        username: username,
      },
    });
  },
};

const LOGIN = {
  loginRequest(data) {
    return axios.post("/api/auth/login", data);
  },
};

export { SIGNUP, LOGIN };
