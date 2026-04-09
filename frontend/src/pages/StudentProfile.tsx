import { useEffect, useState } from 'react';
import { useNavigate, useParams, Link } from 'react-router-dom';
import { ArrowLeft, MapPin, BookOpen, GraduationCap, Sparkles, UserPlus, Check } from 'lucide-react';
import API from '../api/api';
import type { MatchResult, UserProfile, FriendDto } from '../types';

function getInitials(name: string) {
  return name.split(' ').map((w) => w[0]).join('').slice(0, 2).toUpperCase();
}

function getScoreClass(score: number) {
  if (score >= 70) return 'score-high';
  if (score >= 40) return 'score-med';
  return 'score-low';
}

type ConnectionStatus = 'none' | 'pending_sent' | 'pending_received' | 'friends';

export default function StudentProfile() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [matchInfo, setMatchInfo] = useState<MatchResult | null>(null);
  const [connectionStatus, setConnectionStatus] = useState<ConnectionStatus>('none');
  const [loading, setLoading] = useState(true);
  const [connectLoading, setConnectLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!id) return;
    void load();
  }, [id]);

  const load = async () => {
    try {
      setLoading(true);
      const [profileRes, matchRes, friendsRes, sentRes, incomingRes] = await Promise.all([
        API.get(`/users/${id}`),
        API.post('/users/match', {}).catch(() => ({ data: [] as MatchResult[] })),
        API.get<FriendDto[]>('/connections/friends').catch(() => ({ data: [] as FriendDto[] })),
        API.get('/connections/requests/sent').catch(() => ({ data: [] as any[] })),
        API.get('/connections/requests/incoming').catch(() => ({ data: [] as any[] })),
      ]);
      setProfile(profileRes.data);

      const found = (matchRes.data as MatchResult[]).find((m) => String(m.user.id) === id);
      if (found) setMatchInfo(found);

      const isFriend = (friendsRes.data as FriendDto[]).some((f) => String(f.user.id) === id);
      if (isFriend) {
        setConnectionStatus('friends');
      } else if ((sentRes.data as any[]).some((r: any) => String(r.receiver?.id) === id)) {
        setConnectionStatus('pending_sent');
      } else if ((incomingRes.data as any[]).some((r: any) => String(r.sender?.id) === id)) {
        setConnectionStatus('pending_received');
      } else {
        setConnectionStatus('none');
      }
    } catch (err: any) {
      if (err.response?.status === 403) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        navigate('/login');
        return;
      }
      setError(err.response?.data?.message || 'Failed to load profile');
    } finally {
      setLoading(false);
    }
  };

  const handleConnect = async () => {
    if (!id) return;
    try {
      setConnectLoading(true);
      await API.post('/connections/request', { receiverId: Number(id) });
      setConnectionStatus('pending_sent');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to send request');
    } finally {
      setConnectLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="loading-page">
        <div className="spinner" />
        Loading profile...
      </div>
    );
  }

  if (error || !profile) {
    return (
      <div className="loading-page">
        <p>{error || 'Profile not found'}</p>
        <Link to="/people" className="btn btn-secondary mt-4">Back to People</Link>
      </div>
    );
  }

  const location = [profile.city, profile.state].filter(Boolean).join(', ');

  const connectButton = () => {
    switch (connectionStatus) {
      case 'friends':
        return <span className="chip chip-success" style={{ padding: '8px 16px', fontSize: 14 }}><Check size={14} /> Connected</span>;
      case 'pending_sent':
        return <span className="chip chip-accent" style={{ padding: '8px 16px', fontSize: 14 }}>Request sent</span>;
      case 'pending_received':
        return <Link to="/network" className="btn btn-primary btn-sm">Respond to request</Link>;
      default:
        return (
          <button className="btn btn-primary" onClick={handleConnect} disabled={connectLoading}>
            <UserPlus size={16} />
            {connectLoading ? 'Sending...' : 'Connect'}
          </button>
        );
    }
  };

  return (
    <>
      <button className="btn btn-ghost mb-4" onClick={() => navigate(-1)}>
        <ArrowLeft size={16} /> Back
      </button>

      <div className="profile-header">
        <div className="avatar avatar-xl">
          {profile.profileImageUrl ? (
            <img src={profile.profileImageUrl} alt="" />
          ) : (
            getInitials(profile.fullName)
          )}
        </div>
        <div className="profile-header-info">
          <h1>{profile.fullName}</h1>
          <p>{profile.school} &middot; {profile.major}</p>
          <div className="profile-header-chips">
            {profile.academicLevel && <span className="chip chip-primary">{profile.academicLevel}</span>}
            {profile.studyStyle && <span className="chip chip-accent">{profile.studyStyle}</span>}
            {profile.preferredMode && <span className="chip chip-gray">{profile.preferredMode}</span>}
          </div>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          {connectButton()}
          {matchInfo && (
            <div className={`score-badge ${getScoreClass(matchInfo.score)}`} style={{ width: 64, height: 64, fontSize: 18 }}>
              {Math.round(matchInfo.score)}%
            </div>
          )}
        </div>
      </div>

      <div className="grid-2">
        <div className="card">
          <div className="profile-section">
            <h3>About</h3>
            <p className="text-sm" style={{ lineHeight: 1.6, color: 'var(--gray-600)' }}>
              {profile.bio || 'No bio provided.'}
            </p>
          </div>

          <div className="profile-section">
            <h3>Details</h3>
            <div className="info-grid">
              <div className="info-item">
                <label><GraduationCap size={12} style={{ marginRight: 4 }} />Academic Level</label>
                <p>{profile.academicLevel || 'Not set'}</p>
              </div>
              <div className="info-item">
                <label><BookOpen size={12} style={{ marginRight: 4 }} />Study Style</label>
                <p>{profile.studyStyle || 'Not set'}</p>
              </div>
              <div className="info-item">
                <label>Preferred Mode</label>
                <p>{profile.preferredMode || 'Not set'}</p>
              </div>
              {profile.locationVisible && location && (
                <div className="info-item">
                  <label><MapPin size={12} style={{ marginRight: 4 }} />Location</label>
                  <p>{location}</p>
                </div>
              )}
            </div>
          </div>

          <div className="profile-section">
            <h3>Courses</h3>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
              {profile.courses?.length ? (
                profile.courses.map((c) => (
                  <span key={c} className="chip chip-primary">{c}</span>
                ))
              ) : (
                <p className="text-sm text-muted">No courses listed.</p>
              )}
            </div>
          </div>
        </div>

        {matchInfo && (
          <div className="card">
            <div className="profile-section">
              <h3 style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <Sparkles size={18} style={{ color: 'var(--primary)' }} />
                Why you're a match
              </h3>
              <ul className="match-card-reasons" style={{ marginTop: 12 }}>
                {matchInfo.reasons.map((r, i) => (
                  <li key={i} style={{ padding: '6px 0' }}>
                    <Sparkles size={12} style={{ color: 'var(--primary)' }} />
                    {r}
                  </li>
                ))}
              </ul>
            </div>
          </div>
        )}
      </div>
    </>
  );
}
