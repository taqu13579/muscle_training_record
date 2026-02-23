import { useParams, Link } from 'react-router-dom';
import { useTrainingRecordsByDate } from '../hooks/useTrainingRecords';
import { TrainingRecordList } from '../components/training/TrainingRecordList';

export function RecordDetailPage() {
  const { date } = useParams<{ date: string }>();
  const { records, loading, refetch, deleteRecord } = useTrainingRecordsByDate(date || '');

  const handleDelete = async (id: number) => {
    if (window.confirm('この記録を削除しますか？')) {
      await deleteRecord(id);
    }
  };

  const formattedDate = date
    ? new Date(date).toLocaleDateString('ja-JP', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        weekday: 'short',
      })
    : '';

  return (
    <div className="bg-white rounded-lg shadow p-4">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-4">
          <Link to="/" className="text-gray-500 hover:text-gray-700">
            &larr; 戻る
          </Link>
          <h1 className="text-xl font-semibold">{formattedDate}</h1>
        </div>
        <Link
          to={`/records/new?date=${date}`}
          className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors"
        >
          + 記録を追加
        </Link>
      </div>
      <TrainingRecordList
        records={records}
        loading={loading}
        onDelete={handleDelete}
        onRefresh={refetch}
      />
    </div>
  );
}
