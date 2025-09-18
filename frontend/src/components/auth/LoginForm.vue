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

const touched = ref({
  email: false,
  password: false,
})

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
      <label class="floating-label" for="email">
        <span class="inline-flex items-center gap-1">
          <i-tabler-mail class="h-4 w-4" />
          邮箱
        </span>
        <input
          v-model="formData.email"
          @blur="touched.email = true"
          id="email"
          type="email"
          placeholder="请输入邮箱"
          class="input w-full"
          :class="{ 'input-error': errors.email && touched.email }"
          required
          data-testid="login-email"
        />
      </label>
      <span
        id="email-error"
        v-if="errors.email && touched.email"
        class="text-error text-xs"
        role="alert"
        >{{ errors.email[0] }}</span
      >
    </div>

    <!-- Password -->
    <div>
      <label class="floating-label" for="password">
        <span class="flex items-center gap-1">
          <i-tabler-lock class="h-4 w-4" />
          密码
        </span>
        <div class="relative">
          <input
            v-model="formData.password"
            @blur="touched.password = true"
            id="password"
            :type="showPassword ? 'text' : 'password'"
            placeholder="请输入密码"
            class="input w-full"
            :class="{ 'input-error': errors.password && touched.password }"
            required
            data-testid="login-password"
          />
          <button
            type="button"
            @click="togglePasswordVisibility"
            class="absolute top-1/2 right-3 z-10 -translate-y-1/2"
            aria-label="切换密码可见性"
            data-testid="toggle-password"
          >
            <i-tabler-eye v-show="!showPassword" class="h-4 w-4" />
            <i-tabler-eye-off v-show="showPassword" class="h-4 w-4" />
          </button>
        </div>
      </label>
      <span v-if="errors.password && touched.password" class="text-error text-xs" role="alert">{{
        errors.password[0]
      }}</span>
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
