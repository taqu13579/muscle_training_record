import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';

export function Header() {
  const location = useLocation();
  const navigate = useNavigate();
  const { isAuthenticated, user, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const isActive = (path: string) => {
    if (path === '/') {
      return location.pathname === '/';
    }
    return location.pathname.startsWith(path);
  };

  const linkClass = (path: string) =>
    `px-4 py-2 rounded-lg transition-colors ${
      isActive(path)
        ? 'bg-blue-600 text-white'
        : 'text-gray-600 hover:bg-gray-100'
    }`;

  return (
    <header className="bg-white shadow-sm border-b">
      <div className="max-w-4xl mx-auto px-4 py-3">
        <div className="flex items-center justify-between">
          <Link to="/" className="text-xl font-bold text-gray-800">
            TrainTrack
          </Link>
          {location.pathname !== '/login' && (
            <div className="flex items-center gap-4">
              <nav className="flex gap-2">
                <Link to="/" className={linkClass('/')}>
                  ホーム
                </Link>
                <Link to="/exercises" className={linkClass('/exercises')}>
                  種目
                </Link>
              </nav>
              {isAuthenticated ? (
                <div className="flex items-center gap-3">
                  <span className="text-gray-600 text-sm">{user?.username}</span>
                  <button
                    onClick={handleLogout}
                    className="text-gray-600 hover:text-red-600 text-sm transition-colors"
                  >
                    ログアウト
                  </button>
                </div>
              ) : (
                <Link
                  to="/login"
                  className="text-blue-600 hover:underline text-sm"
                >
                  ログイン
                </Link>
              )}
            </div>
          )}
        </div>
      </div>
    </header>
  );
}
