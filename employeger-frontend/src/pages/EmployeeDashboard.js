import React, { useState, useEffect } from 'react';
import { Routes, Route, Link, useLocation, Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import axios from 'axios';

const API_BASE = 'http://localhost:8080';

function Dashboard() {
  const { user, logout } = useAuth();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      const response = await axios.get(`${API_BASE}/api/performance/my-stats`, {
        auth: { username: user.id, password: user.password },
      });
      setStats(response.data);
    } catch (err) {
      console.error('Failed to load stats');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.layout}>
      <Sidebar role="employee" />
      <main style={styles.main}>
        <header style={styles.header}>
          <div>
            <h1 style={styles.pageTitle}>Dashboard</h1>
            <p style={styles.welcome}>Welcome back, {user.id}</p>
          </div>
          <button onClick={logout} style={styles.logoutBtn}>
            Logout
          </button>
        </header>

        <div style={styles.statsGrid}>
          <StatCard
            title="Performance Score"
            value={stats?.performanceScore || 0}
            suffix="/100"
            color="#7b1fa2"
          />
          <StatCard
            title="Bonus Earned"
            value={stats?.bonusAmount || 0}
            prefix="$"
            color="#1a237e"
          />
          <StatCard
            title="Commits"
            value={stats?.totalCommits || 0}
            color="#1a237e"
          />
          <StatCard
            title="PRs Merged"
            value={stats?.totalPrMerged || 0}
            color="#7b1fa2"
          />
        </div>

        <div style={styles.section}>
          <h2 style={styles.sectionTitle}>Quick Actions</h2>
          <div style={styles.actionGrid}>
            <Link to="/employee/leave" style={styles.actionCard}>
              <div style={styles.actionIcon}>📝</div>
              <h3 style={styles.actionTitle}>Request Leave</h3>
              <p style={styles.actionDesc}>Submit a leave request for manager approval</p>
            </Link>
            <Link to="/employee/integrations" style={styles.actionCard}>
              <div style={styles.actionIcon}>🔗</div>
              <h3 style={styles.actionTitle}>Connect Accounts</h3>
              <p style={styles.actionDesc}>Link GitHub and Microsoft Teams</p>
            </Link>
          </div>
        </div>
      </main>
    </div>
  );
}

function LeaveRequest() {
  const { user } = useAuth();
  const [formData, setFormData] = useState({ leaveReason: '', Manager_name: '' });
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await axios.post(`${API_BASE}/api/employee/leave`, formData, {
        auth: { username: user.id, password: user.password },
      });
      setSuccess(true);
      setFormData({ leaveReason: '', Manager_name: '' });
    } catch (err) {
      setError('Failed to submit leave request');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.container}>
      <Sidebar role="employee" />
      <div style={styles.contentArea}>
        <div style={styles.card}>
          <h2 style={styles.cardTitle}>Request Leave</h2>
          {success && (
            <div style={styles.successBox}>
              ✓ Leave request submitted successfully!
            </div>
          )}
          <form onSubmit={handleSubmit} style={styles.form}>
            <div style={styles.inputGroup}>
              <label style={styles.label}>Reason for Leave</label>
              <textarea
                value={formData.leaveReason}
                onChange={(e) => setFormData({ ...formData, leaveReason: e.target.value })}
                style={{ ...styles.input, minHeight: '120px', resize: 'vertical' }}
                required
              />
            </div>
            <div style={styles.inputGroup}>
              <label style={styles.label}>Manager's Email</label>
              <input
                type="email"
                value={formData.Manager_name}
                onChange={(e) => setFormData({ ...formData, Manager_name: e.target.value })}
                style={styles.input}
                required
              />
            </div>
            {error && <p style={styles.error}>{error}</p>}
            <button type="submit" style={styles.button} disabled={loading}>
              {loading ? 'Submitting...' : 'Submit Request'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}

