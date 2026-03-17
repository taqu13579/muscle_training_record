import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { useBodyParts } from '../hooks/useBodyParts';
import { useExercises } from '../hooks/useExercises';
import { adminApi } from '../api/adminApi';
import type { User, Exercise } from '../types';

export function AdminPage() {
  const { isAdmin } = useAuth();
  const navigate = useNavigate();
  const { bodyParts, refetch: refetchBodyParts } = useBodyParts();
  const { exercises, updateExercise } = useExercises();

  const [users, setUsers] = useState<User[]>([]);
  const [usersLoading, setUsersLoading] = useState(true);
  const [activeTab, setActiveTab] = useState<'users' | 'bodyparts' | 'exercises'>('users');

  // 管理者ユーザー作成フォームの状態
  const [showAddAdminForm, setShowAddAdminForm] = useState(false);
  const [newAdmin, setNewAdmin] = useState({ email: '', username: '', password: '' });
  const [addAdminError, setAddAdminError] = useState<string | null>(null);
  const [addAdminLoading, setAddAdminLoading] = useState(false);

  // 部位管理のフォーム状態
  const [showAddBodyPartForm, setShowAddBodyPartForm] = useState(false);
  const [newBodyPart, setNewBodyPart] = useState({ name: '', displayOrder: 1 });
  const [editingBodyPartId, setEditingBodyPartId] = useState<number | null>(null);
  const [editBodyPart, setEditBodyPart] = useState({ name: '', displayOrder: 1 });

  // 種目編集の状態
  const [editingExerciseId, setEditingExerciseId] = useState<number | null>(null);
  const [editExercise, setEditExercise] = useState<{
    name: string;
    description: string;
    auxiliaryMuscleBodyPartIds: number[];
  }>({ name: '', description: '', auxiliaryMuscleBodyPartIds: [] });
  const [exerciseSaveLoading, setExerciseSaveLoading] = useState(false);

  useEffect(() => {
    if (!isAdmin) {
      navigate('/');
      return;
    }
    loadUsers();
  }, [isAdmin, navigate]);

  const loadUsers = async () => {
    try {
      setUsersLoading(true);
      const res = await adminApi.getUsers();
      setUsers(res.data);
    } catch {
      // エラー時はそのまま
    } finally {
      setUsersLoading(false);
    }
  };

  const handleAddAdminUser = async () => {
    if (!newAdmin.email.trim() || !newAdmin.username.trim() || !newAdmin.password.trim()) return;
    setAddAdminError(null);
    setAddAdminLoading(true);
    try {
      const res = await adminApi.createAdminUser(newAdmin.email, newAdmin.username, newAdmin.password);
      setUsers((prev) => [...prev, res.data]);
      setNewAdmin({ email: '', username: '', password: '' });
      setShowAddAdminForm(false);
    } catch (err: unknown) {
      const message =
        (err as { response?: { data?: { message?: string } } })?.response?.data?.message ??
        '管理者ユーザーの作成に失敗しました';
      setAddAdminError(message);
    } finally {
      setAddAdminLoading(false);
    }
  };

  const handleRoleChange = async (userId: number, newRole: string) => {
    if (!window.confirm(`ロールを ${newRole} に変更しますか？`)) return;
    try {
      const res = await adminApi.updateUserRole(userId, newRole);
      setUsers((prev) => prev.map((u) => (u.id === userId ? res.data : u)));
    } catch {
      alert('ロールの変更に失敗しました');
    }
  };

  const handleAddBodyPart = async () => {
    if (!newBodyPart.name.trim()) return;
    try {
      await adminApi.createBodyPart(newBodyPart.name, newBodyPart.displayOrder);
      setNewBodyPart({ name: '', displayOrder: 1 });
      setShowAddBodyPartForm(false);
      refetchBodyParts();
    } catch {
      alert('部位の追加に失敗しました');
    }
  };

  const handleEditBodyPart = (id: number, name: string, displayOrder: number) => {
    setEditingBodyPartId(id);
    setEditBodyPart({ name, displayOrder });
  };

  const handleSaveBodyPart = async (id: number) => {
    if (!editBodyPart.name.trim()) return;
    try {
      await adminApi.updateBodyPart(id, editBodyPart.name, editBodyPart.displayOrder);
      setEditingBodyPartId(null);
      refetchBodyParts();
    } catch {
      alert('部位の更新に失敗しました');
    }
  };

  const handleDeleteBodyPart = async (id: number, name: string) => {
    if (!window.confirm(`「${name}」を削除しますか？`)) return;
    try {
      await adminApi.deleteBodyPart(id);
      refetchBodyParts();
    } catch {
      alert('部位の削除に失敗しました');
    }
  };

  const handleStartEditExercise = (exercise: Exercise) => {
    setEditingExerciseId(exercise.id);
    setEditExercise({
      name: exercise.name,
      description: exercise.description ?? '',
      auxiliaryMuscleBodyPartIds: exercise.auxiliaryMuscles?.map((m) => m.id) ?? [],
    });
  };

  const handleSaveExercise = async (id: number) => {
    if (!editExercise.name.trim()) return;
    setExerciseSaveLoading(true);
    try {
      await updateExercise(id, {
        name: editExercise.name,
        description: editExercise.description || undefined,
        auxiliaryMuscleBodyPartIds: editExercise.auxiliaryMuscleBodyPartIds,
      });
      setEditingExerciseId(null);
    } catch {
      alert('種目の更新に失敗しました');
    } finally {
      setExerciseSaveLoading(false);
    }
  };

  const toggleAuxiliaryMuscle = (bodyPartId: number) => {
    setEditExercise((prev) => ({
      ...prev,
      auxiliaryMuscleBodyPartIds: prev.auxiliaryMuscleBodyPartIds.includes(bodyPartId)
        ? prev.auxiliaryMuscleBodyPartIds.filter((id) => id !== bodyPartId)
        : [...prev.auxiliaryMuscleBodyPartIds, bodyPartId],
    }));
  };

  if (!isAdmin) return null;

  return (
    <div className="bg-white rounded-lg shadow p-4">
      <h1 className="text-xl font-semibold mb-4">管理者ダッシュボード</h1>

      <div className="flex gap-2 mb-6 border-b">
        <button
          onClick={() => setActiveTab('users')}
          className={`px-4 py-2 -mb-px font-medium text-sm transition-colors ${
            activeTab === 'users'
              ? 'border-b-2 border-blue-600 text-blue-600'
              : 'text-gray-500 hover:text-gray-700'
          }`}
        >
          ユーザー管理
        </button>
        <button
          onClick={() => setActiveTab('bodyparts')}
          className={`px-4 py-2 -mb-px font-medium text-sm transition-colors ${
            activeTab === 'bodyparts'
              ? 'border-b-2 border-blue-600 text-blue-600'
              : 'text-gray-500 hover:text-gray-700'
          }`}
        >
          部位管理
        </button>
        <button
          onClick={() => setActiveTab('exercises')}
          className={`px-4 py-2 -mb-px font-medium text-sm transition-colors ${
            activeTab === 'exercises'
              ? 'border-b-2 border-blue-600 text-blue-600'
              : 'text-gray-500 hover:text-gray-700'
          }`}
        >
          種目管理
        </button>
      </div>

      {activeTab === 'users' && (
        <div>
          <div className="flex items-center justify-between mb-3">
            <h2 className="text-lg font-medium">ユーザー一覧</h2>
            <button
              onClick={() => { setShowAddAdminForm(true); setAddAdminError(null); }}
              className="bg-purple-600 text-white px-3 py-1.5 rounded-lg hover:bg-purple-700 transition-colors text-sm"
            >
              + 管理者を追加
            </button>
          </div>

          {showAddAdminForm && (
            <div className="mb-4 p-4 bg-purple-50 border border-purple-200 rounded-lg">
              <h3 className="font-medium mb-3 text-purple-800">新規管理者ユーザーを作成</h3>
              {addAdminError && (
                <p className="text-red-600 text-sm mb-3">{addAdminError}</p>
              )}
              <div className="space-y-3">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">メールアドレス</label>
                  <input
                    type="email"
                    value={newAdmin.email}
                    onChange={(e) => setNewAdmin({ ...newAdmin, email: e.target.value })}
                    className="w-full border rounded-lg px-3 py-2"
                    placeholder="admin@example.com"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">ユーザー名</label>
                  <input
                    type="text"
                    value={newAdmin.username}
                    onChange={(e) => setNewAdmin({ ...newAdmin, username: e.target.value })}
                    className="w-full border rounded-lg px-3 py-2"
                    placeholder="3〜50文字、英数字・アンダースコア・ハイフンのみ"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">パスワード</label>
                  <input
                    type="password"
                    value={newAdmin.password}
                    onChange={(e) => setNewAdmin({ ...newAdmin, password: e.target.value })}
                    className="w-full border rounded-lg px-3 py-2"
                    placeholder="8文字以上、英字と数字を含める"
                  />
                </div>
                <div className="flex gap-2">
                  <button
                    onClick={handleAddAdminUser}
                    disabled={addAdminLoading || !newAdmin.email.trim() || !newAdmin.username.trim() || !newAdmin.password.trim()}
                    className="bg-purple-600 text-white px-4 py-2 rounded-lg hover:bg-purple-700 transition-colors disabled:bg-gray-300 disabled:cursor-not-allowed"
                  >
                    {addAdminLoading ? '作成中...' : '作成'}
                  </button>
                  <button
                    onClick={() => { setShowAddAdminForm(false); setNewAdmin({ email: '', username: '', password: '' }); setAddAdminError(null); }}
                    className="bg-gray-200 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-300 transition-colors"
                  >
                    キャンセル
                  </button>
                </div>
              </div>
            </div>
          )}

          {usersLoading ? (
            <div className="animate-pulse space-y-3">
              {[1, 2, 3].map((i) => (
                <div key={i} className="h-12 bg-gray-200 rounded"></div>
              ))}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b text-left text-gray-500">
                    <th className="py-2 pr-4">ID</th>
                    <th className="py-2 pr-4">ユーザー名</th>
                    <th className="py-2 pr-4">メール</th>
                    <th className="py-2 pr-4">ロール</th>
                    <th className="py-2">操作</th>
                  </tr>
                </thead>
                <tbody>
                  {users.map((user) => (
                    <tr key={user.id} className="border-b hover:bg-gray-50">
                      <td className="py-2 pr-4 text-gray-400">{user.id}</td>
                      <td className="py-2 pr-4 font-medium">{user.username}</td>
                      <td className="py-2 pr-4 text-gray-600">{user.email}</td>
                      <td className="py-2 pr-4">
                        <span
                          className={`px-2 py-1 rounded text-xs font-medium ${
                            user.role === 'ADMIN'
                              ? 'bg-purple-100 text-purple-700'
                              : 'bg-gray-100 text-gray-600'
                          }`}
                        >
                          {user.role}
                        </span>
                      </td>
                      <td className="py-2">
                        {user.role === 'ADMIN' ? (
                          <button
                            onClick={() => handleRoleChange(user.id, 'USER')}
                            className="text-red-600 hover:text-red-800 text-xs"
                          >
                            USERに変更
                          </button>
                        ) : (
                          <button
                            onClick={() => handleRoleChange(user.id, 'ADMIN')}
                            className="text-blue-600 hover:text-blue-800 text-xs"
                          >
                            ADMINに変更
                          </button>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      )}

      {activeTab === 'bodyparts' && (
        <div>
          <div className="flex items-center justify-between mb-3">
            <h2 className="text-lg font-medium">部位一覧</h2>
            <button
              onClick={() => setShowAddBodyPartForm(true)}
              className="bg-blue-600 text-white px-3 py-1.5 rounded-lg hover:bg-blue-700 transition-colors text-sm"
            >
              + 部位を追加
            </button>
          </div>

          {showAddBodyPartForm && (
            <div className="mb-4 p-4 bg-gray-50 rounded-lg">
              <h3 className="font-medium mb-3">新しい部位を追加</h3>
              <div className="space-y-3">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">部位名</label>
                  <input
                    type="text"
                    value={newBodyPart.name}
                    onChange={(e) => setNewBodyPart({ ...newBodyPart, name: e.target.value })}
                    className="w-full border rounded-lg px-3 py-2"
                    placeholder="例: 前腕"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">表示順</label>
                  <input
                    type="number"
                    min={1}
                    value={newBodyPart.displayOrder}
                    onChange={(e) =>
                      setNewBodyPart({ ...newBodyPart, displayOrder: Number(e.target.value) })
                    }
                    className="w-32 border rounded-lg px-3 py-2"
                  />
                </div>
                <div className="flex gap-2">
                  <button
                    onClick={handleAddBodyPart}
                    disabled={!newBodyPart.name.trim()}
                    className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors disabled:bg-gray-300 disabled:cursor-not-allowed"
                  >
                    追加
                  </button>
                  <button
                    onClick={() => {
                      setShowAddBodyPartForm(false);
                      setNewBodyPart({ name: '', displayOrder: 1 });
                    }}
                    className="bg-gray-200 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-300 transition-colors"
                  >
                    キャンセル
                  </button>
                </div>
              </div>
            </div>
          )}

          <ul className="space-y-2">
            {bodyParts.map((bp) => (
              <li
                key={bp.id}
                className="flex items-center justify-between p-3 bg-gray-50 rounded-lg"
              >
                {editingBodyPartId === bp.id ? (
                  <div className="flex items-center gap-2 flex-1">
                    <input
                      type="text"
                      value={editBodyPart.name}
                      onChange={(e) => setEditBodyPart({ ...editBodyPart, name: e.target.value })}
                      className="flex-1 border rounded px-2 py-1"
                      autoFocus
                    />
                    <input
                      type="number"
                      min={1}
                      value={editBodyPart.displayOrder}
                      onChange={(e) =>
                        setEditBodyPart({ ...editBodyPart, displayOrder: Number(e.target.value) })
                      }
                      className="w-20 border rounded px-2 py-1"
                    />
                    <button
                      onClick={() => handleSaveBodyPart(bp.id)}
                      className="text-blue-600 hover:text-blue-800 text-sm"
                    >
                      保存
                    </button>
                    <button
                      onClick={() => setEditingBodyPartId(null)}
                      className="text-gray-500 hover:text-gray-700 text-sm"
                    >
                      キャンセル
                    </button>
                  </div>
                ) : (
                  <>
                    <div>
                      <span className="font-medium">{bp.name}</span>
                      <span className="text-gray-400 text-xs ml-2">順: {bp.displayOrder}</span>
                    </div>
                    <div className="flex items-center gap-2">
                      <button
                        onClick={() => handleEditBodyPart(bp.id, bp.name, bp.displayOrder)}
                        className="text-blue-600 hover:text-blue-800 text-sm"
                      >
                        編集
                      </button>
                      <button
                        onClick={() => handleDeleteBodyPart(bp.id, bp.name)}
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
      )}
      {activeTab === 'exercises' && (
        <div>
          <h2 className="text-lg font-medium mb-3">種目一覧（説明・補助筋の編集）</h2>
          <ul className="space-y-2">
            {exercises.map((exercise) => (
              <li key={exercise.id} className="p-3 bg-gray-50 rounded-lg">
                {editingExerciseId === exercise.id ? (
                  <div className="space-y-3">
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">種目名</label>
                      <input
                        type="text"
                        value={editExercise.name}
                        onChange={(e) => setEditExercise({ ...editExercise, name: e.target.value })}
                        className="w-full border rounded-lg px-3 py-2"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">説明</label>
                      <textarea
                        value={editExercise.description}
                        onChange={(e) =>
                          setEditExercise({ ...editExercise, description: e.target.value })
                        }
                        rows={3}
                        className="w-full border rounded-lg px-3 py-2"
                        placeholder="フォームのポイントや注意点など"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">補助筋</label>
                      <div className="flex flex-wrap gap-2">
                        {bodyParts.map((bp) => (
                          <label key={bp.id} className="flex items-center gap-1 cursor-pointer">
                            <input
                              type="checkbox"
                              checked={editExercise.auxiliaryMuscleBodyPartIds.includes(bp.id)}
                              onChange={() => toggleAuxiliaryMuscle(bp.id)}
                              className="rounded"
                            />
                            <span className="text-sm">{bp.name}</span>
                          </label>
                        ))}
                      </div>
                    </div>
                    <div className="flex gap-2">
                      <button
                        onClick={() => handleSaveExercise(exercise.id)}
                        disabled={exerciseSaveLoading || !editExercise.name.trim()}
                        className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors disabled:bg-gray-300 disabled:cursor-not-allowed text-sm"
                      >
                        {exerciseSaveLoading ? '保存中...' : '保存'}
                      </button>
                      <button
                        onClick={() => setEditingExerciseId(null)}
                        className="bg-gray-200 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-300 transition-colors text-sm"
                      >
                        キャンセル
                      </button>
                    </div>
                  </div>
                ) : (
                  <div className="flex items-center justify-between">
                    <div>
                      <span className="font-medium">{exercise.name}</span>
                      {exercise.bodyPart && (
                        <span className="text-gray-400 text-xs ml-2">{exercise.bodyPart.name}</span>
                      )}
                      {exercise.auxiliaryMuscles && exercise.auxiliaryMuscles.length > 0 && (
                        <div className="flex flex-wrap gap-1 mt-1">
                          {exercise.auxiliaryMuscles.map((m) => (
                            <span
                              key={m.id}
                              className="px-2 py-0.5 bg-blue-50 text-blue-600 rounded text-xs"
                            >
                              {m.name}
                            </span>
                          ))}
                        </div>
                      )}
                      {exercise.description && (
                        <p className="text-gray-500 text-xs mt-1 line-clamp-1">
                          {exercise.description}
                        </p>
                      )}
                    </div>
                    <button
                      onClick={() => handleStartEditExercise(exercise)}
                      className="text-blue-600 hover:text-blue-800 text-sm ml-4 flex-shrink-0"
                    >
                      編集
                    </button>
                  </div>
                )}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
