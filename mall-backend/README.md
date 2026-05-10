# 商城系统 (Mall System)

基于 Spring Boot 3 + MySQL + MyBatis-Plus + JWT 的后端商城系统

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.1.8 | 基础框架 |
| MySQL | 8.x | 关系型数据库 |
| Redis | - | 缓存、限流、分布式锁 |
| Redisson | 3.25.0 | Redis 客户端（分布式锁） |
| MyBatis-Plus | 3.5.14 | ORM 框架 |
| Spring Security | - | 安全框架 |
| Spring AOP | - | 面向切面编程（限流、锁切面） |
| JWT | 0.12.3 | 身份认证 |
| Knife4j | 4.4.0 | 接口文档 |
| Hutool | 5.8.25 | 工具类库 |
| Lombok | - | 简化代码 |

## 项目结构

```
mall-backend/
├── pom.xml                                   # Maven 依赖配置文件
├── README.md                                 # 项目说明文档
├── PLAN.md                                   # 功能规划文档
├── src/
│   ├── main/
│   │   ├── java/com/mall/
│   │   │   ├── MallApplication.java           # Spring Boot 主启动类
│   │   │   ├── config/                       # 配置包
│   │   │   │   ├── CorsConfig.java            # 跨域请求配置
│   │   │   │   ├── JwtConfig.java             # JWT 配置属性类
│   │   │   │   ├── Knife4jConfig.java         # Knife4j 接口文档配置
│   │   │   │   ├── MyBatisPlusConfig.java     # MyBatis-Plus 配置（分页等）
│   │   │   │   ├── MyMetaObjectHandler.java   # 自动填充处理器（createTime/updateTime）
│   │   │   │   ├── SecurityConfig.java        # Spring Security 安全配置
│   │   │   │   └── WebConfig.java             # Web 配置
│   │   │   ├── controller/                   # 控制器层（API 接口）
│   │   │   │   ├── AuthController.java        # 认证控制器（注册/登录）
│   │   │   │   ├── TestController.java        # 测试接口
│   │   │   │   └── RedisController.java       # Redis 测试控制器
│   │   │   ├── exception/                    # 异常处理
│   │   │   │   ├── BusinessException.java     # 自定义业务异常
│   │   │   │   └── GlobalExceptionHandler.java # 全局异常处理器
│   │   │   ├── entity/                       # 实体类包
│   │   │   │   ├── BaseEntity.java            # 基础实体类
│   │   │   │   └── User.java                  # 用户实体
│   │   │   ├── mapper/                       # 数据访问层（MyBatis Mapper）
│   │   │   │   └── UserMapper.java           # 用户 Mapper
│   │   │   ├── service/                      # 服务层
│   │   │   │   ├── UserService.java          # 用户服务接口
│   │   │   │   └── impl/                      # 服务实现层
│   │   │   │       └── UserServiceImpl.java   # 用户服务实现
│   │   │   ├── dto/                          # 数据传输对象
│   │   │   │   ├── LoginDTO.java             # 登录请求 DTO
│   │   │   │   └── RegisterDTO.java          # 注册请求 DTO
│   │   │   ├── vo/                           # 视图对象
│   │   │   │   └── LoginResponseVO.java       # 登录响应 VO
│   │   │   ├── interceptor/                  # 拦截器
│   │   │   │   └── JwtInterceptor.java        # JWT 认证拦截器
│   │   │   └── utils/                        # 工具类包
│   │   │       ├── JwtUtil.java              # JWT 生成、解析、验证工具
│   │   │       └── Result.java               # 统一响应结果封装
│   │   └── resources/
│   │       ├── application.yml               # 应用配置文件
│   │       ├── sql/                          # 数据库脚本目录
│   │       │   └── init.sql                  # 数据库初始化脚本
│   │       ├── static/                       # 静态资源目录
│   │       └── templates/                    # 模板文件目录
│   └── test/
│       └── java/com/mall/                   # 测试代码目录
│           └── AuthControllerTest.java       # 认证控制器测试
└── .gitignore                                # Git 忽略文件配置
```

