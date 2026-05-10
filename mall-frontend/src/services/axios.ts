import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse, AxiosError, InternalAxiosRequestConfig } from 'axios';

// 基础配置
const baseURL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';
const TIMEOUT = 10000;

// 请求取消映射
const pendingRequests = new Map<string, AbortController>();

// 生成请求唯一key
const generateRequestKey = (config: InternalAxiosRequestConfig): string => {
  const { method, url, data, params } = config;
  return [method, url, JSON.stringify(data), JSON.stringify(params)].join('&');
};

// 创建 axios 实例
const axiosInstance: AxiosInstance = axios.create({
  baseURL,
  timeout: TIMEOUT,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 请求拦截器
axiosInstance.interceptors.request.use(
  (config) => {
    const requestKey = generateRequestKey(config);

    // 取消重复请求
    if (pendingRequests.has(requestKey)) {
      const controller = pendingRequests.get(requestKey);
      controller?.abort();
    }

    // 创建新的 AbortController
    const controller = new AbortController();
    config.signal = controller.signal;
    pendingRequests.set(requestKey, controller);

    // 添加 token
    const token = localStorage.getItem('token');
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    // 添加时间戳防止缓存
    if (config.method === 'get') {
      config.params = {
        ...config.params,
        _t: Date.now(),
      };
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
axiosInstance.interceptors.response.use(
  (response: AxiosResponse) => {
    // 请求完成后移除 pending 记录
    const requestKey = generateRequestKey(response.config as InternalAxiosRequestConfig);
    pendingRequests.delete(requestKey);

    return response;
  },
  async (error: AxiosError) => {
    // 请求失败后移除 pending 记录
    if (error.config) {
      const requestKey = generateRequestKey(error.config as InternalAxiosRequestConfig);
      pendingRequests.delete(requestKey);
    }

    // 处理取消请求
    if (axios.isCancel(error)) {
      return Promise.reject({ code: -1, message: '请求已取消', data: null });
    }

    // 处理网络错误
    if (!error.response) {
      return Promise.reject({
        code: -2,
        message: '网络连接失败，请检查网络设置',
        data: null,
      });
    }

    const { status, data } = error.response as any;

    // 处理 401 未授权
    if (status === 401) {
      localStorage.removeItem('token');
      // 不在登录页时跳转
      if (window.location.pathname !== '/login') {
        window.location.href = '/login';
      }
      return Promise.reject({
        code: 401,
        message: '登录已过期，请重新登录',
        data: null,
      });
    }

    // 处理 403 无权限
    if (status === 403) {
      return Promise.reject({
        code: 403,
        message: '没有权限访问该资源',
        data: null,
      });
    }

    // 处理 404 未找到
    if (status === 404) {
      return Promise.reject({
        code: 404,
        message: '请求的资源不存在',
        data: null,
      });
    }

    // 处理 500 服务器错误
    if (status >= 500) {
      return Promise.reject({
        code: 500,
        message: '服务器错误，请稍后重试',
        data: null,
      });
    }

    // 返回后端错误信息
    return Promise.reject({
      code: status,
      message: data?.message || '请求失败',
      data: data || null,
    });
  }
);

// 取消所有请求
export const cancelAllRequests = () => {
  pendingRequests.forEach((controller) => {
    controller.abort();
  });
  pendingRequests.clear();
};

// 导出 axios 实例
export default axiosInstance;

// 导出类型
export type { AxiosInstance, AxiosRequestConfig, AxiosResponse, AxiosError };
