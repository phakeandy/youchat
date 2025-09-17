# .claude 目录说明

本目录包含 YouChat 项目的 Claude Code 开发规范和配置。

## 文件结构

```
.claude/
├── README.md                           # 本文件，目录说明
├── settings.local.json                 # Claude Code 权限配置
├── development-standards.md            # 详细开发规范文档
└── quick-reference.md                  # 快速参考指南
```

## 文档说明

### development-standards.md
包含完整的开发规范，涵盖：
- TDD 开发流程
- 技术栈要求
- DaisyUI 语义化颜色系统
- 组件规范
- 测试要求
- 项目结构
- 性能优化
- 安全考虑

### quick-reference.md
快速参考指南，包含：
- 核心原则总结
- 颜色系统对照表
- 常用组件类
- 测试模板
- 常用图标
- 必须执行的命令
- 禁止事项
- 最佳实践

### settings.local.json
Claude Code 的权限配置文件，定义了：
- 允许的操作权限
- 工具使用限制
- 安全访问控制

## 使用指南

### 新开发者入门
1. 首先阅读 `quick-reference.md` 了解核心原则
2. 然后阅读 `development-standards.md` 了解详细规范
3. 遵循 TDD 流程进行开发

### 日常开发参考
- 快速查阅颜色对照和组件类 → `quick-reference.md`
- 详细了解特定规范要求 → `development-standards.md`
- 确认权限设置 → `settings.local.json`

## 核心要求摘要

1. **TDD 开发**: 失败测试 → 最小实现 → 小步重构
2. **语义化颜色**: 禁止硬编码，使用 DaisyUI 语义化类
3. **组件化**: 使用 DaisyUI 组件和 Tabler Icons
4. **测试覆盖**: 确保所有功能都有对应测试
5. **代码质量**: 使用 TypeScript 严格模式，遵循 ESLint 规范

## 更新日志

- 2025-09-17: 创建开发规范文档
- 2025-09-17: 添加颜色系统规范
- 2025-09-17: 完善 TDD 和测试要求

---

**注意**: 所有开发者必须严格遵守这些规范，确保项目代码质量和一致性。