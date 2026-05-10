# AuthModal 组件文档

## 概述

AuthModal 是一个功能完整的用户认证弹窗组件，支持登录、注册和忘记密码三个功能模块。

## 特性

### UX 设计
- ✅ Tab 切换动画，平滑过渡
- ✅ Blur 时实时表单验证
- ✅ 错误提示和辅助文字
- ✅ 加载状态反馈
- ✅ 键盘无障碍支持（ESC 关闭、Tab 导航）
- ✅ 背景滚动锁定
- ✅ 点击遮罩关闭
- ✅ 自动聚焦管理

### 表单验证
- ✅ 用户名：3-20字符，仅字母数字下划线
- ✅ 密码：6-20字符
- ✅ 昵称：最多30字符
- ✅ 手机号：中国手机号格式
- ✅ 邮箱：标准邮箱格式
- ✅ 验证码：6位数字

### 视觉设计
- ✅ 玻璃拟态风格（glass-card）
- ✅ 响应式设计
- ✅ 支持移动端和桌面端
- ✅ 符合 Swiss Modernism 2.0 设计规范

## API

### Props

| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| isOpen | boolean | false | 控制弹窗显示/隐藏 |
| onClose | () => void | - | 关闭弹窗回调 |
| onSuccess | () => void | - | 认证成功回调 |

## 使用示例

### 基础用法

```tsx
import { useState } from 'react';
import { AuthModal } from '@/components';

function App() {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <>
      <button onClick={() => setIsOpen(true)}>打开登录</button>
      <AuthModal
        isOpen={isOpen}
        onClose={() => setIsOpen(false)}
        onSuccess={() => console.log('认证成功')}
      />
    </>
  );
}
```

### 与状态管理集成

```tsx
import { useState } from 'react';
import { AuthModal } from '@/components';
import { useAuthStore } from '@/store/userStore';

function App() {
  const [isOpen, setIsOpen] = useState(false);
  const { login } = useAuthStore();

  return (
    <>
      <button onClick={() => setIsOpen(true)}>打开登录</button>
      <AuthModal
        isOpen={isOpen}
        onClose={() => setIsOpen(false)}
        onSuccess={() => {
          // 更新用户状态
          login(user);
          setIsOpen(false);
        }}
      />
    </>
  );
}
```

## 组件结构

```
AuthModal
├── Modal (模态框容器)
│   ├── Header (Tab 切换)
│   │   ├── 登录
│   │   ├── 注册
│   │   └── 忘记密码
│   └── Content (表单内容)
│       ├── LoginForm
│       │   ├── 用户名输入
│       │   ├── 密码输入
│       │   ├── 记住我选项
│       │   └── 登录按钮
│       ├── RegisterForm
│       │   ├── 用户名输入
│       │   ├── 密码输入
│       │   ├── 昵称输入
│       │   ├── 手机号输入
│       │   ├── 邮箱输入
│       │   └── 注册按钮
│       └── ForgotPasswordForm
│           ├── 手机号输入
│           ├── 验证码输入
│           │   └── 发送验证码按钮
│           ├── 新密码输入
│           └── 重置密码按钮
└── Footer
    └── 切换登录/注册提示
```

## 验证规则

| 字段 | 规则 | 错误提示 |
|------|------|----------|
| 用户名 | 3-20字符，仅字母数字下划线 | 用户名至少3个字符 |
| 密码 | 6-20字符 | 密码至少6个字符 |
| 昵称 | 最多30字符 | 昵称最多30个字符 |
| 手机号 | 1[3-9]xxxxxxxxx | 请输入有效的手机号 |
| 邮箱 | 标准邮箱格式 | 请输入有效的邮箱地址 |
| 验证码 | 6位数字 | 验证码必须是6位数字 |

## 依赖项

- `react-dom` - createPortal
- `@/components/Button` - 按钮组件
- `@/components/Input` - 输入框组件
- `@/components/Modal` - 模态框组件
- `@/services/modules/auth` - 认证 API

## 注意事项

1. **API 集成**：当前组件已集成 `login` 和 `register` API，但重置密码 API 需要根据实际接口完善
2. **验证码发送**：发送验证码功能已实现 UI，需连接实际 API
3. **记住我**：记住我功能已实现 UI，需根据后端实现持久化逻辑
4. **Token 处理**：成功登录后需要处理 Token 存储和后续请求拦截器

## 访问演示

访问 `/demo` 路由查看完整的认证弹窗演示页面。
