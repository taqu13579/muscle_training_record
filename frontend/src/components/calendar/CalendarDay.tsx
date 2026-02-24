interface CalendarDayProps {
  day: number;
  isSelected: boolean;
  hasRecords: boolean;
  recordCount: number;
  dayOfWeek: number;
  onClick: () => void;
}

export function CalendarDay({
  day,
  isSelected,
  hasRecords,
  recordCount,
  dayOfWeek,
  onClick,
}: CalendarDayProps) {
  const textColor =
    dayOfWeek === 0
      ? 'text-red-500'
      : dayOfWeek === 6
      ? 'text-blue-500'
      : 'text-gray-700';

  return (
    <button
      type="button"
      onClick={onClick}
      className={`h-12 rounded-lg flex flex-col items-center justify-center transition-colors relative ${
        isSelected
          ? 'bg-blue-600 text-white'
          : hasRecords
          ? 'bg-blue-50 hover:bg-blue-100'
          : 'hover:bg-gray-100'
      } ${isSelected ? '' : textColor}`}
    >
      <span className="text-sm font-medium">{day}</span>
      {hasRecords && !isSelected && (
        <span className="text-xs text-blue-600">{recordCount}件</span>
      )}
      {hasRecords && isSelected && (
        <span className="text-xs">{recordCount}件</span>
      )}
    </button>
  );
}
