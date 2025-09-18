<script setup lang="ts">
// Hero section with title, description, and action buttons
</script>

<template>
  <section class="text-center">
    <div class="mb-6 flex items-center justify-center gap-4">
      <i-tabler-message-circle class="text-primary text-6xl" />
      <h1 class="text-primary-content text-6xl font-bold">YouChat</h1>
    </div>
    <p class="text-base-content mb-8 text-xl opacity-80">简易在线互动聊天系统</p>
    <p class="text-base-content/60 mx-auto max-w-2xl">
      基于 Vue.js + Spring Boot 构建的现代化即时通讯应用，为用户提供稳定、安全、便捷的聊天体验
    </p>

    <div class="mt-12 flex justify-center gap-12">
      <button
        class="btn btn-primary btn-xl shadow-lg transition-all hover:scale-105 hover:shadow-xl"
      >
        <i-tabler-login class="mr-2" />
        登录
      </button>
      <button
        class="btn btn-secondary btn-xl shadow-lg transition-all hover:scale-105 hover:shadow-xl"
      >
        <i-tabler-user-plus class="mr-2" />
        注册
      </button>
    </div>
  </section>
</template>
