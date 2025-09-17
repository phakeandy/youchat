import { mount } from '@vue/test-utils'
import { createPinia } from 'pinia'
import { describe, expect, it } from 'vitest'
import { createRouter, createWebHistory } from 'vue-router'

// 示例测试：测试一个简单的计数器组件
describe('Counter', () => {
  it('increments value on click', async () => {
    // 创建一个简单的计数器组件用于测试
    const Counter = {
      template: `
        <div>
          <span>{{ count }}</span>
          <button @click="increment">+</button>
        </div>
      `,
      data() {
        return { count: 0 }
      },
      methods: {
        increment() {
          this.count++
        },
      },
    }

    const wrapper = mount(Counter)

    expect(wrapper.text()).toContain('0')

    await wrapper.find('button').trigger('click')

    expect(wrapper.text()).toContain('1')
  })

  it('works with Pinia store', () => {
    const pinia = createPinia()
    // 这里可以测试你的 Pinia store
    expect(pinia).toBeDefined()
  })

  it('works with Vue Router', () => {
    const router = createRouter({
      history: createWebHistory(),
      routes: [{ path: '/', component: { template: '<div>Home</div>' } }],
    })

    expect(router).toBeDefined()
  })
})
