import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
  Calendar,
  Plus,
  Clock,
  MapPin,
  Users,
  Monitor,
  X,
  LogIn,
  LogOut as LogOutIcon,
  Trash2,
} from 'lucide-react';
import API from '../api/api';
import type { StudySessionDto, UserProfile } from '../types';

type Tab = 'upcoming' | 'mine' | 'hosted';

function getInitials(name: string) {
  return name.split(' ').map((w) => w[0]).join('').slice(0, 2).toUpperCase();
}

function formatDate(dateStr: string) {
  const d = new Date(dateStr);
  return d.toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric' });
}

function formatTime(dateStr: string) {
  const d = new Date(dateStr);
  return d.toLocaleTimeString('en-US', { hour: 'numeric', minute: '2-digit' });
}

function formatRange(start: string, end?: string) {
  if (!end) return formatTime(start);
  return `${formatTime(start)} – ${formatTime(end)}`;
}

const MODE_ICONS: Record<string, typeof Monitor> = {
  Online: Monitor,
  'In-person': MapPin,
  Hybrid: Calendar,
};

export default function Schedule() {
  const navigate = useNavigate();
  const [tab, setTab] = useState<Tab>('upcoming');
  const [sessions, setSessions] = useState<StudySessionDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [showCreate, setShowCreate] = useState(false);
  const [actionLoading, setActionLoading] = useState<number | null>(null);
  const [error, setError] = useState('');

  const stored = localStorage.getItem('user');
  const currentUser: UserProfile | null = stored ? JSON.parse(stored) : null;

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) { navigate('/login'); return; }
    void loadSessions();
  }, [tab]);

  const loadSessions = async () => {
    try {
      setLoading(true);
      setError('');
      const endpoint =
        tab === 'upcoming' ? '/sessions/upcoming' :
        tab === 'mine' ? '/sessions/mine' :
        '/sessions/hosted';
      const res = await API.get<StudySessionDto[]>(endpoint);
      setSessions(res.data);
    } catch (err: any) {
      if (err.response?.status === 403) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        navigate('/login');
        return;
      }
      setError(err.response?.data?.message || 'Failed to load sessions');
    } finally {
      setLoading(false);
    }
  };

  const handleJoin = async (id: number) => {
    try {
      setActionLoading(id);
      await API.post(`/sessions/${id}/join`);
      await loadSessions();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to join');
    } finally {
      setActionLoading(null);
    }
  };

  const handleLeave = async (id: number) => {
    try {
      setActionLoading(id);
      await API.post(`/sessions/${id}/leave`);
      await loadSessions();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to leave');
    } finally {
      setActionLoading(null);
    }
  };

  const handleCancel = async (id: number) => {
    try {
      setActionLoading(id);
      await API.post(`/sessions/${id}/cancel`);
      await loadSessions();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to cancel');
    } finally {
      setActionLoading(null);
    }
  };

  const tabs: { key: Tab; label: string }[] = [
    { key: 'upcoming', label: 'Browse Sessions' },
    { key: 'mine', label: 'My Sessions' },
    { key: 'hosted', label: 'Hosted' },
  ];

  return (
    <>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 24 }}>
        <div className="page-header" style={{ marginBottom: 0 }}>
          <h1>Schedule</h1>
          <p>Browse, create, and join study sessions.</p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowCreate(true)}>
          <Plus size={16} /> New Session
        </button>
      </div>

      <div className="profile-tabs" style={{ marginBottom: 24 }}>
        {tabs.map((t) => (
          <button
            key={t.key}
            className={`profile-tab ${tab === t.key ? 'active' : ''}`}
            onClick={() => setTab(t.key)}
          >
            {t.label}
          </button>
        ))}
      </div>

      {error && <div className="alert alert-error mb-4">{error}</div>}

      {showCreate && (
        <CreateSessionModal
          onClose={() => setShowCreate(false)}
          onCreated={() => { setShowCreate(false); void loadSessions(); }}
          courses={currentUser?.courses || []}
        />
      )}

      {loading ? (
        <div className="loading-page">
          <div className="spinner" />
          Loading sessions...
        </div>
      ) : sessions.length === 0 ? (
        <div className="card">
          <div className="empty-state">
            <Calendar size={48} />
            <h3>{tab === 'upcoming' ? 'No upcoming sessions' : tab === 'mine' ? "You haven't joined any sessions" : "You haven't hosted any sessions"}</h3>
            <p>{tab === 'upcoming' ? 'Be the first to create a study session!' : 'Join or create a session to get started.'}</p>
            <button className="btn btn-primary mt-4" onClick={() => setShowCreate(true)}>
              <Plus size={16} /> Create Session
            </button>
          </div>
        </div>
      ) : (
        <div className="grid-2">
          {sessions.map((session) => {
            const ModeIcon = MODE_ICONS[session.mode] || Calendar;
            const isInvolved = session.isHost || session.isParticipant;

            return (
              <div key={session.id} className="card card-hover">
                <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', marginBottom: 12 }}>
                  <div>
                    <h3 style={{ fontSize: 16, fontWeight: 700 }}>{session.title}</h3>
                    {session.course && <p className="text-sm text-muted">{session.course}{session.topic ? ` — ${session.topic}` : ''}</p>}
                  </div>
                  <span className={`chip ${session.status === 'UPCOMING' ? 'chip-success' : session.status === 'CANCELLED' ? 'chip-gray' : 'chip-primary'}`}>
                    {session.status.replace('_', ' ')}
                  </span>
                </div>

                <div style={{ display: 'flex', flexWrap: 'wrap', gap: 16, marginBottom: 14, fontSize: 13, color: 'var(--gray-600)' }}>
                  <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
                    <Calendar size={14} /> {formatDate(session.startTime)}
                  </span>
                  <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
                    <Clock size={14} /> {formatRange(session.startTime, session.endTime)}
                  </span>
                  <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
                    <ModeIcon size={14} /> {session.mode}
                  </span>
                  <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
                    <Users size={14} /> {session.currentParticipantCount}/{session.maxParticipants}
                  </span>
                </div>

                {session.location && (
                  <p className="text-sm text-muted mb-2" style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
                    <MapPin size={12} /> {session.location}
                  </p>
                )}

                {session.description && (
                  <p className="text-sm mb-2" style={{ color: 'var(--gray-600)', lineHeight: 1.5 }}>
                    {session.description}
                  </p>
                )}

                <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginTop: 14 }}>
                  <div className="avatar avatar-sm">
                    {session.host.profileImageUrl
                      ? <img src={session.host.profileImageUrl} alt="" />
                      : getInitials(session.host.fullName)
                    }
                  </div>
                  <span className="text-sm">
                    {session.isHost ? 'You' : session.host.fullName}
                    {session.isHost && <span className="chip chip-primary" style={{ marginLeft: 6, fontSize: 10, padding: '1px 6px' }}>Host</span>}
                  </span>
                </div>

                {session.status === 'UPCOMING' && (
                  <div style={{ display: 'flex', gap: 8, marginTop: 14 }}>
                    {session.isHost ? (
                      <button
                        className="btn btn-danger btn-sm"
                        onClick={() => handleCancel(session.id)}
                        disabled={actionLoading === session.id}
                      >
                        <Trash2 size={14} /> Cancel
                      </button>
                    ) : session.isParticipant ? (
                      <button
                        className="btn btn-secondary btn-sm"
                        onClick={() => handleLeave(session.id)}
                        disabled={actionLoading === session.id}
                      >
                        <LogOutIcon size={14} /> Leave
                      </button>
                    ) : (
                      <button
                        className="btn btn-primary btn-sm"
                        onClick={() => handleJoin(session.id)}
                        disabled={actionLoading === session.id || session.isFull}
                      >
                        <LogIn size={14} /> {session.isFull ? 'Full' : 'Join'}
                      </button>
                    )}

                    {!session.isHost && (
                      <Link to={`/people/${session.host.id}`} className="btn btn-ghost btn-sm">
                        View {session.host.fullName.split(' ')[0]}'s profile
                      </Link>
                    )}
                  </div>
                )}
              </div>
            );
          })}
        </div>
      )}
    </>
  );
}

