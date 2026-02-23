import { useState, useEffect } from 'react';
import { useExercises } from '../../hooks/useExercises';
import { useBodyParts } from '../../hooks/useBodyParts';
import type { CreateTrainingRecordRequest } from '../../types';

interface TrainingRecordFormProps {
  defaultValues: {
    exerciseId: number;
    weightKg: number;
    repCount: number;
    setCount: number;
    trainingDate: string;
    memo: string;
  };
  onSubmit: (data: CreateTrainingRecordRequest) => Promise<void>;
  onCancel: () => void;
}

export function TrainingRecordForm({
  defaultValues,
  onSubmit,
  onCancel,
}: TrainingRecordFormProps) {
  const [selectedBodyPartId, setSelectedBodyPartId] = useState<number | null>(null);
  const [formData, setFormData] = useState(defaultValues);
  const [submitting, setSubmitting] = useState(false);

  const { bodyParts } = useBodyParts();
  const { exercises } = useExercises(selectedBodyPartId || undefined);

  useEffect(() => {
    if (defaultValues.exerciseId && exercises.length > 0) {
      const exercise = exercises.find((e) => e.id === defaultValues.exerciseId);
      if (exercise?.bodyPart) {
        setSelectedBodyPartId(exercise.bodyPart.id);
      }
    }
  }, [defaultValues.exerciseId, exercises]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.exerciseId || submitting) return;

    setSubmitting(true);
    try {
      await onSubmit({
        exerciseId: formData.exerciseId,
        weightKg: formData.weightKg,
        repCount: formData.repCount,
        setCount: formData.setCount,
        trainingDate: formData.trainingDate,
        memo: formData.memo || undefined,
      });
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          トレーニング日
        </label>
        <input
          type="date"
          value={formData.trainingDate}
          onChange={(e) => setFormData({ ...formData, trainingDate: e.target.value })}
          className="w-full border rounded-lg px-3 py-2"
          required
        />
      </div>

      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          部位
        </label>
        <select
          value={selectedBodyPartId || ''}
          onChange={(e) => {
            setSelectedBodyPartId(e.target.value ? Number(e.target.value) : null);
            setFormData({ ...formData, exerciseId: 0 });
          }}
          className="w-full border rounded-lg px-3 py-2"
        >
          <option value="">すべての部位</option>
          {bodyParts.map((bp) => (
            <option key={bp.id} value={bp.id}>
              {bp.name}
            </option>
          ))}
        </select>
      </div>

      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          種目 <span className="text-red-500">*</span>
        </label>
        <select
          value={formData.exerciseId}
          onChange={(e) => setFormData({ ...formData, exerciseId: Number(e.target.value) })}
          className="w-full border rounded-lg px-3 py-2"
          required
        >
          <option value={0}>選択してください</option>
          {exercises.map((ex) => (
            <option key={ex.id} value={ex.id}>
              {ex.name}
            </option>
          ))}
        </select>
      </div>

      <div className="grid grid-cols-3 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            重量 (kg) <span className="text-red-500">*</span>
          </label>
          <input
            type="number"
            step="0.5"
            min="0"
            max="999.99"
            value={formData.weightKg}
            onChange={(e) => setFormData({ ...formData, weightKg: Number(e.target.value) })}
            className="w-full border rounded-lg px-3 py-2"
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            回数 <span className="text-red-500">*</span>
          </label>
          <input
            type="number"
            min="1"
            max="1000"
            value={formData.repCount}
            onChange={(e) => setFormData({ ...formData, repCount: Number(e.target.value) })}
            className="w-full border rounded-lg px-3 py-2"
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            セット数 <span className="text-red-500">*</span>
          </label>
          <input
            type="number"
            min="1"
            max="100"
            value={formData.setCount}
            onChange={(e) => setFormData({ ...formData, setCount: Number(e.target.value) })}
            className="w-full border rounded-lg px-3 py-2"
            required
          />
        </div>
      </div>

      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          メモ
        </label>
        <textarea
          value={formData.memo}
          onChange={(e) => setFormData({ ...formData, memo: e.target.value })}
          className="w-full border rounded-lg px-3 py-2"
          rows={3}
          maxLength={1000}
          placeholder="メモを入力（任意）"
        />
      </div>

      <div className="flex gap-3 pt-2">
        <button
          type="submit"
          disabled={!formData.exerciseId || submitting}
          className="flex-1 bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition-colors disabled:bg-gray-300 disabled:cursor-not-allowed"
        >
          {submitting ? '保存中...' : '保存'}
        </button>
        <button
          type="button"
          onClick={onCancel}
          className="flex-1 bg-gray-200 text-gray-700 py-2 rounded-lg hover:bg-gray-300 transition-colors"
        >
          キャンセル
        </button>
      </div>
    </form>
  );
}
