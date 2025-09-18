import { mount } from '@vue/test-utils'
import { describe, expect, it } from 'vitest'
import HomeView from './HomeView.vue'

// 创建图标组件的 stub
const createIconStub = () => ({
  template: '<span class="icon-stub"></span>',
  props: ['class'],
})

// 全局组件 stub
const globalStubs = {
  'i-tabler-message-circle': createIconStub(),
  'i-tabler-users': createIconStub(),
  'i-tabler-home-plus': createIconStub(),
  'i-tabler-upload': createIconStub(),
  'i-tabler-login': createIconStub(),
  'i-tabler-user-plus': createIconStub(),
  'i-tabler-sun': createIconStub(),
  'i-tabler-moon': createIconStub(),
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

    const buttonTexts = buttons.map((btn) => btn.text())
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
    const classes = loginButton.classes()
    expect(
      classes.some((cls) => cls.includes('btn') || cls.includes('primary') || cls.includes('lg')),
    ).toBe(true)
  })

  it('should display feature cards with DaisyUI styling', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: globalStubs,
      },
    })

    // 测试功能卡片是否存在
    const cards = wrapper.findAll('.card')
    expect(cards.length).toBe(4)

    // 测试第一个卡片包含预期的内容
    const firstCard = cards[0]
    expect(firstCard.text()).toContain('即时通讯')
    expect(firstCard.text()).toContain('支持实时文字聊天，消息秒达')

    // 测试卡片是否使用了 DaisyUI 的 card 类
    expect(firstCard.classes()).toContain('card')

    // 测试卡片内部是否包含 card-body
    const cardBody = firstCard.find('.card-body')
    expect(cardBody.exists()).toBe(true)
  })

  it('should use semantic color classes instead of hardcoded colors', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: globalStubs,
      },
    })

    // 测试主标题使用语义化颜色
    const title = wrapper.find('h1')
    expect(title.classes()).toContain('text-primary-content')

    // 测试副标题使用语义化颜色 - 需要更精确地定位 hero section 中的副标题
    const heroSection = wrapper.find('section')
    const subtitle = heroSection.find('.text-xl')
    expect(subtitle.classes()).toContain('text-base-content')

    // 测试卡片使用语义化背景色
    const cards = wrapper.findAll('.card')
    cards.forEach((card) => {
      expect(card.classes()).toContain('bg-base-100')
    })

    // 测试卡片标题使用语义化颜色
    const cardTitles = wrapper.findAll('.card-title')
    cardTitles.forEach((title) => {
      expect(title.classes()).toContain('text-primary-content')
    })

    // 测试底部文字使用语义化颜色
    const footer = wrapper.find('footer')
    expect(footer.classes()).toContain('text-base-content')
  })

  it('should render components with proper structure', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: globalStubs,
      },
    })

    // 测试主容器存在
    const mainContainer = wrapper.find('.min-h-screen')
    expect(mainContainer.exists()).toBe(true)
    expect(mainContainer.classes()).toContain('bg-gradient-to-br')

    // 测试 main 标签存在
    const main = wrapper.find('main')
    expect(main.exists()).toBe(true)
    expect(main.classes()).toContain('container')

    // 测试 footer 脱离文档流并在底部
    const footer = wrapper.find('footer')
    expect(footer.exists()).toBe(true)
    expect(footer.classes()).toContain('bg-base-300')
  })

  it('should render HeroSection component', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: globalStubs,
      },
    })

    // 测试 HeroSection 存在并包含正确内容
    expect(wrapper.text()).toContain('YouChat')
    expect(wrapper.text()).toContain('简易在线互动聊天系统')
    expect(wrapper.text()).toContain('Vue.js + Spring Boot')
  })

  it('should render FeatureCard components for all features', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: globalStubs,
      },
    })

    // 测试所有功能特性都被渲染
    expect(wrapper.text()).toContain('即时通讯')
    expect(wrapper.text()).toContain('好友管理')
    expect(wrapper.text()).toContain('群组聊天')
    expect(wrapper.text()).toContain('文件传输')
  })
})
