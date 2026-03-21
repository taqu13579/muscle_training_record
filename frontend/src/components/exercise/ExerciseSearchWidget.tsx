import { useState } from 'react';
import { Link } from 'react-router-dom';
import { useExercises } from '../../hooks/useExercises';
import { useBodyParts } from '../../hooks/useBodyParts';

export function ExerciseSearchWidget() {
  const { exercises, loading: exercisesLoading } = useExercises();
  const { bodyParts } = useBodyParts();
  const [selectedBodyPartId, setSelectedBodyPartId] = useState<number | null>(null);
  const [searchText, setSearchText] = useState('');

  const filtered = exercises
    .filter(ex => ex.isActive)
    .filter(ex => selectedBodyPartId === null || ex.bodyPart?.id === selectedBodyPartId)
    .filter(ex => ex.name.startsWith(searchText));

  return (
    <div className="bg-white rounded-lg shadow p-4">
      <h2 className="text-lg font-semibold mb-3">種目を探す</h2>

      <div className="flex flex-wrap gap-2 mb-3">
        <button
          onClick={() => setSelectedBodyPartId(null)}
          className={`px-3 py-1 rounded-full text-sm font-medium transition-colors ${
            selectedBodyPartId === null
              ? 'bg-blue-600 text-white'
              : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
          }`}
        >
          すべて
        </button>
        {bodyParts.map(bp => (
          <button
            key={bp.id}
            onClick={() => setSelectedBodyPartId(bp.id)}
            className={`px-3 py-1 rounded-full text-sm font-medium transition-colors ${
              selectedBodyPartId === bp.id
                ? 'bg-blue-600 text-white'
                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
            }`}
          >
            {bp.name}
          </button>
        ))}
      </div>

      <input
        type="text"
        value={searchText}
        onChange={e => setSearchText(e.target.value)}
        placeholder="種目を検索..."
        className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm mb-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
      />

      {exercisesLoading ? (
        <div className="space-y-2">
          {[...Array(3)].map((_, i) => (
            <div key={i} className="h-10 bg-gray-100 rounded animate-pulse" />
          ))}
        </div>
      ) : filtered.length === 0 ? (
        <p className="text-sm text-gray-500 py-2">該当する種目がありません</p>
      ) : (
        <ul className="divide-y divide-gray-100">
          {filtered.map(ex => (
            <li key={ex.id}>
              <Link
                to={`/exercises/${ex.id}`}
                className="flex items-center justify-between py-2 px-1 hover:bg-gray-50 rounded transition-colors"
              >
                <span className="text-sm font-medium text-gray-800">
                  {ex.name}
                  {ex.bodyPart && (
                    <span className="ml-2 text-xs text-gray-500">（{ex.bodyPart.name}）</span>
                  )}
                </span>
                <span className="text-gray-400 text-sm">›</span>
              </Link>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
