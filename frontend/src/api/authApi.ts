import apiClient from './client';
import type { RegisterRequest, User } from '../types';

export const authApi = {
  register: (data: RegisterRequest) =>
    apiClient.post<User>('/api/v1/auth/register', data),
};
