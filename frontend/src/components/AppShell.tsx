import { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import {
  LayoutDashboard,
  Users,
  UserCircle,
  Link2,
  Calendar,
  LogOut,
  Menu,
  X,
  Sparkles,
} from 'lucide-react';
import type { UserProfile } from '../types';

const NAV_ITEMS = [
  { to: '/dashboard', icon: LayoutDashboard, label: 'Dashboard' },
  { to: '/people', icon: Users, label: 'People' },
  { to: '/schedule', icon: Calendar, label: 'Schedule' },
  { to: '/network', icon: Link2, label: 'Network' },
  { to: '/profile', icon: UserCircle, label: 'My Profile' },
];

function getInitials(name: string) {
  return name
    .split(' ')
    .map((w) => w[0])
    .join('')
    .slice(0, 2)
    .toUpperCase();
}

export default function AppShell({ children }: { children: React.ReactNode }) {
  const location = useLocation();
  const navigate = useNavigate();
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const stored = localStorage.getItem('user');
  const user: UserProfile | null = stored ? JSON.parse(stored) : null;

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    navigate('/login');
  };

  return (
    <div className="app-layout">
      <div
        className={`sidebar-overlay ${sidebarOpen ? 'open' : ''}`}
        onClick={() => setSidebarOpen(false)}
      />

      <aside className={`sidebar ${sidebarOpen ? 'open' : ''}`}>
        <div className="sidebar-brand">
          <div className="sidebar-brand-icon">
            <Sparkles size={18} />
          </div>
          <div>
            <h1>StudyMatch</h1>
            <span>Find your study partner</span>
          </div>
        </div>

        <nav className="sidebar-nav">
          <div className="sidebar-section-title">Menu</div>
          {NAV_ITEMS.map((item) => (
            <Link
              key={item.to}
              to={item.to}
              className={`sidebar-link ${location.pathname === item.to ? 'active' : ''}`}
              onClick={() => setSidebarOpen(false)}
            >
              <item.icon />
              {item.label}
            </Link>
          ))}
        </nav>

        <div className="sidebar-footer">
          <div className="sidebar-user" onClick={logout} role="button" tabIndex={0}>
            <div className="sidebar-avatar">
              {user?.profileImageUrl ? (
                <img src={user.profileImageUrl} alt="" />
              ) : (
                getInitials(user?.fullName || 'U')
              )}
            </div>
            <div className="sidebar-user-info">
              <p>{user?.fullName || 'User'}</p>
              <p>Sign out</p>
            </div>
            <LogOut size={16} style={{ color: 'var(--gray-400)' }} />
          </div>
        </div>
      </aside>

      <div className="main-content">
        <div className="mobile-topbar">
          <button onClick={() => setSidebarOpen(true)}>
            {sidebarOpen ? <X size={22} /> : <Menu size={22} />}
          </button>
          <span style={{ fontWeight: 600, fontSize: 15 }}>StudyMatch</span>
        </div>
        <div className="page-container">{children}</div>
      </div>
    </div>
  );
}
