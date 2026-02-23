import apiClient from './client';
import type { BodyPart } from '../types';

export const bodyPartApi = {
  getAll: () => apiClient.get<BodyPart[]>('/api/v1/body-parts'),
};
