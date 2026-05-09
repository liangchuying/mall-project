# 商城项目

## 项目结构

```
mall-project/
├── mall-backend/    # 后端项目 (Spring Boot)
└── mall-frontend/   # 前端项目 (预留)
```

## 技术栈

### 后端
- Spring Boot 3.1.8
- MySQL 8.x
- MyBatis-Plus 3.5.14
- JWT (io.jsonwebtoken 0.12.3)
- Spring Security

### 前端
- 待定

## 快速开始

### 后端启动

1. 进入后端目录
```bash
cd mall-backend
```

2. 配置数据库
编辑 `src/main/resources/application.yml`，修改数据库连接信息

3. 初始化数据库
```bash
mysql -u root -p < src/main/resources/sql/init.sql
```

4. 启动项目
```bash
mvn spring-boot:run
```

5. 访问接口
```
http://localhost:8080/api/test/hello
```

## 功能模块

- [x] 用户认证授权
- [ ] 商品模块
- [ ] 购物车模块
- [ ] 订单模块
- [ ] 支付模块
- [ ] 营销模块
- [ ] 评价模块
- [ ] 管理后台

详细功能规划请查看 [PLAN.md](./mall-backend/PLAN.md)
