import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest'
import { mount, VueWrapper } from '@vue/test-utils'
import { ref } from 'vue'
import AuthModal from '../AuthModal.vue'

describe('AuthModal', () => {
  let wrapper: VueWrapper<unknown>
  let authModal: VueWrapper<unknown>

  const createWrapper = (props = {}) => {
    return mount({
      components: { AuthModal },
      setup() {
        const open = ref(true)
        const closeModal = () => { open.value = false }
        return { open, closeModal }
      },
      template: `
        <AuthModal
          :open="open"
          :default-tab="defaultTab"
          @close="closeModal"
        />
      `,
      props: ['defaultTab']
    }, {
      props: { defaultTab: 'login', ...props }
    })
  }

  beforeEach(() => {
    wrapper = createWrapper()
    authModal = wrapper.findComponent(AuthModal)
  })

  afterEach(() => {
    wrapper.unmount()
  })

  describe('Basic Rendering', () => {
    it('should render modal when open prop is true', () => {
      expect(authModal.exists()).toBe(true)
      expect(authModal.find('[data-testid="modal-backdrop"]').exists()).toBe(true)
      expect(authModal.find('[data-testid="modal-close"]').exists()).toBe(true)
    })

    it('should render login form by default', () => {
      expect(authModal.find('[data-testid="login-form"]').exists()).toBe(true)
      expect(authModal.find('[data-testid="register-form"]').exists()).toBe(false)
    })
  })

  describe('Tab Switching', () => {
    it('should switch to register tab when clicking register button', async () => {
      await authModal.find('[data-testid="register-tab"]').trigger('click')
      await wrapper.vm.$nextTick()

      expect(authModal.find('[data-testid="register-form"]').exists()).toBe(true)
      expect(authModal.find('[data-testid="login-form"]').exists()).toBe(false)
    })

    it('should switch back to login tab when clicking login button', async () => {
      // First switch to register
      await authModal.find('[data-testid="register-tab"]').trigger('click')
      await wrapper.vm.$nextTick()

      // Then switch back to login
      await authModal.find('[data-testid="login-tab"]').trigger('click')
      await wrapper.vm.$nextTick()

      expect(authModal.find('[data-testid="login-form"]').exists()).toBe(true)
      expect(authModal.find('[data-testid="register-form"]').exists()).toBe(false)
    })

    it('should respect defaultTab prop', () => {
      wrapper = createWrapper({ defaultTab: 'register' })
      authModal = wrapper.findComponent(AuthModal)

      expect(authModal.find('[data-testid="register-form"]').exists()).toBe(true)
      expect(authModal.find('[data-testid="login-form"]').exists()).toBe(false)
    })
  })

describe('Form Submission', () => {
    it('should handle login submission with child component', async () => {
      const spy = vi.spyOn(console, 'log')
      const emailInput = authModal.find('[data-testid="login-email"]')
      const passwordInput = authModal.find('[data-testid="login-password"]')
      const submitButton = authModal.find('[data-testid="login-submit"]')

      await emailInput.setValue('test@example.com')
      await passwordInput.setValue('Password123')
      await submitButton.trigger('submit')
      await wrapper.vm.$nextTick()

      expect(spy).toHaveBeenCalledWith('Login from parent:', {
        email: 'test@example.com',
        password: 'Password123'
      })
      spy.mockRestore()
    })

    it('should handle register submission with child component', async () => {
      // Switch to register tab
      await authModal.find('[data-testid="register-tab"]').trigger('click')
      await wrapper.vm.$nextTick()

      const spy = vi.spyOn(console, 'log')
      const emailInput = authModal.find('[data-testid="register-email"]')
      const usernameInput = authModal.find('[data-testid="register-username"]')
      const passwordInput = authModal.find('[data-testid="register-password"]')
      const confirmPasswordInput = authModal.find('[data-testid="register-confirm-password"]')
      const submitButton = authModal.find('[data-testid="register-submit"]')

      await emailInput.setValue('test@example.com')
      await usernameInput.setValue('testuser')
      await passwordInput.setValue('Password123')
      await confirmPasswordInput.setValue('Password123')
      await submitButton.trigger('submit')
      await wrapper.vm.$nextTick()

      expect(spy).toHaveBeenCalledWith('Register from parent:', {
        email: 'test@example.com',
        username: 'testuser',
        password: 'Password123',
        confirmPassword: 'Password123'
      })
      spy.mockRestore()
    })

    it('should show loading state during submission', async () => {
      vi.useFakeTimers()
      const emailInput = authModal.find('[data-testid="login-email"]')
      const passwordInput = authModal.find('[data-testid="login-password"]')
      const submitButton = authModal.find('[data-testid="login-submit"]')

      await emailInput.setValue('test@example.com')
      await passwordInput.setValue('Password123')

      // Start submission
      await submitButton.trigger('submit')
      await wrapper.vm.$nextTick()

      // Should show loading spinner
      expect(submitButton.find('.loading').exists()).toBe(true)
      expect(submitButton.attributes('disabled')).toBeDefined()

      // Fast forward timer
      vi.advanceTimersByTime(1000)
      await wrapper.vm.$nextTick()

      vi.useRealTimers()
    })
  })

  describe('Component Composition', () => {
    it('should render LoginForm component when on login tab', () => {
      expect(authModal.findComponent({ name: 'LoginForm' }).exists()).toBe(true)
      expect(authModal.findComponent({ name: 'RegisterForm' }).exists()).toBe(false)
    })

    it('should render RegisterForm component when on register tab', async () => {
      await authModal.find('[data-testid="register-tab"]').trigger('click')
      await wrapper.vm.$nextTick()

      expect(authModal.findComponent({ name: 'LoginForm' }).exists()).toBe(false)
      expect(authModal.findComponent({ name: 'RegisterForm' }).exists()).toBe(true)
    })

    it('should pass submitting prop to child components', async () => {
      // Test with login form
      expect(authModal.findComponent({ name: 'LoginForm' }).props('submitting')).toBe(false)

      // Switch to register form
      await authModal.find('[data-testid="register-tab"]').trigger('click')
      await wrapper.vm.$nextTick()

      expect(authModal.findComponent({ name: 'RegisterForm' }).props('submitting')).toBe(false)
    })
  })

  describe('Modal Close', () => {
    it('should render close button and backdrop', () => {
      expect(authModal.find('[data-testid="modal-close"]').exists()).toBe(true)
      expect(authModal.find('[data-testid="modal-backdrop"]').exists()).toBe(true)
    })
  })

})