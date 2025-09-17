import { describe, it, expect } from 'vitest'
import { mount, VueWrapper } from '@vue/test-utils'
import HomeView from './HomeView.vue'

// 创建图标组件的 stub
const createIconStub = () => ({
  template: '<span class="icon-stub"></span>',
  props: ['class']
})

// 全局组件 stub
const globalStubs = {
  'i-tabler-message-circle': createIconStub(),
  'i-tabler-users': createIconStub(),
  'i-tabler-home-plus': createIconStub(),
  'i-tabler-upload': createIconStub(),
  'i-tabler-login': createIconStub(),
  'i-tabler-user-plus': createIconStub()
}

describe('HomeView', () => {
  it('should display YouChat app information', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: globalStubs
      }
    })

    // 测试标题显示
    expect(wrapper.text()).toContain('YouChat')
    expect(wrapper.text()).toContain('简易在线互动聊天系统')
  })

  it('should have login and register buttons', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: globalStubs
      }
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
        stubs: globalStubs
      }
    })

    const loginButton = wrapper.find('button')
    const registerButton = wrapper.findAll('button')[1]

    // 测试按钮是否可见
    expect(loginButton.isVisible()).toBe(true)
    expect(registerButton.isVisible()).toBe(true)

    // 测试按钮是否包含突出的样式类
    const classes = loginButton.classes()
    expect(classes.some(cls =>
      cls.includes('btn') || cls.includes('primary') || cls.includes('lg')
    )).toBe(true)
  })

  it('should display feature cards with DaisyUI styling', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: globalStubs
      }
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
        stubs: globalStubs
      }
    })

    // 测试主标题使用语义化颜色
    const title = wrapper.find('h1')
    expect(title.classes()).toContain('text-primary-content')

    // 测试副标题使用语义化颜色
    const subtitle = wrapper.find('.text-xl')
    expect(subtitle.classes()).toContain('text-base-content')

    // 测试卡片使用语义化背景色
    const cards = wrapper.findAll('.card')
    cards.forEach(card => {
      expect(card.classes()).toContain('bg-base-100')
    })

    // 测试卡片标题使用语义化颜色
    const cardTitles = wrapper.findAll('.card-title')
    cardTitles.forEach(title => {
      expect(title.classes()).toContain('text-primary-content')
    })

    // 测试底部文字使用语义化颜色
    const footer = wrapper.find('.mt-16')
    expect(footer.classes()).toContain('text-base-content/50')
  })
})