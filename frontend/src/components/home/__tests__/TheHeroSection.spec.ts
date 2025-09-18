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
  it('should render all textual content correctly', () => {
    const wrapper = mount(TheHeroSection, { global: { stubs: globalStubs } })
    const text = wrapper.text()
    expect(text).toContain('YouChat')
    expect(text).toContain('简易在线互动聊天系统')
    expect(text).toContain('现代化即时通讯应用')
  })

  it('should render primary actions for the user', () => {
    const wrapper = mount(TheHeroSection, { global: { stubs: globalStubs } })

    const loginButton = wrapper.find('button[aria-label="Login"]')
    expect(loginButton.exists()).toBe(true)
    expect(loginButton.text()).toBe('登录')

    const registerButton = wrapper.find('button[aria-label="Register"]')
    expect(registerButton.exists()).toBe(true)
    expect(registerButton.text()).toBe('注册')
  })

  it('should render a title with an associated icon', () => {
    const wrapper = mount(TheHeroSection, { global: { stubs: globalStubs } })

    const titleContainer = wrapper.find('[data-testid="title-container"]')
    expect(titleContainer.exists()).toBe(true)

    const title = titleContainer.find('h1')
    const icon = titleContainer.find('.icon-stub')

    expect(title.exists()).toBe(true)
    expect(icon.exists()).toBe(true)
  })
})