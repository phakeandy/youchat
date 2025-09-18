<script setup lang="ts">
import { useDark, useToggle } from '@vueuse/core'

const isDark = useDark({
  selector: 'html',
  attribute: 'data-theme',
  valueDark: 'business',
  valueLight: 'bumblebee',
})
const toggleDark = useToggle(isDark)
</script>

<template>
  <div class="navbar bg-primary/1 shadow-sm">
    <div class="flex-1">
      <a class="btn btn-ghost text-xl">YouChat</a>
    </div>
    <div class="flex-none">
      <button
        class="btn btn-circle btn-ghost"
        @click="toggleDark()"
        :aria-label="isDark ? 'Switch to light mode' : 'Switch to dark mode'"
      >
        <i-tabler-sun v-if="isDark" class="text-xl text-yellow-500" />
        <i-tabler-moon v-else class="text-xl text-blue-500" />
      </button>
    </div>
  </div>
</template>
