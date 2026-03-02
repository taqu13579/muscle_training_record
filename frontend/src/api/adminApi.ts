import apiClient from './client';
import type { User, BodyPart } from '../types';

export const adminApi = {
  getUsers: () => apiClient.get<User[]>('/api/v1/admin/users'),

  updateUserRole: (userId: number, role: string) =>
    apiClient.patch<User>(`/api/v1/admin/users/${userId}/role`, { role }),

  createBodyPart: (name: string, displayOrder: number) =>
    apiClient.post<BodyPart>('/api/v1/body-parts', { name, displayOrder }),

  updateBodyPart: (id: number, name: string, displayOrder: number) =>
    apiClient.put<BodyPart>(`/api/v1/body-parts/${id}`, { name, displayOrder }),

  deleteBodyPart: (id: number) => apiClient.delete(`/api/v1/body-parts/${id}`),
};
