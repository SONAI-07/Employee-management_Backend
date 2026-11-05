import React, { useState, useRef } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import './Login.css';

const API_BASE = 'http://localhost:8080';

/* ---------------- Inline SVG icons ---------------- */
const FlameLogo = () => (
  <svg width="112" height="128" viewBox="0 0 120 140" fill="none">
    <defs>
      <linearGradient id="fg-dark" x1="0" y1="1" x2="1" y2="0">
        <stop offset="0%" stopColor="#0a2f9c" /><stop offset="55%" stopColor="#1e46d8" /><stop offset="100%" stopColor="#3f6cf5" />
      </linearGradient>
      <linearGradient id="fg-mid" x1="0" y1="1" x2="1" y2="0">
        <stop offset="0%" stopColor="#2f6df0" /><stop offset="50%" stopColor="#4f8bff" /><stop offset="100%" stopColor="#8ab6ff" />
      </linearGradient>
      <linearGradient id="fg-white" x1="0" y1="1" x2="1" y2="0">
        <stop offset="0%" stopColor="#b9c8f0" /><stop offset="45%" stopColor="#e8edfb" /><stop offset="100%" stopColor="#ffffff" />
      </linearGradient>
    </defs>
    <path d="M0 76 C0 40 16 14 46 0 C30 27 30 49 46 76 C32 90 14 90 0 76 Z" fill="url(#fg-dark)" transform="translate(8,44)" />
    <path d="M0 76 C0 40 16 14 46 0 C30 27 30 49 46 76 C32 90 14 90 0 76 Z" fill="url(#fg-mid)" transform="translate(32,22)" />
    <path d="M0 76 C0 40 16 14 46 0 C30 27 30 49 46 76 C32 90 14 90 0 76 Z" fill="url(#fg-white)" transform="translate(56,0)" />
  </svg>
);

const UserIcon = () => (
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" /><circle cx="12" cy="7" r="4" />
  </svg>
);

const LockIcon = () => (
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <rect x="3" y="11" width="18" height="11" rx="2" /><path d="M7 11V7a5 5 0 0 1 10 0v4" />
  </svg>
);

const EyeIcon = ({ off }) => (
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    {off ? (
      <>
        <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94" />
        <path d="M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19" />
        <path d="M14.12 14.12a3 3 0 1 1-4.24-4.24" /><line x1="1" y1="1" x2="23" y2="23" />
      </>
    ) : (
      <>
        <path d="M1 12s4-7 11-7 11 7 11 7-4 7-11 7S1 12 1 12Z" /><circle cx="12" cy="12" r="3" />
      </>
    )}
  </svg>
);

const MoonIcon = () => (
  <svg width="22" height="22" viewBox="0 0 24 24" fill="currentColor">
    <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79Z" />
  </svg>
);

const SunIcon = () => (
  <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round">
    <circle cx="12" cy="12" r="4" fill="currentColor" />
    <path d="M12 2v2M12 20v2M4.93 4.93l1.41 1.41M17.66 17.66l1.41 1.41M2 12h2M20 12h2M4.93 19.07l1.41-1.41M17.66 6.34l1.41-1.41" />
  </svg>
);

const ArrowIcon = () => (
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#fff" strokeWidth="2.4" strokeLinecap="round" strokeLinejoin="round">
    <line x1="5" y1="12" x2="19" y2="12" /><polyline points="12 5 19 12 12 19" />
  </svg>
);

const CheckIcon = () => (
  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#fff" strokeWidth="3.5" strokeLinecap="round" strokeLinejoin="round">
    <polyline points="20 6 9 17 4 12" />
  </svg>
);

const GitHubIcon = () => (
  <svg width="30" height="30" viewBox="0 0 24 24" fill="currentColor">
    <path d="M12 .5C5.65.5.5 5.65.5 12c0 5.08 3.29 9.39 7.86 10.91.58.11.79-.25.79-.56 0-.28-.01-1.02-.02-2-3.2.7-3.88-1.54-3.88-1.54-.52-1.33-1.28-1.68-1.28-1.68-1.04-.71.08-.7.08-.7 1.15.08 1.76 1.18 1.76 1.18 1.03 1.76 2.7 1.25 3.35.96.1-.75.4-1.25.72-1.54-2.55-.29-5.24-1.28-5.24-5.68 0-1.26.45-2.28 1.18-3.09-.12-.29-.51-1.46.11-3.05 0 0 .96-.31 3.15 1.18a10.9 10.9 0 0 1 5.74 0c2.19-1.49 3.15-1.18 3.15-1.18.62 1.59.23 2.76.11 3.05.73.81 1.18 1.83 1.18 3.09 0 4.41-2.69 5.38-5.25 5.67.41.35.77 1.05.77 2.12 0 1.53-.01 2.76-.01 3.14 0 .31.21.67.8.56A10.52 10.52 0 0 0 23.5 12C23.5 5.65 18.35.5 12 .5Z" />
  </svg>
);

