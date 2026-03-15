import { useState } from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { useVolumeStats } from '../hooks/useVolumeStats';
import { useBodyParts } from '../hooks/useBodyParts';
import { useExercises } from '../hooks/useExercises';
import { VolumeChart } from '../components/stats/VolumeChart';

const PERIOD_OPTIONS = [
  { label: '7日', value: 7 },
  { label: '30日', value: 30 },
  { label: '90日', value: 90 },
];

export function StatsPage() {
  const { isAuthenticated } = useAuth();
  const [days, setDays] = useState(30);
  const [selectedBodyPartId, setSelectedBodyPartId] = useState<number | undefined>(undefined);
  const [selectedExerciseId, setSelectedExerciseId] = useState<number | undefined>(undefined);

  const { bodyParts } = useBodyParts();
  const { exercises } = useExercises(selectedBodyPartId);
  const { stats, loading, error } = useVolumeStats(days, selectedExerciseId, selectedBodyPartId);

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  const handleBodyPartChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const val = e.target.value ? Number(e.target.value) : undefined;
    setSelectedBodyPartId(val);
    setSelectedExerciseId(undefined);
  };

  const handleExerciseChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedExerciseId(e.target.value ? Number(e.target.value) : undefined);
  };

  const totalVolume = stats.reduce((sum, d) => sum + Number(d.totalVolume), 0);
  const maxVolume = stats.length > 0 ? Math.max(...stats.map((d) => Number(d.totalVolume))) : 0;
  const avgVolume = stats.length > 0 ? totalVolume / stats.length : 0;

  const filteredExercises = selectedBodyPartId
    ? exercises.filter((e) => e.bodyPart?.id === selectedBodyPartId)
    : exercises;

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-gray-800">統計</h1>

      {/* フィルター */}
      <div className="bg-white rounded-lg shadow p-4 space-y-4">
        {/* 期間セレクタ */}
        <div className="flex gap-2">
          {PERIOD_OPTIONS.map((opt) => (
            <button
              key={opt.value}
              onClick={() => setDays(opt.value)}
              className={`px-4 py-2 rounded-lg text-sm font-medium transition-colors ${
                days === opt.value
                  ? 'bg-emerald-600 text-white'
                  : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
              }`}
            >
              {opt.label}
            </button>
          ))}
        </div>

        {/* 部位・種目フィルタ */}
        <div className="flex gap-3">
          <select
            value={selectedBodyPartId ?? ''}
            onChange={handleBodyPartChange}
            className="border rounded-lg px-3 py-2 text-sm text-gray-700 flex-1"
          >
            <option value="">すべての部位</option>
            {bodyParts.map((bp) => (
              <option key={bp.id} value={bp.id}>{bp.name}</option>
            ))}
          </select>
          <select
            value={selectedExerciseId ?? ''}
            onChange={handleExerciseChange}
            className="border rounded-lg px-3 py-2 text-sm text-gray-700 flex-1"
            disabled={!selectedBodyPartId}
          >
            <option value="">すべての種目</option>
            {filteredExercises.map((ex) => (
              <option key={ex.id} value={ex.id}>{ex.name}</option>
            ))}
          </select>
        </div>
      </div>

      {/* グラフ */}
      <div className="bg-white rounded-lg shadow p-4">
        <h2 className="text-sm font-semibold text-gray-600 mb-3">1日ごとの総負荷量 (kg)</h2>
        {loading ? (
          <p className="text-gray-400 text-sm text-center py-4">読み込み中...</p>
        ) : error ? (
          <p className="text-red-500 text-sm text-center py-4">{error}</p>
        ) : (
          <VolumeChart records={stats} />
        )}
      </div>

      {/* サマリー */}
      {!loading && !error && stats.length > 0 && (
        <div className="grid grid-cols-3 gap-4">
          <div className="bg-white rounded-lg shadow p-4 text-center">
            <p className="text-xs text-gray-500 mb-1">合計</p>
            <p className="text-lg font-bold text-emerald-600">
              {Math.round(totalVolume).toLocaleString()}
              <span className="text-sm font-normal text-gray-500 ml-1">kg</span>
            </p>
          </div>
          <div className="bg-white rounded-lg shadow p-4 text-center">
            <p className="text-xs text-gray-500 mb-1">最大（1日）</p>
            <p className="text-lg font-bold text-emerald-600">
              {Math.round(maxVolume).toLocaleString()}
              <span className="text-sm font-normal text-gray-500 ml-1">kg</span>
            </p>
          </div>
          <div className="bg-white rounded-lg shadow p-4 text-center">
            <p className="text-xs text-gray-500 mb-1">平均（1日）</p>
            <p className="text-lg font-bold text-emerald-600">
              {Math.round(avgVolume).toLocaleString()}
              <span className="text-sm font-normal text-gray-500 ml-1">kg</span>
            </p>
          </div>
        </div>
      )}
    </div>
  );
}
