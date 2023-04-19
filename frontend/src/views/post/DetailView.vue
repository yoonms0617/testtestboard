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
            <div v-if="post.writer === authStore.getNickname">
              <router-link :to="`/post/update/${post.postNum}`" class="btn btn-sm btn-secondary me-2">수정</router-link>
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

<script setup>
import BaseNavigation from "@/components/BaseNavigation.vue";

import Viewer from "@toast-ui/editor/dist/toastui-editor-viewer";
import "@toast-ui/editor/dist/toastui-editor-viewer.css";

import { useRoute } from "vue-router";
import { useAuthStore } from "@/store/authStore";
import { ref, onMounted } from "vue";

import { POST } from "@/api/post";

const route = useRoute();
const authStore = useAuthStore();

let viewer = null;
const post = ref({});

onMounted(() => {
  setUpViewer();
  postDetail();
});

function setUpViewer() {
  viewer = new Viewer({
    el: document.querySelector("#viewer"),
  });
}

function postDetail() {
  const postNum = route.params.postNum;
  POST.detailRequest(postNum).then((res) => {
    post.value = res.data;
    viewer.setMarkdown(post.value.content);
  });
}
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
