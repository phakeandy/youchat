import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import TheFeatureList from '../TheFeatureList.vue'

// 创建图标组件的 stub
const createIconStub = () => ({
  template: '<span class="icon-stub"></span>',
  props: ['class'],
})

// Mock the imported icon components
const mockIconComponents = {
  MessageCircleIcon: createIconStub(),
  UsersIcon: createIconStub(),
  HomePlusIcon: createIconStub(),
  UploadIcon: createIconStub(),
}

// Also stub the unplugin-icons components
const unpluginIconStubs = {
  'i-tabler-message-circle': createIconStub(),
  'i-tabler-users': createIconStub(),
  'i-tabler-home-plus': createIconStub(),
  'i-tabler-upload': createIconStub(),
}

describe('TheFeatureList', () => {
  it('should render feature list container', () => {
    const wrapper = mount(TheFeatureList, {
      global: {
        stubs: {
          '../icons/MessageCircleIcon.vue': mockIconComponents.MessageCircleIcon,
          '../icons/UsersIcon.vue': mockIconComponents.UsersIcon,
          '../icons/HomePlusIcon.vue': mockIconComponents.HomePlusIcon,
          '../icons/UploadIcon.vue': mockIconComponents.UploadIcon,
          ...unpluginIconStubs,
        },
      },
    })

    const featureList = wrapper.find('[data-testid="feature-list"]')
    expect(featureList.exists()).toBe(true)
  })

  it('should display all expected feature titles', () => {
    const wrapper = mount(TheFeatureList, {
      global: {
        stubs: {
          '../icons/MessageCircleIcon.vue': mockIconComponents.MessageCircleIcon,
          '../icons/UsersIcon.vue': mockIconComponents.UsersIcon,
          '../icons/HomePlusIcon.vue': mockIconComponents.HomePlusIcon,
          '../icons/UploadIcon.vue': mockIconComponents.UploadIcon,
          ...unpluginIconStubs,
        },
      },
    })

    expect(wrapper.text()).toContain('即时通讯')
    expect(wrapper.text()).toContain('好友管理')
    expect(wrapper.text()).toContain('群组聊天')
    expect(wrapper.text()).toContain('文件传输')
  })

  it('should display all feature descriptions', () => {
    const wrapper = mount(TheFeatureList, {
      global: {
        stubs: {
          '../icons/MessageCircleIcon.vue': mockIconComponents.MessageCircleIcon,
          '../icons/UsersIcon.vue': mockIconComponents.UsersIcon,
          '../icons/HomePlusIcon.vue': mockIconComponents.HomePlusIcon,
          '../icons/UploadIcon.vue': mockIconComponents.UploadIcon,
          ...unpluginIconStubs,
        },
      },
    })

    expect(wrapper.text()).toContain('支持实时文字聊天，消息秒达')
    expect(wrapper.text()).toContain('添加好友，查看在线状态')
    expect(wrapper.text()).toContain('创建群组，多人同时交流')
    expect(wrapper.text()).toContain('支持图片、文档等文件分享')
  })

  it('should render feature cards for all features', () => {
    const wrapper = mount(TheFeatureList, {
      global: {
        stubs: {
          '../icons/MessageCircleIcon.vue': mockIconComponents.MessageCircleIcon,
          '../icons/UsersIcon.vue': mockIconComponents.UsersIcon,
          '../icons/HomePlusIcon.vue': mockIconComponents.HomePlusIcon,
          '../icons/UploadIcon.vue': mockIconComponents.UploadIcon,
          ...unpluginIconStubs,
        },
      },
    })

    const cards = wrapper.findAll('[data-testid^="feature-card-"]')
    expect(cards.length).toBe(4)
  })
})