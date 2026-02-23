import apiClient from './client';
import type { Exercise, CreateExerciseRequest, UpdateExerciseRequest } from '../types';

export const exerciseApi = {
  getAll: (bodyPartId?: number) =>
    apiClient.get<Exercise[]>('/api/v1/exercises', {
      params: bodyPartId ? { bodyPartId } : undefined,
    }),

  create: (data: CreateExerciseRequest) =>
    apiClient.post<Exercise>('/api/v1/exercises', data),

  update: (id: number, data: UpdateExerciseRequest) =>
    apiClient.put<Exercise>(`/api/v1/exercises/${id}`, data),

  delete: (id: number) => apiClient.delete(`/api/v1/exercises/${id}`),
};
