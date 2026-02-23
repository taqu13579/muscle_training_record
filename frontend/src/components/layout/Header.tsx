import { Link, useLocation } from 'react-router-dom';

export function Header() {
  const location = useLocation();

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
          <nav className="flex gap-2">
            <Link to="/" className={linkClass('/')}>
              ホーム
            </Link>
            <Link to="/exercises" className={linkClass('/exercises')}>
              種目
            </Link>
          </nav>
        </div>
      </div>
    </header>
  );
}
