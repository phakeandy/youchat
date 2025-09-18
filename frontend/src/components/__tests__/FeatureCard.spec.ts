import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import FeatureCard from '../FeatureCard.vue'

describe('FeatureCard', () => {
  const mockIconComponent = {
    template: '<span class="icon-component">Icon</span>',
  }

  const mockFeature = {
    title: '即时通讯',
    description: '支持实时文字聊天，消息秒达',
    icon: mockIconComponent,
  }

  it('should render feature card with correct structure', () => {
    const wrapper = mount(FeatureCard, {
      props: { feature: mockFeature },
    })

    const card = wrapper.find('[data-testid="feature-card"]')
    expect(card.exists()).toBe(true)
  })

  it('should display feature title and description correctly', () => {
    const wrapper = mount(FeatureCard, {
      props: { feature: mockFeature },
    })

    const title = wrapper.find('[data-testid="feature-title"]')
    const description = wrapper.find('[data-testid="feature-description"]')

    expect(title.exists()).toBe(true)
    expect(description.exists()).toBe(true)
    expect(title.text()).toBe(mockFeature.title)
    expect(description.text()).toBe(mockFeature.description)
  })

  it('should have proper content structure', () => {
    const wrapper = mount(FeatureCard, {
      props: { feature: mockFeature },
    })

    const content = wrapper.find('[data-testid="card-content"]')
    const iconContainer = wrapper.find('[data-testid="icon-container"]')

    expect(content.exists()).toBe(true)
    expect(iconContainer.exists()).toBe(true)
  })

  it('should render icon component when provided', () => {
    const wrapper = mount(FeatureCard, {
      props: { feature: mockFeature },
    })

    const iconComponent = wrapper.findComponent(mockIconComponent)
    expect(iconComponent.exists()).toBe(true)
  })
})