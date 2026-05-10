import { Outlet, Link, useLocation } from 'react-router-dom';

const navLinks = [
  { path: '/', label: '首页' },
  { path: '/products', label: '商品' },
  { path: '/categories', label: '分类' },
  { path: '/coupons', label: '优惠券' },
];

export default function Layout() {
  const location = useLocation();
  const isHomePage = location.pathname === '/';

  return (
    <div className="min-h-screen bg-gray-50">
      {/* 导航栏 - 全宽 */}
      <nav className="h-16 sticky top-0 z-50 bg-white/80 backdrop-blur-lg border-b border-gray-100">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-full">
          <div className="flex items-center justify-between h-full">
            <div className="flex items-center space-x-8">
              <Link
                to="/"
                className="text-xl font-bold text-gray-900 hover:text-primary-400 transition-colors"
              >
                购物商城
              </Link>
              <div className="hidden md:flex space-x-1">
                {navLinks.map((link) => (
                  <Link
                    key={link.path}
                    to={link.path}
                    className={`px-4 py-2 rounded-lg text-sm font-medium transition-all duration-300 ${
                      location.pathname === link.path
                        ? 'bg-primary-50 text-primary-600'
                        : 'text-gray-600 hover:text-primary-400 hover:bg-gray-50'
                    }`}
                  >
                    {link.label}
                  </Link>
                ))}
              </div>
            </div>
            <div className="flex items-center space-x-4">
              <Link
                to="/cart"
                className="w-10 h-10 flex items-center justify-center text-gray-600 hover:text-primary-400 rounded-full hover:bg-gray-100 transition-colors"
              >
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                </svg>
              </Link>
              <Link
                to="/login"
                className="px-5 py-2 bg-primary-400 text-white rounded-lg text-sm font-semibold hover:bg-primary-600 transition-colors"
              >
                登录
              </Link>
            </div>
          </div>
        </div>
      </nav>

      {/* 主内容区域 */}
      <main>
        <Outlet />
      </main>

      {/* 页脚 - 全宽 */}
      <footer className="bg-white border-t border-gray-100 mt-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <p className="text-center text-gray-500 text-sm">
            © 2026 购物商城. All rights reserved.
          </p>
        </div>
      </footer>
    </div>
  );
}
