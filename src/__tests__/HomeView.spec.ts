import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import HomeView from '../views/HomeView.vue'

describe('HomeView', () => {
  it('should display YouChat app information', () => {
    const wrapper = mount(HomeView)

    // 测试标题显示
    expect(wrapper.text()).toContain('YouChat')
    expect(wrapper.text()).toContain('简易在线互动聊天系统')
  })

  it('should have login and register buttons', () => {
    const wrapper = mount(HomeView)

    // 测试存在登录和注册按钮
    const buttons = wrapper.findAll('button')
    expect(buttons.length).toBeGreaterThanOrEqual(2)

    const buttonTexts = buttons.map(btn => btn.text())
    expect(buttonTexts).toContain('登录')
    expect(buttonTexts).toContain('注册')
  })

  it('should have visible and prominent buttons', () => {
    const wrapper = mount(HomeView)

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
})