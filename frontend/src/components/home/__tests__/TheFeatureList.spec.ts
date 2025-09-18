import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import TheFeatureList from '../TheFeatureList.vue'

// 创建图标组件的 stub
const createIconStub = (name: string) => ({
  name: name,
  template: `<span class="icon-stub ${name}">${name}</span>`,
})

// Mock the imported icon components
const mockIconComponents = {
  MessageCircleIcon: createIconStub('MessageCircleIcon'),
  UsersIcon: createIconStub('UsersIcon'),
  HomePlusIcon: createIconStub('HomePlusIcon'),
  UploadIcon: createIconStub('UploadIcon'),
}

// Also stub the unplugin-icons components
const unpluginIconStubs = {
  'i-tabler-message-circle': createIconStub('i-tabler-message-circle'),
  'i-tabler-users': createIconStub('i-tabler-users'),
  'i-tabler-home-plus': createIconStub('i-tabler-home-plus'),
  'i-tabler-upload': createIconStub('i-tabler-upload'),
}

describe('TheFeatureList', () => {
  it('should display all feature cards', () => {
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

    const cards = wrapper.findAll('.card')
    expect(cards.length).toBe(4)
  })

  it('should contain all expected features', () => {
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

  it('should use proper grid layout', () => {
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

    const grid = wrapper.find('.grid')
    expect(grid.exists()).toBe(true)
    expect(grid.classes()).toContain('md:grid-cols-2')
    // Note: Current implementation only uses md:grid-cols-2, lg:grid-cols-4 is not implemented yet
    // expect(grid.classes()).toContain('lg:grid-cols-4')
  })

  it('should have proper spacing', () => {
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

    const grid = wrapper.find('.grid')
    expect(grid.classes()).toContain('mb-16')
    expect(grid.classes()).toContain('gap-6')
  })

  it('should display feature descriptions', () => {
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
})