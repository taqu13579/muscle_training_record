import { useParams, useSearchParams, useNavigate } from 'react-router-dom';
import { TrainingRecordForm } from '../components/training/TrainingRecordForm';
import { useTrainingRecord } from '../hooks/useTrainingRecords';
import { trainingRecordApi } from '../api/trainingRecordApi';
import type { CreateTrainingRecordRequest, UpdateTrainingRecordRequest } from '../types';

export function RecordFormPage() {
  const { id } = useParams<{ id: string }>();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const recordId = id ? parseInt(id, 10) : null;
  const defaultDate = searchParams.get('date') || new Date().toISOString().split('T')[0];

  const { record, loading } = useTrainingRecord(recordId);

  const handleSubmit = async (data: CreateTrainingRecordRequest | UpdateTrainingRecordRequest) => {
    if (recordId) {
      await trainingRecordApi.update(recordId, data as UpdateTrainingRecordRequest);
    } else {
      await trainingRecordApi.create(data as CreateTrainingRecordRequest);
    }
    navigate(-1);
  };

  const handleCancel = () => {
    navigate(-1);
  };

  if (loading) {
    return (
      <div className="bg-white rounded-lg shadow p-4">
        <div className="animate-pulse">
          <div className="h-8 bg-gray-200 rounded w-1/4 mb-4"></div>
          <div className="space-y-4">
            <div className="h-10 bg-gray-200 rounded"></div>
            <div className="h-10 bg-gray-200 rounded"></div>
            <div className="h-10 bg-gray-200 rounded"></div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-lg shadow p-4">
      <h1 className="text-xl font-semibold mb-4">
        {recordId ? '記録を編集' : '記録を追加'}
      </h1>
      <TrainingRecordForm
        defaultValues={
          record
            ? {
                exerciseId: record.exercise?.id || 0,
                weightKg: record.weightKg,
                repCount: record.repCount,
                setCount: record.setCount,
                trainingDate: record.trainingDate,
                memo: record.memo || '',
              }
            : {
                exerciseId: 0,
                weightKg: 0,
                repCount: 10,
                setCount: 3,
                trainingDate: defaultDate,
                memo: '',
              }
        }
        onSubmit={handleSubmit}
        onCancel={handleCancel}
      />
    </div>
  );
}
