<script setup lang="ts">
const features = [
  {
    title: '即时通讯',
    description: '支持实时文字聊天，消息秒达',
    icon: 'i-tabler-message-circle'
  },
  {
    title: '好友管理',
    description: '添加好友，查看在线状态',
    icon: 'i-tabler-users'
  },
  {
    title: '群组聊天',
    description: '创建群组，多人同时交流',
    icon: 'i-tabler-home-plus'
  },
  {
    title: '文件传输',
    description: '支持图片、文档等文件分享',
    icon: 'i-tabler-upload'
  }
]
</script>

<template>
  <main class="min-h-screen bg-gradient-to-br from-primary/5 to-secondary/10">
    <div class="container mx-auto px-4 py-16">
      <!-- 标题区域 -->
      <div class="text-center mb-16">
        <div class="flex justify-center items-center gap-4 mb-6">
          <i-tabler-message-circle class="text-6xl text-primary" />
          <h1 class="text-6xl font-bold text-primary-content">YouChat</h1>
        </div>
        <p class="text-xl text-base-content mb-8 opacity-80">简易在线互动聊天系统</p>
        <p class="text-base-content/60 max-w-2xl mx-auto">
          基于 Vue.js + Spring Boot 构建的现代化即时通讯应用，为用户提供稳定、安全、便捷的聊天体验
        </p>
      </div>

      <!-- 功能特性 -->
      <div class="grid md:grid-cols-2 lg:grid-cols-4 gap-6 mb-16">
        <div
          v-for="feature in features"
          :key="feature.title"
          class="card bg-base-100 shadow-lg hover:shadow-xl transition-all hover:scale-105 border-0"
        >
          <div class="card-body items-center text-center">
            <div class="text-4xl mb-4 text-primary">
              <component :is="feature.icon" />
            </div>
            <h3 class="card-title text-lg text-primary-content">{{ feature.title }}</h3>
            <p class="text-base-content/70 text-sm">{{ feature.description }}</p>
          </div>
        </div>
      </div>

      <!-- 按钮区域 -->
      <div class="flex justify-center gap-6">
        <button class="btn btn-primary btn-lg shadow-lg hover:shadow-xl transition-all hover:scale-105">
          <i-tabler-login class="mr-2" />
          登录
        </button>
        <button class="btn btn-secondary btn-lg shadow-lg hover:shadow-xl transition-all hover:scale-105">
          <i-tabler-user-plus class="mr-2" />
          注册
        </button>
      </div>

      <!-- 底部信息 -->
      <div class="text-center mt-16 text-base-content/50">
        <p>© 2025 YouChat. 基于 Vue 3 + DaisyUI + Tailwind CSS 构建</p>
      </div>
    </div>
  </main>
</template>
