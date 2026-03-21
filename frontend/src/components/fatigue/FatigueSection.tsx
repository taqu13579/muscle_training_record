import { useBodyPartFatigue } from '../../hooks/useBodyPartFatigue';

function getBarColor(percentage: number): string {
  if (percentage >= 70) return 'bg-red-500';
  if (percentage >= 40) return 'bg-yellow-400';
  return 'bg-green-500';
}

function getLabelColor(percentage: number): string {
  if (percentage >= 70) return 'text-red-600';
  if (percentage >= 40) return 'text-yellow-600';
  return 'text-green-600';
}

export function FatigueSection() {
  const { fatigue, loading, error } = useBodyPartFatigue();

  return (
    <div className="bg-white rounded-lg shadow p-4">
      <h2 className="text-lg font-semibold mb-3">部位別疲労度（直近3日間）</h2>

      {loading && (
        <p className="text-gray-400 text-sm">読み込み中...</p>
      )}

      {error && (
        <p className="text-red-500 text-sm">{error}</p>
      )}

      {!loading && !error && (
        <div className="space-y-2">
          {fatigue.map(item => (
            <div key={item.bodyPartId} className="flex items-center gap-3">
              <span className="w-16 text-sm text-gray-700 shrink-0">{item.bodyPartName}</span>
              <div className="flex-1 bg-gray-100 rounded-full h-3 overflow-hidden">
                <div
                  className={`h-full rounded-full transition-all duration-300 ${getBarColor(item.fatiguePercentage)}`}
                  style={{ width: `${item.fatiguePercentage}%` }}
                />
              </div>
              <span className={`w-10 text-xs font-medium text-right shrink-0 ${getLabelColor(item.fatiguePercentage)}`}>
                {item.fatiguePercentage}%
              </span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
