import { useState, useEffect, useCallback } from 'react';
import { bodyPartApi } from '../api/bodyPartApi';
import type { BodyPart } from '../types';

export function useBodyParts() {
  const [bodyParts, setBodyParts] = useState<BodyPart[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchBodyParts = useCallback(async () => {
    try {
      setLoading(true);
      const response = await bodyPartApi.getAll();
      setBodyParts(response.data);
    } catch (err) {
      setError(err as Error);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchBodyParts();
  }, [fetchBodyParts]);

  return { bodyParts, loading, error, refetch: fetchBodyParts };
}
