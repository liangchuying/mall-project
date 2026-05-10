import { useState } from 'react';
import { Modal } from './Modal';
import { Button } from './Button';
import { Input } from './Input';
import { login, register } from '../services/modules/auth';

type AuthTab = 'login' | 'register' | 'forgot';

interface AuthModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSuccess?: () => void;
}

export const AuthModal = ({ isOpen, onClose, onSuccess }: AuthModalProps) => {
  const [activeTab, setActiveTab] = useState<AuthTab>('login');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  // 登录表单状态
  const [loginForm, setLoginForm] = useState({
    username: '',
    password: '',
  });
  const [loginErrors, setLoginErrors] = useState<{
    username?: string;
    password?: string;
  }>({});

  // 注册表单状态
  const [registerForm, setRegisterForm] = useState({
    username: '',
    password: '',
    nickname: '',
    phone: '',
    email: '',
  });
  const [registerErrors, setRegisterErrors] = useState<{
    username?: string;
    password?: string;
    nickname?: string;
    phone?: string;
    email?: string;
  }>({});

  // 忘记密码表单状态
  const [forgotForm, setForgotForm] = useState({
    phone: '',
    code: '',
    newPassword: '',
  });
  const [forgotErrors, setForgotErrors] = useState<{
    phone?: string;
    code?: string;
    newPassword?: string;
  }>({});
  const [codeSent, setCodeSent] = useState(false);
  const [countdown, setCountdown] = useState(0);

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

    // 验证
    const usernameError = validateUsername(loginForm.username);
    const passwordError = validatePassword(loginForm.password);

    setLoginErrors({
      username: usernameError,
      password: passwordError,
    });

    if (usernameError || passwordError) return;

    setIsLoading(true);
    try {
      await login({
        username: loginForm.username,
        password: loginForm.password,
      });
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

    // 验证
    const usernameError = validateUsername(registerForm.username);
    const passwordError = validatePassword(registerForm.password);
    const nicknameError = validateNickname(registerForm.nickname);
    const phoneError = validatePhone(registerForm.phone);
    const emailError = validateEmail(registerForm.email);

    setRegisterErrors({
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
        username: registerForm.username,
        password: registerForm.password,
        nickname: registerForm.nickname,
        phone: registerForm.phone,
        email: registerForm.email,
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
    const phoneError = validatePhone(forgotForm.phone);
    setForgotErrors({ phone: phoneError });

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

    // 验证
    const phoneError = validatePhone(forgotForm.phone);
    const codeError = validateCode(forgotForm.code);
    const passwordError = validatePassword(forgotForm.newPassword);

    setForgotErrors({
      phone: phoneError,
      code: codeError,
      newPassword: passwordError,
    });

    if (phoneError || codeError || passwordError) return;

    setIsLoading(true);
    try {
      // TODO: 调用重置密码 API
      setActiveTab('login');
      setError('');
    } catch (err: any) {
      setError('重置密码失败，请重试');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} className="glass-card">
      {/* Header Tabs */}
      <div className="border-b border-gray-200">
        <div className="flex">
          {(['login', 'register', 'forgot'] as AuthTab[]).map((tab) => (
            <button
              key={tab}
              onClick={() => {
                setActiveTab(tab);
                setError('');
              }}
              className={`flex-1 border-b-2 px-6 py-4 text-sm font-semibold transition-colors
                ${
                  activeTab === tab
                    ? 'border-primary-400 text-primary-500'
                    : 'border-transparent text-gray-500 hover:text-gray-700'
                }
              `}
            >
              {tab === 'login' && '登录'}
              {tab === 'register' && '注册'}
              {tab === 'forgot' && '忘记密码'}
            </button>
          ))}
        </div>
      </div>

      {/* Content */}
      <div className="px-8 py-6">
        {error && (
          <div className="mb-6 rounded-lg bg-red-50 p-4 text-sm text-red-600" role="alert">
            {error}
          </div>
        )}

        {activeTab === 'login' && (
          <form onSubmit={handleLogin} className="space-y-5">
            <Input
              id="login-username"
              label="用户名"
              type="text"
              placeholder="请输入用户名"
              value={loginForm.username}
              onChange={(e) => setLoginForm({ ...loginForm, username: e.target.value })}
              onBlur={() =>
                setLoginErrors({
                  ...loginErrors,
                  username: validateUsername(loginForm.username),
                })
              }
              error={loginErrors.username}
              autoComplete="username"
              required
            />

            <Input
              id="login-password"
              label="密码"
              type="password"
              placeholder="请输入密码"
              value={loginForm.password}
              onChange={(e) => setLoginForm({ ...loginForm, password: e.target.value })}
              onBlur={() =>
                setLoginErrors({
                  ...loginErrors,
                  password: validatePassword(loginForm.password),
                })
              }
              error={loginErrors.password}
              helperText="密码至少6个字符"
              autoComplete="current-password"
              required
            />

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
                onClick={() => setActiveTab('forgot')}
                className="text-sm text-primary-400 hover:text-primary-600 transition-colors"
              >
                忘记密码？
              </button>
            </div>

            <Button
              type="submit"
              variant="primary"
              size="lg"
              className="w-full"
              isLoading={isLoading}
            >
              登录
            </Button>
          </form>
        )}

        {activeTab === 'register' && (
          <form onSubmit={handleRegister} className="space-y-5">
            <Input
              id="register-username"
              label="用户名"
              type="text"
              placeholder="请输入用户名"
              value={registerForm.username}
              onChange={(e) =>
                setRegisterForm({ ...registerForm, username: e.target.value })
              }
              onBlur={() =>
                setRegisterErrors({
                  ...registerErrors,
                  username: validateUsername(registerForm.username),
                })
              }
              error={registerErrors.username}
              helperText="用户名只能包含字母、数字和下划线"
              autoComplete="username"
              required
            />

            <Input
              id="register-password"
              label="密码"
              type="password"
              placeholder="请输入密码"
              value={registerForm.password}
              onChange={(e) =>
                setRegisterForm({ ...registerForm, password: e.target.value })
              }
              onBlur={() =>
                setRegisterErrors({
                  ...registerErrors,
                  password: validatePassword(registerForm.password),
                })
              }
              error={registerErrors.password}
              helperText="密码至少6个字符"
              autoComplete="new-password"
              required
            />

            <Input
              id="register-nickname"
              label="昵称"
              type="text"
              placeholder="请输入昵称"
              value={registerForm.nickname}
              onChange={(e) =>
                setRegisterForm({ ...registerForm, nickname: e.target.value })
              }
              onBlur={() =>
                setRegisterErrors({
                  ...registerErrors,
                  nickname: validateNickname(registerForm.nickname),
                })
              }
              error={registerErrors.nickname}
              autoComplete="nickname"
              required
            />

            <Input
              id="register-phone"
              label="手机号"
              type="tel"
              placeholder="请输入手机号"
              value={registerForm.phone}
              onChange={(e) =>
                setRegisterForm({ ...registerForm, phone: e.target.value })
              }
              onBlur={() =>
                setRegisterErrors({
                  ...registerErrors,
                  phone: validatePhone(registerForm.phone),
                })
              }
              error={registerErrors.phone}
              autoComplete="tel"
              required
            />

            <Input
              id="register-email"
              label="邮箱（选填）"
              type="email"
              placeholder="请输入邮箱"
              value={registerForm.email}
              onChange={(e) =>
                setRegisterForm({ ...registerForm, email: e.target.value })
              }
              onBlur={() =>
                setRegisterErrors({
                  ...registerErrors,
                  email: validateEmail(registerForm.email),
                })
              }
              error={registerErrors.email}
              autoComplete="email"
            />

            <Button
              type="submit"
              variant="primary"
              size="lg"
              className="w-full"
              isLoading={isLoading}
            >
              注册
            </Button>
          </form>
        )}

        {activeTab === 'forgot' && (
          <form onSubmit={handleResetPassword} className="space-y-5">
            <Input
              id="forgot-phone"
              label="手机号"
              type="tel"
              placeholder="请输入手机号"
              value={forgotForm.phone}
              onChange={(e) =>
                setForgotForm({ ...forgotForm, phone: e.target.value })
              }
              onBlur={() =>
                setForgotErrors({
                  ...forgotErrors,
                  phone: validatePhone(forgotForm.phone),
                })
              }
              error={forgotErrors.phone}
              autoComplete="tel"
              required
            />

            <div className="space-y-2">
              <label htmlFor="forgot-code" className="block text-sm font-medium text-gray-700">
                验证码
              </label>
              <div className="flex gap-3">
                <Input
                  id="forgot-code"
                  type="text"
                  placeholder="请输入验证码"
                  value={forgotForm.code}
                  onChange={(e) =>
                    setForgotForm({ ...forgotForm, code: e.target.value })
                  }
                  onBlur={() =>
                    setForgotErrors({
                      ...forgotErrors,
                      code: validateCode(forgotForm.code),
                    })
                  }
                  error={forgotErrors.code}
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
              id="forgot-password"
              label="新密码"
              type="password"
              placeholder="请输入新密码"
              value={forgotForm.newPassword}
              onChange={(e) =>
                setForgotForm({ ...forgotForm, newPassword: e.target.value })
              }
              onBlur={() =>
                setForgotErrors({
                  ...forgotErrors,
                  newPassword: validatePassword(forgotForm.newPassword),
                })
              }
              error={forgotErrors.newPassword}
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

            <div className="text-center">
              <button
                type="button"
                onClick={() => setActiveTab('login')}
                className="text-sm text-primary-400 hover:text-primary-600 transition-colors"
              >
                返回登录
              </button>
            </div>
          </form>
        )}
      </div>

      {/* Footer */}
      {activeTab !== 'forgot' && (
        <div className="border-t border-gray-200 px-8 py-4">
          <p className="text-center text-sm text-gray-600">
            {activeTab === 'login' ? (
              <>
                还没有账号？{' '}
                <button
                  type="button"
                  onClick={() => setActiveTab('register')}
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
                  onClick={() => setActiveTab('login')}
                  className="font-semibold text-primary-400 hover:text-primary-600 transition-colors"
                >
                  立即登录
                </button>
              </>
            )}
          </p>
        </div>
      )}
    </Modal>
  );
};

AuthModal.displayName = 'AuthModal';
