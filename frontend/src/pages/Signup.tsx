import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Sparkles, ArrowLeft, ArrowRight, Check } from 'lucide-react';
import API from '../api/api';
import type { AuthResponse, ReferenceOption, StateOption } from '../types';

interface FormState {
  fullName: string;
  email: string;
  password: string;
  state: string;
  university: string;
  major: string;
  academicLevel: string;
  studyStyle: string;
  preferredMode: string;
  city: string;
  bio: string;
  courses: string[];
  locationVisible: boolean;
}

const INITIAL: FormState = {
  fullName: '',
  email: '',
  password: '',
  state: '',
  university: '',
  major: '',
  academicLevel: '',
  studyStyle: '',
  preferredMode: 'Hybrid',
  city: '',
  bio: '',
  courses: [],
  locationVisible: true,
};

const STEPS = [
  { title: 'Create your account', desc: 'Basic info to get started' },
  { title: 'Your university', desc: 'Tell us about your school' },
  { title: 'Study preferences', desc: 'How do you like to study?' },
  { title: 'Select courses', desc: 'Pick the courses you\'re taking' },
];

export default function Signup() {
  const navigate = useNavigate();
  const [step, setStep] = useState(0);
  const [form, setForm] = useState<FormState>(INITIAL);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const [states, setStates] = useState<StateOption[]>([]);
  const [universities, setUniversities] = useState<string[]>([]);
  const [majors, setMajors] = useState<ReferenceOption[]>([]);
  const [levels, setLevels] = useState<ReferenceOption[]>([]);
  const [studyStyles, setStudyStyles] = useState<ReferenceOption[]>([]);
  const [courseList, setCourseList] = useState<string[]>([]);

  useEffect(() => {
    void Promise.all([
      API.get<StateOption[]>('/reference/states').then((r) => setStates(r.data)),
      API.get<ReferenceOption[]>('/reference/majors').then((r) => setMajors(r.data)),
      API.get<ReferenceOption[]>('/reference/academic-levels').then((r) => setLevels(r.data)),
      API.get<ReferenceOption[]>('/reference/study-styles').then((r) => setStudyStyles(r.data)),
    ]).catch(() => {});
  }, []);

  useEffect(() => {
    if (form.state) {
      void API.get<string[]>(`/reference/universities?state=${form.state}`).then((r) =>
        setUniversities(r.data),
      );
    }
  }, [form.state]);

  useEffect(() => {
    if (form.academicLevel) {
      const params = new URLSearchParams({ academicLevel: form.academicLevel });
      if (form.major) params.set('major', form.major);
      void API.get<Array<ReferenceOption | string>>(
        `/reference/courses?${params}`,
      ).then((r) => {
        setCourseList(r.data.map((c) => (typeof c === 'string' ? c : c.label)));
        setForm((prev) => ({ ...prev, courses: [] }));
      });
    }
  }, [form.academicLevel, form.major]);

  const set = (name: keyof FormState, value: any) =>
    setForm((prev) => ({ ...prev, [name]: value }));

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>,
  ) => {
    const { name, value, type } = e.target;
    if (type === 'checkbox' && 'checked' in e.target) {
      set(name as keyof FormState, (e.target as HTMLInputElement).checked);
    } else {
      set(name as keyof FormState, value);
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

  const canAdvance = () => {
    if (step === 0) return form.fullName && form.email && form.password;
    if (step === 1) return form.state && form.university && form.major;
    if (step === 2) return form.academicLevel && form.studyStyle;
    if (step === 3) return form.courses.length > 0;
    return true;
  };

  const next = () => {
    setError('');
    if (!canAdvance()) {
      setError('Please fill in all required fields');
      return;
    }
    if (step < STEPS.length - 1) setStep(step + 1);
  };

  const back = () => {
    setError('');
    if (step > 0) setStep(step - 1);
  };

  const submit = async () => {
    if (!canAdvance()) {
      setError('Please select at least one course');
      return;
    }
    try {
      setLoading(true);
      setError('');
      const payload = { ...form, school: form.university };
      const res = await API.post<AuthResponse>('/auth/register', payload);
      localStorage.setItem('token', res.data.token);
      localStorage.setItem('user', JSON.stringify(res.data.user));
      navigate('/dashboard');
    } catch (err: any) {
      setError(err.response?.data?.message ?? 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-layout">
      <div className="auth-left">
        <div className="auth-left-content">
          <h1>Join StudyMatch</h1>
          <p>Create your profile and start finding compatible study partners at your university.</p>
        </div>
      </div>

      <div className="auth-right">
        <div className="auth-card">
          <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 20 }}>
            <div className="sidebar-brand-icon"><Sparkles size={16} /></div>
            <span style={{ fontWeight: 700, fontSize: 16 }}>StudyMatch</span>
          </div>

          <div className="steps-indicator">
            {STEPS.map((_, i) => (
              <div
                key={i}
                className={`step-dot ${i === step ? 'active' : ''} ${i < step ? 'done' : ''}`}
              />
            ))}
          </div>

          <div className="step-title">{STEPS[step].title}</div>
          <div className="step-desc">{STEPS[step].desc}</div>

          {error && <div className="auth-error" style={{ marginBottom: 16 }}>{error}</div>}

          {step === 0 && (
            <div className="auth-form">
              <div className="form-group">
                <label className="form-label">Full name</label>
                <input className="form-input" name="fullName" placeholder="Jane Doe" value={form.fullName} onChange={handleChange} />
              </div>
              <div className="form-group">
                <label className="form-label">Email</label>
                <input className="form-input" name="email" type="email" placeholder="you@university.edu" value={form.email} onChange={handleChange} />
              </div>
              <div className="form-group">
                <label className="form-label">Password</label>
                <input className="form-input" name="password" type="password" placeholder="Create a password" value={form.password} onChange={handleChange} />
              </div>
            </div>
          )}

          {step === 1 && (
            <div className="auth-form">
              <div className="form-group">
                <label className="form-label">State</label>
                <select className="form-select" name="state" value={form.state} onChange={handleChange}>
                  <option value="">Select state</option>
                  {states.map((s) => <option key={s.code} value={s.code}>{s.label}</option>)}
                </select>
              </div>
              <div className="form-group">
                <label className="form-label">University</label>
                <select className="form-select" name="university" value={form.university} onChange={handleChange}>
                  <option value="">Select university</option>
                  {universities.map((u) => <option key={u} value={u}>{u}</option>)}
                </select>
              </div>
              <div className="form-group">
                <label className="form-label">Major</label>
                <select className="form-select" name="major" value={form.major} onChange={handleChange}>
                  <option value="">Select major</option>
                  {majors.map((m) => <option key={m.label} value={m.label}>{m.label}</option>)}
                </select>
              </div>
              <div className="form-group">
                <label className="form-label">City</label>
                <input className="form-input" name="city" placeholder="Your city" value={form.city} onChange={handleChange} />
              </div>
            </div>
          )}

          {step === 2 && (
            <div className="auth-form">
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
              <div className="form-group">
                <label className="form-label">Preferred study mode</label>
                <select className="form-select" name="preferredMode" value={form.preferredMode} onChange={handleChange}>
                  <option value="Hybrid">Hybrid</option>
                  <option value="Online">Online</option>
                  <option value="In-person">In-person</option>
                </select>
              </div>
              <div className="form-group">
                <label className="form-label">Short bio</label>
                <textarea className="form-textarea" name="bio" placeholder="Tell others about yourself..." value={form.bio} onChange={handleChange} rows={3} />
              </div>
              <label className="form-checkbox-label">
                <input type="checkbox" name="locationVisible" checked={form.locationVisible} onChange={handleChange} />
                Show my location to matches
              </label>
            </div>
          )}

          {step === 3 && (
            <div className="auth-form">
              {courseList.length === 0 ? (
                <p className="text-sm text-muted">Select a major and academic level in the previous steps to see courses for your program.</p>
              ) : (
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
              )}
              <p className="text-sm text-muted mt-2">
                {form.courses.length} course{form.courses.length !== 1 ? 's' : ''} selected
              </p>
            </div>
          )}

          <div className="step-actions">
            {step > 0 ? (
              <button className="btn btn-secondary" onClick={back}>
                <ArrowLeft size={16} /> Back
              </button>
            ) : (
              <div />
            )}

            {step < STEPS.length - 1 ? (
              <button className="btn btn-primary" onClick={next}>
                Continue <ArrowRight size={16} />
              </button>
            ) : (
              <button className="btn btn-primary" onClick={submit} disabled={loading}>
                {loading ? 'Creating account...' : 'Create account'}
              </button>
            )}
          </div>

          <div className="auth-footer">
            Already have an account?{' '}
            <Link to="/login">Sign in</Link>
          </div>
        </div>
      </div>
    </div>
  );
}