## 文件说明

### 配置文件

| 文件 | 说明 |
|------|------|
| `pom.xml` | Maven 项目配置，定义依赖版本和构建插件 |
| `application.yml` | Spring Boot 应用配置，包含数据库、JWT、服务器端口等 |
| `sql/init.sql` | 数据库初始化脚本，包含建库和建表语句 |

### 核心类文件

| 文件 | 说明 |
|------|------|
| `MallApplication.java` | 程序入口，包含 `@SpringBootApplication` 和 `@MapperScan` 注解 |
| `BaseEntity.java` | 所有实体类的基类，提供通用字段和逻辑删除功能 |
| `User.java` | 用户实体类，继承 BaseEntity |

### 配置类

| 文件 | 说明 |
|------|------|
| `CorsConfig.java` | 配置跨域请求支持，允许所有来源、方法和请求头 |
| `JwtConfig.java` | 使用 `@ConfigurationProperties` 读取 JWT 配置（secret、expiration） |
| `Knife4jConfig.java` | Knife4j 接口文档配置，配置 API 文档信息和 JWT 认证 |
| `MyBatisPlusConfig.java` | 配置 MyBatis-Plus 拦截器，支持 MySQL 分页 |
| `MyMetaObjectHandler.java` | 实现 `MetaObjectHandler`，自动填充创建时间和更新时间 |
| `SecurityConfig.java` | Spring Security 安全配置，配置认证规则和过滤器 |
| `WebConfig.java` | Web 配置，注册拦截器等 |
| `RedisConfig.java` | Redis 配置类，配置序列化和缓存管理器 |
| `RedisProperties.java` | Redis 配置属性类 |
| `RedissonConfig.java` | Redisson 配置类，配置分布式锁客户端 |

### 工具类

| 文件 | 说明 |
|------|------|
| `Result.java` | 统一返回格式 `Result<T>`，包含 code、message、data 字段 |
| `JwtUtil.java` | 提供 JWT 生成、解析、过期判断等方法 |
| `RedisUtil.java` | Redis 操作工具类，封装常用的 Redis 操作 |

### 异常处理

| 文件 | 说明 |
|------|------|
| `BusinessException.java` | 自定义业务异常类 |
| `GlobalExceptionHandler.java` | 全局异常处理器，统一处理各类异常 |

### 数据传输对象（DTO）

| 文件 | 说明 |
|------|------|
| `LoginDTO.java` | 登录请求参数（用户名/密码） |
| `RegisterDTO.java` | 注册请求参数（用户名/密码/手机号） |

### 视图对象（VO）

| 文件 | 说明 |
|------|------|
| `LoginResponseVO.java` | 登录响应（token、用户信息） |

### 数据访问层（Mapper）

| 文件 | 说明 |
|------|------|
| `UserMapper.java` | 用户数据访问接口，继承 BaseMapper |

### 服务层（Service）

| 文件 | 说明 |
|------|------|
| `UserService.java` | 用户服务接口 |
| `UserServiceImpl.java` | 用户服务实现类 |

### 拦截器

| 文件 | 说明 |
|------|------|
| `JwtInterceptor.java` | JWT 认证拦截器，验证 Token 有效性 |

## 快速开始

### 1. 初始化数据库

执行 SQL 脚本文件：`src/main/resources/sql/init.sql`

```bash
mysql -u root -p < src/main/resources/sql/init.sql
```

或在 MySQL 客户端中执行：

```sql
source /path/to/mall-system/src/main/resources/sql/init.sql;
```

### 2. 修改配置

编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mall_system?...
    username: your_username
    password: your_password

jwt:
  secret: your-secret-key-change-in-production
```

#### 环境配置

项目支持多环境配置，默认使用 `dev` 环境。

**开发环境** (`application-dev.yml`)：
```yaml
spring:
  profiles:
    active: dev
```

**生产环境** (`application-prod.yml`)：
```yaml
spring:
  profiles:
    active: prod
