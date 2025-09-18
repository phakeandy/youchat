import { describe, it, expect, beforeEach } from 'vitest'
import { mount, VueWrapper } from '@vue/test-utils'
import RegisterForm from '../RegisterForm.vue'

describe('RegisterForm', () => {
  let wrapper: VueWrapper<unknown>

  beforeEach(() => {
    wrapper = mount(RegisterForm)
  })

  afterEach(() => {
    wrapper.unmount()
  })

  describe('Basic Rendering', () => {
    it('should render register form', () => {
      expect(wrapper.find('[data-testid="register-form"]').exists()).toBe(true)
      expect(wrapper.find('[data-testid="register-email"]').exists()).toBe(true)
      expect(wrapper.find('[data-testid="register-username"]').exists()).toBe(true)
      expect(wrapper.find('[data-testid="register-password"]').exists()).toBe(true)
      expect(wrapper.find('[data-testid="register-confirm-password"]').exists()).toBe(true)
      expect(wrapper.find('[data-testid="register-submit"]').exists()).toBe(true)
    })

    it('should have submit button disabled initially', () => {
      const submitButton = wrapper.find('[data-testid="register-submit"]')
      expect(submitButton.attributes('disabled')).toBeDefined()
    })
  })

  describe('Form Validation', () => {
    it('should validate email format', async () => {
      const emailInput = wrapper.find('[data-testid="register-email"]')

      // Invalid email
      await emailInput.setValue('invalid-email')
      await wrapper.vm.$nextTick()

      expect(emailInput.classes()).toContain('input-error')
      expect(wrapper.text()).toContain('请输入有效的邮箱地址')

      // Valid email
      await emailInput.setValue('valid@email.com')
      await wrapper.vm.$nextTick()

      expect(emailInput.classes()).not.toContain('input-error')
    })

    it('should validate username length', async () => {
      const usernameInput = wrapper.find('[data-testid="register-username"]')

      // Too short
      await usernameInput.setValue('a')
      await wrapper.vm.$nextTick()

      expect(usernameInput.classes()).toContain('input-error')
      expect(wrapper.text()).toContain('用户名至少需要2个字符')

      // Too long
      await usernameInput.setValue('a'.repeat(25))
      await wrapper.vm.$nextTick()

      expect(usernameInput.classes()).toContain('input-error')
      expect(wrapper.text()).toContain('用户名最多20个字符')

      // Valid
      await usernameInput.setValue('validuser')
      await wrapper.vm.$nextTick()

      expect(usernameInput.classes()).not.toContain('input-error')
    })

    it('should validate password complexity', async () => {
      const passwordInput = wrapper.find('[data-testid="register-password"]')

      // No uppercase
      await passwordInput.setValue('password123')
      await wrapper.vm.$nextTick()

      expect(passwordInput.classes()).toContain('input-error')
      expect(wrapper.text()).toContain('密码必须包含至少一个大写字母')

      // No lowercase
      await passwordInput.setValue('PASSWORD123')
      await wrapper.vm.$nextTick()

      expect(passwordInput.classes()).toContain('input-error')
      expect(wrapper.text()).toContain('密码必须包含至少一个小写字母')

      // No numbers
      await passwordInput.setValue('Password')
      await wrapper.vm.$nextTick()

      expect(passwordInput.classes()).toContain('input-error')
      expect(wrapper.text()).toContain('密码必须包含至少一个数字')

      // Valid
      await passwordInput.setValue('Password123')
      await wrapper.vm.$nextTick()

      expect(passwordInput.classes()).not.toContain('input-error')
    })

    it('should validate password confirmation', async () => {
      const passwordInput = wrapper.find('[data-testid="register-password"]')
      const confirmPasswordInput = wrapper.find('[data-testid="register-confirm-password"]')

      // Fill valid password
      await passwordInput.setValue('Password123')
      await wrapper.vm.$nextTick()

      // Mismatched confirmation
      await confirmPasswordInput.setValue('Different123')
      await wrapper.vm.$nextTick()

      expect(confirmPasswordInput.classes()).toContain('input-error')
      expect(wrapper.text()).toContain('密码不匹配')

      // Matching confirmation
      await confirmPasswordInput.setValue('Password123')
      await wrapper.vm.$nextTick()

      expect(confirmPasswordInput.classes()).not.toContain('input-error')
    })

    it('should enable submit button when form is valid', async () => {
      const submitButton = wrapper.find('[data-testid="register-submit"]')
      const emailInput = wrapper.find('[data-testid="register-email"]')
      const usernameInput = wrapper.find('[data-testid="register-username"]')
      const passwordInput = wrapper.find('[data-testid="register-password"]')
      const confirmPasswordInput = wrapper.find('[data-testid="register-confirm-password"]')

      // Initially disabled
      expect(submitButton.attributes('disabled')).toBeDefined()

      // Fill valid data
      await emailInput.setValue('test@example.com')
      await usernameInput.setValue('testuser')
      await passwordInput.setValue('Password123')
      await confirmPasswordInput.setValue('Password123')
      await wrapper.vm.$nextTick()

      // Should be enabled
      expect(submitButton.attributes('disabled')).toBeUndefined()
    })
  })

  describe('Form Submission', () => {
    it('should emit submit event with valid data', async () => {
      const emailInput = wrapper.find('[data-testid="register-email"]')
      const usernameInput = wrapper.find('[data-testid="register-username"]')
      const passwordInput = wrapper.find('[data-testid="register-password"]')
      const confirmPasswordInput = wrapper.find('[data-testid="register-confirm-password"]')
      const submitButton = wrapper.find('[data-testid="register-submit"]')

      await emailInput.setValue('test@example.com')
      await usernameInput.setValue('testuser')
      await passwordInput.setValue('Password123')
      await confirmPasswordInput.setValue('Password123')
      await submitButton.trigger('submit')

      expect(wrapper.emitted('submit')).toBeTruthy()
      expect(wrapper.emitted('submit')[0][0]).toEqual({
        email: 'test@example.com',
        username: 'testuser',
        password: 'Password123',
        confirmPassword: 'Password123'
      })
    })

    it('should not submit invalid form', async () => {
      const submitButton = wrapper.find('[data-testid="register-submit"]')
      await submitButton.trigger('submit')

      expect(wrapper.emitted('submit')).toBeFalsy()
    })
  })

  describe('Password Visibility', () => {
    it('should toggle password visibility', async () => {
      const passwordInput = wrapper.find('[data-testid="register-password"]')
      const toggleButton = wrapper.find('[data-testid="toggle-password"]')

      // Initially password type
      expect(passwordInput.attributes('type')).toBe('password')

      // Click to show
      await toggleButton.trigger('click')
      await wrapper.vm.$nextTick()
      expect(passwordInput.attributes('type')).toBe('text')

      // Click to hide
      await toggleButton.trigger('click')
      await wrapper.vm.$nextTick()
      expect(passwordInput.attributes('type')).toBe('password')
    })

    it('should toggle confirm password visibility', async () => {
      const confirmPasswordInput = wrapper.find('[data-testid="register-confirm-password"]')
      const toggleButton = wrapper.find('[data-testid="toggle-confirm-password"]')

      // Initially password type
      expect(confirmPasswordInput.attributes('type')).toBe('password')

      // Click to show
      await toggleButton.trigger('click')
      await wrapper.vm.$nextTick()
      expect(confirmPasswordInput.attributes('type')).toBe('text')

      // Click to hide
      await toggleButton.trigger('click')
      await wrapper.vm.$nextTick()
      expect(confirmPasswordInput.attributes('type')).toBe('password')
    })
  })

  describe('Submitting State', () => {
    it('should show loading spinner when submitting', async () => {
      await wrapper.setProps({ submitting: true })
      await wrapper.vm.$nextTick()

      const submitButton = wrapper.find('[data-testid="register-submit"]')
      expect(submitButton.find('.loading').exists()).toBe(true)
    })

    it('should disable submit button when submitting', async () => {
      const emailInput = wrapper.find('[data-testid="register-email"]')
      const usernameInput = wrapper.find('[data-testid="register-username"]')
      const passwordInput = wrapper.find('[data-testid="register-password"]')
      const confirmPasswordInput = wrapper.find('[data-testid="register-confirm-password"]')

      await emailInput.setValue('test@example.com')
      await usernameInput.setValue('testuser')
      await passwordInput.setValue('Password123')
      await confirmPasswordInput.setValue('Password123')
      await wrapper.setProps({ submitting: true })
      await wrapper.vm.$nextTick()

      const submitButton = wrapper.find('[data-testid="register-submit"]')
      expect(submitButton.attributes('disabled')).toBeDefined()
    })
  })
})