import { api } from '../services';
import type { ApiResponse } from '../types';

/**
 * 请求工具类 - 提供更便捷的 API 调用方式
 */
class RequestUtil {
  /**
   * 通用请求方法
   * @param method 请求方法
   * @param url 请求地址
   * @param data 请求数据
   * @param config 请求配置
   * @returns Promise<T>
   */
  async request<T = any>(
    method: 'get' | 'post' | 'put' | 'delete' | 'patch',
    url: string,
    data?: any,
    config?: any
  ): Promise<T> {
    return api[method]<T>(url, data, config);
  }

  /**
   * GET 请求
   */
  get<T = any>(url: string, config?: any): Promise<T> {
    return this.request<T>('get', url, undefined, config);
  }

  /**
   * POST 请求
   */
  post<T = any>(url: string, data?: any, config?: any): Promise<T> {
    return this.request<T>('post', url, data, config);
  }

  /**
   * PUT 请求
   */
  put<T = any>(url: string, data?: any, config?: any): Promise<T> {
    return this.request<T>('put', url, data, config);
  }

  /**
   * DELETE 请求
   */
  delete<T = any>(url: string, config?: any): Promise<T> {
    return this.request<T>('delete', url, undefined, config);
  }

  /**
   * PATCH 请求
   */
  patch<T = any>(url: string, data?: any, config?: any): Promise<T> {
    return this.request<T>('patch', url, data, config);
  }

  /**
   * 上传文件
   */
  upload<T = any>(url: string, file: File, config?: any): Promise<T> {
    return api.upload<T>(url, file, config);
  }

  /**
   * 下载文件
   */
  download(url: string, filename?: string): void {
    api.download(url, filename);
  }
}

// 导出单例
export const request = new RequestUtil();

// 默认导出
export default request;
