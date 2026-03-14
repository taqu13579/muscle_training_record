import apiClient from './client';
import type { BodyWeight, CreateBodyWeightRequest } from '../types';

export const bodyWeightApi = {
  getAll: () => apiClient.get<BodyWeight[]>('/api/v1/body-weight'),

  getByDateRange: (startDate: string, endDate: string) =>
    apiClient.get<BodyWeight[]>('/api/v1/body-weight/range', {
      params: { startDate, endDate },
    }),

  create: (data: CreateBodyWeightRequest) =>
    apiClient.post<BodyWeight>('/api/v1/body-weight', data),

  delete: (id: number) => apiClient.delete(`/api/v1/body-weight/${id}`),
};
