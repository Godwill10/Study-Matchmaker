import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Sparkles, Users, BookOpen, Target } from 'lucide-react';
import API from '../api/api';
import type { AuthResponse } from '../types';

export default function Login() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setLoading(true);
      setError('');
      const res = await API.post<AuthResponse>('/auth/login', form);
      localStorage.setItem('token', res.data.token);
      localStorage.setItem('user', JSON.stringify(res.data.user));
      navigate('/dashboard');
    } catch (err: any) {
      setError(err.response?.data?.message ?? 'Invalid email or password');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-layout">
      <div className="auth-left">
        <div className="auth-left-content">
          <h1>Welcome back</h1>
          <p>Sign in to continue finding study partners and growing your network.</p>
          <div className="auth-features">
            <div className="auth-feature">
              <div className="auth-feature-icon"><Target size={18} /></div>
              Smart matching algorithm
            </div>
            <div className="auth-feature">
              <div className="auth-feature-icon"><Users size={18} /></div>
              Thousands of students
            </div>
            <div className="auth-feature">
              <div className="auth-feature-icon"><BookOpen size={18} /></div>
              Course-based discovery
            </div>
          </div>
        </div>
      </div>

      <div className="auth-right">
        <div className="auth-card">
          <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 24 }}>
            <div className="sidebar-brand-icon"><Sparkles size={16} /></div>
            <span style={{ fontWeight: 700, fontSize: 16 }}>StudyMatch</span>
          </div>

          <h2>Sign in</h2>
          <p>Enter your credentials to access your account.</p>

          {error && <div className="auth-error">{error}</div>}

          <form className="auth-form" onSubmit={handleSubmit}>
            <div className="form-group">
              <label className="form-label">Email</label>
              <input
                className="form-input"
                type="email"
                name="email"
                placeholder="you@university.edu"
                value={form.email}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label className="form-label">Password</label>
              <input
                className="form-input"
                type="password"
                name="password"
                placeholder="Enter your password"
                value={form.password}
                onChange={handleChange}
                required
              />
            </div>

            <button type="submit" className="btn btn-primary btn-lg" disabled={loading} style={{ width: '100%' }}>
              {loading ? 'Signing in...' : 'Sign in'}
            </button>
          </form>

          <div className="auth-footer">
            Don't have an account?{' '}
            <Link to="/signup">Create one</Link>
          </div>
        </div>
      </div>
    </div>
  );
}
