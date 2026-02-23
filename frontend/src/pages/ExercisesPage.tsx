import { useState } from 'react';
import { useExercises } from '../hooks/useExercises';
import { useBodyParts } from '../hooks/useBodyParts';
import type { Exercise } from '../types';

export function ExercisesPage() {
  const { exercises, loading, createExercise, updateExercise, deleteExercise } = useExercises();
  const { bodyParts } = useBodyParts();
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editName, setEditName] = useState('');
  const [showAddForm, setShowAddForm] = useState(false);
  const [newExercise, setNewExercise] = useState({ name: '', bodyPartId: 0 });

  const handleEdit = (exercise: Exercise) => {
    setEditingId(exercise.id);
    setEditName(exercise.name);
  };

  const handleSaveEdit = async (id: number) => {
    if (!editName.trim()) return;
    await updateExercise(id, { name: editName });
    setEditingId(null);
    setEditName('');
  };

  const handleCancelEdit = () => {
    setEditingId(null);
    setEditName('');
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('この種目を削除しますか？')) {
      await deleteExercise(id);
    }
  };

  const handleAdd = async () => {
    if (!newExercise.name.trim() || !newExercise.bodyPartId) return;
    await createExercise(newExercise);
    setNewExercise({ name: '', bodyPartId: 0 });
    setShowAddForm(false);
  };

  const groupedExercises = exercises.reduce((acc, exercise) => {
    const bodyPartName = exercise.bodyPart?.name || '未分類';
    if (!acc[bodyPartName]) {
      acc[bodyPartName] = [];
    }
    acc[bodyPartName].push(exercise);
    return acc;
  }, {} as Record<string, Exercise[]>);

  if (loading) {
    return (
      <div className="bg-white rounded-lg shadow p-4">
        <div className="animate-pulse space-y-4">
          <div className="h-8 bg-gray-200 rounded w-1/4"></div>
          <div className="h-12 bg-gray-200 rounded"></div>
          <div className="h-12 bg-gray-200 rounded"></div>
          <div className="h-12 bg-gray-200 rounded"></div>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-lg shadow p-4">
      <div className="flex items-center justify-between mb-4">
        <h1 className="text-xl font-semibold">種目管理</h1>
        <button
          onClick={() => setShowAddForm(true)}
          className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors"
        >
          + 種目を追加
        </button>
      </div>

      {showAddForm && (
        <div className="mb-6 p-4 bg-gray-50 rounded-lg">
          <h3 className="font-medium mb-3">新しい種目を追加</h3>
          <div className="space-y-3">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                部位
              </label>
              <select
                value={newExercise.bodyPartId}
                onChange={(e) =>
                  setNewExercise({ ...newExercise, bodyPartId: Number(e.target.value) })
                }
                className="w-full border rounded-lg px-3 py-2"
              >
                <option value={0}>選択してください</option>
                {bodyParts.map((bp) => (
                  <option key={bp.id} value={bp.id}>
                    {bp.name}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                種目名
              </label>
              <input
                type="text"
                value={newExercise.name}
                onChange={(e) => setNewExercise({ ...newExercise, name: e.target.value })}
                className="w-full border rounded-lg px-3 py-2"
                placeholder="例: ベンチプレス"
              />
            </div>
            <div className="flex gap-2">
              <button
                onClick={handleAdd}
                disabled={!newExercise.name.trim() || !newExercise.bodyPartId}
                className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors disabled:bg-gray-300 disabled:cursor-not-allowed"
              >
                追加
              </button>
              <button
                onClick={() => {
                  setShowAddForm(false);
                  setNewExercise({ name: '', bodyPartId: 0 });
                }}
                className="bg-gray-200 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-300 transition-colors"
              >
                キャンセル
              </button>
            </div>
          </div>
        </div>
      )}

      <div className="space-y-6">
        {Object.entries(groupedExercises).map(([bodyPartName, exercises]) => (
          <div key={bodyPartName}>
            <h2 className="text-lg font-medium text-gray-700 mb-2 border-b pb-1">
              {bodyPartName}
            </h2>
            <ul className="space-y-2">
              {exercises.map((exercise) => (
                <li
                  key={exercise.id}
                  className="flex items-center justify-between p-3 bg-gray-50 rounded-lg"
                >
                  {editingId === exercise.id ? (
                    <div className="flex items-center gap-2 flex-1">
                      <input
                        type="text"
                        value={editName}
                        onChange={(e) => setEditName(e.target.value)}
                        className="flex-1 border rounded px-2 py-1"
                        autoFocus
                      />
                      <button
                        onClick={() => handleSaveEdit(exercise.id)}
                        className="text-blue-600 hover:text-blue-800"
                      >
                        保存
                      </button>
                      <button
                        onClick={handleCancelEdit}
                        className="text-gray-500 hover:text-gray-700"
                      >
                        キャンセル
                      </button>
                    </div>
                  ) : (
                    <>
                      <span className={exercise.isActive ? '' : 'text-gray-400'}>
                        {exercise.name}
                      </span>
                      <div className="flex items-center gap-2">
                        <button
                          onClick={() => handleEdit(exercise)}
                          className="text-blue-600 hover:text-blue-800 text-sm"
                        >
                          編集
                        </button>
                        <button
                          onClick={() => handleDelete(exercise.id)}
                          className="text-red-600 hover:text-red-800 text-sm"
                        >
                          削除
                        </button>
                      </div>
                    </>
                  )}
                </li>
              ))}
            </ul>
          </div>
        ))}
      </div>
    </div>
  );
}
