import { useState } from 'react';
import { Modal } from './Modal';
import { Button } from './Button';
import { Input } from './Input';
import { login, register } from '../services/modules/auth';

interface AuthModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSuccess?: () => void;
}

export const AuthModal = ({ isOpen, onClose, onSuccess }: AuthModalProps) => {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [mode, setMode] = useState<'login' | 'register'>('login');

  // 统一表单状态
  const [form, setForm] = useState({
    username: '',
    password: '',
    nickname: '',
    phone: '',
    email: '',
    code: '',
  });

  // 表单验证错误
  const [formErrors, setFormErrors] = useState<{
    username?: string;
    password?: string;
    nickname?: string;
    phone?: string;
    email?: string;
    code?: string;
  }>({});

  // 验证码相关
  const [codeSent, setCodeSent] = useState(false);
  const [countdown, setCountdown] = useState(0);
  const [resetMode, setResetMode] = useState(false);

  // 验证用户名
  const validateUsername = (value: string) => {
    if (!value) return '请输入用户名';
    if (value.length < 3) return '用户名至少3个字符';
    if (value.length > 20) return '用户名最多20个字符';
    if (!/^[a-zA-Z0-9_]+$/.test(value)) return '用户名只能包含字母、数字和下划线';
    return '';
  };

  // 验证密码
  const validatePassword = (value: string) => {
    if (!value) return '请输入密码';
    if (value.length < 6) return '密码至少6个字符';
    if (value.length > 20) return '密码最多20个字符';
    return '';
  };

  // 验证昵称
  const validateNickname = (value: string) => {
    if (!value) return '请输入昵称';
    if (value.length > 30) return '昵称最多30个字符';
    return '';
  };

  // 验证手机号
  const validatePhone = (value: string) => {
    if (!value) return '请输入手机号';
    if (!/^1[3-9]\d{9}$/.test(value)) return '请输入有效的手机号';
    return '';
  };

  // 验证邮箱
  const validateEmail = (value: string) => {
    if (value && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
      return '请输入有效的邮箱地址';
    }
    return '';
  };

  // 验证验证码
  const validateCode = (value: string) => {
    if (!value) return '请输入验证码';
    if (value.length !== 6) return '验证码必须是6位数字';
    return '';
  };

  // 处理登录
  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    const usernameError = validateUsername(form.username);
    const passwordError = validatePassword(form.password);

    setFormErrors({ username: usernameError, password: passwordError });

    if (usernameError || passwordError) return;

    setIsLoading(true);
    try {
      await login({ username: form.username, password: form.password });
      onSuccess?.();
      onClose();
    } catch (err: any) {
      setError(err.response?.data?.message || '登录失败，请检查用户名和密码');
    } finally {
      setIsLoading(false);
    }
  };

  // 处理注册
  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    const usernameError = validateUsername(form.username);
    const passwordError = validatePassword(form.password);
    const nicknameError = validateNickname(form.nickname);
    const phoneError = validatePhone(form.phone);
    const emailError = validateEmail(form.email);

    setFormErrors({
      username: usernameError,
      password: passwordError,
      nickname: nicknameError,
      phone: phoneError,
      email: emailError,
    });

    if (usernameError || passwordError || nicknameError || phoneError || emailError) return;

    setIsLoading(true);
    try {
      await register({
        username: form.username,
        password: form.password,
        nickname: form.nickname,
        phone: form.phone,
        email: form.email,
      });
      onSuccess?.();
      onClose();
    } catch (err: any) {
      setError(err.response?.data?.message || '注册失败，请重试');
    } finally {
      setIsLoading(false);
    }
  };

  // 处理发送验证码
  const handleSendCode = async () => {
    const phoneError = validatePhone(form.phone);
    setFormErrors({ phone: phoneError });

    if (phoneError) return;

    try {
      // TODO: 调用发送验证码 API
      setCodeSent(true);
      setCountdown(60);
      const timer = setInterval(() => {
        setCountdown((prev) => {
          if (prev <= 1) {
            clearInterval(timer);
            setCodeSent(false);
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
    } catch (err: any) {
      setError('发送验证码失败，请重试');
    }
  };

  // 处理重置密码
  const handleResetPassword = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    const phoneError = validatePhone(form.phone);
    const codeError = validateCode(form.code);
    const passwordError = validatePassword(form.password);

    setFormErrors({
      phone: phoneError,
      code: codeError,
      password: passwordError,
    });

    if (phoneError || codeError || passwordError) return;

    setIsLoading(true);
    try {
      // TODO: 调用重置密码 API
      setResetMode(false);
      setError('');
    } catch (err: any) {
      setError('重置密码失败，请重试');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} className="glass-card">
      {/* Header */}
      <div className="border-b border-gray-200 px-8 py-4">
        <h2 className="text-xl font-semibold text-gray-900">
          {resetMode ? '重置密码' : (mode === 'login' ? '登录' : '注册')}
        </h2>
      </div>

      {/* Content */}
      <div className="px-8 py-6">
        {error && (
          <div className="mb-6 rounded-lg bg-red-50 p-4 text-sm text-red-600" role="alert">
            {error}
          </div>
        )}

        <form onSubmit={resetMode ? handleResetPassword : (mode === 'login' ? handleLogin : handleRegister)} className="space-y-5">
          {resetMode ? (
            <>
              <Input
                id="reset-phone"
                label="手机号"
                type="tel"
                placeholder="请输入手机号"
                value={form.phone}
                onChange={(e) => setForm({ ...form, phone: e.target.value })}
                onBlur={() => setFormErrors({ ...formErrors, phone: validatePhone(form.phone) })}
                error={formErrors.phone}
                autoComplete="tel"
                required
              />

              <div className="space-y-2">
                <label htmlFor="reset-code" className="block text-sm font-medium text-gray-700">
                  验证码
                </label>
                <div className="flex gap-3">
                  <Input
                    id="reset-code"
                    type="text"
                    placeholder="请输入验证码"
                    value={form.code}
                    onChange={(e) => setForm({ ...form, code: e.target.value })}
                    onBlur={() => setFormErrors({ ...formErrors, code: validateCode(form.code) })}
                    error={formErrors.code}
                    maxLength={6}
                    required
                  />
                  <Button
                    type="button"
                    variant={codeSent ? 'secondary' : 'primary'}
                    disabled={codeSent || countdown > 0}
                    onClick={handleSendCode}
                    className="whitespace-nowrap px-4"
                  >
                    {countdown > 0 ? `${countdown}秒` : codeSent ? '已发送' : '发送验证码'}
                  </Button>
                </div>
              </div>

              <Input
                id="reset-password"
                label="新密码"
                type="password"
                placeholder="请输入新密码"
                value={form.password}
                onChange={(e) => setForm({ ...form, password: e.target.value })}
                onBlur={() => setFormErrors({ ...formErrors, password: validatePassword(form.password) })}
                error={formErrors.password}
                helperText="密码至少6个字符"
                autoComplete="new-password"
                required
              />

              <Button
                type="submit"
                variant="primary"
                size="lg"
                className="w-full"
                isLoading={isLoading}
              >
                重置密码
              </Button>
            </>
          ) : (
            <>
              <Input
                id="username"
                label="用户名"
                type="text"
                placeholder="请输入用户名"
                value={form.username}
                onChange={(e) => setForm({ ...form, username: e.target.value })}
                onBlur={() => setFormErrors({ ...formErrors, username: validateUsername(form.username) })}
                error={formErrors.username}
                helperText={mode === 'register' ? '用户名只能包含字母、数字和下划线' : ''}
                autoComplete="username"
                required
              />

              <Input
                id="password"
                label="密码"
                type="password"
                placeholder="请输入密码"
                value={form.password}
                onChange={(e) => setForm({ ...form, password: e.target.value })}
                onBlur={() => setFormErrors({ ...formErrors, password: validatePassword(form.password) })}
                error={formErrors.password}
                helperText="密码至少6个字符"
                autoComplete={mode === 'login' ? 'current-password' : 'new-password'}
                required
              />

              {mode === 'register' && (
                <>
                  <Input
                    id="nickname"
                    label="昵称"
                    type="text"
                    placeholder="请输入昵称"
                    value={form.nickname}
                    onChange={(e) => setForm({ ...form, nickname: e.target.value })}
                    onBlur={() => setFormErrors({ ...formErrors, nickname: validateNickname(form.nickname) })}
                    error={formErrors.nickname}
                    autoComplete="nickname"
                    required
                  />

                  <Input
                    id="phone"
                    label="手机号"
                    type="tel"
                    placeholder="请输入手机号"
                    value={form.phone}
                    onChange={(e) => setForm({ ...form, phone: e.target.value })}
                    onBlur={() => setFormErrors({ ...formErrors, phone: validatePhone(form.phone) })}
                    error={formErrors.phone}
                    autoComplete="tel"
                    required
                  />

                  <Input
                    id="email"
                    label="邮箱（选填）"
                    type="email"
                    placeholder="请输入邮箱"
                    value={form.email}
                    onChange={(e) => setForm({ ...form, email: e.target.value })}
                    onBlur={() => setFormErrors({ ...formErrors, email: validateEmail(form.email) })}
                    error={formErrors.email}
                    autoComplete="email"
                  />
                </>
              )}

              {mode === 'login' && (
                <div className="flex items-center justify-between">
                  <label className="flex items-center gap-2 cursor-pointer">
                    <input
                      type="checkbox"
                      className="h-4 w-4 rounded border-gray-300 text-primary-400 focus:ring-primary-400"
                    />
                    <span className="text-sm text-gray-600">记住我</span>
                  </label>
                  <button
                    type="button"
                    onClick={() => setResetMode(true)}
                    className="text-sm text-primary-400 hover:text-primary-600 transition-colors"
                  >
                    忘记密码？
                  </button>
                </div>
              )}

              <Button
                type="submit"
                variant="primary"
                size="lg"
                className="w-full"
                isLoading={isLoading}
              >
                {mode === 'login' ? '登录' : '注册'}
              </Button>
            </>
          )}
        </form>
      </div>

      {/* Footer */}
      {!resetMode && (
        <div className="border-t border-gray-200 px-8 py-4">
          <p className="text-center text-sm text-gray-600">
            {mode === 'login' ? (
              <>
                还没有账号？{' '}
                <button
                  type="button"
                  onClick={() => setMode('register')}
                  className="font-semibold text-primary-400 hover:text-primary-600 transition-colors"
                >
                  立即注册
                </button>
              </>
            ) : (
              <>
                已有账号？{' '}
                <button
                  type="button"
                  onClick={() => setMode('login')}
                  className="font-semibold text-primary-400 hover:text-primary-600 transition-colors"
                >
                  立即登录
                </button>
              </>
            )}
          </p>
        </div>
      )}

      {resetMode && (
        <div className="px-8 py-4">
          <button
            type="button"
            onClick={() => setResetMode(false)}
            className="w-full text-sm text-center text-gray-600 hover:text-gray-900 transition-colors"
          >
            返回登录
          </button>
        </div>
      )}
    </Modal>
  );
};

AuthModal.displayName = 'AuthModal';
