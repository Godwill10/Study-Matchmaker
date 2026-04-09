import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Save, Check } from 'lucide-react';
import API from '../api/api';
import type { UserProfile, ReferenceOption, StateOption } from '../types';

function getInitials(name: string) {
  return name.split(' ').map((w) => w[0]).join('').slice(0, 2).toUpperCase();
}

export default function Profile() {
  const navigate = useNavigate();
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [tab, setTab] = useState<'view' | 'edit'>('view');

  const [form, setForm] = useState({
    fullName: '',
    major: '',
    academicLevel: '',
    studyStyle: '',
    preferredMode: 'Hybrid',
    city: '',
    bio: '',
    courses: [] as string[],
    locationVisible: true,
  });

  const [states, setStates] = useState<StateOption[]>([]);
  const [majors, setMajors] = useState<ReferenceOption[]>([]);
  const [levels, setLevels] = useState<ReferenceOption[]>([]);
  const [studyStyles, setStudyStyles] = useState<ReferenceOption[]>([]);
  const [courseList, setCourseList] = useState<string[]>([]);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) { navigate('/login'); return; }
    void load();
  }, []);

  const load = async () => {
    try {
      setLoading(true);
      const [me, majorsRes, levelsRes, stylesRes] = await Promise.all([
        API.get<UserProfile>('/users/me'),
        API.get<ReferenceOption[]>('/reference/majors'),
        API.get<ReferenceOption[]>('/reference/academic-levels'),
        API.get<ReferenceOption[]>('/reference/study-styles'),
      ]);
      setProfile(me.data);
      localStorage.setItem('user', JSON.stringify(me.data));
      setMajors(majorsRes.data);
      setLevels(levelsRes.data);
      setStudyStyles(stylesRes.data);

      setForm({
        fullName: me.data.fullName,
        major: me.data.major || '',
        academicLevel: me.data.academicLevel || '',
        studyStyle: me.data.studyStyle || '',
        preferredMode: me.data.preferredMode || 'Hybrid',
        city: me.data.city || '',
        bio: me.data.bio || '',
        courses: me.data.courses || [],
        locationVisible: me.data.locationVisible ?? true,
      });

      if (me.data.academicLevel) {
        void loadCourses(me.data.academicLevel, me.data.major || '');
      }
    } catch (err: any) {
      if (err.response?.status === 403) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        navigate('/login');
        return;
      }
      setError('Failed to load profile');
    } finally {
      setLoading(false);
    }
  };

  const loadCourses = async (level: string, major: string) => {
    const params = new URLSearchParams({ academicLevel: level });
    if (major) params.set('major', major);
    const res = await API.get<Array<ReferenceOption | string>>(
      `/reference/courses?${params}`,
    );
    setCourseList(res.data.map((c) => (typeof c === 'string' ? c : c.label)));
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>,
  ) => {
    const { name, value, type } = e.target;
    if (type === 'checkbox' && 'checked' in e.target) {
      setForm((prev) => ({ ...prev, [name]: (e.target as HTMLInputElement).checked }));
    } else {
      setForm((prev) => ({ ...prev, [name]: value }));
    }

    if (name === 'academicLevel' && value) {
      void loadCourses(value, form.major);
    }
    if (name === 'major' && value && form.academicLevel) {
      void loadCourses(form.academicLevel, value);
    }
  };

  const toggleCourse = (c: string) => {
    setForm((prev) => ({
      ...prev,
      courses: prev.courses.includes(c)
        ? prev.courses.filter((x) => x !== c)
        : [...prev.courses, c],
    }));
  };

  const handleSave = async () => {
    try {
      setSaving(true);
      setError('');
      setSuccess('');
      const res = await API.put<UserProfile>('/users/me', form);
      setProfile(res.data);
      localStorage.setItem('user', JSON.stringify(res.data));
      setSuccess('Profile updated successfully');
      setTab('view');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to save profile');
    } finally {
      setSaving(false);
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

  if (!profile) {
    return <div className="loading-page"><p>Could not load profile.</p></div>;
  }

  const completeness = [
    profile.fullName,
    profile.major,
    profile.academicLevel,
    profile.studyStyle,
    profile.bio,
    profile.courses?.length,
  ].filter(Boolean).length;
  const totalFields = 6;
  const pct = Math.round((completeness / totalFields) * 100);

  return (
    <>
      <div className="page-header">
        <h1>My Profile</h1>
        <p>Manage your profile to improve match quality.</p>
      </div>

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
            <span className={`chip ${pct === 100 ? 'chip-success' : 'chip-accent'}`}>
              {pct}% complete
            </span>
            {profile.academicLevel && <span className="chip chip-primary">{profile.academicLevel}</span>}
            {profile.studyStyle && <span className="chip chip-gray">{profile.studyStyle}</span>}
          </div>
        </div>
        <button
          className={`btn ${tab === 'edit' ? 'btn-ghost' : 'btn-primary'}`}
          onClick={() => setTab(tab === 'edit' ? 'view' : 'edit')}
        >
          {tab === 'edit' ? 'Cancel' : 'Edit Profile'}
        </button>
      </div>

      {error && <div className="alert alert-error mb-4">{error}</div>}
      {success && <div className="alert alert-success mb-4"><Check size={16} /> {success}</div>}

      {tab === 'view' ? (
        <div className="grid-2">
          <div className="card">
            <div className="profile-section">
              <h3>About</h3>
              <p className="text-sm" style={{ lineHeight: 1.6, color: 'var(--gray-600)' }}>
                {profile.bio || 'No bio added yet.'}
              </p>
            </div>
            <div className="profile-section">
              <h3>Details</h3>
              <div className="info-grid">
                <div className="info-item"><label>School</label><p>{profile.school}</p></div>
                <div className="info-item"><label>Major</label><p>{profile.major || 'Not set'}</p></div>
                <div className="info-item"><label>Level</label><p>{profile.academicLevel || 'Not set'}</p></div>
                <div className="info-item"><label>Study Style</label><p>{profile.studyStyle || 'Not set'}</p></div>
                <div className="info-item"><label>Preferred Mode</label><p>{profile.preferredMode || 'Not set'}</p></div>
                <div className="info-item"><label>City</label><p>{profile.city || 'Not set'}</p></div>
                <div className="info-item"><label>Location Visible</label><p>{profile.locationVisible ? 'Yes' : 'No'}</p></div>
              </div>
            </div>
          </div>
          <div className="card">
            <div className="profile-section">
              <h3>Courses</h3>
              <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
                {profile.courses?.length ? (
                  profile.courses.map((c) => <span key={c} className="chip chip-primary">{c}</span>)
                ) : (
                  <p className="text-sm text-muted">No courses listed.</p>
                )}
              </div>
            </div>
          </div>
        </div>
      ) : (
        <div className="card" style={{ maxWidth: 640 }}>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
            <div className="form-group">
              <label className="form-label">Full name</label>
              <input className="form-input" name="fullName" value={form.fullName} onChange={handleChange} />
            </div>
            <div className="form-group">
              <label className="form-label">Major</label>
              <select className="form-select" name="major" value={form.major} onChange={handleChange}>
                <option value="">Select major</option>
                {majors.map((m) => <option key={m.label} value={m.label}>{m.label}</option>)}
              </select>
            </div>
            <div className="grid-2">
              <div className="form-group">
                <label className="form-label">Academic level</label>
                <select className="form-select" name="academicLevel" value={form.academicLevel} onChange={handleChange}>
                  <option value="">Select level</option>
                  {levels.map((l) => <option key={l.label} value={l.label}>{l.label}</option>)}
                </select>
              </div>
              <div className="form-group">
                <label className="form-label">Study style</label>
                <select className="form-select" name="studyStyle" value={form.studyStyle} onChange={handleChange}>
                  <option value="">Select style</option>
                  {studyStyles.map((s) => <option key={s.label} value={s.label}>{s.label}</option>)}
                </select>
              </div>
            </div>
            <div className="form-group">
              <label className="form-label">Preferred study mode</label>
              <select className="form-select" name="preferredMode" value={form.preferredMode} onChange={handleChange}>
                <option value="Hybrid">Hybrid</option>
                <option value="Online">Online</option>
                <option value="In-person">In-person</option>
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">City</label>
              <input className="form-input" name="city" value={form.city} onChange={handleChange} placeholder="Your city" />
            </div>
            <div className="form-group">
              <label className="form-label">Bio</label>
              <textarea className="form-textarea" name="bio" value={form.bio} onChange={handleChange} rows={3} placeholder="Tell others about yourself..." />
            </div>
            <label className="form-checkbox-label">
              <input type="checkbox" name="locationVisible" checked={form.locationVisible} onChange={handleChange} />
              Show my location to matches
            </label>

            {courseList.length > 0 && (
              <div className="form-group">
                <label className="form-label">Courses ({form.courses.length} selected)</label>
                <div className="courses-grid">
                  {courseList.map((c) => (
                    <div
                      key={c}
                      className={`chip chip-outline chip-selectable ${form.courses.includes(c) ? 'selected' : ''}`}
                      onClick={() => toggleCourse(c)}
                      role="button"
                      tabIndex={0}
                    >
                      {form.courses.includes(c) && <Check size={12} />}
                      {c}
                    </div>
                  ))}
                </div>
              </div>
            )}

            <button className="btn btn-primary" onClick={handleSave} disabled={saving}>
              <Save size={16} />
              {saving ? 'Saving...' : 'Save Changes'}
            </button>
          </div>
        </div>
      )}
    </>
  );
}
