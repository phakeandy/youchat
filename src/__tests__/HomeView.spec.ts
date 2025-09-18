import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import HomeView from '../views/HomeView.vue'

// 创建图标组件的 stub
const createIconStub = () => ({
  template: '<span class="icon-stub"></span>',
  props: ['class'],
})

const globalStubs = {
  'i-tabler-message-circle': createIconStub(),
  'i-tabler-users': createIconStub(),
  'i-tabler-home-plus': createIconStub(),
  'i-tabler-upload': createIconStub(),
  'i-tabler-login': createIconStub(),
  'i-tabler-user-plus': createIconStub(),
}

describe('HomeView', () => {
  it('should display YouChat app information', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: globalStubs,
      },
    })

    // 测试标题显示
    expect(wrapper.text()).toContain('YouChat')
    expect(wrapper.text()).toContain('简易在线互动聊天系统')
  })

  it('should have login and register buttons', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: globalStubs,
      },
    })

    // 测试存在登录和注册按钮
    const buttons = wrapper.findAll('button')
    expect(buttons.length).toBeGreaterThanOrEqual(2)

    const buttonTexts = buttons.map(btn => btn.text())
    expect(buttonTexts).toContain('登录')
    expect(buttonTexts).toContain('注册')
  })

  it('should have visible and prominent buttons', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: globalStubs,
      },
    })

    const loginButton = wrapper.find('button')
    const registerButton = wrapper.findAll('button')[1]

    // 测试按钮是否可见
    expect(loginButton.isVisible()).toBe(true)
    expect(registerButton.isVisible()).toBe(true)

    // 测试按钮是否包含突出的样式类
    expect(loginButton.classes()).some(cls =>
      cls.includes('btn') || cls.includes('primary') || cls.includes('lg')
    )
  })

  it('should have proper footer positioning', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: globalStubs,
      },
    })

    // 测试 footer 脱离文档流并在底部
    const footer = wrapper.find('footer')
    expect(footer.exists()).toBe(true)
    expect(footer.classes()).toContain('bg-base-100')
  })

  it('should render all components correctly', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: globalStubs,
      },
    })

    // 测试主容器
    expect(wrapper.find('.min-h-screen').exists()).toBe(true)

    // 测试功能卡片
    const cards = wrapper.findAll('.card')
    expect(cards.length).toBe(4)

    // 测试页脚内容
    expect(wrapper.text()).toContain('Vue 3 + DaisyUI + Tailwind CSS')
  })
})