import { get, post, put, del } from '../api';
import type { Category } from '../../types';

/**
 * 获取分类列表
 * @param params 查询参数
 * @returns Promise<Category[]>
 */
export const getCategories = (params?: {
  parentId?: number | null;
  includeChildren?: boolean;
}): Promise<Category[]> => {
  return get<Category[]>('/categories', { params });
};

/**
 * 获取分类详情
 * @param id 分类ID
 * @returns Promise<Category>
 */
export const getCategory = (id: number): Promise<Category> => {
  return get<Category>(`/categories/${id}`);
};

/**
 * 创建分类
 * @param data 分类数据
 * @returns Promise<Category>
 */
export const createCategory = (data: Partial<Category>): Promise<Category> => {
  return post<Category>('/categories', data);
};

/**
 * 更新分类
 * @param id 分类ID
 * @param data 分类数据
 * @returns Promise<Category>
 */
export const updateCategory = (id: number, data: Partial<Category>): Promise<Category> => {
  return put<Category>(`/categories/${id}`, data);
};

/**
 * 删除分类
 * @param id 分类ID
 * @returns Promise<void>
 */
export const deleteCategory = (id: number): Promise<void> => {
  return del<void>(`/categories/${id}`);
};

/**
 * 获取分类树
 * @returns Promise<Category[]>
 */
export const getCategoryTree = (): Promise<Category[]> => {
  return get<Category[]>('/categories/tree');
};
