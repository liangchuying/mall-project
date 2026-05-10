import { post, patch } from '../api';

/**
 * 用户登录
 * @param data 登录数据
 * @returns Promise<{ token: string; user: any }>
 */
export const login = (data: {
  username: string;
  password: string;
}): Promise<{ token: string; user: any }> => {
  return post<{ token: string; user: any }>('/auth/login', data);
};

/**
 * 用户注册
 * @param data 注册数据
 * @returns Promise<any>
 */
export const register = (data: {
  username: string;
  password: string;
  email?: string;
  phone?: string;
}): Promise<any> => {
  return post<any>('/auth/register', data);
};

/**
 * 退出登录
 * @returns Promise<void>
 */
export const logout = (): Promise<void> => {
  return post<void>('/auth/logout');
};

/**
 * 刷新token
 * @param refreshToken 刷新token
 * @returns Promise<{ token: string }>
 */
export const refreshToken = (refreshToken: string): Promise<{ token: string }> => {
  return post<{ token: string }>('/auth/refresh', { refreshToken });
};

/**
 * 修改密码
 * @param data 密码数据
 * @returns Promise<void>
 */
export const changePassword = (data: {
  oldPassword: string;
  newPassword: string;
}): Promise<void> => {
  return patch<void>('/auth/password', data);
};

/**
 * 忘记密码
 * @param email 邮箱
 * @returns Promise<void>
 */
export const forgotPassword = (email: string): Promise<void> => {
  return post<void>('/auth/forgot-password', { email });
};

/**
 * 重置密码
 * @param data 重置密码数据
 * @returns Promise<void>
 */
export const resetPassword = (data: {
  token: string;
  newPassword: string;
}): Promise<void> => {
  return post<void>('/auth/reset-password', data);
};
