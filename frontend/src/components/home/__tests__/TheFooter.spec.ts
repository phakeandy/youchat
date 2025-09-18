import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import TheFooter from '../TheFooter.vue'

describe('TheFooter', () => {
  it('should render footer with proper classes', () => {
    const wrapper = mount(TheFooter)

    const footer = wrapper.find('footer')
    expect(footer.exists()).toBe(true)
    expect(footer.classes()).toContain('footer')
    expect(footer.classes()).toContain('footer-center')
    expect(footer.classes()).toContain('bg-base-300')
    expect(footer.classes()).toContain('text-base-content')
  })

  it('should display copyright information', () => {
    const wrapper = mount(TheFooter)

    expect(wrapper.text()).toContain('Copyright ©')
    expect(wrapper.text()).toContain('Vue 3 + DaisyUI + Tailwind CSS')
  })

  it('should have current year in copyright', () => {
    const wrapper = mount(TheFooter)
    const currentYear = new Date().getFullYear()

    expect(wrapper.text()).toContain(currentYear.toString())
  })

  it('should have proper padding', () => {
    const wrapper = mount(TheFooter)

    const footer = wrapper.find('footer')
    expect(footer.classes()).toContain('p-4')
  })

  it('should have aside element for content', () => {
    const wrapper = mount(TheFooter)

    const aside = wrapper.find('aside')
    expect(aside.exists()).toBe(true)
  })

  it('should be responsive', () => {
    const wrapper = mount(TheFooter)

    const footer = wrapper.find('footer')
    expect(footer.classes()).toContain('sm:footer-horizontal')
  })
})