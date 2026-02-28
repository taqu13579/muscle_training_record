import { useNavigate } from 'react-router-dom';
import { RegisterForm } from '../components/auth/RegisterForm';
import { useRegister } from '../hooks/useRegister';
import type { RegisterRequest } from '../types';

export function RegisterPage() {
  const navigate = useNavigate();
  const { register, loading, error } = useRegister();

  const handleSubmit = async (data: RegisterRequest) => {
    const user = await register(data);
    if (user) {
      navigate('/');
    }
  };

  const handleCancel = () => {
    navigate('/');
  };

  return (
    <div className="max-w-md mx-auto">
      <div className="bg-white rounded-lg shadow p-6">
        <h1 className="text-xl font-semibold mb-6">ユーザー登録</h1>
        <RegisterForm
          onSubmit={handleSubmit}
          onCancel={handleCancel}
          loading={loading}
          error={error}
        />
      </div>
    </div>
  );
}
