import { Link } from 'react-router-dom';
import type { TrainingRecord } from '../../types';

interface TrainingRecordCardProps {
  record: TrainingRecord;
  onDelete: (id: number) => void;
}

export function TrainingRecordCard({ record, onDelete }: TrainingRecordCardProps) {
  return (
    <div className="p-4 bg-gray-50 rounded-lg">
      <div className="flex items-start justify-between">
        <div className="flex-1">
          <div className="flex items-center gap-2 mb-1">
            {record.exercise?.bodyPart && (
              <span className="text-xs bg-blue-100 text-blue-700 px-2 py-0.5 rounded">
                {record.exercise.bodyPart.name}
              </span>
            )}
            <span className="font-medium">{record.exercise?.name || '不明な種目'}</span>
          </div>
          <div className="text-sm text-gray-600 space-x-4">
            <span>{record.weightKg}kg</span>
            <span>{record.repCount}回</span>
            <span>{record.setCount}セット</span>
            <span className="text-blue-600 font-medium">
              総負荷: {record.totalVolume.toLocaleString()}kg
            </span>
          </div>
          {record.memo && (
            <p className="text-sm text-gray-500 mt-2">{record.memo}</p>
          )}
        </div>
        <div className="flex items-center gap-2 ml-4">
          <Link
            to={`/records/${record.id}/edit`}
            className="text-blue-600 hover:text-blue-800 text-sm"
          >
            編集
          </Link>
          <button
            onClick={() => onDelete(record.id)}
            className="text-red-600 hover:text-red-800 text-sm"
          >
            削除
          </button>
        </div>
      </div>
    </div>
  );
}
