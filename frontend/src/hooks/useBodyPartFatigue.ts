import { useState, useEffect } from 'react';
import { trainingRecordApi } from '../api/trainingRecordApi';
import type { BodyPartFatigue } from '../types';

export function useBodyPartFatigue() {
  const [fatigue, setFatigue] = useState<BodyPartFatigue[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    setError(null);
    trainingRecordApi.getBodyPartFatigue()
      .then(res => {
        if (!cancelled) setFatigue(res.data);
      })
      .catch(() => {
        if (!cancelled) setError('疲労データの取得に失敗しました');
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });
    return () => { cancelled = true; };
  }, []);

  return { fatigue, loading, error };
}
