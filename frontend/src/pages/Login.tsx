import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/api';
import type { AuthResponse } from '../types';
import RegistrationForm, { type RegistrationData } from '../components/RegistrationForm';

export default function Login() {
  const navigate = useNavigate();
  const [isRegister, setIsRegister] = useState(false);
  const [loginData, setLoginData] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const saveAuth = (data: AuthResponse) => {
    localStorage.setItem('token', data.token);
    localStorage.setItem('user', JSON.stringify(data.user));
    navigate('/dashboard');
  };

  const submitLogin = async () => {
    try {
      setLoading(true);
      setError('');

      const response = await api.post<AuthResponse>('/auth/login', loginData);

      saveAuth(response.data);

    } catch (err: any) {
      setError(err.response?.data?.message ?? 'Unable to login');
    } finally {
      setLoading(false);
    }
  };

  const submitRegister = async (payload: RegistrationData) => {
    try {
      setLoading(true);
      setError('');

      const response = await api.post<AuthResponse>('/auth/register', payload);

      saveAuth(response.data);

    } catch (err: any) {
      setError(err.response?.data?.message ?? 'Unable to register');
    } finally {
      setLoading(false);
    }
  };

  return (
      <div className="auth-page">
        <div className="panel">

          <div className="toggle-row">
            <button
                className={`tab ${!isRegister ? 'active' : ''}`}
                onClick={() => setIsRegister(false)}
            >
              Login
            </button>

            <button
                className={`tab ${isRegister ? 'active' : ''}`}
                onClick={() => setIsRegister(true)}
            >
              Register
            </button>
          </div>

          {error && <div className="error-box">{error}</div>}

          {!isRegister ? (
              <div className="form-grid">
                <input
                    placeholder="Email"
                    value={loginData.email}
                    onChange={e =>
                        setLoginData({ ...loginData, email: e.target.value })
                    }
                />

                <input
                    type="password"
                    placeholder="Password"
                    value={loginData.password}
                    onChange={e =>
                        setLoginData({ ...loginData, password: e.target.value })
                    }
                />

                <button
                    className="btn primary"
                    onClick={submitLogin}
                    disabled={loading}
                >
                  {loading ? 'Loading...' : 'Login'}
                </button>
              </div>
          ) : (
              <RegistrationForm onSubmit={submitRegister} loading={loading} />
          )}
        </div>
      </div>
  );
}