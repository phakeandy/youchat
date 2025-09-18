<script setup lang="ts">
import type { Component } from 'vue'

interface Feature {
  title: string
  description: string
  icon: Component
}

defineProps<{
  feature: Feature
}>()
</script>

<template>
  <div
    class="card bg-base-100 border-0 shadow-lg transition-all hover:scale-105 hover:shadow-xl"
    data-testid="feature-card"
  >
    <div class="card-body items-center text-center" data-testid="card-content">
      <div class="text-primary mt-4 text-4xl" data-testid="icon-container">
        <component :is="feature.icon" />
      </div>
      <h3 class="card-title text-primary-content text-lg" data-testid="feature-title">
        {{ feature.title }}
      </h3>
      <p class="text-base-content/70 text-sm" data-testid="feature-description">
        {{ feature.description }}
      </p>
    </div>
  </div>
</template>