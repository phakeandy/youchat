<script setup lang="ts">
import { ref, computed } from 'vue'
import { type LoginForm, validateLogin } from '@/schemas/auth'

// Props
interface Props {
  submitting?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  submitting: false,
})

// Emits
const emit = defineEmits<{
  submit: [payload: LoginForm]
}>()

// Form data
const formData = ref({
  email: '',
  password: '',
})

// UI state
const showPassword = ref(false)

// Validation
const errors = computed(() => {
  const result = validateLogin(formData.value)
  return result.success ? {} : result.errors
})

const isValid = computed(() => {
  return validateLogin(formData.value).success
})

// Methods
const handleSubmit = () => {
  if (!isValid.value) return

  const result = validateLogin(formData.value)
  if (result.success) {
    emit('submit', result.data)
  }
}

const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value
}
</script>

<template>
  <form @submit.prevent="handleSubmit" class="space-y-4" data-testid="login-form">
    <!-- Email -->
    <div>
      <!-- <label class="label">
        <span class="flex items-center gap-2">
          <i-tabler-mail class="h-4 w-4" />
          邮箱
        </span>
      </label>
      <input
        v-model="formData.email"
        type="email"
        placeholder="请输入邮箱"
        class="input input-bordered"
        :class="{ 'input-error': errors.email }"
        required
        data-testid="login-email"
      /> -->
      <label
        class="input input-bordered flex items-center gap-2"
        :class="{ 'input-error': errors.email }"
      >
        <i-tabler-mail class="h-4 w-4" />
        <input
          v-model="formData.email"
          type="email"
          class="grow"
          placeholder="邮箱"
          required
          data-testid="login-email"
        />
      </label>
      <label v-if="errors.email" class="label">
        <span class="-alt text-error">{{ errors.email[0] }}</span>
      </label>
    </div>

    <!-- Password -->
    <div>
      <label class="label">
        <span class="flex items-center gap-2">
          <i-tabler-lock class="h-4 w-4" />
          密码
        </span>
      </label>
      <div class="relative">
        <input
          v-model="formData.password"
          :type="showPassword ? 'text' : 'password'"
          placeholder="请输入密码"
          class="input input-bordered w-full"
          :class="{ 'input-error': errors.password }"
          required
          data-testid="login-password"
        />
        <button
          type="button"
          @click="togglePasswordVisibility"
          class="btn btn-ghost btn-sm btn-circle absolute top-1/2 right-3 -translate-y-1/2"
          aria-label="切换密码可见性"
          data-testid="toggle-password"
        >
          <i-tabler-eye v-if="!showPassword" class="h-4 w-4" />
          <i-tabler-eye-off v-else class="h-4 w-4" />
        </button>
      </div>
      <label v-if="errors.password" class="label">
        <span class="-alt text-error">{{ errors.password[0] }}</span>
      </label>
    </div>

    <!-- Submit -->
    <button
      type="submit"
      class="btn btn-primary w-full"
      :disabled="!isValid || props.submitting"
      data-testid="login-submit"
    >
      <span v-if="props.submitting" class="loading loading-spinner loading-sm"></span>
      <span v-else>登录</span>
    </button>
  </form>
</template>
