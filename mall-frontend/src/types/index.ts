export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  stock: number;
  image: string;
  categoryId: number;
  status: number;
  createdAt: string;
  updatedAt: string;
}

export interface ProductSku {
  id: number;
  productId: number;
  skuName: string;
  price: number;
  stock: number;
  attributes: Record<string, string>;
}

export interface Category {
  id: number;
  name: string;
  parentId: number | null;
  sortOrder: number;
}

export interface Coupon {
  id: number;
  name: string;
  type: number;
  discount: number;
  minAmount: number;
  maxDiscount: number;
  total: number;
  used: number;
  startTime: string;
  endTime: string;
  status: number;
}

export interface Order {
  id: string;
  userId: number;
  totalAmount: number;
  discountAmount: number;
  actualAmount: number;
  status: number;
  paymentMethod: string;
  items: OrderItem[];
  address: OrderAddress;
  createdAt: string;
}

export interface OrderItem {
  id: number;
  productId: number;
  productName: string;
  productImage: string;
  skuId: number;
  skuName: string;
  price: number;
  quantity: number;
}

export interface OrderAddress {
  receiverName: string;
  receiverPhone: string;
  province: string;
  city: string;
  district: string;
  detail: string;
}

export interface PageResponse<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

export interface ApiResponse<T = any> {
  code: number;
  message: string;
  data: T;
}
