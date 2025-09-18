<script setup lang="ts">
import TheFeatureList from '@/components/home/TheFeatureList.vue'
import TheHeroSection from '../components/home/TheHeroSection.vue'
import TheFooter from '../components/home/TheFooter.vue'
</script>

<template>
  <div class="from-primary/5 to-secondary/10 min-h-screen bg-gradient-to-br">
    <main class="container mx-auto px-4 py-16">
      <div class="mb-16">
        <TheHeroSection />
      </div>
      <div class="container">
        <TheFeatureList />
      </div>
    </main>
    <TheFooter />
  </div>
</template>
