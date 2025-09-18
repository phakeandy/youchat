import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import FeatureCard from '../FeatureCard.vue'

// 创建图标组件的 stub
const createIconStub = () => ({
  template: '<span class="icon-stub"></span>',
  props: ['class'],
})

describe('FeatureCard', () => {
  const mockFeature = {
    title: '即时通讯',
    description: '支持实时文字聊天，消息秒达',
    icon: 'i-tabler-message-circle',
  }

  it('should display feature title and description', () => {
    const wrapper = mount(FeatureCard, {
      props: { feature: mockFeature },
      global: {
        stubs: {
          'i-tabler-message-circle': createIconStub(),
        },
      },
    })

    expect(wrapper.text()).toContain(mockFeature.title)
    expect(wrapper.text()).toContain(mockFeature.description)
  })

  it('should use DaisyUI card classes', () => {
    const wrapper = mount(FeatureCard, {
      props: { feature: mockFeature },
      global: {
        stubs: {
          'i-tabler-message-circle': createIconStub(),
        },
      },
    })

    const card = wrapper.find('.card')
    expect(card.exists()).toBe(true)
    expect(card.classes()).toContain('bg-base-100')
    expect(card.classes()).toContain('border-0')
    expect(card.classes()).toContain('shadow-lg')
  })

  it('should have card body with proper styling', () => {
    const wrapper = mount(FeatureCard, {
      props: { feature: mockFeature },
      global: {
        stubs: {
          'i-tabler-message-circle': createIconStub(),
        },
      },
    })

    const cardBody = wrapper.find('.card-body')
    expect(cardBody.exists()).toBe(true)
    expect(cardBody.classes()).toContain('items-center')
    expect(cardBody.classes()).toContain('text-center')
  })

  it('should display card title with semantic colors', () => {
    const wrapper = mount(FeatureCard, {
      props: { feature: mockFeature },
      global: {
        stubs: {
          'i-tabler-message-circle': createIconStub(),
        },
      },
    })

    const cardTitle = wrapper.find('.card-title')
    expect(cardTitle.exists()).toBe(true)
    expect(cardTitle.classes()).toContain('text-primary-content')
    expect(cardTitle.text()).toBe(mockFeature.title)
  })

  it('should render icon component', () => {
    const wrapper = mount(FeatureCard, {
      props: { feature: mockFeature },
      global: {
        stubs: {
          'i-tabler-message-circle': createIconStub(),
        },
      },
    })

    const iconContainer = wrapper.find('.text-primary')
    expect(iconContainer.exists()).toBe(true)
    expect(iconContainer.classes()).toContain('text-4xl')
  })

  it('should have hover effects', () => {
    const wrapper = mount(FeatureCard, {
      props: { feature: mockFeature },
      global: {
        stubs: {
          'i-tabler-message-circle': createIconStub(),
        },
      },
    })

    const card = wrapper.find('.card')
    expect(card.classes()).toContain('transition-all')
    expect(card.classes()).toContain('hover:scale-105')
    expect(card.classes()).toContain('hover:shadow-xl')
  })
})