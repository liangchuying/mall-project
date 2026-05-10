# API 接口测试文档

## 基本信息

- **基础 URL**: `http://localhost:8080/api`
- **接口文档地址**: `http://localhost:8080/doc.html`
- **认证方式**: JWT Bearer Token

---

## 认证模块

### 1. 用户注册

**接口信息**
- **接口路径**: `/auth/register`
- **请求方式**: `POST`
- **Content-Type**: `application/json`
- **是否需要认证**: 否

**请求参数**

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| username | String | 是 | 用户名 | testuser |
| password | String | 是 | 密码（至少6位） | password123 |
| nickname | String | 否 | 昵称 | 测试用户 |
| phone | String | 否 | 手机号 | 13800138000 |
| email | String | 否 | 邮箱 | test@example.com |

**请求示例**

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "nickname": "测试用户",
    "phone": "13800138000",
    "email": "test@example.com"
  }'
```

**测试用例**

| 用例编号 | 测试场景 | 预期结果 |
|----------|----------|----------|
| TC-001 | 正常注册 | 返回 code 200，注册成功 |
| TC-002 | 用户名重复注册 | 返回 code 500，提示"用户名已存在" |
| TC-003 | 手机号重复注册 | 返回 code 500，提示"手机号已注册" |
| TC-004 | 邮箱重复注册 | 返回 code 500，提示"邮箱已注册" |
| TC-005 | 密码少于6位 | 返回参数校验错误 |
| TC-006 | 用户名为空 | 返回参数校验错误 |
| TC-007 | 手机号格式错误 | 返回参数校验错误 |
| TC-008 | 频繁注册（60秒内超过5次） | 返回 code 429，提示"注册请求过于频繁" |

---

### 2. 用户登录

**接口信息**
- **接口路径**: `/auth/login`
- **请求方式**: `POST`
- **Content-Type**: `application/json`
- **是否需要认证**: 否

**请求参数**

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| username | String | 是 | 用户名 | testuser |
| password | String | 是 | 密码 | password123 |

**请求示例**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

**测试用例**

| 用例编号 | 测试场景 | 预期结果 |
|----------|----------|----------|
| TC-009 | 正常登录 | 返回 code 200，包含 token 和用户信息 |
| TC-010 | 用户名不存在 | 返回 code 500，提示"用户不存在" |
| TC-011 | 密码错误 | 返回 code 500，提示"密码错误" |
| TC-012 | 账号被禁用 | 返回 code 500，提示"账号已被禁用" |
| TC-013 | 用户名为空 | 返回参数校验错误 |
| TC-014 | 密码为空 | 返回参数校验错误 |
| TC-015 | 频繁登录（60秒内超过10次） | 返回 code 429，提示"登录请求过于频繁" |

---

### 3. 获取用户信息

**接口信息**
- **接口路径**: `/auth/info`
- **请求方式**: `GET`
- **是否需要认证**: 是

**请求头**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| Authorization | String | 是 | JWT Token，格式：`Bearer {token}` |

**请求示例**

```bash
curl -X GET http://localhost:8080/api/auth/info \
  -H "Authorization: Bearer {your_token_here}"
```

**测试用例**

| 用例编号 | 测试场景 | 预期结果 |
|----------|----------|----------|
| TC-014 | 正常获取用户信息 | 返回 code 200，包含用户详细信息（密码为空） |
| TC-015 | 未携带 Token | 返回 code 401，提示"未登录或token无效" |
| TC-016 | Token 格式错误 | 返回 code 401，提示"未登录或token无效" |
| TC-017 | Token 已过期 | 返回 code 401，提示"token已过期" |
| TC-018 | Token 无效 | 返回 code 401，提示"token无效" |

---

### 4. 用户登出

**接口信息**
- **接口路径**: `/auth/logout`
- **请求方式**: `POST`
- **是否需要认证**: 是

**请求头**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| Authorization | String | 是 | JWT Token，格式：`Bearer {token}` |

**请求示例**

```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer {your_token_here}"
```

**测试用例**

| 用例编号 | 测试场景 | 预期结果 |
|----------|----------|----------|
| TC-019 | 正常登出 | 返回 code 200，Token 被加入黑名单 |
| TC-020 | 未携带 Token | 返回 code 401，提示"未登录或token无效" |
| TC-021 | 使用已登出的 Token 访问其他接口 | 返回 code 401，提示"token已失效，请重新登录" |

---

## 附录

### HTTP 状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未授权（未登录或 Token 无效） |
| 403 | 无权访问 |
| 429 | 请求过于频繁（限流） |
| 500 | 服务器内部错误 |

### 业务状态码说明

| code | 说明 |
|------|------|
| 200 | 操作成功 |
| 400 | 参数校验失败 |
| 401 | 未授权 |
| 403 | 无权访问 |
| 429 | 请求过于频繁（限流） |
| 500 | 业务异常或系统错误 |

### 依赖服务启动

启动项目前需要启动以下服务：

```bash
# 启动 MySQL
brew services start mysql

# 启动 Redis
brew services start redis

# 启动应用
mvn spring-boot:run
```
