import axiosInstance from './axios';
import type { ApiResponse } from '../types';

/**
 * API 响应统一处理
 * @param response axios 响应对象
 * @returns 处理后的数据
 */
const handleResponse = <T>(response: any): T => {
  const { data } = response;

  if (data.code === 200 || data.code === 0) {
    return data.data;
  }

  throw new Error(data.message || '请求失败');
};

/**
 * GET 请求
 * @param url 请求地址
 * @param config 请求配置
 * @returns Promise<T>
 */
export const get = <T = any>(
  url: string,
  config?: any
): Promise<T> => {
  return axiosInstance.get<ApiResponse<T>>(url, config).then(handleResponse);
};

/**
 * POST 请求
 * @param url 请求地址
 * @param data 请求数据
 * @param config 请求配置
 * @returns Promise<T>
 */
export const post = <T = any>(
  url: string,
  data?: any,
  config?: any
): Promise<T> => {
  return axiosInstance.post<ApiResponse<T>>(url, data, config).then(handleResponse);
};

/**
 * PUT 请求
 * @param url 请求地址
 * @param data 请求数据
 * @param config 请求配置
 * @returns Promise<T>
 */
export const put = <T = any>(
  url: string,
  data?: any,
  config?: any
): Promise<T> => {
  return axiosInstance.put<ApiResponse<T>>(url, data, config).then(handleResponse);
};

/**
 * DELETE 请求
 * @param url 请求地址
 * @param config 请求配置
 * @returns Promise<T>
 */
export const del = <T = any>(
  url: string,
  config?: any
): Promise<T> => {
  return axiosInstance.delete<ApiResponse<T>>(url, config).then(handleResponse);
};

/**
 * PATCH 请求
 * @param url 请求地址
 * @param data 请求数据
 * @param config 请求配置
 * @returns Promise<T>
 */
export const patch = <T = any>(
  url: string,
  data?: any,
  config?: any
): Promise<T> => {
  return axiosInstance.patch<ApiResponse<T>>(url, data, config).then(handleResponse);
};

/**
 * 文件上传
 * @param url 请求地址
 * @param file 文件对象
 * @param config 请求配置
 * @returns Promise<T>
 */
export const upload = <T = any>(
  url: string,
  file: File,
  config?: any
): Promise<T> => {
  const formData = new FormData();
  formData.append('file', file);

  return axiosInstance.post<ApiResponse<T>>(url, formData, {
    ...config,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  }).then(handleResponse);
};

/**
 * 多文件上传
 * @param url 请求地址
 * @param files 文件数组
 * @param config 请求配置
 * @returns Promise<T>
 */
export const uploadMultiple = <T = any>(
  url: string,
  files: File[],
  config?: any
): Promise<T> => {
  const formData = new FormData();
  files.forEach((file) => {
    formData.append('files', file);
  });

  return axiosInstance.post<ApiResponse<T>>(url, formData, {
    ...config,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  }).then(handleResponse);
};

/**
 * 文件下载
 * @param url 请求地址
 * @param filename 下载文件名
 * @param config 请求配置
 */
export const download = (
  url: string,
  filename?: string,
  config?: any
): void => {
  axiosInstance
    .get(url, {
      ...config,
      responseType: 'blob',
    })
    .then((response) => {
      const blob = new Blob([response.data]);
      const link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = filename || 'download';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(link.href);
    })
    .catch((error) => {
      console.error('下载失败:', error);
    });
};

/**
 * 请求重试
 * @param fn 请求函数
 * @param retries 重试次数
 * @param delay 重试延迟
 * @returns Promise<T>
 */
export const retryRequest = async <T>(
  fn: () => Promise<T>,
  retries = 3,
  delay = 1000
): Promise<T> => {
  try {
    return await fn();
  } catch (error) {
    if (retries <= 0) {
      throw error;
    }
    await new Promise((resolve) => setTimeout(resolve, delay));
    return retryRequest(fn, retries - 1, delay * 2);
  }
};

/**
 * 并发请求
 * @param requests 请求函数数组
 * @returns Promise<T[]>
 */
export const all = <T extends any[]>(
  requests: [...{ [K in keyof T]: () => Promise<T[K]> }]
): Promise<T> => {
  return Promise.all(requests.map((request) => request()));
};

/**
 * 导出 API 对象（兼容旧代码）
 */
export const api = {
  get,
  post,
  put,
  delete: del,
  patch,
  upload,
  uploadMultiple,
  download,
};

export default api;
