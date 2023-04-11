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
          <button type="button" @click="write" class="btn btn-lg btn-primary me-3">작성</button>
          <router-link to="/" class="btn btn-lg btn-danger">취소</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import Editor from "@toast-ui/editor";
import "@toast-ui/editor/dist/toastui-editor.css";
import "@toast-ui/editor/dist/i18n/ko-kr";
import BaseNavigation from "@/components/BaseNavigation.vue";
import { CLIENT_MESSAGE } from "@/constants/message.js";
import { POST } from "@/api/post";

export default {
  name: "WriteView",
  components: {
    BaseNavigation,
  },
  data() {
    return {
      editor: "",
      post: {
        title: "",
        content: "",
      },
    };
  },
  mounted() {
    this.editor = new Editor({
      el: document.querySelector("#editor"),
      height: "400px",
      previewStyle: "vertical",
      initialEditType: "wysiwyg",
      hideModeSwitch: true,
      autofocus: false,
      language: "ko-KR",
    });
    this.editor.removeToolbarItem("image");
    this.editor.removeToolbarItem("indent");
    this.editor.removeToolbarItem("outdent");
    this.editor.removeToolbarItem("task");
  },
  methods: {
    write() {
      const title = this.post.title;
      const content = this.editor.getHTML();
      if (title === "") {
        alert(CLIENT_MESSAGE.INPUT_POST_TITLE);
        return;
      }
      if (content === "<p><br></p>") {
        alert(CLIENT_MESSAGE.INPUT_POST_CONTNET);
        return;
      }
      const data = {
        title: this.post.title,
        content: content,
      };
      POST.writeRequest(data).then(() => {
        this.$router.push("/");
      });
    },
  },
};
</script>

<style>
.write-form {
  max-width: 900px;
  margin: auto;
}

.toastui-editor-contents hr {
  border-top: 1px solid black !important;
}
</style>
