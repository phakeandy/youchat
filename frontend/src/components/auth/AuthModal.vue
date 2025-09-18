<script setup lang="ts">
import { ref, watch } from 'vue'
import LoginForm from './LoginForm.vue'
import RegisterForm from './RegisterForm.vue'
import type { LoginForm as LoginFormType, RegisterForm as RegisterFormType } from '@/schemas/auth'

// Props
const props = defineProps<{
  open: boolean
  defaultTab?: 'login' | 'register'
}>()

// Emits
const emit = defineEmits<{
  close: []
}>()

// State
const activeTab = ref<'login' | 'register'>(props.defaultTab || 'login')
const isSubmitting = ref(false)

// Watch for defaultTab changes
watch(
  () => props.defaultTab,
  (newTab) => {
    if (newTab) {
      activeTab.value = newTab
    }
  },
)

// Methods
const handleLogin = async (payload: LoginFormType) => {
  isSubmitting.value = true
  try {
    console.log('Login from parent:', payload)
    await new Promise((resolve) => setTimeout(resolve, 1000))
    handleModalClose()
  } catch (error) {
    console.error('Login failed:', error)
  } finally {
    isSubmitting.value = false
  }
}

const handleRegister = async (payload: RegisterFormType) => {
  isSubmitting.value = true
  try {
    console.log('Register from parent:', payload)
    await new Promise((resolve) => setTimeout(resolve, 1000))
    handleModalClose()
  } catch (error) {
    console.error('Registration failed:', error)
  } finally {
    isSubmitting.value = false
  }
}

const handleModalClose = () => {
  emit('close')
}
</script>

<template>
  <div v-if="open" class="fixed inset-0 z-50 flex items-center justify-center">
    <!-- Backdrop -->
    <div
      class="fixed inset-0 bg-black/50 backdrop-blur-sm"
      @click="handleModalClose"
      data-testid="modal-backdrop"
    ></div>

    <!-- Modal -->
    <div class="bg-base-100 relative w-full max-w-md rounded-xl shadow-2xl">
      <!-- Close button -->
      <button
        @click="handleModalClose"
        class="btn btn-ghost btn-sm btn-circle absolute top-4 right-4"
        aria-label="关闭"
        data-testid="modal-close"
      >
        <i-tabler-x class="h-4 w-4" />
      </button>

      <!-- Modal content -->
      <div class="p-8">
        <!-- Title -->
        <h2 class="text-primary mb-6 text-center text-2xl font-bold">
          {{ activeTab === 'login' ? '登录账户' : '注册账户' }}
        </h2>

        <!-- Tabs -->
        <div class="tabs tabs-border mb-6 font-black">
          <button
            @click="activeTab = 'login'"
            class="tab flex-1"
            :class="{ 'tab-active': activeTab === 'login' }"
            data-testid="login-tab"
          >
            登录
          </button>
          <button
            @click="activeTab = 'register'"
            class="tab flex-1"
            :class="{ 'tab-active': activeTab === 'register' }"
            data-testid="register-tab"
          >
            注册
          </button>
        </div>

        <LoginForm v-if="activeTab === 'login'" :submitting="isSubmitting" @submit="handleLogin" />
        <RegisterForm v-else :submitting="isSubmitting" @submit="handleRegister" />
      </div>
    </div>
  </div>
</template>
