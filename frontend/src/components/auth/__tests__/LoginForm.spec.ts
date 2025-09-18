import { describe, it, expect, beforeEach } from 'vitest'
import { mount, VueWrapper } from '@vue/test-utils'
import LoginForm from '../LoginForm.vue'

describe('LoginForm', () => {
  let wrapper: VueWrapper<unknown>

  beforeEach(() => {
    wrapper = mount(LoginForm)
  })

  afterEach(() => {
    wrapper.unmount()
  })

  describe('Basic Rendering', () => {
    it('should render login form', () => {
      expect(wrapper.find('[data-testid="login-form"]').exists()).toBe(true)
      expect(wrapper.find('[data-testid="login-email"]').exists()).toBe(true)
      expect(wrapper.find('[data-testid="login-password"]').exists()).toBe(true)
      expect(wrapper.find('[data-testid="login-submit"]').exists()).toBe(true)
    })

    it('should have submit button disabled initially', () => {
      const submitButton = wrapper.find('[data-testid="login-submit"]')
      expect(submitButton.attributes('disabled')).toBeDefined()
    })
  })

  describe('Form Validation', () => {
    it('should validate email format', async () => {
      const emailInput = wrapper.find('[data-testid="login-email"]')

      // Invalid email
      await emailInput.setValue('invalid-email')
      await emailInput.trigger('blur')
      await wrapper.vm.$nextTick()

      expect(emailInput.classes()).toContain('input-error')
      expect(wrapper.text()).toContain('请输入有效的邮箱地址')

      // Valid email
      await emailInput.setValue('valid@email.com')
      await emailInput.trigger('blur')
      await wrapper.vm.$nextTick()

      expect(emailInput.classes()).not.toContain('input-error')
    })

    it('should validate password length', async () => {
      const passwordInput = wrapper.find('[data-testid="login-password"]')

      // Short password
      await passwordInput.setValue('123')
      await passwordInput.trigger('blur')
      await wrapper.vm.$nextTick()

      expect(passwordInput.classes()).toContain('input-error')
      expect(wrapper.text()).toContain('密码至少需要6个字符')

      // Valid password
      await passwordInput.setValue('password123')
      await passwordInput.trigger('blur')
      await wrapper.vm.$nextTick()

      expect(passwordInput.classes()).not.toContain('input-error')
    })

    it('should enable submit button when form is valid', async () => {
      const submitButton = wrapper.find('[data-testid="login-submit"]')
      const emailInput = wrapper.find('[data-testid="login-email"]')
      const passwordInput = wrapper.find('[data-testid="login-password"]')

      // Initially disabled
      expect(submitButton.attributes('disabled')).toBeDefined()

      // Fill valid data
      await emailInput.setValue('test@example.com')
      await passwordInput.setValue('Password123')
      await emailInput.trigger('blur')
      await passwordInput.trigger('blur')
      await wrapper.vm.$nextTick()

      // Should be enabled
      expect(submitButton.attributes('disabled')).toBeUndefined()
    })
  })

  describe('Form Submission', () => {
    it('should emit submit event with valid data', async () => {
      const emailInput = wrapper.find('[data-testid="login-email"]')
      const passwordInput = wrapper.find('[data-testid="login-password"]')
      const submitButton = wrapper.find('[data-testid="login-submit"]')

      await emailInput.setValue('test@example.com')
      await passwordInput.setValue('Password123')
      await emailInput.trigger('blur')
      await passwordInput.trigger('blur')
      await submitButton.trigger('submit')

      expect(wrapper.emitted('submit')).toBeTruthy()
      expect(wrapper.emitted('submit')[0][0]).toEqual({
        email: 'test@example.com',
        password: 'Password123'
      })
    })

    it('should not submit invalid form', async () => {
      const submitButton = wrapper.find('[data-testid="login-submit"]')
      await submitButton.trigger('submit')

      expect(wrapper.emitted('submit')).toBeFalsy()
    })
  })

  describe('Password Visibility', () => {
    it('should toggle password visibility', async () => {
      const passwordInput = wrapper.find('[data-testid="login-password"]')
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
  })

  describe('Submitting State', () => {
    it('should show loading spinner when submitting', async () => {
      await wrapper.setProps({ submitting: true })
      await wrapper.vm.$nextTick()

      const submitButton = wrapper.find('[data-testid="login-submit"]')
      expect(submitButton.find('.loading').exists()).toBe(true)
    })

    it('should disable submit button when submitting', async () => {
      const emailInput = wrapper.find('[data-testid="login-email"]')
      const passwordInput = wrapper.find('[data-testid="login-password"]')

      await emailInput.setValue('test@example.com')
      await passwordInput.setValue('Password123')
      await emailInput.trigger('blur')
      await passwordInput.trigger('blur')
      await wrapper.setProps({ submitting: true })
      await wrapper.vm.$nextTick()

      const submitButton = wrapper.find('[data-testid="login-submit"]')
      expect(submitButton.attributes('disabled')).toBeDefined()
    })
  })
})