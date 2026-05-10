import { HTMLAttributes, useEffect, useRef } from 'react';
import { createPortal } from 'react-dom';

interface ModalProps extends HTMLAttributes<HTMLDivElement> {
  isOpen: boolean;
  onClose: () => void;
  closeOnBackdropClick?: boolean;
  closeOnEscape?: boolean;
}

export const Modal = ({
  isOpen,
  onClose,
  closeOnBackdropClick = true,
  closeOnEscape = true,
  children,
  className = '',
  ...props
}: ModalProps) => {
  const modalRef = useRef<HTMLDivElement>(null);

  // 处理 ESC 键关闭
  useEffect(() => {
    if (!closeOnEscape) return;

    const handleEscape = (e: KeyboardEvent) => {
      if (e.key === 'Escape' && isOpen) {
        onClose();
      }
    };

    window.addEventListener('keydown', handleEscape);
    return () => window.removeEventListener('keydown', handleEscape);
  }, [isOpen, closeOnEscape, onClose]);

  // 锁定背景滚动
  useEffect(() => {
    if (isOpen) {
      document.body.style.overflow = 'hidden';
      return () => {
        document.body.style.overflow = '';
      };
    }
  }, [isOpen]);

  // 聚焦管理
  useEffect(() => {
    if (isOpen && modalRef.current) {
      const focusableElements = modalRef.current.querySelectorAll<HTMLElement>(
        'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])'
      );
      const firstElement = focusableElements[0];
      if (firstElement) firstElement.focus();
    }
  }, [isOpen]);

  if (!isOpen) return null;

  const handleBackdropClick = (e: React.MouseEvent<HTMLDivElement>) => {
    if (e.target === e.currentTarget && closeOnBackdropClick) {
      onClose();
    }
  };

  return createPortal(
    <div
      role="dialog"
      aria-modal="true"
      className="fixed inset-0 z-50 flex items-center justify-center p-4"
      onClick={handleBackdropClick}
    >
      {/* Backdrop */}
      <div className="absolute inset-0 bg-gray-900/50 backdrop-blur-sm transition-opacity duration-200" aria-hidden="true" />

      {/* Modal Content */}
      <div
        ref={modalRef}
        className={`relative w-full max-w-md transform overflow-hidden rounded-2xl bg-white shadow-xl transition-all duration-300 ${className}`}
        onClick={(e) => e.stopPropagation()}
        {...props}
      >
        {/* Close Button */}
        <button
          onClick={onClose}
          className="absolute right-4 top-4 rounded-full p-2 text-gray-400 transition-colors hover:bg-gray-100 hover:text-gray-600 focus:outline-none focus:ring-2 focus:ring-primary-400 focus:ring-offset-2"
          aria-label="关闭"
        >
          <svg
            className="h-5 w-5"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M6 18L18 6M6 6l12 12"
            />
          </svg>
        </button>

        {children}
      </div>
    </div>,
    document.body
  );
};

Modal.displayName = 'Modal';
