import { get, post, put, patch } from '../api';
import type { Order, PageResponse } from '../../types';

/**
 * 获取订单列表
 * @param params 查询参数
 * @returns Promise<PageResponse<Order>>
 */
export const getOrders = (params?: {
  page?: number;
  size?: number;
  status?: number;
  keyword?: string;
}): Promise<PageResponse<Order>> => {
  return get<PageResponse<Order>>('/orders', { params });
};

/**
 * 获取订单详情
 * @param id 订单ID
 * @returns Promise<Order>
 */
export const getOrder = (id: string): Promise<Order> => {
  return get<Order>(`/orders/${id}`);
};

/**
 * 创建订单
 * @param data 订单数据
 * @returns Promise<Order>
 */
export const createOrder = (data: {
  items: Array<{ productId: number; skuId: number; quantity: number }>;
  addressId: number;
  couponId?: number;
}): Promise<Order> => {
  return post<Order>('/orders', data);
};

/**
 * 取消订单
 * @param id 订单ID
 * @returns Promise<void>
 */
export const cancelOrder = (id: string): Promise<void> => {
  return patch<void>(`/orders/${id}/cancel`);
};

/**
 * 确认订单
 * @param id 订单ID
 * @returns Promise<void>
 */
export const confirmOrder = (id: string): Promise<void> => {
  return patch<void>(`/orders/${id}/confirm`);
};

/**
 * 完成订单
 * @param id 订单ID
 * @returns Promise<void>
 */
export const completeOrder = (id: string): Promise<void> => {
  return patch<void>(`/orders/${id}/complete`);
};
