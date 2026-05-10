import { Link } from 'react-router-dom';
import HeroCarousel from '../components/HeroCarousel';

interface Product {
  id: number;
  name: string;
  price: number;
  originalPrice?: number;
  image: string;
  category: string;
  stock: number;
  badge?: string;
}

const featuredProducts: Product[] = [
  {
    id: 1,
    name: '无线蓝牙耳机 Pro Max',
    price: 299,
    originalPrice: 399,
    image: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&h=600&fit=crop',
    category: '电子产品',
    stock: 50,
    badge: '热销',
  },
  {
    id: 2,
    name: '智能运动手表 Ultra',
    price: 599,
    image: 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&h=600&fit=crop',
    category: '智能设备',
    stock: 30,
    badge: '新品',
  },
  {
    id: 3,
    name: '极简双肩背包',
    price: 199,
    originalPrice: 259,
    image: 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600&h=600&fit=crop',
    category: '箱包',
    stock: 100,
  },
  {
    id: 4,
    name: '机械键盘 RGB',
    price: 499,
    image: 'https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?w=600&h=600&fit=crop',
    category: '电脑配件',
    stock: 25,
    badge: '限时',
  },
  {
    id: 5,
    name: '无线充电器 Pro',
    price: 129,
    image: 'https://images.unsplash.com/photo-1586816879360-004f5b0c51e3?w=600&h=600&fit=crop',
    category: '配件',
    stock: 80,
  },
  {
    id: 6,
    name: '降噪耳机 Studio',
    price: 799,
    originalPrice: 999,
    image: 'https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=600&h=600&fit=crop',
    category: '音频',
    stock: 15,
    badge: '旗舰',
  },
  {
    id: 7,
    name: '智能音箱 Mini',
    price: 299,
    image: 'https://images.unsplash.com/photo-1589492477829-5e65395b66cc?w=600&h=600&fit=crop',
    category: '智能设备',
    stock: 60,
  },
  {
    id: 8,
    name: '运动相机 Action',
    price: 449,
    image: 'https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600&h=600&fit=crop',
    category: '摄影',
    stock: 35,
    badge: '热销',
  },
];

const categories = [
  { name: '电子产品', image: 'https://images.unsplash.com/photo-1498049794561-7780e7231661?w=800&h=600&fit=crop&auto=format', count: 128 },
  { name: '智能设备', image: 'https://images.unsplash.com/photo-1550009158-9ebf69173e03?w=800&h=600&fit=crop&auto=format', count: 86 },
  { name: '箱包配饰', image: 'https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=800&h=600&fit=crop&auto=format', count: 64 },
  { name: '生活家居', image: 'https://images.unsplash.com/photo-1484101403633-562f891dc89a?w=800&h=600&fit=crop&auto=format', count: 92 },
];

const features = [
  {
    icon: (
      <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
      </svg>
    ),
    title: '免费配送',
    description: '订单满 ¥99 即可享受全国免费配送服务',
  },
  {
    icon: (
      <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
      </svg>
    ),
    title: '正品保障',
    description: '100% 正品保证，假一赔十，售后无忧',
  },
  {
    icon: (
      <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
      </svg>
    ),
    title: '无忧退换',
    description: '7天无理由退换货，30天质量问题包退',
  },
  {
    icon: (
      <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M18.364 5.636l-3.536 3.536m0 5.656l3.536 3.536M9.172 9.172L5.636 5.636m3.536 9.192l-3.536 3.536M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-5 0a4 4 0 11-8 0 4 4 0 018 0z" />
      </svg>
    ),
    title: '专业客服',
    description: '7x24小时在线客服，随时为您解答疑问',
  },
];

