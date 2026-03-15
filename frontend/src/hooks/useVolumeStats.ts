import { useState, useEffect, useCallback } from 'react';
import { trainingRecordApi } from '../api/trainingRecordApi';
import type { DailyVolume } from '../types';

export function useVolumeStats(days: number, exerciseId?: number, bodyPartId?: number) {
  const [stats, setStats] = useState<DailyVolume[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchStats = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await trainingRecordApi.getVolumeStats(days, exerciseId, bodyPartId);
      setStats(res.data);
    } catch {
      setError('統計データの取得に失敗しました');
    } finally {
      setLoading(false);
    }
  }, [days, exerciseId, bodyPartId]);

  useEffect(() => {
    fetchStats();
  }, [fetchStats]);

  return { stats, loading, error };
}
