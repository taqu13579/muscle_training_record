import { useState } from 'react';
import { useBodyWeights } from '../../hooks/useBodyWeights';
import { BodyWeightChart } from './BodyWeightChart';

function formatDate(date: Date): string {
  return date.toISOString().split('T')[0];
}

export function BodyWeightSection() {
  const today = formatDate(new Date());
  const { records, loading, createRecord, deleteRecord } = useBodyWeights();
  const [showForm, setShowForm] = useState(false);
  const [weightKg, setWeightKg] = useState('');
  const [recordedDate, setRecordedDate] = useState(today);
  const [memo, setMemo] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const kg = parseFloat(weightKg);
    if (isNaN(kg) || kg <= 0) return;
    setSubmitting(true);
    const ok = await createRecord({ weightKg: kg, recordedDate, memo: memo || undefined });
    if (ok) {
      setWeightKg('');
      setMemo('');
      setRecordedDate(today);
      setShowForm(false);
    }
    setSubmitting(false);
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('この体重記録を削除しますか？')) {
      await deleteRecord(id);
    }
  };

  const recentRecords = records.slice(0, 7);

  return (
    <div className="bg-white rounded-lg shadow p-4">
      <div className="flex items-center justify-between mb-3">
        <h2 className="text-lg font-semibold">体重推移</h2>
        <button
          onClick={() => setShowForm((v) => !v)}
          className="bg-blue-600 text-white px-3 py-1.5 rounded-lg text-sm hover:bg-blue-700 transition-colors"
        >
          {showForm ? 'キャンセル' : '+ 記録'}
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="mb-4 space-y-2 bg-gray-50 p-3 rounded-lg">
          <div className="flex gap-2">
            <div className="flex-1">
              <label className="block text-xs text-gray-600 mb-1">体重 (kg)</label>
              <input
                type="number"
                step="0.1"
                min="1"
                max="999.9"
                value={weightKg}
                onChange={(e) => setWeightKg(e.target.value)}
                required
                className="w-full border rounded px-2 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="例: 65.5"
              />
            </div>
            <div className="flex-1">
              <label className="block text-xs text-gray-600 mb-1">記録日</label>
              <input
                type="date"
                value={recordedDate}
                onChange={(e) => setRecordedDate(e.target.value)}
                required
                className="w-full border rounded px-2 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </div>
          <div>
            <label className="block text-xs text-gray-600 mb-1">メモ（任意）</label>
            <input
              type="text"
              value={memo}
              onChange={(e) => setMemo(e.target.value)}
              maxLength={500}
              className="w-full border rounded px-2 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="メモを入力"
            />
          </div>
          <button
            type="submit"
            disabled={submitting}
            className="w-full bg-blue-600 text-white py-1.5 rounded text-sm hover:bg-blue-700 transition-colors disabled:bg-gray-300"
          >
            {submitting ? '保存中...' : '保存'}
          </button>
        </form>
      )}

      {loading ? (
        <p className="text-gray-500 text-sm text-center py-4">読み込み中...</p>
      ) : (
        <>
          <BodyWeightChart records={recentRecords} />
          {records.length > 0 && (
            <div className="mt-3 space-y-1 max-h-40 overflow-y-auto">
              {records.map((r) => (
                <div key={r.id} className="flex items-center justify-between text-sm py-1 border-b last:border-0">
                  <span className="text-gray-600">{r.recordedDate}</span>
                  <span className="font-medium">{r.weightKg} kg</span>
                  {r.memo && <span className="text-gray-400 text-xs truncate max-w-24">{r.memo}</span>}
                  <button
                    onClick={() => handleDelete(r.id)}
                    className="text-red-400 hover:text-red-600 text-xs ml-2"
                  >
                    削除
                  </button>
                </div>
              ))}
            </div>
          )}
        </>
      )}
    </div>
  );
}
