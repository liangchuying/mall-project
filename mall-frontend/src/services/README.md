# API 服务文档

## 目录结构

```
services/
├── axios.ts          # axios 实例配置和拦截器
├── api.ts            # 通用 API 方法封装
├── modules/          # 业务模块 API
│   ├── product.ts    # 商品相关 API
│   ├── category.ts   # 分类相关 API
│   ├── order.ts      # 订单相关 API
│   ├── coupon.ts     # 优惠券相关 API
│   ├── auth.ts       # 认证相关 API
│   └── index.ts      # 模块统一导出
├── index.ts          # 服务统一导出
└── usage.example.ts  # 使用示例
```

## 功能特性

### 1. 请求拦截器

- ✅ 自动添加 Bearer Token
- ✅ 请求防重复（取消重复请求）
- ✅ GET 请求添加时间戳防止缓存
- ✅ 请求超时处理（10秒）

### 2. 响应拦截器

- ✅ 统一响应格式处理
- ✅ 401 自动跳转登录页
- ✅ 403/404/500 错误统一处理
- ✅ 网络错误提示
- ✅ 自动清理请求记录

### 3. 通用 API 方法

- `get<T>(url, config?)` - GET 请求
- `post<T>(url, data?, config?)` - POST 请求
- `put<T>(url, data?, config?)` - PUT 请求
- `delete<T>(url, config?)` - DELETE 请求
- `patch<T>(url, data?, config?)` - PATCH 请求
- `upload<T>(url, file, config?)` - 文件上传
- `uploadMultiple<T>(url, files, config?)` - 多文件上传
- `download(url, filename?, config?)` - 文件下载
- `retryRequest(fn, retries?, delay?)` - 请求重试
- `all<T[]>(requests)` - 并发请求

### 4. 业务模块 API

#### 商品模块 (product)
- `getProducts(params?)` - 获取商品列表
- `getProduct(id)` - 获取商品详情
- `createProduct(data)` - 创建商品
- `updateProduct(id, data)` - 更新商品
- `deleteProduct(id)` - 删除商品
- `getProductSkus(id)` - 获取商品 SKU
- `updateProductStock(id, stock)` - 更新库存

#### 分类模块 (category)
- `getCategories(params?)` - 获取分类列表
- `getCategory(id)` - 获取分类详情
- `createCategory(data)` - 创建分类
- `updateCategory(id, data)` - 更新分类
- `deleteCategory(id)` - 删除分类
- `getCategoryTree()` - 获取分类树

#### 订单模块 (order)
- `getOrders(params?)` - 获取订单列表
- `getOrder(id)` - 获取订单详情
- `createOrder(data)` - 创建订单
- `cancelOrder(id)` - 取消订单
- `confirmOrder(id)` - 确认订单
- `completeOrder(id)` - 完成订单

#### 优惠券模块 (coupon)
- `getCoupons(params?)` - 获取优惠券列表
- `getAvailableCoupons(params?)` - 获取可用优惠券
- `getCoupon(id)` - 获取优惠券详情
- `createCoupon(data)` - 创建优惠券
- `updateCoupon(id, data)` - 更新优惠券
- `deleteCoupon(id)` - 删除优惠券
- `claimCoupon(id)` - 领取优惠券
- `useCoupon(id, orderId)` - 使用优惠券

#### 认证模块 (auth)
- `login(data)` - 用户登录
- `register(data)` - 用户注册
- `logout()` - 退出登录
- `refreshToken(refreshToken)` - 刷新 Token
- `changePassword(data)` - 修改密码
- `forgotPassword(email)` - 忘记密码
- `resetPassword(data)` - 重置密码

## 使用方法

### 方法 1: 使用通用 API 方法

```typescript
import { api } from '@/services';

// 获取商品列表
const products = await api.get<PageResponse<Product>>('/products', {
  params: { page: 1, size: 10 }
});

// 创建订单
const order = await api.post<Order>('/orders', {
  items: [{ productId: 1, skuId: 1, quantity: 2 }],
  addressId: 1
});
```

