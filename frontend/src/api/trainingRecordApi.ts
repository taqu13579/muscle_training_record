import apiClient from './client';
import type {
  TrainingRecord,
  CreateTrainingRecordRequest,
  UpdateTrainingRecordRequest,
  Page,
  CalendarResponse,
} from '../types';

export const trainingRecordApi = {
  getAll: (page = 0, size = 20) =>
    apiClient.get<Page<TrainingRecord>>('/api/v1/training-records', {
      params: { page, size },
    }),

  getById: (id: number) =>
    apiClient.get<TrainingRecord>(`/api/v1/training-records/${id}`),

  getByDate: (date: string) =>
    apiClient.get<TrainingRecord[]>(`/api/v1/training-records/date/${date}`),

  getCalendar: (yearMonth: string) =>
    apiClient.get<CalendarResponse>('/api/v1/training-records/calendar', {
      params: { yearMonth },
    }),

  create: (data: CreateTrainingRecordRequest) =>
    apiClient.post<TrainingRecord>('/api/v1/training-records', data),

  update: (id: number, data: UpdateTrainingRecordRequest) =>
    apiClient.put<TrainingRecord>(`/api/v1/training-records/${id}`, data),

  delete: (id: number) => apiClient.delete(`/api/v1/training-records/${id}`),
};
