import { get, post, put, del } from '../api';
import type { Coupon, PageResponse } from '../../types';

/**
 * 获取优惠券列表
 * @param params 查询参数
 * @returns Promise<PageResponse<Coupon>>
 */
export const getCoupons = (params?: {
  page?: number;
  size?: number;
  status?: number;
  keyword?: string;
}): Promise<PageResponse<Coupon>> => {
  return get<PageResponse<Coupon>>('/coupons', { params });
};

/**
 * 获取可用优惠券
 * @param params 查询参数
 * @returns Promise<Coupon[]>
 */
export const getAvailableCoupons = (params?: {
  amount?: number;
}): Promise<Coupon[]> => {
  return get<Coupon[]>('/coupons/available', { params });
};

/**
 * 获取优惠券详情
 * @param id 优惠券ID
 * @returns Promise<Coupon>
 */
export const getCoupon = (id: number): Promise<Coupon> => {
  return get<Coupon>(`/coupons/${id}`);
};

/**
 * 创建优惠券
 * @param data 优惠券数据
 * @returns Promise<Coupon>
 */
export const createCoupon = (data: Partial<Coupon>): Promise<Coupon> => {
  return post<Coupon>('/coupons', data);
};

/**
 * 更新优惠券
 * @param id 优惠券ID
 * @param data 优惠券数据
 * @returns Promise<Coupon>
 */
export const updateCoupon = (id: number, data: Partial<Coupon>): Promise<Coupon> => {
  return put<Coupon>(`/coupons/${id}`, data);
};

/**
 * 删除优惠券
 * @param id 优惠券ID
 * @returns Promise<void>
 */
export const deleteCoupon = (id: number): Promise<void> => {
  return del<void>(`/coupons/${id}`);
};

/**
 * 领取优惠券
 * @param id 优惠券ID
 * @returns Promise<void>
 */
export const claimCoupon = (id: number): Promise<void> => {
  return post<void>(`/coupons/${id}/claim`);
};

/**
 * 使用优惠券
 * @param id 优惠券ID
 * @returns Promise<{ discountAmount: number }>
 */
export const useCoupon = (id: number, orderId: string): Promise<{ discountAmount: number }> => {
  return post<{ discountAmount: number }>(`/coupons/${id}/use`, { orderId });
};