export default function Home() {
  return (
    <div>
      {/* 全宽走马灯 */}
      <HeroCarousel />

      {/* 内容区域 - 统一最大宽度 */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <CategorySection categories={categories} />
        <FeaturedSection products={featuredProducts} />
      </div>

      {/* 全宽背景的特征区域 */}
      <FeatureSection features={features} />

      {/* 订阅区域 */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <NewsletterSection />
      </div>
    </div>
  );
}

function CategorySection({ categories }: { categories: typeof categories }) {
  return (
    <section className="py-20 md:py-24">
      <div className="flex items-center justify-between mb-12 md:mb-16">
        <div>
          <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-2">
            热门分类
          </h2>
          <p className="text-gray-500 text-base md:text-lg">探索我们精心挑选的商品分类</p>
        </div>
        <Link
          to="/categories"
          className="hidden md:flex items-center gap-2 text-primary-400 font-semibold hover:text-primary-600 transition-colors"
        >
          查看全部
          <svg className="w-5 h-5 group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 8l4 4m0 0l-4 4m4-4H3"/>
          </svg>
        </Link>
      </div>
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 md:gap-6">
        {categories.map((category, index) => (
          <Link
            key={category.name}
            to={`/categories/${encodeURIComponent(category.name)}`}
            className="group relative overflow-hidden rounded-2xl aspect-[3/2] cursor-pointer"
          >
            <img
              src={category.image}
              alt={category.name}
              className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
            />
            <div className="absolute inset-0 bg-gradient-to-t from-gray-900/85 via-gray-900/30 to-transparent transition-all duration-300 group-hover:from-gray-900/90"/>
            <div className="absolute bottom-0 left-0 right-0 p-3 sm:p-4">
              <h3 className="text-sm sm:text-base font-semibold text-white mb-1.5">{category.name}</h3>
              <div className="flex items-center gap-1.5">
                <span className="text-gray-300 text-xs">{category.count} 件商品</span>
                <svg className="w-3.5 h-3.5 text-gray-300 group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7"/>
                </svg>
              </div>
            </div>
          </Link>
        ))}
      </div>
    </section>
  );
}

function FeaturedSection({products}: { products: Product[] }) {
  return (
    <section className="py-20 md:py-24">
      <div className="flex items-center justify-between mb-12 md:mb-16">
        <div>
          <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-2">
            精选商品
          </h2>
          <p className="text-gray-500 text-base md:text-lg">本周最受欢迎的优质商品</p>
        </div>
        <Link
          to="/products"
          className="hidden md:flex items-center gap-2 text-primary-400 font-semibold hover:text-primary-600 transition-colors"
        >
          查看全部
          <svg className="w-5 h-5 group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 8l4 4m0 0l-4 4m4-4H3"/>
          </svg>
        </Link>
      </div>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 md:gap-6">
        {products.map((product) => (
          <ProductCard key={product.id} product={product}/>
        ))}
      </div>
    </section>
  );
}

function FeatureSection({features}: { features: typeof features }) {
  return (
    <section className="py-20 md:py-24 bg-gray-100">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center mb-12 md:mb-16">
          <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-3">
            为什么选择我们
          </h2>
          <p className="text-gray-500 text-base md:text-lg max-w-2xl mx-auto">
            我们致力于提供最优质的购物体验，从商品质量到售后服务，每一个环节都精心把控
          </p>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 md:gap-8">
          {features.map((feature, index) => (
            <div key={index} className="text-center group">
              <div className="inline-flex items-center justify-center w-16 h-16 bg-primary-50 rounded-2xl text-primary-400 mb-4 transition-all duration-300 group-hover:scale-110 group-hover:bg-primary-100">
                {feature.icon}
              </div>
              <h3 className="text-lg font-bold text-gray-900 mb-2">{feature.title}</h3>
              <p className="text-gray-500 text-sm leading-relaxed">{feature.description}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

function NewsletterSection() {
  return (
    <section className="py-20 md:py-24">
      <div className="max-w-4xl mx-auto text-center">
        <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-3">
          订阅我们的新闻
        </h2>
        <p className="text-gray-500 text-base md:text-lg mb-8 max-w-2xl mx-auto">
          第一时间获取新品资讯、优惠活动和购物指南，不错过任何精彩内容
        </p>
        <form className="flex flex-col sm:flex-row gap-3 max-w-xl mx-auto">
          <input
            type="email"
            placeholder="输入您的邮箱地址"
            className="flex-1 px-5 py-3.5 rounded-xl border border-gray-200 text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-primary-400 focus:border-transparent transition-all"
          />
          <button
            type="submit"
            className="px-6 py-3.5 bg-primary-400 text-white rounded-xl font-semibold hover:bg-primary-600 transition-all duration-300 hover:shadow-lg whitespace-nowrap"
          >
            立即订阅
          </button>
        </form>
      </div>
    </section>
  );
}

function ProductCard({ product }: { product: Product }) {
  const discount = product.originalPrice
    ? Math.round(((product.originalPrice - product.price) / product.originalPrice) * 100)
    : 0;

  return (
    <Link
      to={`/products/${product.id}`}
      className="group bg-white rounded-2xl overflow-hidden border border-gray-100 shadow-sm hover:shadow-2xl hover:shadow-diffuse-md transition-all duration-300"
    >
      <div className="relative aspect-square overflow-hidden bg-gray-50">
        <img
          src={product.image}
          alt={product.name}
          className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
        />
        <div className="absolute top-3 left-3 flex gap-2">
          {product.badge && (
            <span className="px-2.5 py-1 bg-gradient-to-r from-primary-400 to-primary-500 text-white text-xs font-semibold rounded-full shadow-soft-sm">
              {product.badge}
            </span>
          )}
          {discount > 0 && (
            <span className="px-2.5 py-1 bg-gradient-to-r from-red-500 to-red-600 text-white text-xs font-semibold rounded-full shadow-soft-sm">
              -{discount}%
            </span>
          )}
        </div>
      </div>
      <div className="p-4 bg-gradient-to-b from-white to-gray-50/50">
        <p className="text-xs text-primary-400 font-semibold mb-1.5 tracking-wide uppercase">{product.category}</p>
        <h3 className="text-sm font-semibold text-gray-900 mb-3 line-clamp-2 group-hover:text-primary-500 transition-colors">
          {product.name}
        </h3>
        <div className="flex items-center gap-2">
          <span className="text-lg font-bold text-gray-900">¥{product.price}</span>
          {product.originalPrice && (
            <span className="text-sm text-gray-400 line-through">¥{product.originalPrice}</span>
          )}
        </div>
      </div>
    </Link>
  );
}
