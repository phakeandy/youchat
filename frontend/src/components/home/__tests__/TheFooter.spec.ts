import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import TheFooter from '../TheFooter.vue'

describe('TheFooter', () => {
  it('should render footer element', () => {
    const wrapper = mount(TheFooter)

    const footer = wrapper.find('[data-testid="footer"]')
    expect(footer.exists()).toBe(true)
  })

  it('should display copyright information with current year', () => {
    const wrapper = mount(TheFooter)
    const currentYear = new Date().getFullYear()

    const copyright = wrapper.find('[data-testid="copyright"]')
    expect(copyright.exists()).toBe(true)
    expect(copyright.text()).toContain('Copyright ©')
    expect(copyright.text()).toContain(currentYear.toString())
    expect(copyright.text()).toContain('Vue 3 + DaisyUI + Tailwind CSS')
  })

  it('should have content wrapper', () => {
    const wrapper = mount(TheFooter)

    const content = wrapper.find('[data-testid="footer-content"]')
    expect(content.exists()).toBe(true)
  })
})