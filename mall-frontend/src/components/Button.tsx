import { ButtonHTMLAttributes, forwardRef } from 'react';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'ghost' | 'cta';
  size?: 'sm' | 'md' | 'lg';
  isLoading?: boolean;
}

const sizeClasses = {
  sm: 'px-3 py-1.5 text-sm',
  md: 'px-6 py-3 text-sm',
  lg: 'px-8 py-4 text-base',
};

const variantClasses = {
  primary:
    'bg-primary-400 text-white hover:bg-primary-600 active:scale-[0.98] disabled:bg-gray-400 disabled:cursor-not-allowed',
  secondary:
    'bg-white text-primary-400 border-2 border-primary-400 hover:bg-primary-50 active:scale-[0.98] disabled:border-gray-400 disabled:text-gray-400 disabled:cursor-not-allowed',
  ghost:
    'bg-transparent text-primary-400 hover:bg-primary-50 active:scale-[0.98] disabled:text-gray-400 disabled:cursor-not-allowed',
  cta:
    'bg-accent-coral text-white hover:bg-accent-coral-hover active:scale-[0.98] shadow-cta disabled:bg-gray-400 disabled:shadow-none disabled:cursor-not-allowed',
};

export const Button = forwardRef<HTMLButtonElement, ButtonProps>(
  ({ variant = 'primary', size = 'md', isLoading, children, className = '', disabled, ...props }, ref) => {
    return (
      <button
        ref={ref}
        className={`rounded-lg font-semibold transition-all duration-200 ${sizeClasses[size]} ${variantClasses[variant]} ${className}`}
        disabled={disabled || isLoading}
        {...props}
      >
        {isLoading ? (
          <span className="flex items-center justify-center gap-2">
            <svg
              className="animate-spin h-4 w-4"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
            >
              <circle
                className="opacity-25"
                cx="12"
                cy="12"
                r="10"
                stroke="currentColor"
                strokeWidth="4"
              ></circle>
              <path
                className="opacity-75"
                fill="currentColor"
                d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
              ></path>
            </svg>
            加载中...
          </span>
        ) : (
          children
        )}
      </button>
    );
  }
);

Button.displayName = 'Button';
