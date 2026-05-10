import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

interface Slide {
  id: number;
  image: string;
}

const slides: Slide[] = [
  {
    id: 1,
    image: 'https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=1920&h=1080&fit=crop&auto=format',
  },
  {
    id: 2,
    image: 'https://images.unsplash.com/photo-1531297461136-82lw9z211b34?w=1920&h=1080&fit=crop&auto=format',
  },
  {
    id: 3,
    image: 'https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da?w=1920&h=1080&fit=crop&auto=format',
  },
];

const stats = [
  { value: '10K+', label: '商品数量' },
  { value: '50K+', label: '满意客户' },
  { value: '4.9', label: '用户评分' },
];

export default function HeroCarousel() {
  const [currentSlide, setCurrentSlide] = useState(0);
  const [isTransitioning, setIsTransitioning] = useState(false);

  useEffect(() => {
    const timer = setInterval(() => {
      nextSlide();
    }, 5000);

    return () => clearInterval(timer);
  }, [currentSlide]);

  const nextSlide = () => {
    if (isTransitioning) return;
    setIsTransitioning(true);
    setTimeout(() => {
      setCurrentSlide((prev) => (prev + 1) % slides.length);
      setIsTransitioning(false);
    }, 300);
  };

  const prevSlide = () => {
    if (isTransitioning) return;
    setIsTransitioning(true);
    setTimeout(() => {
      setCurrentSlide((prev) => (prev - 1 + slides.length) % slides.length);
      setIsTransitioning(false);
    }, 300);
  };

  const goToSlide = (index: number) => {
    if (isTransitioning || index === currentSlide) return;
    setIsTransitioning(true);
    setTimeout(() => {
      setCurrentSlide(index);
      setIsTransitioning(false);
    }, 300);
  };

  return (
    <section className="relative min-h-[700px] md:min-h-[800px] lg:min-h-[900px] flex items-center overflow-hidden">
      {/* 背景轮播 */}
      <div className="absolute inset-0">
        {slides.map((slide, index) => (
          <div
            key={slide.id}
            className={`absolute inset-0 transition-all duration-700 ease-in-out ${
              index === currentSlide ? 'opacity-100 scale-100' : 'opacity-0 scale-105'
            }`}
          >
            <img
              src={slide.image}
              alt=""
              className="w-full h-full object-cover"
            />
            <div className="absolute inset-0 bg-gradient-to-r from-gray-900/90 via-gray-900/70 to-gray-900/30" />
          </div>
        ))}
      </div>

      {/* 轮播控制按钮 */}
      <div className="group absolute inset-0 z-30">
        <button
          onClick={prevSlide}
          className="absolute left-6 sm:left-8 md:left-12 lg:left-16 top-1/2 -translate-y-1/2 w-11 h-11 bg-white/20 backdrop-blur-sm rounded-full flex items-center justify-center text-white opacity-0 group-hover:opacity-100 transition-all duration-300 hover:bg-white/30"
          aria-label="上一张"
        >
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
          </svg>
        </button>
        <button
          onClick={nextSlide}
          className="absolute right-6 sm:right-8 md:right-12 lg:right-16 top-1/2 -translate-y-1/2 w-11 h-11 bg-white/20 backdrop-blur-sm rounded-full flex items-center justify-center text-white opacity-0 group-hover:opacity-100 transition-all duration-300 hover:bg-white/30"
          aria-label="下一张"
        >
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
          </svg>
        </button>
      </div>

      {/* 指示器 */}
      <div className="absolute bottom-20 left-1/2 transform -translate-x-1/2 z-30 flex gap-2.5">
        {slides.map((_, index) => (
          <button
            key={index}
            onClick={() => goToSlide(index)}
            className={`w-10 h-1 rounded-full transition-all duration-300 ${
              index === currentSlide ? 'bg-white' : 'bg-white/40 hover:bg-white/60'
            }`}
            aria-label={`切换到第 ${index + 1} 张`}
          />
        ))}
      </div>

      {/* 内容区域 - 固定内容 */}
      <div className="relative z-10 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16 md:py-24">
        <div className="max-w-2xl">
          <div className="inline-flex items-center gap-2 bg-primary-400 text-white px-4 py-2 rounded-full text-sm font-semibold mb-6">
            <span className="w-2 h-2 bg-white rounded-full animate-pulse" />
            新品上市
          </div>
          <h1 className="text-4xl md:text-6xl font-bold text-white mb-4 leading-tight">
            发现精选好物
          </h1>
          <p className="text-lg md:text-xl text-gray-200 mb-8 leading-relaxed">
            探索最新科技产品，享受品质生活。每一个细节都经过精心挑选，只为给你最好的体验。
          </p>

          <div className="flex flex-wrap gap-3">
            <Link
              to="/products"
              className="group px-8 py-3.5 bg-primary-400 text-white rounded-xl font-semibold hover:bg-primary-600 transition-all duration-300 shadow-lg"
            >
              立即浏览
            </Link>
            <Link
              to="/categories"
              className="px-8 py-3.5 bg-white/20 backdrop-blur-sm text-white rounded-xl font-semibold hover:bg-white/30 transition-all duration-300"
            >
              浏览分类
            </Link>
          </div>

          <div className="flex items-center gap-8 mt-12">
            {stats.map((stat, index) => (
              <div key={index} className="bg-white/10 backdrop-blur-sm px-5 py-3 rounded-xl text-center">
                <div className="text-2xl font-bold text-white">{stat.value}</div>
                <div className="text-xs text-gray-200 mt-1">{stat.label}</div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}
