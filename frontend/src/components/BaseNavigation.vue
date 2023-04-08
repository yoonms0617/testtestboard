<template>
  <div class="container">
    <header class="d-flex justify-content-center py-3 mb-4 border-bottom">
      <router-link to="/" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-dark text-decoration-none">
        <span class="fs-4">게시판</span>
      </router-link>
      <ul class="nav nav-pills">
        <li class="nav-item">
          <router-link to="/login" class="nav-link">로그인</router-link>
        </li>
        <li class="nav-item">
          <router-link to="/signup" class="nav-link">회원가입</router-link>
        </li>
      </ul>
    </header>
  </div>
</template>

<script>
export default {
  name: "BaseNavigation",
};
</script>
