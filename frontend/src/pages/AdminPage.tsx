import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { useBodyParts } from '../hooks/useBodyParts';
import { adminApi } from '../api/adminApi';
import type { User } from '../types';

export function AdminPage() {
  const { isAdmin } = useAuth();
  const navigate = useNavigate();
  const { bodyParts, refetch: refetchBodyParts } = useBodyParts();

  const [users, setUsers] = useState<User[]>([]);
  const [usersLoading, setUsersLoading] = useState(true);
  const [activeTab, setActiveTab] = useState<'users' | 'bodyparts'>('users');

  // 部位管理のフォーム状態
  const [showAddBodyPartForm, setShowAddBodyPartForm] = useState(false);
  const [newBodyPart, setNewBodyPart] = useState({ name: '', displayOrder: 1 });
  const [editingBodyPartId, setEditingBodyPartId] = useState<number | null>(null);
  const [editBodyPart, setEditBodyPart] = useState({ name: '', displayOrder: 1 });

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
      </div>

      {activeTab === 'users' && (
        <div>
          <h2 className="text-lg font-medium mb-3">ユーザー一覧</h2>
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
    </div>
  );
}
