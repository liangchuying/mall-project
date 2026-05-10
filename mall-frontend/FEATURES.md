# FEATURES.md - 商城前端功能需求清单

> 基于 mall-backend 后台 API 接口分析整理

---

## 一、用户认证模块

| 功能 | 路由 | 页面/组件 | API 接口 | 状态 |
|------|------|-----------|----------|------|
| 用户注册 | 弹窗 | AuthModal | `POST /auth/register` | ✅ 已完成 |
| 用户登录 | 弹窗 | AuthModal | `POST /auth/login` | ✅ 已完成 |
| 用户登出 | - | - | `POST /auth/logout` | ⬜ 待开发 |
| 获取用户信息 | `/profile` | ProfilePage | `GET /auth/info` | ⬜ 待开发 |
| 忘记密码 | 弹窗 | AuthModal | `POST /password/send-code` | ✅ 已完成 |
| 重置密码 | 弹窗 | AuthModal | `POST /password/reset` | ✅ 已完成 |
| 修改密码 | `/profile/password` | ChangePasswordPage | `POST /password/change` | ⬜ 待开发 |

---

## 二、首页模块

| 功能 | 组件 | API 接口 | 状态 |
|------|------|----------|------|
| 轮播图展示 | HeroCarousel | - | ✅ 已创建 |
| 商品推荐 | ProductRecommend | `GET /product/search` | ⬜ 待开发 |
| 热门分类展示 | CategoryList | `GET /category/tree` | ⬜ 待开发 |

---

## 三、商品模块

| 功能 | 路由 | 页面/组件 | API 接口 | 状态 |
|------|------|-----------|----------|------|
| 商品分类 | `/category` | CategoryPage | `GET /category/tree` | ⬜ 待开发 |
| 商品列表/搜索 | `/products` | ProductListPage | `GET /product/search` | ⬜ 待开发 |
| 商品详情 | `/product/:id` | ProductDetailPage | `GET /product/spu/:id` | ⬜ 待开发 |
| 搜索建议 | SearchInput | `GET /product/search/suggestions` | ⬜ 待开发 |
| 品牌筛选 | BrandFilter | `GET /product/brand/list` | ⬜ 待开发 |
| 分类筛选 | CategoryFilter | `GET /category/tree` | ⬜ 待开发 |
| 价格排序 | PriceSort | `GET /product/search` | ⬜ 待开发 |
| 销量排序 | SalesSort | `GET /product/search` | ⬜ 待开发 |

### 商品模块子组件

| 组件名称 | 用途 | 状态 |
|----------|------|------|
| ProductCard | 商品卡片 | ⬜ 待开发 |
| ProductImageGallery | 商品图片画廊 | ⬜ 待开发 |
| ProductSkuSelector | SKU 规格选择器 | ⬜ 待开发 |
| ProductSpecSelector | 商品规格选择 | ⬜ 待开发 |
| AddToCartButton | 加入购物车按钮 | ⬜ 待开发 |
| ProductQuantitySelector | 商品数量选择器 | ⬜ 待开发 |
| SearchBar | 搜索栏 | ⬜ 待开发 |
| FilterSidebar | 筛选侧边栏 | ⬜ 待开发 |
| SortBar | 排序栏 | ⬜ 待开发 |

---

## 四、购物车模块

| 功能 | 路由 | 页面/组件 | API 接口 | 状态 |
|------|------|-----------|----------|------|
| 购物车列表 | `/cart` | CartPage | `GET /cart` | ⬜ 待开发 |
| 添加到购物车 | - | AddToCart | `POST /cart` | ⬜ 待开发 |
| 更新购物车 | - | UpdateCartItem | `PUT /cart` | ⬜ 待开发 |
| 删除购物车商品 | - | DeleteCartItem | `DELETE /cart/{cartId}` | ⬜ 待开发 |
| 批量删除 | - | BatchDeleteCartItems | `DELETE /cart/batch` | ⬜ 待开发 |
| 全选/取消全选 | - | SelectAllCart | `PUT /cart/select-all` | ⬜ 待开发 |
| 获取购物车总价 | - | - | `GET /cart/total-price` | ⬜ 待开发 |
| 获取购物车数量 | - | CartBadge | `GET /cart/count` | ⬜ 待开发 |

### 购物车模块子组件

| 组件名称 | 用途 | 状态 |
|----------|------|------|
| CartItem | 购物车商品项 | ⬜ 待开发 |
| CartItemQuantity | 商品数量控制器 | ⬜ 待开发 |
| CartItemCheckbox | 商品选择框 | ⬜ 待开发 |
| CartSummary | 购物车汇总 | ⬜ 待开发 |
| EmptyCart | 空购物车提示 | ⬜ 待开发 |

---

## 五、订单模块

