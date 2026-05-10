import { useState } from 'react';
import { AuthModal } from '../components/AuthModal';
import { Button } from '../components';

export default function Demo() {
  const [isAuthModalOpen, setIsAuthModalOpen] = useState(false);

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 px-4">
      <div className="w-full max-w-md">
        <h1 className="mb-8 text-center text-3xl font-bold text-gray-900">
          用户认证弹窗示例
        </h1>

        <div className="glass-card space-y-6 p-8">
          <p className="text-center text-gray-600">
            点击下方按钮打开认证弹窗，体验完整的登录、注册和忘记密码功能。
          </p>

          <div className="flex gap-4">
            <Button
              variant="primary"
              size="lg"
              className="flex-1"
              onClick={() => setIsAuthModalOpen(true)}
            >
              打开登录弹窗
            </Button>
          </div>

          <div className="space-y-3 rounded-lg bg-blue-50 p-4 text-sm text-blue-800">
            <h3 className="font-semibold">功能特性：</h3>
            <ul className="ml-4 list-disc space-y-1">
              <li>登录 / 注册 / 忘记密码三个 Tab 切换</li>
              <li>实时表单验证（Blur 时触发）</li>
              <li>错误提示和辅助文字</li>
              <li>记住我选项</li>
              <li>验证码倒计时</li>
              <li>玻璃拟态设计风格</li>
              <li>响应式设计，支持移动端</li>
              <li>键盘无障碍支持（ESC 关闭、Tab 导航）</li>
              <li>加载状态显示</li>
            </ul>
          </div>
        </div>
      </div>

      <AuthModal
        isOpen={isAuthModalOpen}
        onClose={() => setIsAuthModalOpen(false)}
        onSuccess={() => {
          console.log('认证成功！');
          setIsAuthModalOpen(false);
        }}
      />
    </div>
  );
}
