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

## 密码管理模块

### 1. 发送重置密码验证码

**接口信息**
- **接口路径**: `/password/send-code`
- **请求方式**: `POST`
- **Content-Type**: `application/json`
- **是否需要认证**: 否

**请求参数**

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| phone | String | 是 | 手机号 | 13800138000 |

**请求示例**

```bash
curl -X POST http://localhost:8080/api/password/send-code \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "13800138000"
  }'
```

**测试用例**

| 用例编号 | 测试场景 | 预期结果 |
|----------|----------|----------|
| TC-022 | 正常发送验证码 | 返回 code 200，验证码发送成功 |
| TC-023 | 手机号未注册 | 返回 code 500，提示"手机号未注册" |
| TC-024 | 手机号格式错误 | 返回参数校验错误 |
| TC-025 | 手机号为空 | 返回参数校验错误 |

---

### 2. 重置密码

**接口信息**
- **接口路径**: `/password/reset`
- **请求方式**: `POST`
- **Content-Type**: `application/json`
- **是否需要认证**: 否

**请求参数**

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| phone | String | 是 | 手机号 | 13800138000 |
| code | String | 是 | 验证码（6位） | 123456 |
| newPassword | String | 是 | 新密码（至少6位） | newpassword123 |

**请求示例**

```bash
curl -X POST http://localhost:8080/api/password/reset \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "13800138000",
    "code": "123456",
    "newPassword": "newpassword123"
  }'
```

**测试用例**

| 用例编号 | 测试场景 | 预期结果 |
|----------|----------|----------|
| TC-026 | 正常重置密码 | 返回 code 200，密码重置成功 |
| TC-027 | 验证码错误 | 返回 code 500，提示"验证码错误" |
| TC-028 | 验证码已过期 | 返回 code 500，提示"验证码已过期或不存在" |
| TC-029 | 手机号未注册 | 返回 code 500，提示"用户不存在" |
| TC-030 | 新密码少于6位 | 返回参数校验错误 |
| TC-031 | 验证码位数错误 | 返回参数校验错误 |

---

### 3. 修改密码

**接口信息**
- **接口路径**: `/password/change`
- **请求方式**: `POST`
- **Content-Type**: `application/json`
- **是否需要认证**: 是

**请求参数**

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| oldPassword | String | 是 | 旧密码 | oldpassword123 |
| newPassword | String | 是 | 新密码（至少6位） | newpassword123 |

**请求示例**

```bash
curl -X POST http://localhost:8080/api/password/change \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {your_token_here}" \
  -d '{
    "oldPassword": "oldpassword123",
    "newPassword": "newpassword123"
  }'
```

**测试用例**

| 用例编号 | 测试场景 | 预期结果 |
|----------|----------|----------|
| TC-032 | 正常修改密码 | 返回 code 200，密码修改成功 |
| TC-033 | 旧密码错误 | 返回 code 500，提示"旧密码错误" |
| TC-034 | 新旧密码相同 | 返回 code 500，提示"新密码不能与旧密码相同" |
| TC-035 | 旧密码为空 | 返回参数校验错误 |
| TC-036 | 新密码为空 | 返回参数校验错误 |
| TC-037 | 新密码少于6位 | 返回参数校验错误 |
| TC-038 | 未携带 Token | 返回 code 401，提示"未登录或token无效" |
| TC-039 | Token 已过期 | 返回 code 401，提示"token已过期" |

---

## 商品分类模块

### 1. 创建商品分类

**接口信息**
- **接口路径**: `/category`
- **请求方式**: `POST`
- **Content-Type**: `application/json`
- **是否需要认证**: 否

**请求参数**

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| name | String | 是 | 分类名称 | 手机 |
| parentId | Long | 是 | 父分类ID，0表示顶级分类 | 0 |
| icon | String | 否 | 分类图标 | icon-phone.png |
| sort | Integer | 否 | 分类排序号 | 1 |
| status | Integer | 否 | 分类状态：0=禁用，1=启用 | 1 |

**请求示例**

```bash
curl -X POST http://localhost:8080/api/category \
  -H "Content-Type: application/json" \
  -d '{
    "name": "手机",
    "parentId": 0,
    "icon": "icon-phone.png",
    "sort": 1,
    "status": 1
  }'
```

**测试用例**

| 用例编号 | 测试场景 | 预期结果 |
|----------|----------|----------|
| TC-040 | 正常创建顶级分类 | 返回 code 200，返回分类ID |
| TC-041 | 正常创建二级分类 | 返回 code 200，返回分类ID |
| TC-042 | 分类名称为空 | 返回参数校验错误 |
| TC-043 | 父分类ID为空 | 返回参数校验错误 |

---

### 2. 更新商品分类

**接口信息**
- **接口路径**: `/category`
- **请求方式**: `PUT`
- **Content-Type**: `application/json`
- **是否需要认证**: 否

**请求参数**

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| id | Long | 是 | 分类ID | 1 |
| name | String | 否 | 分类名称 | 手机 |
| icon | String | 否 | 分类图标 | icon-phone.png |
| sort | Integer | 否 | 分类排序号 | 1 |
| status | Integer | 否 | 分类状态：0=禁用，1=启用 | 1 |

**请求示例**

```bash
curl -X PUT http://localhost:8080/api/category \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "name": "智能手机",
    "icon": "icon-phone.png",
    "sort": 2,
    "status": 1
  }'
```

**测试用例**

| 用例编号 | 测试场景 | 预期结果 |
|----------|----------|----------|
| TC-044 | 正常更新分类 | 返回 code 200，更新成功 |
| TC-045 | 分类ID不存在 | 返回 code 500，提示"分类不存在" |
| TC-046 | 分类ID为空 | 返回参数校验错误 |
| TC-047 | 排序号小于0 | 返回参数校验错误 |

---

### 3. 删除商品分类

**接口信息**
- **接口路径**: `/category/{id}`
- **请求方式**: `DELETE`
- **是否需要认证**: 否

**请求示例**

```bash
curl -X DELETE http://localhost:8080/api/category/1
```

**测试用例**

| 用例编号 | 测试场景 | 预期结果 |
|----------|----------|----------|
| TC-048 | 正常删除分类 | 返回 code 200，删除成功 |
| TC-049 | 分类ID不存在 | 返回 code 500，提示"分类不存在" |
| TC-050 | 分类下有子分类 | 返回 code 500，提示"该分类下有子分类，无法删除" |

---

### 4. 获取分类详情

**接口信息**
- **接口路径**: `/category/{id}`
- **请求方式**: `GET`
- **是否需要认证**: 否

**请求示例**

```bash
curl -X GET http://localhost:8080/api/category/1
```

**测试用例**

| 用例编号 | 测试场景 | 预期结果 |
|----------|----------|----------|
| TC-051 | 正常获取分类详情 | 返回 code 200，包含分类详细信息 |
| TC-052 | 分类ID不存在 | 返回 code 500，提示"分类不存在" |

---

### 5. 获取分类树

**接口信息**
- **接口路径**: `/category/tree`
- **请求方式**: `GET`
- **是否需要认证**: 否

**请求示例**

```bash
curl -X GET http://localhost:8080/api/category/tree
```

**测试用例**

| 用例编号 | 测试场景 | 预期结果 |
|----------|----------|----------|
| TC-053 | 获取分类树 | 返回 code 200，包含树形结构数据 |
| TC-054 | 无分类数据 | 返回空数组或默认数据 |

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
