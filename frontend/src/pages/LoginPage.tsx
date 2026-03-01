import { useNavigate } from 'react-router-dom';
import { LoginForm } from '../components/auth/LoginForm';
import { useLogin } from '../hooks/useLogin';
import type { LoginRequest } from '../types';

export function LoginPage() {
  const navigate = useNavigate();
  const { login, loading, error } = useLogin();

  const handleSubmit = async (data: LoginRequest) => {
    const success = await login(data);
    if (success) {
      navigate('/');
    }
  };

  return (
    <div className="max-w-md mx-auto">
      <div className="bg-white rounded-lg shadow p-6">
        <h1 className="text-xl font-semibold mb-6">ログイン</h1>
        <LoginForm onSubmit={handleSubmit} loading={loading} error={error} />
      </div>
    </div>
  );
}
