import apiClient from './client';
import type { RegisterRequest, LoginRequest, LoginResponse, User } from '../types';

export const authApi = {
  register: (data: RegisterRequest) =>
    apiClient.post<User>('/api/v1/auth/register', data),

  login: (data: LoginRequest) =>
    apiClient.post<LoginResponse>('/api/v1/auth/login', data),
};
