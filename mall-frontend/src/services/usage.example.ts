/**
 * API 使用示例
 *
 * 本文件展示了如何使用封装好的 API 服务
 */

import { api, product, category, order, coupon, auth } from '../services';
import request from '../utils/request';

// ==================== 使用通用 API 方法 ====================

// 示例 1: 获取商品列表
const fetchProducts = async () => {
  try {
    const result = await api.get<PageResponse<Product>>('/products', {
      params: {
        page: 1,
        size: 10,
        keyword: '手机',
      },
    });
    console.log('商品列表:', result);
    return result;
  } catch (error) {
    console.error('获取商品列表失败:', error);
    throw error;
  }
};

// 示例 2: 创建订单
const createNewOrder = async () => {
  try {
    const result = await api.post<Order>('/orders', {
      items: [
        { productId: 1, skuId: 1, quantity: 2 },
      ],
      addressId: 1,
      couponId: 2,
    });
    console.log('订单创建成功:', result);
    return result;
  } catch (error) {
    console.error('创建订单失败:', error);
    throw error;
  }
};

// ==================== 使用模块化 API 方法 ====================

// 示例 3: 获取商品详情
const fetchProductDetail = async (id: number) => {
  try {
    const productDetail = await product.getProduct(id);
    console.log('商品详情:', productDetail);
    return productDetail;
  } catch (error) {
    console.error('获取商品详情失败:', error);
    throw error;
  }
};

// 示例 4: 获取分类树
const fetchCategoryTree = async () => {
  try {
    const tree = await category.getCategoryTree();
    console.log('分类树:', tree);
    return tree;
  } catch (error) {
    console.error('获取分类树失败:', error);
    throw error;
  }
};

// 示例 5: 获取可用优惠券
const fetchAvailableCoupons = async (amount: number) => {
  try {
    const coupons = await coupon.getAvailableCoupons({ amount });
    console.log('可用优惠券:', coupons);
    return coupons;
  } catch (error) {
    console.error('获取优惠券失败:', error);
    throw error;
  }
};

// 示例 6: 领取优惠券
const claimCoupon = async (id: number) => {
  try {
    await coupon.claimCoupon(id);
    console.log('优惠券领取成功');
  } catch (error) {
    console.error('领取优惠券失败:', error);
    throw error;
  }
};

// 示例 7: 用户登录
const userLogin = async () => {
  try {
    const result = await auth.login({
      username: 'admin',
      password: 'admin123',
    });

    // 保存 token
    localStorage.setItem('token', result.token);
    console.log('登录成功:', result);
    return result;
  } catch (error) {
    console.error('登录失败:', error);
    throw error;
  }
};

// 示例 8: 退出登录
const userLogout = async () => {
  try {
    await auth.logout();
    localStorage.removeItem('token');
    console.log('退出登录成功');
  } catch (error) {
    console.error('退出登录失败:', error);
    throw error;
  }
};

// 示例 9: 取消订单
const cancelOrder = async (orderId: string) => {
  try {
    await order.cancelOrder(orderId);
    console.log('订单取消成功');
  } catch (error) {
    console.error('取消订单失败:', error);
    throw error;
  }
};

// ==================== 使用 request 工具类 ====================

// 示例 10: 使用 request 工具类
const fetchProductsUsingRequest = async () => {
  try {
    const result = await request.get<PageResponse<Product>>('/products');
    console.log('商品列表:', result);
    return result;
  } catch (error) {
    console.error('获取商品列表失败:', error);
    throw error;
  }
};

// 示例 11: 使用 request 工具类发送 POST 请求
const createOrderUsingRequest = async () => {
  try {
    const result = await request.post<Order>('/orders', {
      items: [{ productId: 1, skuId: 1, quantity: 1 }],
      addressId: 1,
    });
    console.log('订单创建成功:', result);
    return result;
  } catch (error) {
    console.error('创建订单失败:', error);
    throw error;
  }
};

// ==================== 文件上传示例 ====================

// 示例 12: 上传商品图片
const uploadProductImage = async (file: File) => {
  try {
    const result = await api.upload<{ url: string }>('/upload/image', file);
    console.log('图片上传成功:', result);
    return result;
  } catch (error) {
    console.error('图片上传失败:', error);
    throw error;
  }
};

// 示例 13: 批量上传
const uploadMultipleImages = async (files: File[]) => {
  try {
    const result = await api.uploadMultiple<{ urls: string[] }>('/upload/images', files);
    console.log('批量上传成功:', result);
    return result;
  } catch (error) {
    console.error('批量上传失败:', error);
    throw error;
  }
};

// ==================== 文件下载示例 ====================

// 示例 14: 下载订单文件
const downloadOrderFile = async (orderId: string) => {
  api.download(`/orders/${orderId}/export`, `订单_${orderId}.xlsx`);
};

// ==================== 类型定义补充 ====================

/**
 * 商品查询参数
 */
interface ProductQueryParams {
  page?: number;
  size?: number;
  keyword?: string;
  categoryId?: number;
  status?: number;
  minPrice?: number;
  maxPrice?: number;
  sortBy?: 'price_asc' | 'price_desc' | 'sales_desc' | 'created_desc';
}

/**
 * 分页响应
 */
interface PageResponse<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

/**
 * 商品实体
 */
interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  stock: number;
  image: string;
  categoryId: number;
  status: number;
  createdAt: string;
  updatedAt: string;
}

/**
 * 订单实体
 */
interface Order {
  id: string;
  userId: number;
  totalAmount: number;
  discountAmount: number;
  actualAmount: number;
  status: number;
  paymentMethod: string;
  items: OrderItem[];
  address: OrderAddress;
  createdAt: string;
}

/**
 * 订单项
 */
interface OrderItem {
  id: number;
  productId: number;
  productName: string;
  productImage: string;
  skuId: number;
  skuName: string;
  price: number;
  quantity: number;
}

/**
 * 订单地址
 */
interface OrderAddress {
  receiverName: string;
  receiverPhone: string;
  province: string;
  city: string;
  district: string;
  detail: string;
}

/**
 * 分类实体
 */
interface Category {
  id: number;
  name: string;
  parentId: number | null;
  sortOrder: number;
}

/**
 * 优惠券实体
 */
interface Coupon {
  id: number;
  name: string;
  type: number;
  discount: number;
  minAmount: number;
  maxDiscount: number;
  total: number;
  used: number;
  startTime: string;
  endTime: string;
  status: number;
}
