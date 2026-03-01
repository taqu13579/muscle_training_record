import { useState } from 'react';
import { authApi } from '../api/authApi';
import { useAuth } from '../contexts/AuthContext';
import type { LoginRequest } from '../types';

interface LoginError {
  message: string;
  details?: string[];
}

export function useLogin() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<LoginError | null>(null);
  const { login: setAuth } = useAuth();

  const login = async (data: LoginRequest): Promise<boolean> => {
    setLoading(true);
    setError(null);
    try {
      const response = await authApi.login(data);
      setAuth(response.data.user, response.data.accessToken);
      return true;
    } catch (err: unknown) {
      const axiosError = err as {
        response?: { data?: { message?: string; details?: string[] } };
      };
      const message =
        axiosError.response?.data?.message || 'ログインに失敗しました';
      const details = axiosError.response?.data?.details;
      setError({ message, details });
      return false;
    } finally {
      setLoading(false);
    }
  };

  const clearError = () => setError(null);

  return { login, loading, error, clearError };
}
