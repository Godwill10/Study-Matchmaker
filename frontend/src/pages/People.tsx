import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Search, Sparkles, Users } from 'lucide-react';
import API from '../api/api';
import type { MatchResult } from '../types';

function getScoreClass(score: number) {
  if (score >= 70) return 'score-high';
  if (score >= 40) return 'score-med';
  return 'score-low';
}

function getInitials(name: string) {
  return name.split(' ').map((w) => w[0]).join('').slice(0, 2).toUpperCase();
}

export default function People() {
  const navigate = useNavigate();
  const [matches, setMatches] = useState<MatchResult[]>([]);
  const [filtered, setFiltered] = useState<MatchResult[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [search, setSearch] = useState('');
  const [styleFilter, setStyleFilter] = useState('');
  const [modeFilter, setModeFilter] = useState('');

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) { navigate('/login'); return; }
    void load();
  }, []);

  useEffect(() => {
    let result = matches;
    if (search) {
      const q = search.toLowerCase();
      result = result.filter(
        (m) =>
          m.user.fullName.toLowerCase().includes(q) ||
          m.user.school?.toLowerCase().includes(q) ||
          m.user.major?.toLowerCase().includes(q) ||
          m.user.courses?.some((c) => c.toLowerCase().includes(q)),
      );
    }
    if (styleFilter) {
      result = result.filter((m) => m.user.studyStyle === styleFilter);
    }
    if (modeFilter) {
      result = result.filter((m) => m.user.preferredMode === modeFilter);
    }
    setFiltered(result);
  }, [matches, search, styleFilter, modeFilter]);

  const load = async () => {
    try {
      setLoading(true);
      const res = await API.post('/users/match', {});
      setMatches(res.data);
    } catch (err: any) {
      if (err.response?.status === 403) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        navigate('/login');
        return;
      }
      setError(err.response?.data?.message || 'Failed to load matches');
    } finally {
      setLoading(false);
    }
  };

  const studyStyles = [...new Set(matches.map((m) => m.user.studyStyle).filter(Boolean))] as string[];
  const modes = [...new Set(matches.map((m) => m.user.preferredMode).filter(Boolean))] as string[];

  if (loading) {
    return (
      <div className="loading-page">
        <div className="spinner" />
        Finding compatible study partners...
      </div>
    );
  }

  return (
    <>
      <div className="page-header">
        <h1>People</h1>
        <p>Discover compatible study partners based on your profile and courses.</p>
      </div>

      <div className="filters-bar">
        <div className="search-input">
          <Search size={16} />
          <input
            placeholder="Search by name, school, major, or course..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
        <select className="filter-select" value={styleFilter} onChange={(e) => setStyleFilter(e.target.value)}>
          <option value="">All study styles</option>
          {studyStyles.map((s) => <option key={s} value={s}>{s}</option>)}
        </select>
        <select className="filter-select" value={modeFilter} onChange={(e) => setModeFilter(e.target.value)}>
          <option value="">All modes</option>
          {modes.map((m) => <option key={m} value={m}>{m}</option>)}
        </select>
      </div>

      {error && <div className="alert alert-error mb-4">{error}</div>}

      {filtered.length === 0 ? (
        <div className="card">
          <div className="empty-state">
            <Users size={48} />
            <h3>No matches found</h3>
            <p>Try adjusting your filters or updating your profile to find more study partners.</p>
          </div>
        </div>
      ) : (
        <>
          <p className="text-sm text-muted mb-4">{filtered.length} match{filtered.length !== 1 ? 'es' : ''} found</p>
          <div className="grid-3">
            {filtered.map((match, i) => (
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
                    <p>{match.user.school}</p>
                  </div>
                  <div className={`score-badge ${getScoreClass(match.score)}`}>
                    {Math.round(match.score)}%
                  </div>
                </div>

                <div className="match-card-chips">
                  {match.user.major && <span className="chip chip-primary">{match.user.major}</span>}
                  {match.user.academicLevel && <span className="chip chip-gray">{match.user.academicLevel}</span>}
                  {match.user.studyStyle && <span className="chip chip-accent">{match.user.studyStyle}</span>}
                </div>

                <ul className="match-card-reasons">
                  {match.reasons.slice(0, 2).map((r, j) => (
                    <li key={j}>
                      <Sparkles size={12} style={{ color: 'var(--primary)' }} />
                      {r}
                    </li>
                  ))}
                </ul>

                <div className="match-card-actions">
                  <Link to={`/people/${match.user.id}`} className="btn btn-primary btn-sm" style={{ flex: 1 }}>
                    View Profile
                  </Link>
                </div>
              </div>
            ))}
          </div>
        </>
      )}
    </>
  );
}
