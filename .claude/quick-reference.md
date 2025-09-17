# YouChat 开发快速参考

## 核心原则
✅ **TDD**: 失败测试 → 最小实现 → 重构
✅ **语义化颜色**: 禁止 `text-gray-500`，使用 `text-base-content/60`
✅ **DaisyUI**: 使用 `card`, `btn`, `btn-primary` 等组件类
✅ **Tabler Icons**: 使用 `i-tabler-[icon-name]`

## 颜色系统对照表

| 禁止使用 | 推荐使用 | 用途 |
|---------|---------|------|
| `text-gray-800` | `text-primary-content` | 主标题 |
| `text-gray-600` | `text-base-content opacity-80` | 副标题 |
| `text-gray-500` | `text-base-content/60` | 描述文字 |
| `bg-gray-100` | `bg-base-100` | 卡片背景 |
| `bg-blue-50` | `from-primary/5 to-secondary/10` | 页面背景 |

## 常用组件类

### Card 组件
```html
<div class="card bg-base-100 shadow-lg">
  <div class="card-body">
    <h3 class="card-title text-primary-content">标题</h3>
    <p class="text-base-content/70">内容</p>
  </div>
</div>
```

### Button 组件
```html
<button class="btn btn-primary btn-lg">
  <i-tabler-login class="mr-2" />
  登录
</button>
```

## 测试模板

### 组件测试文件结构
```typescript
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import Component from './Component.vue'

describe('Component', () => {
  it('should work', () => {
    const wrapper = mount(Component)
    expect(wrapper.text()).toContain('Expected Text')
  })
})
```

## 常用图标

| 功能 | 图标 |
|------|------|
| 消息 | `i-tabler-message-circle` |
| 用户 | `i-tabler-users` |
| 登录 | `i-tabler-login` |
| 注册 | `i-tabler-user-plus` |
| 上传 | `i-tabler-upload` |

## 响应式网格
```html
<div class="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
  <!-- 内容 -->
</div>
```

## 必须执行的命令
```bash
pnpm test          # 运行测试
pnpm lint          # 代码检查
pnpm format        # 代码格式化
pnpm build         # 构建项目
```

## 禁止事项
❌ 使用硬编码颜色类（如 `text-gray-500`）
❌ 跳过测试直接写代码
❌ 不使用语义化 HTML 标签
❌ 忽略 TypeScript 类型检查

## 最佳实践
✅ 先写测试，再写实现
✅ 使用语义化颜色和组件
✅ 保持代码简洁可读
✅ 定期运行测试验证
✅ 遵循 TDD 小步重构

---

*详细规范请参考 `development-standards.md`*