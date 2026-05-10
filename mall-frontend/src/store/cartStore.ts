import { create } from 'zustand';

export interface CartItem {
  id: number;
  productId: number;
  productName: string;
  productImage: string;
  price: number;
  quantity: number;
  skuId: number;
  skuName: string;
}

interface CartState {
  items: CartItem[];
  total: number;
  addItem: (item: CartItem) => void;
  removeItem: (id: number) => void;
  updateQuantity: (id: number, quantity: number) => void;
  clearCart: () => void;
  calculateTotal: () => void;
}

export const useCartStore = create<CartState>((set, get) => ({
  items: [],
  total: 0,
  addItem: (item) => {
    const items = get().items;
    const existingItem = items.find((i) => i.skuId === item.skuId);
    if (existingItem) {
      set({
        items: items.map((i) =>
          i.skuId === item.skuId ? { ...i, quantity: i.quantity + item.quantity } : i
        ),
      });
    } else {
      set({ items: [...items, item] });
    }
    get().calculateTotal();
  },
  removeItem: (id) => {
    const items = get().items.filter((item) => item.id !== id);
    set({ items });
    get().calculateTotal();
  },
  updateQuantity: (id, quantity) => {
    const items = get().items.map((item) =>
      item.id === id ? { ...item, quantity } : item
    );
    set({ items });
    get().calculateTotal();
  },
  clearCart: () => {
    set({ items: [], total: 0 });
  },
  calculateTotal: () => {
    const total = get().items.reduce(
      (sum, item) => sum + item.price * item.quantity,
      0
    );
    set({ total });
  },
}));
