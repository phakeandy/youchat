<script setup lang="ts">
interface Feature {
  title: string
  description: string
  icon: string
}

defineProps<{
  feature: Feature
}>()
</script>

<template>
  <div class="card bg-base-100 border-0 shadow-lg transition-all hover:scale-105 hover:shadow-xl">
    <div class="card-body items-center text-center">
      <div class="text-primary mt-4 text-4xl">
        <component :is="feature.icon" />
      </div>
      <h3 class="card-title text-primary-content text-lg">{{ feature.title }}</h3>
      <p class="text-base-content/70 text-sm">{{ feature.description }}</p>
    </div>
  </div>
</template>