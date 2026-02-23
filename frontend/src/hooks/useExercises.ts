import { useState, useEffect, useCallback } from 'react';
import { exerciseApi } from '../api/exerciseApi';
import type { Exercise, CreateExerciseRequest, UpdateExerciseRequest } from '../types';

export function useExercises(bodyPartId?: number) {
  const [exercises, setExercises] = useState<Exercise[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchExercises = useCallback(async () => {
    try {
      setLoading(true);
      const response = await exerciseApi.getAll(bodyPartId);
      setExercises(response.data);
    } catch (err) {
      setError(err as Error);
    } finally {
      setLoading(false);
    }
  }, [bodyPartId]);

  useEffect(() => {
    fetchExercises();
  }, [fetchExercises]);

  const createExercise = async (data: CreateExerciseRequest) => {
    const response = await exerciseApi.create(data);
    await fetchExercises();
    return response.data;
  };

  const updateExercise = async (id: number, data: UpdateExerciseRequest) => {
    const response = await exerciseApi.update(id, data);
    await fetchExercises();
    return response.data;
  };

  const deleteExercise = async (id: number) => {
    await exerciseApi.delete(id);
    await fetchExercises();
  };

  return {
    exercises,
    loading,
    error,
    refetch: fetchExercises,
    createExercise,
    updateExercise,
    deleteExercise,
  };
}
