import type { CalendarResponse } from '../../types';
import { CalendarDay } from './CalendarDay';

interface CalendarProps {
  yearMonth: string;
  calendar: CalendarResponse | null;
  loading: boolean;
  selectedDate: string;
  onDateSelect: (date: string) => void;
  onPrevMonth: () => void;
  onNextMonth: () => void;
}

export function Calendar({
  yearMonth,
  calendar,
  loading,
  selectedDate,
  onDateSelect,
  onPrevMonth,
  onNextMonth,
}: CalendarProps) {
  const [year, month] = yearMonth.split('-').map(Number);
  const firstDay = new Date(year, month - 1, 1);
  const lastDay = new Date(year, month, 0);
  const daysInMonth = lastDay.getDate();
  const startDayOfWeek = firstDay.getDay();

  const monthName = new Date(year, month - 1).toLocaleDateString('ja-JP', {
    year: 'numeric',
    month: 'long',
  });

  const dayLabels = ['日', '月', '火', '水', '木', '金', '土'];

  const recordsByDate = new Map(
    calendar?.days.map((day) => [day.date, day]) || []
  );

  const days: (number | null)[] = [];
  for (let i = 0; i < startDayOfWeek; i++) {
    days.push(null);
  }
  for (let i = 1; i <= daysInMonth; i++) {
    days.push(i);
  }

  const getDateString = (day: number) => {
    return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
  };

  if (loading) {
    return (
      <div className="animate-pulse">
        <div className="flex items-center justify-between mb-4">
          <div className="h-8 bg-gray-200 rounded w-32"></div>
          <div className="flex gap-2">
            <div className="h-8 w-8 bg-gray-200 rounded"></div>
            <div className="h-8 w-8 bg-gray-200 rounded"></div>
          </div>
        </div>
        <div className="grid grid-cols-7 gap-1">
          {Array.from({ length: 35 }).map((_, i) => (
            <div key={i} className="h-12 bg-gray-200 rounded"></div>
          ))}
        </div>
      </div>
    );
  }

  return (
    <div>
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-lg font-semibold">{monthName}</h2>
        <div className="flex gap-2">
          <button
            type="button"
            onClick={onPrevMonth}
            className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
          >
            &lt;
          </button>
          <button
            type="button"
            onClick={onNextMonth}
            className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
          >
            &gt;
          </button>
        </div>
      </div>

      <div className="grid grid-cols-7 gap-1">
        {dayLabels.map((label, i) => (
          <div
            key={label}
            className={`text-center text-sm font-medium py-2 ${
              i === 0 ? 'text-red-500' : i === 6 ? 'text-blue-500' : 'text-gray-600'
            }`}
          >
            {label}
          </div>
        ))}

        {days.map((day, index) => {
          if (day === null) {
            return <div key={`empty-${index}`} className="h-12"></div>;
          }

          const dateString = getDateString(day);
          const dayData = recordsByDate.get(dateString);
          const isSelected = dateString === selectedDate;
          const dayOfWeek = (startDayOfWeek + day - 1) % 7;

          return (
            <CalendarDay
              key={dateString}
              day={day}
              isSelected={isSelected}
              hasRecords={!!dayData}
              recordCount={dayData?.recordCount || 0}
              dayOfWeek={dayOfWeek}
              onClick={() => onDateSelect(dateString)}
            />
          );
        })}
      </div>
    </div>
  );
}
