<template>
  <BaseNavigation />
  <div class="container">
    <div class="list-form mt-5">
      <div class="text-center mb-5">
        <h1>자유 게시판</h1>
      </div>
      <div class="d-flex justify-content-end mb-3" v-if="authStore.isLogin">
        <router-link to="/post/write" class="btn btn-primary">
          <i class="bi bi-pencil"></i>
          &nbsp;글 작성
        </router-link>
      </div>
      <table class="table">
        <thead class="table-primary">
          <tr class="text-center">
            <th class="num">번호</th>
            <th class="title">제목</th>
            <th class="writer">작성자</th>
            <th class="created">작성일</th>
          </tr>
        </thead>
        <tbody class="text-center table-group-divider">
          <tr v-for="post in items.posts" :key="post.postNum">
            <td>{{ post.postNum }}</td>
            <td class="text-start">
              <router-link :to="`/post/detail/${post.postNum}`" class="text-decoration-none text-dark">
                {{ post.title }}
              </router-link>
            </td>
            <td>{{ post.writer }}</td>
            <td>{{ post.createdAt }}</td>
          </tr>
        </tbody>
      </table>
      <div>
        <nav aria-label="Page navigation example">
          <ul class="pagination justify-content-center">
            <li class="page-item disabled">
              <a class="page-link">&laquo;</a>
            </li>
            <li class="page-item">
              <a class="page-link" href="#">1</a>
            </li>
            <li class="page-item">
              <a class="page-link" href="#">&raquo;</a>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </div>
</template>

<script setup>
import BaseNavigation from "@/components/BaseNavigation.vue";
import { useAuthStore } from "@/store/authStore";
import { onMounted, ref } from "vue";
import { POST } from "@/api/post";

const authStore = useAuthStore();

const items = ref({});

onMounted(() => {
  POST.listRequest().then((res) => {
    items.value = res.data;
  });
});
</script>

<style scoped>
.list-form {
  max-width: 1200px;
  margin: auto;
}

.list-form tr th {
  padding: 1.5rem;
}

.list-form tr td {
  padding: 1rem;
}

.list-form .num {
  width: 200px;
}

.list-form .title {
  width: 600px;
}

.list-form .writer {
  width: 200px;
}

.list-form .created {
  width: 200px;
}
</style>
