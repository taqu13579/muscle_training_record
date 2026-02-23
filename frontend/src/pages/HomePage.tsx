import { useState, useMemo } from 'react';
import { Link } from 'react-router-dom';
import { useCalendar, useTrainingRecordsByDate } from '../hooks/useTrainingRecords';
import { Calendar } from '../components/calendar/Calendar';
import { TrainingRecordList } from '../components/training/TrainingRecordList';

function formatDate(date: Date): string {
  return date.toISOString().split('T')[0];
}

function formatYearMonth(date: Date): string {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  return `${year}-${month}`;
}

export function HomePage() {
  const today = useMemo(() => new Date(), []);
  const [currentMonth, setCurrentMonth] = useState(today);
  const [selectedDate, setSelectedDate] = useState(formatDate(today));

  const yearMonth = formatYearMonth(currentMonth);
  const { calendar, loading: calendarLoading } = useCalendar(yearMonth);
  const { records, loading: recordsLoading, refetch, deleteRecord } = useTrainingRecordsByDate(selectedDate);

  const handlePrevMonth = () => {
    setCurrentMonth(prev => new Date(prev.getFullYear(), prev.getMonth() - 1, 1));
  };

  const handleNextMonth = () => {
    setCurrentMonth(prev => new Date(prev.getFullYear(), prev.getMonth() + 1, 1));
  };

  const handleDateSelect = (date: string) => {
    setSelectedDate(date);
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('この記録を削除しますか？')) {
      await deleteRecord(id);
    }
  };

  const formattedSelectedDate = new Date(selectedDate).toLocaleDateString('ja-JP', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'short',
  });

  return (
    <div className="space-y-6">
      <div className="bg-white rounded-lg shadow p-4">
        <Calendar
          yearMonth={yearMonth}
          calendar={calendar}
          loading={calendarLoading}
          selectedDate={selectedDate}
          onDateSelect={handleDateSelect}
          onPrevMonth={handlePrevMonth}
          onNextMonth={handleNextMonth}
        />
      </div>

      <div className="bg-white rounded-lg shadow p-4">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-lg font-semibold">{formattedSelectedDate}の記録</h2>
          <Link
            to={`/records/new?date=${selectedDate}`}
            className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors"
          >
            + 記録を追加
          </Link>
        </div>
        <TrainingRecordList
          records={records}
          loading={recordsLoading}
          onDelete={handleDelete}
          onRefresh={refetch}
        />
      </div>
    </div>
  );
}
