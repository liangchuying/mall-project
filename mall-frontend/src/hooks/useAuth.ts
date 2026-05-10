import { useUserStore } from '../store/userStore';

export function useAuth() {
  const user = useUserStore((state) => state.user);
  const token = useUserStore((state) => state.token);
  const setUser = useUserStore((state) => state.setUser);
  const setToken = useUserStore((state) => state.setToken);
  const logout = useUserStore((state) => state.logout);

  return {
    user,
    token,
    isAuthenticated: !!token,
    setUser,
    setToken,
    logout,
  };
}
