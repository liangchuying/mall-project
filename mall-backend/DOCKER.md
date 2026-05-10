# Docker 部署指南

本指南介绍如何使用 Docker 和 Docker Compose 部署商城系统后端。

## 前置要求

- Docker 20.10+
- Docker Compose 2.0+
- 至少 4GB 可用内存

## 快速开始

### 1. 复制环境变量配置

```bash
cp .env.example .env
```

### 2. 修改环境变量（重要！）

编辑 `.env` 文件，修改以下关键配置：

```bash
# 修改数据库密码（生产环境必须修改）
MYSQL_ROOT_PASSWORD=your_strong_password
MYSQL_PASSWORD=your_strong_password
MYSQL_USER=your_database_user

# 修改 Redis 密码
REDIS_PASSWORD=your_redis_password

# 修改 JWT 密钥（生产环境必须使用强随机密钥）
JWT_SECRET=your-production-secret-key-at-least-256-bits-long
```

**生成安全的 JWT 密钥：**
```bash
openssl rand -base64 32
```

### 3. 构建并启动所有服务

```bash
# 构建并启动所有服务
docker-compose up -d

# 查看日志
docker-compose logs -f app
```

### 4. 初始化数据库

首次启动时，MySQL 会自动执行 `src/main/resources/sql` 目录下的 SQL 脚本。

如果需要手动初始化：

```bash
# 进入 MySQL 容器
docker-compose exec mysql mysql -uroot -p

# 在 MySQL 客户端中
source /docker-entrypoint-initdb.d/init.sql;
```

### 5. 验证部署

```bash
# 检查服务状态
docker-compose ps

# 检查应用健康状态
curl http://localhost:8080/api/actuator/health

# 测试接口
curl http://localhost:8080/api/test/hello
```

## 服务说明

| 服务 | 端口 | 说明 |
|------|------|------|
| app | 8080 | Spring Boot 应用 |
| mysql | 3307 | MySQL 数据库（默认 3306 端口可能被占用） |
| redis | 6379 | Redis 缓存 |
| rocketmq-namesrv | 9876 | RocketMQ NameServer（ARM64/Mac 已禁用） |
| rocketmq-broker | 10911, 10909, 10912 | RocketMQ Broker（ARM64/Mac 已禁用） |
| rocketmq-console | 8180 | RocketMQ 控制台（ARM64/Mac 已禁用） |

**注意：** RocketMQ 在 Apple Silicon (ARM64) Mac 上存在兼容性问题，默认已禁用。应用使用 Mock 实现，不影响核心功能。如需使用 RocketMQ，请在 x86_64 环境下运行并取消注释 docker-compose.yml 中的 RocketMQ 相关服务。

## 常用命令

### 服务管理

```bash
# 启动所有服务
docker-compose up -d

# 停止所有服务
docker-compose stop

# 重启所有服务
docker-compose restart

# 停止并删除所有容器
docker-compose down

# 停止并删除所有容器、网络、卷
docker-compose down -v

# 重新构建并启动
docker-compose up -d --build
```

### 日志查看

```bash
# 查看所有服务日志
docker-compose logs

# 查看指定服务日志
docker-compose logs -f app
docker-compose logs -f mysql
docker-compose logs -f redis

# 查看最近 100 行日志
docker-compose logs --tail=100 app
```

### 容器操作

```bash
# 进入应用容器
docker-compose exec app sh

# 进入 MySQL 容器
docker-compose exec mysql mysql -uroot -p

# 进入 Redis 容器
docker-compose exec redis redis-cli -a your_redis_password

# 查看容器资源使用
docker stats
```

### 数据备份

```bash
# 备份 MySQL 数据
docker-compose exec mysql mysqldump -uroot -p mall_system > backup_$(date +%Y%m%d).sql

# 恢复 MySQL 数据
docker-compose exec -T mysql mysql -uroot -p mall_system < backup_20240101.sql

# 备份 Redis 数据
docker-compose exec redis redis-cli -a your_redis_password BGSAVE
cp docker-compose.yml docker-compose.yml.backup
docker cp mall-redis:/data/dump.rdb redis_backup_$(date +%Y%m%d).rdb
```

## 生产环境部署

### 1. 使用生产环境配置

```bash
# .env 文件中设置
SPRING_PROFILES_ACTIVE=prod
```

### 2. 配置外部数据存储

建议生产环境使用外部存储而非 Docker 卷：

```bash
# 修改 docker-compose.yml 中的 volumes 配置
volumes:
  mysql-data:
    driver: local
    driver_opts:
      type: none
      device: /path/to/mysql/data
      o: bind
```

