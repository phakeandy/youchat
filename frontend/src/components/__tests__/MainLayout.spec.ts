import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import MainLayout from '../MainLayout.vue'

describe('MainLayout', () => {
  it('should render slot content', () => {
    const wrapper = mount(MainLayout, {
      slots: {
        default: '<div class="test-content">Test Content</div>'
      }
    })

    expect(wrapper.find('.test-content').exists()).toBe(true)
    expect(wrapper.find('.test-content').text()).toBe('Test Content')
  })

  it('should have proper footer positioning', () => {
    const wrapper = mount(MainLayout)

    const footer = wrapper.find('footer')
    expect(footer.exists()).toBe(true)
    expect(footer.classes()).toContain('bg-base-100')
    expect(footer.classes()).toContain('text-base-content/50')
  })

  it('should have gradient background', () => {
    const wrapper = mount(MainLayout)

    const mainContainer = wrapper.find('.min-h-screen')
    expect(mainContainer.exists()).toBe(true)
    expect(mainContainer.classes()).toContain('bg-gradient-to-br')
    expect(mainContainer.classes()).toContain('from-primary/5')
    expect(mainContainer.classes()).toContain('to-secondary/10')
  })

  it('should display footer text', () => {
    const wrapper = mount(MainLayout)

    const footer = wrapper.find('footer')
    expect(footer.text()).toContain('© 2025 YouChat')
    expect(footer.text()).toContain('Vue 3 + DaisyUI + Tailwind CSS')
  })
})