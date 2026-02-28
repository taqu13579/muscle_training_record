import { useState } from 'react';
import { authApi } from '../api/authApi';
import type { RegisterRequest, User } from '../types';

interface RegisterError {
  message: string;
  details?: string[];
}

export function useRegister() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<RegisterError | null>(null);

  const register = async (data: RegisterRequest): Promise<User | null> => {
    setLoading(true);
    setError(null);
    try {
      const response = await authApi.register(data);
      return response.data;
    } catch (err: unknown) {
      const axiosError = err as { response?: { data?: { message?: string; details?: string[] } } };
      const message = axiosError.response?.data?.message || '登録に失敗しました';
      const details = axiosError.response?.data?.details;
      setError({ message, details });
      return null;
    } finally {
      setLoading(false);
    }
  };

  const clearError = () => setError(null);

  return { register, loading, error, clearError };
}
