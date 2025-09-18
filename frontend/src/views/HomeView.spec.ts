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

    expect(wrapper.text()).toContain('YouChat')
    expect(wrapper.text()).toContain('简易在线互动聊天系统')
  })

  it('should have login and register buttons', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: globalStubs,
      },
    })

    const loginButton = wrapper.find('button[aria-label="Login"]')
    const registerButton = wrapper.find('button[aria-label="Register"]')

    expect(loginButton.exists()).toBe(true)
    expect(registerButton.exists()).toBe(true)
    expect(loginButton.text()).toBe('登录')
    expect(registerButton.text()).toBe('注册')
  })

  it('should render HeroSection with title and icon', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: globalStubs,
      },
    })

    const titleContainer = wrapper.find('[data-testid="title-container"]')
    expect(titleContainer.exists()).toBe(true)

    expect(wrapper.text()).toContain('YouChat')
    expect(wrapper.text()).toContain('简易在线互动聊天系统')
  })

  it('should render all feature cards', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: globalStubs,
      },
    })

    expect(wrapper.text()).toContain('即时通讯')
    expect(wrapper.text()).toContain('好友管理')
    expect(wrapper.text()).toContain('群组聊天')
    expect(wrapper.text()).toContain('文件传输')
  })

  it('should render footer with copyright information', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: globalStubs,
      },
    })

    const footer = wrapper.find('[data-testid="footer"]')
    expect(footer.exists()).toBe(true)

    expect(wrapper.text()).toContain('Copyright ©')
    expect(wrapper.text()).toContain('Vue 3 + DaisyUI + Tailwind CSS')
  })
})
