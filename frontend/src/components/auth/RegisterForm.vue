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
    <div class="form-control">
      <label class="label">
        <span class="label-text flex items-center gap-2">
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
        data-testid="register-email"
      />
      <label v-if="errors.email" class="label">
        <span class="label-text-alt text-error">{{ errors.email[0] }}</span>
      </label>
    </div>

    <!-- Username -->
    <div class="form-control">
      <label class="label">
        <span class="label-text flex items-center gap-2">
          <i-tabler-user class="h-4 w-4" />
          用户名
        </span>
      </label>
      <input
        v-model="formData.username"
        type="text"
        placeholder="请输入用户名"
        class="input input-bordered"
        :class="{ 'input-error': errors.username }"
        required
        data-testid="register-username"
      />
      <label v-if="errors.username" class="label">
        <span class="label-text-alt text-error">{{ errors.username[0] }}</span>
      </label>
    </div>

    <!-- Password -->
    <div class="form-control">
      <label class="label">
        <span class="label-text flex items-center gap-2">
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
          data-testid="register-password"
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
        <span class="label-text-alt text-error">{{ errors.password[0] }}</span>
      </label>
    </div>

    <!-- Confirm Password -->
    <div class="form-control">
      <label class="label">
        <span class="label-text flex items-center gap-2">
          <i-tabler-lock-check class="h-4 w-4" />
          确认密码
        </span>
      </label>
      <div class="relative">
        <input
          v-model="formData.confirmPassword"
          :type="showConfirmPassword ? 'text' : 'password'"
          placeholder="请再次输入密码"
          class="input input-bordered w-full"
          :class="{ 'input-error': errors.confirmPassword }"
          required
          data-testid="register-confirm-password"
        />
        <button
          type="button"
          @click="toggleConfirmPasswordVisibility"
          class="btn btn-ghost btn-sm btn-circle absolute top-1/2 right-3 -translate-y-1/2"
          aria-label="切换密码可见性"
          data-testid="toggle-confirm-password"
        >
          <i-tabler-eye v-if="!showConfirmPassword" class="h-4 w-4" />
          <i-tabler-eye-off v-else class="h-4 w-4" />
        </button>
      </div>
      <label v-if="errors.confirmPassword" class="label">
        <span class="label-text-alt text-error">{{ errors.confirmPassword[0] }}</span>
      </label>
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
