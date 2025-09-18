<script setup lang="ts">
import { ref, computed } from 'vue'
import { type RegisterForm, validateRegister } from '@/schemas/auth'

// Props
interface Props {
  submitting?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  submitting: false,
})

// Emits
const emit = defineEmits<{
  submit: [payload: RegisterForm]
}>()

// Form data
const formData = ref({
  email: '',
  username: '',
  password: '',
  confirmPassword: '',
})

// UI state
const showPassword = ref(false)
const showConfirmPassword = ref(false)

const touched = ref({
  email: false,
  username: false,
  password: false,
  confirmPassword: false,
})

// Validation
const errors = computed(() => {
  const result = validateRegister(formData.value)
  return result.success ? {} : result.errors
})

const isValid = computed(() => {
  return validateRegister(formData.value).success
})

// Methods
const handleSubmit = () => {
  if (!isValid.value) return

  const result = validateRegister(formData.value)
  if (result.success) {
    emit('submit', result.data)
  }
}

const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value
}

const toggleConfirmPasswordVisibility = () => {
  showConfirmPassword.value = !showConfirmPassword.value
}
</script>

<template>
  <form @submit.prevent="handleSubmit" class="space-y-4" data-testid="register-form">
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
          data-testid="register-email"
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

    <!-- Username -->
    <div>
      <label class="floating-label" for="username">
        <span class="inline-flex items-center gap-1">
          <i-tabler-user class="h-4 w-4" />
          用户名
        </span>
        <input
          v-model="formData.username"
          @blur="touched.username = true"
          id="username"
          type="text"
          placeholder="请输入用户名"
          class="input w-full"
          :class="{ 'input-error': errors.username && touched.username }"
          required
          data-testid="register-username"
        />
      </label>
      <span
        id="username-error"
        v-if="errors.username && touched.username"
        class="text-error text-xs"
        role="alert"
        >{{ errors.username[0] }}</span
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
            data-testid="register-password"
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

    <!-- Confirm Password -->
    <div>
      <label class="floating-label" for="confirmPassword">
        <span class="flex items-center gap-1">
          <i-tabler-lock-check class="h-4 w-4" />
          确认密码
        </span>
        <div class="relative">
          <input
            v-model="formData.confirmPassword"
            @blur="touched.confirmPassword = true"
            id="confirmPassword"
            :type="showConfirmPassword ? 'text' : 'password'"
            placeholder="请再次输入密码"
            class="input w-full"
            :class="{ 'input-error': errors.confirmPassword && touched.confirmPassword }"
            required
            data-testid="register-confirm-password"
          />
          <button
            type="button"
            @click="toggleConfirmPasswordVisibility"
            class="absolute top-1/2 right-3 z-10 -translate-y-1/2"
            aria-label="切换密码可见性"
            data-testid="toggle-confirm-password"
          >
            <i-tabler-eye v-show="!showConfirmPassword" class="h-4 w-4" />
            <i-tabler-eye-off v-show="showConfirmPassword" class="h-4 w-4" />
          </button>
        </div>
      </label>
      <span v-if="errors.confirmPassword && touched.confirmPassword" class="text-error text-xs" role="alert">{{
        errors.confirmPassword[0]
      }}</span>
    </div>

    <!-- Submit -->
    <button
      type="submit"
      class="btn btn-primary w-full"
      :disabled="!isValid || props.submitting"
      data-testid="register-submit"
    >
      <span v-if="props.submitting" class="loading loading-spinner loading-sm"></span>
      <span v-else>注册</span>
    </button>
  </form>
</template>
