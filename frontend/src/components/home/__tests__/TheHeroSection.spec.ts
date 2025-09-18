import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import TheHeroSection from '../TheHeroSection.vue'

// 创建图标组件的 stub
const createIconStub = () => ({
  template: '<span class="icon-stub"></span>',
  props: ['class'],
})

const globalStubs = {
  'i-tabler-message-circle': createIconStub(),
  'i-tabler-login': createIconStub(),
  'i-tabler-user-plus': createIconStub(),
}

describe('TheHeroSection', () => {
  it('should display YouChat title and subtitle', () => {
    const wrapper = mount(TheHeroSection, {
      global: {
        stubs: globalStubs,
      },
    })

    expect(wrapper.text()).toContain('YouChat')
    expect(wrapper.text()).toContain('简易在线互动聊天系统')
  })

  it('should have login and register buttons', () => {
    const wrapper = mount(TheHeroSection, {
      global: {
        stubs: globalStubs,
      },
    })

    const buttons = wrapper.findAll('button')
    expect(buttons.length).toBe(2)

    const buttonTexts = buttons.map((btn) => btn.text())
    expect(buttonTexts).toContain('登录')
    expect(buttonTexts).toContain('注册')
  })

  it('should use semantic color classes', () => {
    const wrapper = mount(TheHeroSection, {
      global: {
        stubs: globalStubs,
      },
    })

    const title = wrapper.find('h1')
    expect(title.classes()).toContain('text-primary-content')

    const subtitle = wrapper.find('.text-xl')
    expect(subtitle.classes()).toContain('text-base-content')
  })

  it('should have properly styled buttons', () => {
    const wrapper = mount(TheHeroSection, {
      global: {
        stubs: globalStubs,
      },
    })

    const buttons = wrapper.findAll('button')
    const loginButton = buttons[0]
    const registerButton = buttons[1]

    expect(loginButton.classes()).toContain('btn-primary')
    expect(loginButton.classes()).toContain('btn-xl')
    expect(registerButton.classes()).toContain('btn-secondary')
    expect(registerButton.classes()).toContain('btn-xl')
  })

  it('should have app description', () => {
    const wrapper = mount(TheHeroSection, {
      global: {
        stubs: globalStubs,
      },
    })

    expect(wrapper.text()).toContain('Vue.js + Spring Boot')
    expect(wrapper.text()).toContain('现代化即时通讯应用')
  })

  it('should have proper layout spacing', () => {
    const wrapper = mount(TheHeroSection, {
      global: {
        stubs: globalStubs,
      },
    })

    const heroContainer = wrapper.find('section')
    expect(heroContainer.exists()).toBe(true)
    expect(heroContainer.classes()).toContain('text-center')
  })

  it('should have hover effects on buttons', () => {
    const wrapper = mount(TheHeroSection, {
      global: {
        stubs: globalStubs,
      },
    })

    const buttons = wrapper.findAll('button')
    buttons.forEach((button) => {
      expect(button.classes()).toContain('transition-all')
      expect(button.classes()).toContain('hover:scale-105')
      expect(button.classes()).toContain('hover:shadow-xl')
    })
  })
})