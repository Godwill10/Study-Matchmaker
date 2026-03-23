import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import API from '../api/api';
import MatchResults from '../components/MatchResults';
import type { MatchResult, UserProfile } from '../types';

export default function Dashboard() {
  const navigate = useNavigate();

  const [matches, setMatches] = useState<MatchResult[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [user, setUser] = useState<UserProfile | null>(null);

  const fetchDashboard = async () => {
    try {
      const token = localStorage.getItem("token");

      // 🚨 NO TOKEN → REDIRECT
      if (!token) {
        console.log("NO TOKEN → redirecting to login");
        navigate("/");
        return;
      }

      console.log("TOKEN FOUND → fetching dashboard");

      setLoading(true);
      setError('');

      const [me, matchRes] = await Promise.all([
        API.get('/users/me'),
        API.post('/users/match', {})
      ]);

      console.log("USER:", me.data);
      console.log("MATCHES:", matchRes.data);

      setUser(me.data);
      localStorage.setItem('user', JSON.stringify(me.data));
      setMatches(matchRes.data);

    } catch (err: any) {
      console.error("DASHBOARD ERROR:", err);

      // 🚨 TOKEN INVALID → FORCE LOGOUT
      if (err.response?.status === 403) {
        console.log("TOKEN INVALID → logging out");

        localStorage.removeItem('token');
        localStorage.removeItem('user');
        navigate('/');
        return;
      }

      setError(err.response?.data?.message || 'Failed to load dashboard');

    } finally {
      setLoading(false);
    }
  };

  // ✅ WAIT FOR TOKEN BEFORE CALLING API
  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token) {
      console.log("WAITING FOR TOKEN...");
      setLoading(false);
      return;
    }

    fetchDashboard();
  }, []);

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    navigate('/');
  };

  return (
      <div className="dashboard">

        {/* HEADER */}
        <div className="dashboard-header">
          <div>
            <h1>Welcome, {user?.fullName || 'Student'} 👋</h1>
            <p>{user?.school} • {user?.major}</p>
          </div>

          <div className="row">
            <Link className="btn secondary" to="/profile">Profile</Link>
            <button className="btn secondary" onClick={logout}>Logout</button>
          </div>
        </div>

        {/* SHORTCUTS */}
        <div className="toolbar-grid">
          <Link className="panel shortcut" to="/students">Students</Link>
          <Link className="panel shortcut" to="/calendar">Calendar</Link>
          <Link className="panel shortcut" to="/requests">Requests</Link>
          <Link className="panel shortcut" to="/friends">Friends</Link>
          <Link className="panel shortcut" to="/history">History</Link>
        </div>

        {/* ERROR */}
        {error && <div className="error-box">{error}</div>}

        {/* LOADING */}
        {loading ? (
            <div className="loading">Loading AI matches...</div>
        ) : (
            <>
              <h2>Suggested matches</h2>
              <MatchResults matches={matches as any} />
            </>
        )}
      </div>
  );
}