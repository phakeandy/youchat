<script setup lang="ts">
const features = [
  {
    title: '即时通讯',
    description: '支持实时文字聊天，消息秒达',
    icon: 'i-tabler-message-circle',
  },
  {
    title: '好友管理',
    description: '添加好友，查看在线状态',
    icon: 'i-tabler-users',
  },
  {
    title: '群组聊天',
    description: '创建群组，多人同时交流',
    icon: 'i-tabler-home-plus',
  },
  {
    title: '文件传输',
    description: '支持图片、文档等文件分享',
    icon: 'i-tabler-upload',
  },
]
</script>

<template>
  <main class="from-primary/5 to-secondary/10 min-h-screen bg-gradient-to-br">
    <div class="container mx-auto px-4 py-16">
      <!-- 标题区域 -->
      <div class="mb-16 text-center">
        <div class="mb-6 flex items-center justify-center gap-4">
          <i-tabler-message-circle class="text-primary text-6xl" />
          <h1 class="text-primary-content text-6xl font-bold">YouChat</h1>
        </div>
        <p class="text-base-content mb-8 text-xl opacity-80">简易在线互动聊天系统</p>
        <p class="text-base-content/60 mx-auto max-w-2xl">
          基于 Vue.js + Spring Boot 构建的现代化即时通讯应用，为用户提供稳定、安全、便捷的聊天体验
        </p>
      </div>

      <!-- 按钮区域 -->
      <div class="flex justify-center gap-12">
        <button
          class="btn btn-primary btn-lg shadow-lg transition-all hover:scale-105 hover:shadow-xl"
        >
          <i-tabler-login class="mr-2" />
          登录
        </button>
        <button
          class="btn btn-secondary btn-lg shadow-lg transition-all hover:scale-105 hover:shadow-xl"
        >
          <i-tabler-user-plus class="mr-2" />
          注册
        </button>
      </div>

      <!-- 功能特性 -->
      <div class="container">
        <div class="mb-16 grid gap-6 md:grid-cols-2 lg:grid-cols-4">
          <div
            v-for="feature in features"
            :key="feature.title"
            class="card bg-base-100 border-0 shadow-lg transition-all hover:scale-105 hover:shadow-xl"
          >
            <div class="card-body items-center text-center">
              <div class="text-primary mb-4 text-4xl">
                <component :is="feature.icon" />
              </div>
              <h3 class="card-title text-primary-content text-lg">{{ feature.title }}</h3>
              <p class="text-base-content/70 text-sm">{{ feature.description }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 底部信息 -->
      <footer class="text-base-content/50 mt-16 text-center">
        <p>© 2025 YouChat. 基于 Vue 3 + DaisyUI + Tailwind CSS 构建</p>
      </footer>
    </div>
  </main>
</template>
