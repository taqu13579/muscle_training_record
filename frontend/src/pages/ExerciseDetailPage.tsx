import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { exerciseApi } from '../api/exerciseApi';
import type { Exercise } from '../types';

export function ExerciseDetailPage() {
  const { id } = useParams<{ id: string }>();
  const [exercise, setExercise] = useState<Exercise | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;
    setLoading(true);
    exerciseApi
      .getById(Number(id))
      .then((res) => setExercise(res.data))
      .catch(() => setError('種目情報の取得に失敗しました'))
      .finally(() => setLoading(false));
  }, [id]);

  if (loading) {
    return (
      <div className="bg-white rounded-lg shadow p-6">
        <div className="animate-pulse space-y-4">
          <div className="h-8 bg-gray-200 rounded w-1/3"></div>
          <div className="h-4 bg-gray-200 rounded w-1/4"></div>
          <div className="h-24 bg-gray-200 rounded"></div>
        </div>
      </div>
    );
  }

  if (error || !exercise) {
    return (
      <div className="bg-white rounded-lg shadow p-6">
        <p className="text-red-600">{error ?? '種目が見つかりません'}</p>
        <Link to="/exercises" className="text-blue-600 hover:underline mt-2 inline-block">
          ← 種目一覧に戻る
        </Link>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-lg shadow p-6">
      <div className="mb-4">
        <Link to="/exercises" className="text-blue-600 hover:underline text-sm">
          ← 種目一覧に戻る
        </Link>
      </div>

      <h1 className="text-2xl font-bold mb-1">{exercise.name}</h1>

      {exercise.bodyPart && (
        <p className="text-gray-500 text-sm mb-4">
          メイン部位: <span className="font-medium text-gray-700">{exercise.bodyPart.name}</span>
        </p>
      )}

      {exercise.auxiliaryMuscles && exercise.auxiliaryMuscles.length > 0 && (
        <div className="mb-4">
          <h2 className="text-sm font-medium text-gray-500 mb-2">補助筋</h2>
          <div className="flex flex-wrap gap-2">
            {exercise.auxiliaryMuscles.map((muscle) => (
              <span
                key={muscle.id}
                className="px-3 py-1 bg-blue-50 text-blue-700 rounded-full text-sm font-medium"
              >
                {muscle.name}
              </span>
            ))}
          </div>
        </div>
      )}

      {exercise.description ? (
        <div className="mt-4">
          <h2 className="text-sm font-medium text-gray-500 mb-2">説明</h2>
          <p className="text-gray-700 whitespace-pre-wrap leading-relaxed">
            {exercise.description}
          </p>
        </div>
      ) : (
        <p className="text-gray-400 text-sm mt-4">説明はまだ登録されていません。</p>
      )}
    </div>
  );
}
