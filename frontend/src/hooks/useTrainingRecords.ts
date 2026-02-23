import { useState, useEffect, useCallback } from 'react';
import { trainingRecordApi } from '../api/trainingRecordApi';
import type {
  TrainingRecord,
  CreateTrainingRecordRequest,
  UpdateTrainingRecordRequest,
  CalendarResponse,
} from '../types';

export function useTrainingRecordsByDate(date: string) {
  const [records, setRecords] = useState<TrainingRecord[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchRecords = useCallback(async () => {
    try {
      setLoading(true);
      const response = await trainingRecordApi.getByDate(date);
      setRecords(response.data);
    } catch (err) {
      setError(err as Error);
    } finally {
      setLoading(false);
    }
  }, [date]);

  useEffect(() => {
    fetchRecords();
  }, [fetchRecords]);

  const createRecord = async (data: CreateTrainingRecordRequest) => {
    const response = await trainingRecordApi.create(data);
    await fetchRecords();
    return response.data;
  };

  const updateRecord = async (id: number, data: UpdateTrainingRecordRequest) => {
    const response = await trainingRecordApi.update(id, data);
    await fetchRecords();
    return response.data;
  };

  const deleteRecord = async (id: number) => {
    await trainingRecordApi.delete(id);
    await fetchRecords();
  };

  return { records, loading, error, refetch: fetchRecords, createRecord, updateRecord, deleteRecord };
}

export function useCalendar(yearMonth: string) {
  const [calendar, setCalendar] = useState<CalendarResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchCalendar = useCallback(async () => {
    try {
      setLoading(true);
      const response = await trainingRecordApi.getCalendar(yearMonth);
      setCalendar(response.data);
    } catch (err) {
      setError(err as Error);
    } finally {
      setLoading(false);
    }
  }, [yearMonth]);

  useEffect(() => {
    fetchCalendar();
  }, [fetchCalendar]);

  return { calendar, loading, error, refetch: fetchCalendar };
}

export function useTrainingRecord(id: number | null) {
  const [record, setRecord] = useState<TrainingRecord | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    if (id === null) {
      setRecord(null);
      return;
    }

    const fetchRecord = async () => {
      try {
        setLoading(true);
        const response = await trainingRecordApi.getById(id);
        setRecord(response.data);
      } catch (err) {
        setError(err as Error);
      } finally {
        setLoading(false);
      }
    };
    fetchRecord();
  }, [id]);

  return { record, loading, error };
}