### 3. 配置反向代理（Nginx）

示例 Nginx 配置：

```nginx
upstream mall_backend {
    server 127.0.0.1:8080;
}

server {
    listen 80;
    server_name your-domain.com;

    location /api/ {
        proxy_pass http://mall_backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### 4. 配置 HTTPS

使用 Let's Encrypt 获取免费 SSL 证书：

```bash
# 安装 certbot
apt-get install certbot python3-certbot-nginx

# 获取证书
certbot --nginx -d your-domain.com
```

### 5. 配置防火墙

```bash
# 仅开放必要端口
ufw allow 22/tcp    # SSH
ufw allow 80/tcp    # HTTP
ufw allow 443/tcp   # HTTPS
ufw enable
```

## 监控和维护

### 查看应用日志

```bash
# 实时日志
docker-compose logs -f app

# 过滤错误日志
docker-compose logs app | grep ERROR
```

### 查看资源使用

```bash
# 容器资源使用
docker stats

# 磁盘使用
docker system df

# 清理未使用的资源
docker system prune -a
```

### 健康检查

```bash
# 检查所有服务健康状态
docker-compose ps

# 检查应用健康
curl http://localhost:8080/api/actuator/health
```

## 故障排查

### 应用无法启动

```bash
# 查看应用日志
docker-compose logs app

# 检查数据库连接
docker-compose exec mysql mysql -uroot -p -e "SELECT 1"

# 检查 Redis 连接
docker-compose exec redis redis-cli ping
```

### 数据库连接失败

1. 检查 MySQL 是否启动：`docker-compose ps mysql`
2. 检查数据库用户和密码配置
3. 查看数据库日志：`docker-compose logs mysql`

### Redis 连接失败

1. 检查 Redis 是否启动：`docker-compose ps redis`
2. 检查 Redis 密码配置
3. 测试连接：`docker-compose exec redis redis-cli ping`

### 端口冲突

如果端口被占用，修改 `.env` 文件中的端口配置：

```bash
APP_PORT=8081
MYSQL_PORT=3307
REDIS_PORT=6380
```

### RocketMQ 在 Apple Silicon/Mac 上无法启动

**问题原因：**
- RocketMQ 官方镜像仅支持 x86_64 (AMD64) 架构
- Apple Silicon (ARM64) Mac 运行 x86_64 镜像会存在兼容性问题

**解决方案：**
1. **开发环境：** 使用 MQ Mock 实现（已默认配置），无需 RocketMQ
2. **生产环境：** 在 x86_64 Linux 服务器上部署 RocketMQ

**启用 RocketMQ（仅 x86_64 环境）：**
1. 取消注释 `docker-compose.yml` 中的 RocketMQ 相关服务
2. 修改 `SPRING_AUTOCONFIGURE_EXCLUDE` 环境变量，移除 RocketMQ 排除配置
3. 重启服务：`docker-compose up -d`

## 性能优化

### 调整 JVM 参数

修改 `Dockerfile` 中的 JVM 参数：

```dockerfile
ENV JAVA_OPTS="-Xms1g -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

### 调整 MySQL 配置

创建自定义 MySQL 配置文件 `docker/mysql/my.cnf`：

```ini
[mysqld]
max_connections=500
innodb_buffer_pool_size=1G
```

### 调整 Redis 配置

修改 `docker-compose.yml` 中的 Redis 命令：

```yaml
command: redis-server --appendonly yes --requirepass your_password --maxmemory 512mb --maxmemory-policy allkeys-lru
```

## 安全建议

1. **修改所有默认密码**：数据库、Redis、JWT 密钥
2. **使用强密码**：至少 16 位，包含大小写字母、数字和特殊字符
3. **定期更新依赖**：`docker-compose pull`
4. **限制网络访问**：使用防火墙和内网
5. **启用 HTTPS**：使用 SSL/TLS 加密通信
6. **定期备份**：设置自动备份任务
7. **监控日志**：设置日志监控和告警

## 更新部署

```bash
# 1. 拉取最新代码
git pull

# 2. 停止旧服务
docker-compose stop

# 3. 重新构建
docker-compose build

# 4. 启动新服务
docker-compose up -d

# 5. 检查状态
docker-compose ps
docker-compose logs -f app
```

## 卸载

```bash
# 停止并删除所有容器、网络、卷
docker-compose down -v

# 删除所有相关镜像
docker rmi $(docker images | grep mall | awk '{print $3}')
```

## 支持与帮助

如遇问题，请查看：
1. 应用日志：`docker-compose logs app`
2. 服务状态：`docker-compose ps`
3. 健康检查：`curl http://localhost:8080/api/actuator/health`
