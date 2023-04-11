<template>
  <BaseNavigation />
  <div class="container">
    <div class="detail-form">
      <div class="mt-5">
        <div class="mt-2">
          <div>
            <span>{{ post.writer }}</span>
          </div>
          <div class="d-flex justify-content-between align-items-center">
            <div>
              <small class="text-muted">{{ post.createdAt }}</small>
            </div>
            <div v-if="isLogin">
              <button class="btn btn-sm btn-secondary me-2">수정</button>
              <button class="btn btn-sm btn-danger">삭제</button>
            </div>
          </div>
          <div class="mb-5 mt-2">
            <h3>{{ post.title }}</h3>
          </div>
        </div>
        <div class="content">
          <div id="viewer" class="pb-3"></div>
        </div>
        <hr />
      </div>
    </div>
  </div>
</template>

<script>
import Viewer from "@toast-ui/editor/dist/toastui-editor-viewer";
import "@toast-ui/editor/dist/toastui-editor-viewer.css";
import BaseNavigation from "@/components/BaseNavigation.vue";
import { POST } from "@/api/post";
import { mapGetters } from "vuex";

export default {
  name: "DetailView",
  components: {
    BaseNavigation,
  },
  data() {
    return {
      viewer: "",
      post: {
        title: "",
        writer: "",
        createdAt: "",
      },
    };
  },
  computed: {
    ...mapGetters(["isLogin"]),
  },
  mounted() {
    this.postDetail();
    this.viewer = new Viewer({
      el: document.querySelector("#viewer"),
      height: "600px",
      initialValue: "<h1>Hello world!!</h1>",
    });
  },
  methods: {
    postDetail() {
      const postNum = this.$route.params.postNum;
      POST.detailRequest(postNum).then((res) => {
        const post = res.data;
        this.post.title = post.title;
        this.post.writer = post.writer;
        this.post.createdAt = post.createdAt;
        this.viewer.setMarkdown(post.content);
      });
    },
  },
};
</script>

<style>
.detail-form {
  max-width: 1000px;
  margin: auto;
}

.toastui-editor-contents h1 {
  margin: 0 0 0 0;
}

.toastui-editor-contents p {
  font-size: 1rem;
}
</style>
