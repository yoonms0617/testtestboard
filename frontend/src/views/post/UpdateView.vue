<template>
  <BaseNavigation />
  <div class="container">
    <div class="write-form">
      <form role="form" class="mt-5">
        <div class="form-floating mb-3">
          <input v-model="post.title" type="text" class="form-control" placeholder="제목" />
          <label>제목</label>
        </div>
        <div id="editor"></div>
        <div class="mt-3 text-end">
          <button type="button" @click="postUpdate()" class="btn btn-lg btn-primary me-3">수정</button>
          <router-link to="/" class="btn btn-lg btn-danger">취소</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import BaseNavigation from "@/components/BaseNavigation.vue";

import Editor from "@toast-ui/editor";
import "@toast-ui/editor/dist/toastui-editor.css";
import "@toast-ui/editor/dist/i18n/ko-kr";

import { useRoute, useRouter } from "vue-router";
import { ref, onMounted } from "vue";

import { CLIENT_MESSAGE } from "@/constants/message";
import { POST } from "@/api/post";

let route = useRoute();
let router = useRouter();

let editor = null;
let post = ref({});

onMounted(() => {
  setUpEditor();
  postDetail();
});

function setUpEditor() {
  editor = new Editor({
    el: document.querySelector("#editor"),
    height: "400px",
    previewStyle: "vertical",
    initialEditType: "wysiwyg",
    hideModeSwitch: true,
    autofocus: false,
    language: "ko-KR",
  });
  editor.removeToolbarItem("image");
  editor.removeToolbarItem("indent");
  editor.removeToolbarItem("outdent");
  editor.removeToolbarItem("task");
}

function postDetail() {
  const postNum = getPostnumFromQuery();
  POST.detailRequest(postNum).then((res) => {
    post.value = res.data;
    console.log(res.data);
    editor.setHTML(post.value.content);
  });
}

function postUpdate() {
  if (post.value.title.trim() === "") {
    alert(CLIENT_MESSAGE.INPUT_POST_TITLE);
    return;
  }
  if (editor.getHTML() === "<p><br></p>") {
    alert(CLIENT_MESSAGE.INPUT_POST_CONTNET);
    return;
  }
  const data = {
    title: post.value.title.trim().replaceAll(/\s{2,}/g, " "),
    content: editor.getHTML(),
  };
  const postNum = getPostnumFromQuery();
  POST.updateRequest(data, postNum).then(() => {
    router.replace("/post/detail/" + postNum);
  });
}

function getPostnumFromQuery() {
  return route.params.postNum;
}
</script>
