# YouChat 项目开发规范

## 核心要求

### 1. 测试驱动开发 (TDD)
- **必须**先写失败的测试 → 最小实现 → 小步重构
- 使用 Vitest + Vue Test Utils 进行单元测试
- 使用 Playwright 进行 E2E 测试
- 每次变更后确保所有测试通过

### 2. 技术栈
- **前端**: Vue 3 + TypeScript + Tailwind CSS + DaisyUI
- **图标**: unplugin-icons (Tabler Icons)
- **状态管理**: Pinia
- **路由**: Vue Router
- **测试**: Vitest + Vue Test Utils + Playwright

### 3. 设计系统与颜色规范

#### DaisyUI 语义化颜色系统
- **必须使用语义化颜色类**，避免硬编码颜色
- **禁止使用**: `text-gray-500`, `text-gray-600`, `text-gray-800` 等
- **推荐使用**:
  - `text-primary`, `text-primary-content`
  - `text-secondary`, `text-secondary-content`
  - `text-accent`, `text-accent-content`
  - `text-base-content`, `text-base-content/[opacity]`
  - `bg-primary`, `bg-secondary`, `bg-accent`
  - `bg-base-100`, `bg-base-200`, etc.

#### 透明度使用
- 使用 `/50`, `/60`, `/70`, `/80` 等透明度来调整文字深浅
- 例如: `text-base-content/60`, `text-base-content/80`

#### 背景渐变
- 使用语义化颜色的透明度变体: `from-primary/5 to-secondary/10`

### 4. 组件规范

#### DaisyUI 组件使用
- **Card组件**: 使用 `card`, `card-body`, `card-title`, `card-actions`
- **Button组件**: 使用 `btn`, `btn-primary`, `btn-secondary`, `btn-lg`
- **响应式设计**: 使用 `grid`, `md:grid-cols-2`, `lg:grid-cols-4`

#### 图标使用
- 使用 unplugin-icons 的 Tabler Icons
- 图标命名: `i-tabler-[icon-name]`
- 例如: `i-tabler-message-circle`, `i-tabler-login`

### 5. 代码质量

#### TypeScript
- 严格类型检查
- 组件使用 `<script setup lang="ts">`
- 正确的类型定义

#### 测试覆盖率
- 组件必须有对应的测试文件
- 测试文件命名: `ComponentName.spec.ts`
- 测试必须覆盖主要功能和边界情况

#### 代码风格
- 使用 ESLint + Prettier
- 遵循 Vue 3 组合式 API 最佳实践
- 组件命名使用 PascalCase

### 6. 项目结构

```
frontend/src/
├── views/          # 页面组件
├── components/     # 可复用组件
├── stores/         # Pinia 状态管理
├── router/         # 路由配置
├── assets/         # 静态资源
├── __tests__/      # 测试文件
└── main.ts         # 应用入口
```

### 7. 开发流程

#### TDD 步骤
1. **写失败的测试** - 验证需求理解正确
2. **最小实现** - 让测试通过的最简单代码
3. **小步重构** - 改进代码质量和结构
4. **验证测试** - 确保所有测试通过

#### 提交规范
- 使用清晰的提交信息
- 确保测试通过再提交
- 使用 `pnpm test` 运行测试
- 使用 `pnpm lint` 检查代码质量

### 8. 性能优化

#### 图片和资源
- 使用适当的图片格式
- 优化资源大小
- 使用 CDN 加速

#### 代码分割
- 使用路由级别的代码分割
- 按需加载组件

### 9. 可访问性

#### 语义化 HTML
- 使用正确的 HTML5 标签
- 提供适当的 ARIA 标签
- 确保键盘导航可用

#### 颜色对比度
- 使用语义化颜色确保对比度
- 支持暗色模式

### 10. 安全考虑

#### 输入验证
- 前端表单验证
- 防止 XSS 攻击
- 使用 HTTPS

#### 状态管理
- 敏感信息不在前端存储
- 使用安全的认证方式

## 工具和命令

### 开发命令
- `pnpm dev` - 启动开发服务器
- `pnpm test` - 运行单元测试
- `pnpm test:e2e` - 运行 E2E 测试
- `pnpm build` - 构建生产版本
- `pnpm lint` - 代码检查
- `pnpm format` - 代码格式化

### 测试命令
- `pnpm test:run` - 运行测试一次
- `pnpm test:coverage` - 测试覆盖率
- `pnpm test:ui` - 测试 UI 界面

## 注意事项

1. **严格遵循 TDD 流程**
2. **使用语义化颜色，禁止硬编码**
3. **确保测试覆盖率**
4. **保持代码简洁和可维护**
5. **遵循 DaisyUI 设计系统**
6. **使用 TypeScript 严格模式**
7. **定期运行测试确保质量**

## 版本要求

- Node.js: ^20.19.0 || >=22.12.0
- Vue: ^3.5.18
- DaisyUI: ^5.1.12
- Tailwind CSS: ^4.1.13

---

**最后更新**: 2025-09-17
**项目**: YouChat - 简易在线互动聊天系统