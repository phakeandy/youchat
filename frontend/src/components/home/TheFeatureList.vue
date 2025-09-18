<script setup lang="ts">
import FeatureCard from '../FeatureCard.vue'
import MessageCircleIcon from '../icons/MessageCircleIcon.vue'
import UsersIcon from '../icons/UsersIcon.vue'
import HomePlusIcon from '../icons/HomePlusIcon.vue'
import UploadIcon from '../icons/UploadIcon.vue'

const features = [
  {
    title: '即时通讯',
    description: '支持实时文字聊天，消息秒达',
    icon: MessageCircleIcon,
  },
  {
    title: '好友管理',
    description: '添加好友，查看在线状态',
    icon: UsersIcon,
  },
  {
    title: '群组聊天',
    description: '创建群组，多人同时交流',
    icon: HomePlusIcon,
  },
  {
    title: '文件传输',
    description: '支持图片、文档等文件分享',
    icon: UploadIcon,
  },
]
</script>

<template>
  <div class="mb-16 grid gap-6 md:grid-cols-2" data-testid="feature-list">
    <FeatureCard
      v-for="feature in features"
      :key="feature.title"
      :feature="feature"
      :data-testid="`feature-card-${feature.title}`"
    />
  </div>
</template>
