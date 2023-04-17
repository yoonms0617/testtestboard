import Axios from "axios";

const ACCESS_TOKEN_KEY = process.env.VUE_APP_ACCESS_TOKEN_KEY;

const accessToken = localStorage.getItem(ACCESS_TOKEN_KEY);

const axios = Axios.create({
  baseURL: process.env.VUE_APP_BASE_URL,
  withCredentials: true,
});

const POST = {
  async listRequest(curPage) {
    return await axios.get("/api/post/list", {
      params: {
        page: curPage,
      },
    });
  },
  async detailRequest(postNum) {
    return await axios.get("/api/post/detail/" + postNum);
  },
  writeRequest(data) {
    return axios.post("/api/post/write", data, {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + accessToken,
      },
    });
  },
};

export { POST };
