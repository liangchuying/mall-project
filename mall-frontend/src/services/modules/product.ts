import { get, post, put, del } from '../api';
import type { Product, PageResponse } from '../../types';

/**
 * 获取商品列表
 * @param params 查询参数
 * @returns Promise<PageResponse<Product>>
 */
export const getProducts = (params?: {
  page?: number;
  size?: number;
  keyword?: string;
  categoryId?: number;
  status?: number;
  minPrice?: number;
  maxPrice?: number;
  sortBy?: string;
}): Promise<PageResponse<Product>> => {
  return get<PageResponse<Product>>('/products', { params });
};

/**
 * 获取商品详情
 * @param id 商品ID
 * @returns Promise<Product>
 */
export const getProduct = (id: number): Promise<Product> => {
  return get<Product>(`/products/${id}`);
};

/**
 * 创建商品
 * @param data 商品数据
 * @returns Promise<Product>
 */
export const createProduct = (data: Partial<Product>): Promise<Product> => {
  return post<Product>('/products', data);
};

/**
 * 更新商品
 * @param id 商品ID
 * @param data 商品数据
 * @returns Promise<Product>
 */
export const updateProduct = (id: number, data: Partial<Product>): Promise<Product> => {
  return put<Product>(`/products/${id}`, data);
};

/**
 * 删除商品
 * @param id 商品ID
 * @returns Promise<void>
 */
export const deleteProduct = (id: number): Promise<void> => {
  return del<void>(`/products/${id}`);
};

/**
 * 获取商品SKU列表
 * @param id 商品ID
 * @returns Promise<ProductSku[]>
 */
export const getProductSkus = (id: number): Promise<any[]> => {
  return get<any[]>(`/products/${id}/skus`);
};

/**
 * 更新商品库存
 * @param id 商品ID
 * @param stock 库存数量
 * @returns Promise<void>
 */
export const updateProductStock = (id: number, stock: number): Promise<void> => {
  return put<void>(`/products/${id}/stock`, { stock });
};
