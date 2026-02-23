import type { TrainingRecord } from '../../types';
import { TrainingRecordCard } from './TrainingRecordCard';

interface TrainingRecordListProps {
  records: TrainingRecord[];
  loading: boolean;
  onDelete: (id: number) => void;
  onRefresh: () => void;
}

export function TrainingRecordList({
  records,
  loading,
  onDelete,
}: TrainingRecordListProps) {
  if (loading) {
    return (
      <div className="space-y-3">
        {[1, 2, 3].map((i) => (
          <div key={i} className="animate-pulse">
            <div className="h-24 bg-gray-200 rounded-lg"></div>
          </div>
        ))}
      </div>
    );
  }

  if (records.length === 0) {
    return (
      <div className="text-center py-8 text-gray-500">
        この日のトレーニング記録はありません
      </div>
    );
  }

  return (
    <div className="space-y-3">
      {records.map((record) => (
        <TrainingRecordCard key={record.id} record={record} onDelete={onDelete} />
      ))}
    </div>
  );
}