function Integrations() {
  const { user } = useAuth();
  const [loading, setLoading] = useState({ github: false, microsoft: false });
  const [toast, setToast] = useState('');

  const handleConnect = async (platform) => {
    setLoading({ ...loading, [platform]: true });
    try {
      const endpoint = platform === 'github'
        ? '/api/integration/github/initiate'
        : '/api/integration/microsoft/initiate';

      const response = await axios.get(`${API_BASE}${endpoint}`, {
        auth: { username: user.id, password: user.password },
      });

      window.open(response.data, '_blank', 'width=600,height=700');
    } catch (err) {
      setToast('Failed to initiate connection');
      setTimeout(() => setToast(''), 3000);
    } finally {
      setLoading({ ...loading, [platform]: false });
    }
  };

  return (
    <div style={styles.container}>
      <Sidebar role="employee" />
      <div style={styles.contentArea}>
        <h2 style={styles.cardTitle}>Connected Accounts</h2>
        <div style={styles.integrationGrid}>
          <IntegrationCard
            title="GitHub"
            icon="🐙"
            description="Track commits, PRs, and code reviews"
            onConnect={() => handleConnect('github')}
            loading={loading.github}
          />
          <IntegrationCard
            title="Microsoft Teams"
            icon="💬"
            description="Monitor responsiveness and collaboration"
            onConnect={() => handleConnect('microsoft')}
            loading={loading.microsoft}
          />
        </div>
        {toast && <div style={styles.toast}>{toast}</div>}
      </div>
    </div>
  );
}

function Sidebar({ role }) {
  const location = useLocation();
  const basePath = role === 'employee' ? '/employee' : '/manager';

  return (
    <aside style={styles.sidebar}>
      <div style={styles.logo}>
        <h2 style={styles.logoText}>Employeger</h2>
      </div>
      <nav style={styles.nav}>
        <Link
          to={`${basePath}/dashboard`}
          style={{
            ...styles.navLink,
            ...(location.pathname === `${basePath}/dashboard` && styles.navLinkActive),
          }}
        >
          📊 Dashboard
        </Link>
        {role === 'employee' && (
          <>
            <Link
              to={`${basePath}/leave`}
              style={{
                ...styles.navLink,
                ...(location.pathname === `${basePath}/leave` && styles.navLinkActive),
              }}
            >
              📝 Leave Requests
            </Link>
            <Link
              to={`${basePath}/integrations`}
              style={{
                ...styles.navLink,
                ...(location.pathname === `${basePath}/integrations` && styles.navLinkActive),
              }}
            >
              🔗 Integrations
            </Link>
          </>
        )}
      </nav>
    </aside>
  );
}

function StatCard({ title, value, prefix = '', suffix = '', color }) {
  return (
    <div style={styles.statCard}>
      <h3 style={styles.statTitle}>{title}</h3>
      <p style={{ ...styles.statValue, color }}>
        {prefix}{value}{suffix}
      </p>
    </div>
  );
}

function IntegrationCard({ title, icon, description, onConnect, loading }) {
  return (
    <div style={styles.integrationCard}>
      <div style={styles.integrationIcon}>{icon}</div>
      <h3 style={styles.integrationTitle}>{title}</h3>
      <p style={styles.integrationDesc}>{description}</p>
      <button onClick={onConnect} style={styles.connectBtn} disabled={loading}>
        {loading ? 'Connecting...' : 'Connect'}
      </button>
    </div>
  );
}

export default function EmployeeDashboard() {
  return (
    <Routes>
      <Route path="dashboard" element={<Dashboard />} />
      <Route path="leave" element={<LeaveRequest />} />
      <Route path="integrations" element={<Integrations />} />
      <Route path="*" element={<Navigate to="dashboard" replace />} />
    </Routes>
  );
}