```

通过环境变量切换：
```bash
# 开发环境
java -jar mall-system.jar --spring.profiles.active=dev

# 生产环境
java -jar mall-system.jar --spring.profiles.active=prod
```

#### JWT 密钥配置

**生产环境必须修改 JWT 密钥！**

生成随机密钥：
```bash
# 运行密钥生成工具
java -cp target/classes com.mall.utils.SecretKeyGenerator
```

或使用环境变量（推荐）：
```bash
export JWT_SECRET=your-generated-secret-key
```

配置文件方式：
```yaml
jwt:
  secret: ${JWT_SECRET:your-default-secret-key}
```

**安全建议**：
- 生产环境使用至少 256 位随机密钥
- 不要在代码中硬编码密钥
- 使用环境变量或密钥管理服务
- 定期更换密钥

#### 日志配置

项目已集成 Logback 日志框架，支持多环境日志配置。

**开发环境**：
- 日志级别：DEBUG
- 输出目标：控制台
- 便于开发调试

**生产环境**：
- 日志级别：INFO
- 输出目标：控制台 + 文件
  - `mall-system-info.log` - INFO 级别日志
  - `mall-system-error.log` - ERROR 级别日志
- 日志文件最大 100MB，保留 30 天
- 使用异步日志提升性能

**日志路径配置**：
```yaml
# 生产环境通过环境变量配置
export LOG_PATH=/var/log/mall-system
```

或配置文件方式：
```yaml
logging:
  file:
    path: ${LOG_PATH:/var/log/mall-system}
```

**日志格式**：
```
2026-05-09 22:30:15.123 [http-nio-8080-exec-1] INFO  c.m.controller.AuthController - 用户登录成功
```

### 3. 启动项目

```bash
cd mall-system
mvn spring-boot:run
```

或使用 IDE 运行 `MallApplication` 主类

### 4. 测试接口

#### 测试接口
访问 `http://localhost:8080/api/test/hello`

预期返回：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": "商城系统启动成功！"
}
```

#### 认证接口
- **注册**: `POST /api/auth/register`
  ```json
  {
    "username": "testuser",
    "password": "password123",
    "phone": "13800138000"
  }
  ```
- **登录**: `POST /api/auth/login`
  ```json
  {
    "username": "testuser",
    "password": "password123"
  }
  ```
- **登出**: `POST /api/auth/logout` (需携带 JWT Token)
- **获取用户信息**: `GET /api/auth/info` (需携带 JWT Token)

#### 接口文档
项目已集成 Knife4j 接口文档，启动后访问：
```
http://localhost:8080/doc.html
```

**功能特性**：
- 在线查看所有 API 接口
- 支持在线调试接口
- 支持 JWT Token 认证
- 接口参数和响应示例
- 接口分组管理

**使用说明**：
1. 点击右上角「Authorize」按钮
2. 输入 JWT Token（登录接口返回的 token）
3. 点击「Authorize」确认
4. 所有需要认证的接口会自动携带 Token

## 已实现功能

- [x] 基础框架配置
- [x] Spring Security 安全配置
- [x] JWT 身份认证
- [x] Redis 缓存集成
  - [x] 用户信息缓存（减少数据库查询）
  - [x] Token 黑名单（登出失效）
  - [x] 接口限流（防刷）
- [x] Redisson 分布式锁
  - [x] 注解方式使用
  - [x] 工具类方式使用
  - [x] SpEL 表达式支持
- [x] Spring AOP 切面（限流、锁拦截）
- [x] MyBatis-Plus 配置（分页、逻辑删除、自动填充）
- [x] 统一响应结果封装
- [x] 跨域配置
- [x] Knife4j 接口文档
- [x] 用户注册/登录接口
- [x] 用户登出接口
- [x] 全局异常处理
- [x] 参数校验（@Valid/@Validated）

## 配置说明

### MyBatis-Plus 配置

- **分页插件**：已配置 MySQL 分页支持
- **逻辑删除**：`deleted=1` 表示已删除，`deleted=0` 表示未删除
- **自动填充**：插入时填充 `createTime`，插入/更新时填充 `updateTime`

### JWT 配置

- **密钥**：建议生产环境使用至少 256 位的随机密钥
- **过期时间**：默认 24 小时（86400000 毫秒）

### 服务器配置

- **端口**：8080
- **上下文路径**：`/api`

### 分布式锁使用

**注解方式**

```java
@DistributedLock(key = "#user.id", waitTime = 5, leaseTime = 30)
public void updateUser(User user) {
    // 业务逻辑
}
```

**参数说明**
- `key`: 锁的 key，支持 SpEL 表达式
- `waitTime`: 等待获取锁的时间（秒）
- `leaseTime`: 锁的持有时间（秒）
- `prefix`: 锁的前缀，默认为 `lock:`

**工具类方式**

```java
@Autowired
private DistributedLockUtil lockUtil;