| 功能 | 路由 | 页面/组件 | API 接口 | 状态 |
|------|------|-----------|----------|------|
| 创建订单 | `/checkout` | CheckoutPage | `POST /order` | ⬜ 待开发 |
| 支付订单 | - | PaymentModal | `POST /order/pay/{orderNo}` | ⬜ 待开发 |
| 取消订单 | - | CancelOrderButton | `POST /order/cancel/{orderNo}` | ⬜ 待开发 |
| 确认收货 | - | ConfirmReceiveButton | `POST /order/receive/{orderNo}` | ⬜ 待开发 |
| 订单详情 | `/order/:orderNo` | OrderDetailPage | `GET /order/{orderNo}` | ⬜ 待开发 |
| 我的订单列表 | `/orders` | MyOrdersPage | `GET /order/my` | ⬜ 待开发 |

### 订单状态

| 状态值 | 含义 | 显示文字 |
|--------|------|----------|
| 0 | 待支付 | 待支付 |
| 1 | 已支付 | 待发货 |
| 2 | 已发货 | 待收货 |
| 3 | 已完成 | 已完成 |
| 4 | 已取消 | 已取消 |

### 订单模块子组件

| 组件名称 | 用途 | 状态 |
|----------|------|------|
| OrderItem | 订单项 | ⬜ 待开发 |
| OrderStatusBadge | 订单状态标签 | ⬜ 待开发 |
| OrderTimeline | 订单时间轴 | ⬜ 待开发 |
| OrderAddress | 收货地址 | ⬜ 待开发 |
| OrderPaymentInfo | 支付信息 | ⬜ 待开发 |
| OrderItemsList | 订单商品列表 | ⬜ 待开发 |

---

## 六、优惠券模块

| 功能 | 路由 | 页面/组件 | API 接口 | 状态 |
|------|------|-----------|----------|------|
| 可领取优惠券 | `/coupons` | CouponsPage | `GET /coupon/available` | ⬜ 待开发 |
| 领取优惠券 | - | ReceiveCouponButton | `POST /coupon/receive/{couponId}` | ⬜ 待开发 |
| 我的优惠券 | `/my-coupons` | MyCouponsPage | `GET /coupon/my` | ⬜ 待开发 |
| 计算优惠券折扣 | - | CouponCalculator | `POST /coupon/calculate` | ⬜ 待开发 |
| 选择优惠券 | - | CouponSelector | - | ⬜ 待开发 |

### 优惠券类型

| 类型 | 描述 |
|------|------|
| 满减券 | 满X元减Y元 |
| 折扣券 | X折优惠 |
| 无门槛券 | 直接抵扣 |

### 优惠券模块子组件

| 组件名称 | 用途 | 状态 |
|----------|------|------|
| CouponCard | 优惠券卡片 | ⬜ 待开发 |
| CouponSelector | 优惠券选择器 | ⬜ 待开发 |
| CouponDetail | 优惠券详情 | ⬜ 待开发 |

---

## 七、支付模块

| 功能 | 路由 | 页面/组件 | API 接口 | 状态 |
|------|------|-----------|----------|------|
| 创建支付 | - | - | `POST /payment` | ⬜ 待开发 |
| 获取支付链接 | - | PaymentModal | `GET /payment/url/{paymentNo}` | ⬜ 待开发 |
| 支付记录详情 | - | PaymentDetail | `GET /payment/{paymentNo}` | ⬜ 待开发 |
| 申请退款 | - | RefundButton | `POST /payment/refund/{paymentNo}` | ⬜ 待开发 |

### 支付方式

| 支付方式 | 描述 |
|----------|------|
| 支付宝 | ALIPAY |
| 微信支付 | WECHAT |

### 支付状态

| 状态 | 描述 |
|------|------|
| 待支付 | 未支付 |
| 已支付 | 支付成功 |
| 已退款 | 退款完成 |
| 已取消 | 支付取消 |

---

## 八、个人中心模块

| 功能 | 路由 | 页面/组件 | API 接口 | 状态 |
|------|------|-----------|----------|------|
| 个人信息 | `/profile` | ProfilePage | `GET /auth/info` | ⬜ 待开发 |
| 编辑个人信息 | `/profile/edit` | EditProfilePage | - | ⬜ 待开发 |
| 修改密码 | `/profile/password` | ChangePasswordPage | `POST /password/change` | ⬜ 待开发 |
| 地址管理 | `/profile/addresses` | AddressPage | - | ⬜ 待开发 |
| 收藏列表 | `/profile/favorites` | FavoritesPage | - | ⬜ 待开发 |
| 浏览记录 | `/profile/history` | HistoryPage | - | ⬜ 待开发 |

### 个人中心子组件