const MicrosoftIcon = () => (
  <svg width="26" height="26" viewBox="0 0 24 24">
    <rect x="1" y="1" width="10" height="10" fill="#f25022" />
    <rect x="13" y="1" width="10" height="10" fill="#7fba00" />
    <rect x="1" y="13" width="10" height="10" fill="#00a4ef" />
    <rect x="13" y="13" width="10" height="10" fill="#ffb900" />
  </svg>
);

/* ---------------- Page ---------------- */
export default function Login() {
  const [id, setId] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [remember, setRemember] = useState(true);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [theme, setTheme] = useState('light');
  const [toast, setToast] = useState('');
  const toastTimer = useRef(null);
  const navigate = useNavigate();
  const { login } = useAuth();

  const showToast = (msg) => {
    setToast(msg);
    clearTimeout(toastTimer.current);
    toastTimer.current = setTimeout(() => setToast(''), 3500);
  };

  /* Role auto-detection: manager-scoped URL returns 403 for employees, 404 for managers */
  const detectRole = async () => {
    const res = await axios.get(`${API_BASE}/api/manager/session-probe`, {
      auth: { username: id, password },
      validateStatus: () => true,
    });
    return res.status === 403 ? 'EMPLOYEE' : 'MANAGER';
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (loading) return;
    setLoading(true);
    setError('');
    try {
      // 1. Verify credentials via Basic Auth on a protected endpoint
      await axios.get(`${API_BASE}/api/performance/my-stats`, {
        auth: { username: id, password },
      });
      // 2. Detect role & enter the right dashboard
      const role = await detectRole();
      login({ id, password, role }, remember);
      navigate(role === 'MANAGER' ? '/manager/dashboard' : '/employee/dashboard');
    } catch (err) {
      setError(
        err.response?.status === 401
          ? 'Invalid ID or password.'
          : 'Cannot reach the backend. Is Spring Boot running on :8080?'
      );
      setLoading(false);
    }
  };

  return (
    <div className="login-root" data-theme={theme}>
      {/* Ambient neumorphic blobs */}
      <div className="blob blob-tl" />
      <div className="blob blob-r" />
      <div className="blob blob-bl" />
      <div className="blob blob-br" />

      {/* Dark mode toggle */}
      <button className="theme-toggle" onClick={() => setTheme(theme === 'light' ? 'dark' : 'light')} aria-label="Toggle theme">
        {theme === 'light' ? <MoonIcon /> : <SunIcon />}
      </button>

      <div className="login-wrap">
        <div className="logo-float">
        <div className="logo-tile"><FlameLogo /></div>
      </div>

          <h1 className="welcome">EMPLOYEGER</h1>
          <p className="sub"> Long Time - No see ? </p>

        <form className="neu-form" onSubmit={handleSubmit}>
          <div className="neu-field">
            <span className="field-icon"><UserIcon /></span>
            <input
              type="text"
              placeholder="Employee / Manager ID"
              value={id}
              onChange={(e) => setId(e.target.value)}
              autoComplete="username"
              required
            />
          </div>

          <div className="neu-field">
            <span className="field-icon"><LockIcon /></span>
            <input
              type={showPassword ? 'text' : 'password'}
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              autoComplete="current-password"
              required
            />
            <button type="button" className="eye-btn" onClick={() => setShowPassword(!showPassword)} aria-label="Toggle password visibility">
              <EyeIcon off={!showPassword} />
            </button>
          </div>

          <div className="neu-row">
            <label className="check">
              <input type="checkbox" checked={remember} onChange={(e) => setRemember(e.target.checked)} />
              <span className={`box${remember ? ' on' : ''}`}><CheckIcon /></span>
              Remember Me
            </label>
            <button type="button" className="forgot" onClick={() => showToast('Password resets are handled by your manager.')}>
              Forgot Password?
            </button>
          </div>

          {error && <p className="error-text">{error}</p>}

          <button type="submit" className="neu-login" disabled={loading}>
            {loading ? <span className="spinner" /> : null}
            {loading ? 'Signing in…' : 'Login'}
            <span className="arrow-circle"><ArrowIcon /></span>
          </button>
        </form>

        <div className="divider">OR CONTINUE WITH</div>

        <div className="socials">
          <button className="social-tile" onClick={() => showToast('GitHub SSO unlocks after manager approval.')} aria-label="GitHub">
            <GitHubIcon />
          </button>
          <button className="social-tile" onClick={() => showToast('Microsoft SSO unlocks after manager approval.')} aria-label="Microsoft">
            <MicrosoftIcon />
          </button>
        </div>

        <p className="signup-hint">
          New employee? <Link to="/signup">Request signup access</Link>
        </p>

        <p className="tagline">WORK &bull; COLLABORATE &bull; GROW TOGETHER</p>
      </div>

      <div className="corner-note">
        More<br />Than<br />A<br />Portal
      </div>

      {toast && <div className="toast">{toast}</div>}
    </div>
  );
}