### 方法 2: 使用模块化 API 方法

```typescript
import { product, category, order, coupon, auth } from '@/services/modules';

// 获取商品列表
const products = await product.getProducts({ page: 1, size: 10 });

// 获取商品详情
const product = await product.getProduct(1);

// 获取分类树
const tree = await category.getCategoryTree();

// 用户登录
const result = await auth.login({ username: 'admin', password: 'admin123' });
localStorage.setItem('token', result.token);
```

### 方法 3: 使用 request 工具类

```typescript
import request from '@/utils/request';

// GET 请求
const products = await request.get<PageResponse<Product>>('/products');

// POST 请求
const order = await request.post<Order>('/orders', { addressId: 1 });

// 文件上传
const result = await request.upload<{ url: string }>('/upload/image', file);

// 文件下载
request.download('/orders/1/export', '订单.xlsx');
```

## 错误处理

### 统一错误格式

```typescript
{
  code: number,    // 错误码
  message: string,  // 错误信息
  data: any       // 错误数据
}
```

### 常见错误码

- `200` - 成功
- `0` - 成功
- `401` - 未授权，需要登录
- `403` - 无权限
- `404` - 资源不存在
- `500` - 服务器错误
- `-1` - 请求已取消
- `-2` - 网络连接失败

### 错误处理示例

```typescript
import { product } from '@/services/modules';

try {
  const productDetail = await product.getProduct(1);
  console.log('商品详情:', productDetail);
} catch (error) {
  console.error('获取商品详情失败:', error);
  // 处理错误
  if (error.code === 401) {
    // 跳转登录页
    window.location.href = '/login';
  } else {
    // 显示错误提示
    showToast(error.message || '获取商品详情失败');
  }
}
```

## Token 管理

### 保存 Token

登录成功后需要手动保存 token：

```typescript
import { auth } from '@/services/modules';

const result = await auth.login({ username, password });
localStorage.setItem('token', result.token);
```

### 清除 Token

```typescript
localStorage.removeItem('token');
```

### 自动 Token 刷新

拦截器会自动在每个请求的 header 中添加 `Authorization: Bearer ${token}`

## 请求配置

### 自定义配置

```typescript
import { api } from '@/services';

const result = await api.get('/products', {
  params: { page: 1 },
  headers: {
    'Custom-Header': 'value'
  },
  timeout: 5000
});
```

### 环境变量配置

在 `.env` 文件中配置 API 地址：

```
VITE_API_BASE_URL=http://localhost:8080/api
```

## 类型支持

所有 API 方法都支持泛型，可以获得完整的类型提示：

```typescript
const products = await api.get<PageResponse<Product>>('/products');
// products 的类型为 PageResponse<Product>

const product = await product.getProduct(1);
// product 的类型为 Product
```

## 注意事项

1. 所有 API 请求都会自动添加 token（如果存在）
2. 401 错误会自动清除 token 并跳转登录页
3. 重复的请求会被自动取消
4. GET 请求会自动添加时间戳参数防止缓存
5. 使用模块化 API 方法可以获得更好的类型提示

## 完整示例

```typescript
import { product, category, auth } from '@/services/modules';

// 登录
const login = async () => {
  try {
    const result = await auth.login({ username: 'admin', password: 'admin123' });
    localStorage.setItem('token', result.token);
    return result;
  } catch (error) {
    console.error('登录失败:', error);
    throw error;
  }
};

// 获取商品列表
const fetchProducts = async () => {
  try {
    const products = await product.getProducts({
      page: 1,
      size: 12,
      status: 1
    });
    return products;
  } catch (error) {
    console.error('获取商品列表失败:', error);
    throw error;
  }
};

// 获取分类树
const fetchCategories = async () => {
  try {
    const categories = await category.getCategoryTree();
    return categories;
  } catch (error) {
    console.error('获取分类失败:', error);
    throw error;
  }
};
```
