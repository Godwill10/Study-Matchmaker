import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Users, BookOpen, Sparkles, TrendingUp } from 'lucide-react';
import API from '../api/api';
import type { MatchResult, UserProfile } from '../types';

function getScoreClass(score: number) {
  if (score >= 70) return 'score-high';
  if (score >= 40) return 'score-med';
  return 'score-low';
}

function getInitials(name: string) {
  return name.split(' ').map((w) => w[0]).join('').slice(0, 2).toUpperCase();
}

export default function Dashboard() {
  const navigate = useNavigate();
  const [matches, setMatches] = useState<MatchResult[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [user, setUser] = useState<UserProfile | null>(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
      return;
    }
    void load();
  }, []);

  const load = async () => {
    try {
      setLoading(true);
      setError('');
      const [me, matchRes] = await Promise.all([
        API.get('/users/me'),
        API.post('/users/match', {}),
      ]);
      setUser(me.data);
      localStorage.setItem('user', JSON.stringify(me.data));
      setMatches(matchRes.data);
    } catch (err: any) {
      if (err.response?.status === 403) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        navigate('/login');
        return;
      }
      setError(err.response?.data?.message || 'Failed to load dashboard');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="loading-page">
        <div className="spinner" />
        Loading your dashboard...
      </div>
    );
  }

  return (
    <>
      <div className="welcome-banner">
        <h1>Welcome back, {user?.fullName?.split(' ')[0] || 'Student'}</h1>
        <p>{user?.school} &middot; {user?.major} &middot; {user?.academicLevel}</p>
      </div>

      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon purple"><Sparkles size={20} /></div>
          <div className="stat-content">
            <h3>{matches.length}</h3>
            <p>Matches found</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon green"><Users size={20} /></div>
          <div className="stat-content">
            <h3>{user?.courses?.length || 0}</h3>
            <p>Courses listed</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon amber"><TrendingUp size={20} /></div>
          <div className="stat-content">
            <h3>{matches.length > 0 ? Math.round(matches[0].score) + '%' : '--'}</h3>
            <p>Top match score</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon blue"><BookOpen size={20} /></div>
          <div className="stat-content">
            <h3>{user?.studyStyle || '--'}</h3>
            <p>Study style</p>
          </div>
        </div>
      </div>

      {error && <div className="alert alert-error mb-4">{error}</div>}

      <div className="section">
        <div className="section-header">
          <h2>Top Matches</h2>
          <Link to="/people" className="btn btn-secondary btn-sm">
            View all
          </Link>
        </div>

        {matches.length === 0 ? (
          <div className="card">
            <div className="empty-state">
              <Users size={48} />
              <h3>No matches yet</h3>
              <p>Update your profile and courses to get better recommendations.</p>
              <Link to="/profile" className="btn btn-primary mt-4">Complete profile</Link>
            </div>
          </div>
        ) : (
          <div className="grid-2">
            {matches.slice(0, 6).map((match, i) => (
              <div key={match.user.id ?? i} className="match-card">
                <div className="match-card-header">
                  <div className="avatar">
                    {match.user.profileImageUrl ? (
                      <img src={match.user.profileImageUrl} alt="" />
                    ) : (
                      getInitials(match.user.fullName)
                    )}
                  </div>
                  <div className="match-card-info">
                    <h3>{match.user.fullName}</h3>
                    <p>{match.user.school} &middot; {match.user.major}</p>
                  </div>
                  <div className={`score-badge ${getScoreClass(match.score)}`}>
                    {Math.round(match.score)}%
                  </div>
                </div>

                <div className="match-card-chips">
                  {match.user.academicLevel && <span className="chip chip-primary">{match.user.academicLevel}</span>}
                  {match.user.studyStyle && <span className="chip chip-accent">{match.user.studyStyle}</span>}
                  {match.user.preferredMode && <span className="chip chip-gray">{match.user.preferredMode}</span>}
                </div>

                <ul className="match-card-reasons">
                  {match.reasons.slice(0, 3).map((r, j) => (
                    <li key={j}>
                      <Sparkles size={12} style={{ color: 'var(--primary)' }} />
                      {r}
                    </li>
                  ))}
                </ul>

                <div className="match-card-actions">
                  <Link to={`/people/${match.user.id}`} className="btn btn-primary btn-sm">
                    View Profile
                  </Link>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </>
  );
}