const styles = {
  layout: {
    display: 'flex',
    minHeight: '100vh',
    background: '#faf8f5',
  },
  container: {
    display: 'flex',
    minHeight: '100vh',
    background: '#faf8f5',
  },
  contentArea: {
    flex: 1,
    marginLeft: '260px',
    padding: '32px',
  },
  sidebar: {
    width: '260px',
    background: '#1a237e',
    color: 'white',
    padding: '24px',
    position: 'fixed',
    height: '100vh',
    overflowY: 'auto',
  },
  logo: {
    marginBottom: '48px',
  },
  logoText: {
    fontSize: '24px',
    fontWeight: '700',
  },
  nav: {
    display: 'flex',
    flexDirection: 'column',
    gap: '8px',
  },
  navLink: {
    padding: '12px 16px',
    borderRadius: '8px',
    color: 'rgba(255,255,255,0.8)',
    textDecoration: 'none',
    fontSize: '15px',
    fontWeight: '500',
    transition: 'all 0.2s',
  },
  navLinkActive: {
    background: 'rgba(255,255,255,0.15)',
    color: 'white',
  },
  main: {
    flex: 1,
    marginLeft: '260px',
    padding: '32px',
  },
  header: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: '32px',
  },
  pageTitle: {
    fontSize: '32px',
    fontWeight: '700',
    color: '#1a237e',
    marginBottom: '4px',
  },
  welcome: {
    color: '#7b1fa2',
    fontSize: '16px',
  },
  logoutBtn: {
    padding: '10px 24px',
    background: 'transparent',
    border: '2px solid #1a237e',
    color: '#1a237e',
    borderRadius: '8px',
    fontWeight: '600',
    transition: 'all 0.2s',
  },
  statsGrid: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))',
    gap: '24px',
    marginBottom: '48px',
  },
  statCard: {
    background: 'white',
    padding: '24px',
    borderRadius: '12px',
    boxShadow: '0 2px 8px rgba(26,35,126,0.08)',
  },
  statTitle: {
    fontSize: '14px',
    color: '#666',
    marginBottom: '8px',
    fontWeight: '500',
  },
  statValue: {
    fontSize: '36px',
    fontWeight: '700',
  },
  section: {
    marginBottom: '48px',
  },
  sectionTitle: {
    fontSize: '24px',
    fontWeight: '700',
    color: '#1a237e',
    marginBottom: '24px',
  },
  actionGrid: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
    gap: '24px',
  },
  actionCard: {
    background: 'white',
    padding: '24px',
    borderRadius: '12px',
    boxShadow: '0 2px 8px rgba(26,35,126,0.08)',
    textDecoration: 'none',
    transition: 'transform 0.2s, box-shadow 0.2s',
    cursor: 'pointer',
  },
  actionIcon: {
    fontSize: '40px',
    marginBottom: '16px',
  },
  actionTitle: {
    fontSize: '20px',
    fontWeight: '600',
    color: '#1a237e',
    marginBottom: '8px',
  },
  actionDesc: {
    color: '#666',
    fontSize: '14px',
    lineHeight: '1.6',
  },
  card: {
    background: 'white',
    padding: '32px',
    borderRadius: '12px',
    boxShadow: '0 2px 8px rgba(26,35,126,0.08)',
    maxWidth: '600px',
  },
  cardTitle: {
    fontSize: '28px',
    fontWeight: '700',
    color: '#1a237e',
    marginBottom: '24px',
  },
  form: {
    display: 'flex',
    flexDirection: 'column',
    gap: '20px',
  },
  inputGroup: {
    display: 'flex',
    flexDirection: 'column',
    gap: '8px',
  },
  label: {
    fontSize: '14px',
    fontWeight: '600',
    color: '#1a237e',
  },
  input: {
    padding: '12px 16px',
    border: '2px solid #e0e0e0',
    borderRadius: '8px',
    fontSize: '15px',
  },
  button: {
    padding: '14px',
    background: '#7b1fa2',
    color: 'white',
    border: 'none',
    borderRadius: '8px',
    fontSize: '16px',
    fontWeight: '600',
  },
  error: {
    color: '#d32f2f',
    fontSize: '14px',
  },
  successBox: {
    background: '#e8f5e9',
    color: '#2e7d32',
    padding: '16px',
    borderRadius: '8px',
    marginBottom: '24px',
    fontWeight: '600',
  },
  integrationGrid: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
    gap: '24px',
    marginTop: '24px',
  },
  integrationCard: {
    background: 'white',
    padding: '24px',
    borderRadius: '12px',
    boxShadow: '0 2px 8px rgba(26,35,126,0.08)',
    textAlign: 'center',
  },
  integrationIcon: {
    fontSize: '48px',
    marginBottom: '16px',
  },
  integrationTitle: {
    fontSize: '20px',
    fontWeight: '600',
    color: '#1a237e',
    marginBottom: '8px',
  },
  integrationDesc: {
    color: '#666',
    fontSize: '14px',
    marginBottom: '20px',
    lineHeight: '1.6',
  },
  connectBtn: {
    padding: '12px 32px',
    background: '#1a237e',
    color: 'white',
    border: 'none',
    borderRadius: '8px',
    fontWeight: '600',
  },
  toast: {
    position: 'fixed',
    bottom: '30px',
    left: '50%',
    transform: 'translateX(-50%)',
    background: '#1a237e',
    color: 'white',
    padding: '12px 24px',
    borderRadius: '8px',
    fontSize: '14px',
    zIndex: 1000,
  },
};