import { HTMLAttributes, forwardRef } from 'react';

interface CardProps extends HTMLAttributes<HTMLDivElement> {
  hoverable?: boolean;
}

export const Card = forwardRef<HTMLDivElement, CardProps>(
  ({ children, hoverable = false, className = '', ...props }, ref) => {
    return (
      <div
        ref={ref}
        className={`bg-white border border-gray-200 rounded-xl p-6 transition-all duration-200
          ${hoverable ? 'hover:border-primary-400 hover:shadow-card-hover' : ''}
          ${className}
        `}
        {...props}
      >
        {children}
      </div>
    );
  }
);

Card.displayName = 'Card';
