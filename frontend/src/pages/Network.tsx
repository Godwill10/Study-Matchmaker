import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { UserPlus, Check, X, Users, Clock, Send } from 'lucide-react';
import API from '../api/api';
import type { ConnectionRequestDto, FriendDto } from '../types';

type Tab = 'incoming' | 'sent' | 'friends';

function getInitials(name: string) {
  return name.split(' ').map((w) => w[0]).join('').slice(0, 2).toUpperCase();
}

function timeAgo(dateStr: string) {
  const diff = Date.now() - new Date(dateStr).getTime();
  const mins = Math.floor(diff / 60000);
  if (mins < 1) return 'just now';
  if (mins < 60) return `${mins}m ago`;
  const hrs = Math.floor(mins / 60);
  if (hrs < 24) return `${hrs}h ago`;
  const days = Math.floor(hrs / 24);
  return `${days}d ago`;
}

export default function Network() {
  const navigate = useNavigate();
  const [tab, setTab] = useState<Tab>('incoming');
  const [incoming, setIncoming] = useState<ConnectionRequestDto[]>([]);
  const [sent, setSent] = useState<ConnectionRequestDto[]>([]);
  const [friends, setFriends] = useState<FriendDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState<number | null>(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) { navigate('/login'); return; }
    void loadAll();
  }, []);

  const loadAll = async () => {
    try {
      setLoading(true);
      const [incRes, sentRes, friendsRes] = await Promise.all([
        API.get<ConnectionRequestDto[]>('/connections/requests/incoming'),
        API.get<ConnectionRequestDto[]>('/connections/requests/sent'),
        API.get<FriendDto[]>('/connections/friends'),
      ]);
      setIncoming(incRes.data);
      setSent(sentRes.data);
      setFriends(friendsRes.data);
    } catch (err: any) {
      if (err.response?.status === 403) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        navigate('/login');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleAccept = async (id: number) => {
    try {
      setActionLoading(id);
      await API.post(`/connections/request/${id}/accept`);
      await loadAll();
    } finally {
      setActionLoading(null);
    }
  };

  const handleDecline = async (id: number) => {
    try {
      setActionLoading(id);
      await API.post(`/connections/request/${id}/decline`);
      await loadAll();
    } finally {
      setActionLoading(null);
    }
  };

  if (loading) {
    return (
      <div className="loading-page">
        <div className="spinner" />
        Loading your network...
      </div>
    );
  }

  const tabs: { key: Tab; label: string; count: number; icon: typeof Users }[] = [
    { key: 'incoming', label: 'Incoming', count: incoming.length, icon: UserPlus },
    { key: 'sent', label: 'Sent', count: sent.length, icon: Send },
    { key: 'friends', label: 'Friends', count: friends.length, icon: Users },
  ];

  return (
    <>
      <div className="page-header">
        <h1>Network</h1>
        <p>Manage your connection requests and study partners.</p>
      </div>

      <div className="profile-tabs" style={{ marginBottom: 24 }}>
        {tabs.map((t) => (
          <button
            key={t.key}
            className={`profile-tab ${tab === t.key ? 'active' : ''}`}
            onClick={() => setTab(t.key)}
          >
            <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6 }}>
              <t.icon size={15} />
              {t.label}
              {t.count > 0 && (
                <span className={`chip ${t.key === 'incoming' ? 'chip-primary' : 'chip-gray'}`} style={{ fontSize: 11, padding: '2px 7px' }}>
                  {t.count}
                </span>
              )}
            </span>
          </button>
        ))}
      </div>

      {tab === 'incoming' && (
        incoming.length === 0 ? (
          <div className="card">
            <div className="empty-state">
              <UserPlus size={48} />
              <h3>No incoming requests</h3>
              <p>When someone sends you a connection request, it will appear here.</p>
            </div>
          </div>
        ) : (
          <div className="grid-2">
            {incoming.map((req) => (
              <div key={req.id} className="card card-hover">
                <div style={{ display: 'flex', alignItems: 'center', gap: 14, marginBottom: 14 }}>
                  <div className="avatar">
                    {req.sender.profileImageUrl ? (
                      <img src={req.sender.profileImageUrl} alt="" />
                    ) : (
                      getInitials(req.sender.fullName)
                    )}
                  </div>
                  <div style={{ flex: 1, minWidth: 0 }}>
                    <h3 style={{ fontSize: 15, fontWeight: 600 }}>{req.sender.fullName}</h3>
                    <p className="text-sm text-muted">{req.sender.school} &middot; {req.sender.major}</p>
                  </div>
                  <span className="text-sm text-muted">
                    <Clock size={12} style={{ marginRight: 4 }} />
                    {timeAgo(req.createdAt)}
                  </span>
                </div>

                <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6, marginBottom: 14 }}>
                  {req.sender.academicLevel && <span className="chip chip-primary">{req.sender.academicLevel}</span>}
                  {req.sender.studyStyle && <span className="chip chip-accent">{req.sender.studyStyle}</span>}
                </div>

                <div style={{ display: 'flex', gap: 8 }}>
                  <button
                    className="btn btn-primary btn-sm"
                    style={{ flex: 1 }}
                    onClick={() => handleAccept(req.id)}
                    disabled={actionLoading === req.id}
                  >
                    <Check size={14} />
                    Accept
                  </button>
                  <button
                    className="btn btn-secondary btn-sm"
                    style={{ flex: 1 }}
                    onClick={() => handleDecline(req.id)}
                    disabled={actionLoading === req.id}
                  >
                    <X size={14} />
                    Decline
                  </button>
                  <Link
                    to={`/people/${req.sender.id}`}
                    className="btn btn-ghost btn-sm"
                  >
                    View
                  </Link>
                </div>
              </div>
            ))}
          </div>
        )
      )}

      {tab === 'sent' && (
        sent.length === 0 ? (
          <div className="card">
            <div className="empty-state">
              <Send size={48} />
              <h3>No pending sent requests</h3>
              <p>Connection requests you send will appear here until they are accepted or declined.</p>
              <Link to="/people" className="btn btn-primary mt-4">Find people</Link>
            </div>
          </div>
        ) : (
          <div className="grid-2">
            {sent.map((req) => (
              <div key={req.id} className="card card-hover">
                <div style={{ display: 'flex', alignItems: 'center', gap: 14, marginBottom: 14 }}>
                  <div className="avatar">
                    {req.receiver.profileImageUrl ? (
                      <img src={req.receiver.profileImageUrl} alt="" />
                    ) : (
                      getInitials(req.receiver.fullName)
                    )}
                  </div>
                  <div style={{ flex: 1, minWidth: 0 }}>
                    <h3 style={{ fontSize: 15, fontWeight: 600 }}>{req.receiver.fullName}</h3>
                    <p className="text-sm text-muted">{req.receiver.school} &middot; {req.receiver.major}</p>
                  </div>
                  <span className="chip chip-accent" style={{ fontSize: 11 }}>Pending</span>
                </div>
                <p className="text-sm text-muted">
                  Sent {timeAgo(req.createdAt)}
                </p>
                <div style={{ marginTop: 12 }}>
                  <Link to={`/people/${req.receiver.id}`} className="btn btn-ghost btn-sm">
                    View Profile
                  </Link>
                </div>
              </div>
            ))}
          </div>
        )
      )}

      {tab === 'friends' && (
        friends.length === 0 ? (
          <div className="card">
            <div className="empty-state">
              <Users size={48} />
              <h3>No connections yet</h3>
              <p>Start building your study network by connecting with compatible students.</p>
              <Link to="/people" className="btn btn-primary mt-4">Browse people</Link>
            </div>
          </div>
        ) : (
          <div className="grid-3">
            {friends.map((f) => (
              <div key={f.user.id} className="card card-hover">
                <div style={{ display: 'flex', alignItems: 'center', gap: 14, marginBottom: 14 }}>
                  <div className="avatar">
                    {f.user.profileImageUrl ? (
                      <img src={f.user.profileImageUrl} alt="" />
                    ) : (
                      getInitials(f.user.fullName)
                    )}
                  </div>
                  <div style={{ flex: 1, minWidth: 0 }}>
                    <h3 style={{ fontSize: 15, fontWeight: 600 }}>{f.user.fullName}</h3>
                    <p className="text-sm text-muted">{f.user.school}</p>
                  </div>
                </div>

                {f.sharedCourses.length > 0 && (
                  <div style={{ marginBottom: 12 }}>
                    <p className="text-sm font-semibold mb-2" style={{ color: 'var(--gray-600)' }}>
                      Shared courses
                    </p>
                    <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
                      {f.sharedCourses.map((c) => (
                        <span key={c} className="chip chip-primary">{c}</span>
                      ))}
                    </div>
                  </div>
                )}

                <p className="text-sm text-muted mb-2">
                  Connected {timeAgo(f.connectedAt)}
                </p>

                <Link to={`/people/${f.user.id}`} className="btn btn-secondary btn-sm" style={{ width: '100%' }}>
                  View Profile
                </Link>
              </div>
            ))}
          </div>
        )
      )}
    </>
  );
}
