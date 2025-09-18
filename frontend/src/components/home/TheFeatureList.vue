<script lang="ts" setup>
import FeatureCard from '@/components/FeatureCard.vue'

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
  <div class="mb-16 grid gap-6 md:grid-cols-2 lg:grid-cols-4">
    <FeatureCard v-for="feature in features" :key="feature.title" :feature="feature" />
  </div>
</template>
