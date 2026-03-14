import { useState, useEffect, useCallback } from 'react';
import { bodyWeightApi } from '../api/bodyWeightApi';
import type { BodyWeight, CreateBodyWeightRequest } from '../types';

export function useBodyWeights() {
  const [records, setRecords] = useState<BodyWeight[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchAll = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await bodyWeightApi.getAll();
      setRecords(res.data);
    } catch {
      setError('体重記録の取得に失敗しました');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchAll();
  }, [fetchAll]);

  const createRecord = async (data: CreateBodyWeightRequest): Promise<boolean> => {
    try {
      await bodyWeightApi.create(data);
      await fetchAll();
      return true;
    } catch {
      return false;
    }
  };

  const deleteRecord = async (id: number): Promise<void> => {
    try {
      await bodyWeightApi.delete(id);
      await fetchAll();
    } catch {
      setError('削除に失敗しました');
    }
  };

  return { records, loading, error, refetch: fetchAll, createRecord, deleteRecord };
}