| 组件名称 | 用途 | 状态 |
|----------|------|------|
| UserProfileCard | 用户信息卡片 | ⬜ 待开发 |
| UserMenu | 用户菜单 | ⬜ 待开发 |
| AddressForm | 地址表单 | ⬜ 待开发 |
| AddressItem | 地址项 | ⬜ 待开发 |

---

## 九、后台管理模块 (管理员)

| 功能 | 路由 | 页面/组件 | API 接口 | 状态 |
|------|------|-----------|----------|------|
| 分类管理 | `/admin/category` | AdminCategoryPage | `CRUD /category` | ⬜ 待开发 |
| 品牌管理 | `/admin/brand` | AdminBrandPage | `CRUD /product/brand` | ⬜ 待开发 |
| 商品管理 | `/admin/product` | AdminProductPage | `CRUD /product/spu` | ⬜ 待开发 |
| SKU 管理 | `/admin/sku` | AdminSkuPage | `CRUD /product/sku` | ⬜ 待开发 |
| 库存管理 | `/admin/stock` | AdminStockPage | `POST /stock/*` | ⬜ 待开发 |
| 订单管理 | `/admin/order` | AdminOrderPage | - | ⬜ 待开发 |
| 订单发货 | - | - | `POST /order/ship/{orderNo}` | ⬜ 待开发 |

---

## 十、通用组件

| 组件名称 | 用途 | 状态 |
|----------|------|------|
| Layout | 页面布局 | ✅ 已创建 |
| Header | 头部导航 | ✅ 已创建 |
| Footer | 页脚 | ⬜ 待开发 |
| Loading | 加载中 | ⬜ 待开发 |
| Empty | 空状态 | ⬜ 待开发 |
| ErrorBoundary | 错误边界 | ⬜ 待开发 |
| Pagination | 分页组件 | ⬜ 待开发 |
| Modal | 弹窗 | ✅ 已完成 |
| AuthModal | 认证弹窗 | ✅ 已完成 |
| Toast | 提示消息 | ⬜ 待开发 |
| ConfirmDialog | 确认对话框 | ⬜ 待开发 |
| Form表单 | 表单组件 | ⬜ 待开发 |
| ImageUploader | 图片上传 | ⬜ 待开发 |
| StarRating | 星级评分 | ⬜ 待开发 |
| Countdown | 倒计时 | ⬜ 待开发 |

---

## 十一、页面路由规划

### 公共路由

| 路由 | 页面 | 说明 |
|------|------|------|
| `/` | Home | 首页 |
| `/login` | Login | 登录页 |
| `/register` | Register | 注册页 |
| `/forgot-password` | ForgotPassword | 忘记密码 |
| `/reset-password` | ResetPassword | 重置密码 |

### 用户路由（需要登录）

| 路由 | 页面 | 说明 |
|------|------|------|
| `/category` | CategoryList | 分类列表 |
| `/products` | ProductList | 商品列表 |
| `/product/:id` | ProductDetail | 商品详情 |
| `/cart` | Cart | 购物车 |
| `/checkout` | Checkout | 结算页 |
| `/orders` | MyOrders | 我的订单 |
| `/order/:orderNo` | OrderDetail | 订单详情 |
| `/coupons` | Coupons | 优惠券中心 |
| `/my-coupons` | MyCoupons | 我的优惠券 |
| `/profile` | Profile | 个人中心 |
| `/profile/edit` | EditProfile | 编辑资料 |
| `/profile/password` | ChangePassword | 修改密码 |

### 管理员路由

| 路由 | 页面 | 说明 |
|------|------|------|
| `/admin/dashboard` | AdminDashboard | 管理后台首页 |
| `/admin/category` | AdminCategory | 分类管理 |
| `/admin/brand` | AdminBrand | 品牌管理 |
| `/admin/product` | AdminProduct | 商品管理 |
| `/admin/sku` | AdminSku | SKU 管理 |
| `/admin/stock` | AdminStock | 库存管理 |
| `/admin/order` | AdminOrder | 订单管理 |

---

## 十二、开发优先级

### 第一阶段（基础功能）

1. ✅ 首页布局和导航
2. 用户注册/登录
3. 商品列表和搜索
4. 商品详情页
5. 购物车基础功能

### 第二阶段（核心交易）

1. 结算和订单创建
2. 支付流程
3. 订单列表和详情
4. 个人中心基础功能

### 第三阶段（营销功能）

1. 优惠券系统
2. 收藏功能
3. 浏览记录
4. 评价功能

### 第四阶段（管理后台）

1. 商品管理
2. 分类管理
3. 订单管理
4. 数据统计

---

## 更新记录

| 日期 | 更新内容 | 更新人 |
|------|----------|--------|
| 2026-05-10 | 初始化功能清单 | Claude |
| 2026-05-10 | 完成 Modal 和 AuthModal 组件；更新 Layout 集成认证弹窗 | Claude |
