<script setup lang="ts">
import { useDark, useToggle } from '@vueuse/core'

const isDark = useDark()
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
        <i
          :class="isDark ? 'i-tabler-sun text-yellow-500' : 'i-tabler-moon text-blue-500'"
          class="text-xl"
        />
      </button>
    </div>
  </div>
</template>