function CreateSessionModal({
  onClose,
  onCreated,
  courses,
}: {
  onClose: () => void;
  onCreated: () => void;
  courses: string[];
}) {
  const [form, setForm] = useState({
    title: '',
    course: '',
    topic: '',
    description: '',
    startTime: '',
    endTime: '',
    location: '',
    mode: 'Online',
    maxParticipants: 10,
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.title || !form.startTime) {
      setError('Title and start time are required');
      return;
    }
    try {
      setLoading(true);
      setError('');
      await API.post('/sessions', {
        ...form,
        maxParticipants: Number(form.maxParticipants) || 10,
        startTime: form.startTime,
        endTime: form.endTime || null,
      });
      onCreated();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to create session');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{
      position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)',
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      zIndex: 50, padding: 24,
    }}>
      <div className="card" style={{ width: '100%', maxWidth: 520, maxHeight: '90vh', overflow: 'auto' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }}>
          <h2 style={{ fontSize: 18, fontWeight: 700 }}>Create Study Session</h2>
          <button className="btn btn-ghost btn-icon" onClick={onClose}><X size={18} /></button>
        </div>

        {error && <div className="alert alert-error mb-4">{error}</div>}

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          <div className="form-group">
            <label className="form-label">Title *</label>
            <input className="form-input" name="title" placeholder="e.g. Calculus II Study Group" value={form.title} onChange={handleChange} />
          </div>

          <div className="grid-2">
            <div className="form-group">
              <label className="form-label">Course</label>
              <select className="form-select" name="course" value={form.course} onChange={handleChange}>
                <option value="">Select course</option>
                {courses.map((c) => <option key={c} value={c}>{c}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Topic</label>
              <input className="form-input" name="topic" placeholder="e.g. Integrals" value={form.topic} onChange={handleChange} />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">Description</label>
            <textarea className="form-textarea" name="description" placeholder="What will you cover?" value={form.description} onChange={handleChange} rows={2} />
          </div>

          <div className="grid-2">
            <div className="form-group">
              <label className="form-label">Start time *</label>
              <input className="form-input" type="datetime-local" name="startTime" value={form.startTime} onChange={handleChange} />
            </div>
            <div className="form-group">
              <label className="form-label">End time</label>
              <input className="form-input" type="datetime-local" name="endTime" value={form.endTime} onChange={handleChange} />
            </div>
          </div>

          <div className="grid-2">
            <div className="form-group">
              <label className="form-label">Mode</label>
              <select className="form-select" name="mode" value={form.mode} onChange={handleChange}>
                <option value="Online">Online</option>
                <option value="In-person">In-person</option>
                <option value="Hybrid">Hybrid</option>
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Max participants</label>
              <input className="form-input" type="number" name="maxParticipants" min={2} max={50} value={form.maxParticipants} onChange={handleChange} />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">Location</label>
            <input className="form-input" name="location" placeholder="e.g. Library Room 204 or Zoom link" value={form.location} onChange={handleChange} />
          </div>

          <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end', marginTop: 8 }}>
            <button type="button" className="btn btn-secondary" onClick={onClose}>Cancel</button>
            <button type="submit" className="btn btn-primary" disabled={loading}>
              {loading ? 'Creating...' : 'Create Session'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