if (lockUtil.tryLock("user:update:1", 5, 30)) {
    try {
        // 业务逻辑
    } finally {
        lockUtil.unlock("user:update:1");
    }
}
```

### Redis 缓存应用

**用户信息缓存**
- 缓存用户信息 30 分钟，减少数据库查询
- 支持按用户 ID 和用户名两种方式查询

**Token 黑名单**
- 用户登出时将 Token 加入 Redis 黑名单
- 黑名单中的 Token 在过期前无法使用
- 自动根据 Token 过期时间设置黑名单过期时间

**接口限流**
- 基于 IP + 接口路径进行限流
- 使用 Redis 计数器实现分布式限流
- 登录接口：60 秒内最多 10 次请求
- 注册接口：60 秒内最多 5 次请求
- 超过限制返回 429 状态码

**分布式锁**
- 基于 Redisson 实现分布式锁
- 支持注解方式使用，自动加锁和解锁
- 支持自定义锁等待时间和持有时间
- 支持 SpEL 表达式动态生成锁 key
- 获取锁失败返回 409 状态码

**缓存 Key 规则**
- 用户信息: `user:{userId}`
- 用户名查询: `user:username:{username}`
- Token 黑名单: `token:blacklist:{token}`
- 接口限流: `rate_limit:{ip}:{uri}`
- 分布式锁: `lock:{key}`

## 后续开发

详细功能规划请查看 [PLAN.md](./PLAN.md)

### 待开发功能

#### 用户模块
- [ ] Token 刷新机制
- [ ] 登出（Redis 黑名单）
- [ ] 密码重置
- [ ] 查看和修改个人信息
- [ ] 收货地址管理
- [ ] 用户成长系统（等级、积分）

#### 商品模块
- [ ] 商品分类管理
- [ ] SPU/SKU 商品管理
- [ ] 库存管理
- [ ] 商品检索
- [ ] 商品展示

#### 购物车模块
- [ ] 添加/删除/修改购物车商品
- [ ] 选择/全选功能
- [ ] 价格计算

#### 订单模块
- [ ] 创建订单
- [ ] 订单状态流转
- [ ] 订单查询
- [ ] 订单操作

#### 支付模块
- [ ] 微信支付集成
- [ ] 支付宝支付集成
- [ ] 支付回调处理

#### 营销模块
- [ ] 优惠券系统
- [ ] 促销活动（秒杀、拼团、折扣等）

#### 评价模块
- [ ] 订单评价
- [ ] 评价列表和回复

#### 管理后台
- [ ] 用户管理
- [ ] 商品管理
- [ ] 订单管理
- [ ] 营销管理
- [ ] 数据统计

## 注意事项

- [x] 生产环境请修改 JWT 密钥
- [x] 已添加日志配置（Logback）
- [x] 已添加接口文档（Knife4j）
- [x] 已添加全局异常处理
- [x] 已添加参数校验（@Valid/@Validated）
- [x] 已集成 Redis 缓存
- [x] 已添加接口限流和防刷
- [x] 已添加分布式锁
- [ ] 建议添加消息队列（RabbitMQ/RocketMQ）
- [ ] 建议添加接口幂等性处理
