import React, { useState, useEffect } from 'react';
import { Routes, Route, Link, useLocation, Navigate } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import BrandLockup from '../components/BrandLogo';

const API_BASE = 'http://localhost:8080';

/* ---------- shared bits ---------- */
function PageHeader({ title, sub, onLogout }) {
  return (
    <header style={S.header}>
      <div>
        <h1 style={S.pageTitle}>{title}</h1>
        <p style={S.welcome}>{sub}</p>
      </div>
      <button onClick={onLogout} style={S.logoutBtn}>Logout</button>
    </header>
  );
}

function StatCard({ title, value, prefix = '', suffix = '', color }) {
  return (
    <div style={S.statCard}>
      <h3 style={S.statTitle}>{title}</h3>
      <p style={{ ...S.statValue, color }}>{prefix}{value}{suffix}</p>
    </div>
  );
}

function Chip({ label, on }) {
  return <span style={{ ...S.chip, ...(on ? S.chipOn : {}) }}>{label}</span>;
}

function useAuthHeader() {
  const { user } = useAuth();
  return { auth: { username: user.id, password: user.password } };
}

/* ---------- Dashboard ---------- */
function Overview() {
  const { user, logout } = useAuth();
  const auth = useAuthHeader();
  const [pendingCount, setPendingCount] = useState(0);
  const [teamCount, setTeamCount] = useState(0);

  useEffect(() => {
    axios.get(`${API_BASE}/api/manager/pending`, auth).then(r => setPendingCount(r.data.length)).catch(() => {});
    axios.get(`${API_BASE}/api/manager/team`, auth).then(r => setTeamCount(r.data.length)).catch(() => {});
    // eslint-disable-next-line
  }, []);

  return (
    <>
      <PageHeader title="Dashboard" sub={`Welcome back, Manager ${user.id}`} onLogout={logout} />
      <div style={S.statsGrid}>
        <StatCard title="Pending Requests" value={pendingCount} color="#7b1fa2" />
        <StatCard title="Team Members" value={teamCount} color="#1a237e" />
      </div>
      <div style={S.hintCard}>
        Use <b>Approvals</b> to action signup & leave requests, and <b>My Team</b> to inspect individual performance.
      </div>
    </>
  );
}

/* ---------- Approvals ---------- */
function Approvals() {
  const { user, logout } = useAuth();
  const auth = useAuthHeader();
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [busyId, setBusyId] = useState(null);
  const [toast, setToast] = useState('');

  const load = () => {
    axios.get(`${API_BASE}/api/manager/pending`, auth)
      .then(r => setRequests(r.data))
      .catch(() => setToast('Failed to load pending requests'))
      .finally(() => setLoading(false));
  };
  useEffect(load, []); // eslint-disable-line

  const act = async (action, id) => {
    setBusyId(id);
    try {
      await axios.post(`${API_BASE}/api/manager/${action}/${id}`, null, auth);
      setToast(action === 'approve' ? '✅ Request approved' : 'Request rejected');
      load();
    } catch (e) {
      setToast(e.response?.data?.message || `Failed to ${action} request`);
    } finally {
      setBusyId(null);
      setTimeout(() => setToast(''), 2500);
    }
  };

  return (
    <>
      <PageHeader title="Approvals" sub="Signup & leave requests awaiting your decision" onLogout={logout} />
      {loading && <p style={S.muted}>Loading requests…</p>}
      {!loading && requests.length === 0 && (
        <div style={S.hintCard}>🎉 All caught up — no pending requests.</div>
      )}
      <div style={S.cardGrid}>
        {requests.map(r => {
          const id = r.employee_id;
          return (
            <div key={id} style={S.reqCard}>
              <span style={{ ...S.badge, ...(r.type === 'SIGNUP' ? S.badgeSignup : S.badgeLeave) }}>
                {r.type}
              </span>
              <h3 style={S.reqName}>{r.employeeName}</h3>
              <p style={S.reqMeta}>✉️ {r.employeeEmail}</p>
              <p style={S.reqMeta}>👔 Manager: {r.manager_name || '—'}</p>
              {r.leaveReason && <p style={S.reqReason}>“{r.leaveReason}”</p>}
              <p style={S.reqMeta}>🕒 {r.createdAt ? new Date(r.createdAt).toLocaleString() : ''}</p>
              <div style={S.reqActions}>
                <button style={S.approveBtn} disabled={busyId === id} onClick={() => act('approve', id)}>
                  Approve
                </button>
                <button style={S.rejectBtn} disabled={busyId === id} onClick={() => act('reject', id)}>
                  Reject
                </button>
              </div>
            </div>
          );
        })}
      </div>
      {toast && <div style={S.toast}>{toast}</div>}
    </>
  );
}

/* ---------- My Team : ---------- */
function MyTeam() {
  const { user, logout } = useAuth();
  const auth = useAuthHeader();
  const [team, setTeam] = useState([]);
  const [selectedId, setSelectedId] = useState(null);
  const [stats, setStats] = useState(null);
  const [loadingStats, setLoadingStats] = useState(false);

  useEffect(() => {
    axios.get(`${API_BASE}/api/manager/team`, auth).then(r => setTeam(r.data)).catch(() => {});
    // eslint-disable-next-line
  }, []);

  const openMember = (emp) => {
    setSelectedId(emp.id);
    setStats(null);
    setLoadingStats(true);
    axios.get(`${API_BASE}/api/manager/team/${emp.id}/stats`, auth)
      .then(r => setStats(r.data))
      .catch(() => setStats(null))
      .finally(() => setLoadingStats(false));
  };

  return (
    <>
      <PageHeader title="My Team :" sub="Click a team member to open their full performance profile" onLogout={logout} />
      <div style={S.teamWrap}>
        <div style={S.teamList}>
          {team.map(emp => (
            <button
              key={emp.id}
              style={{ ...S.memberBox, ...(selectedId === emp.id ? S.memberBoxActive : {}) }}
              onClick={() => openMember(emp)}
            >
              {emp.name}
            </button>
          ))}
          {team.length === 0 && <p style={S.muted}>No employees yet — approve signups first.</p>}
        </div>

        <div style={S.teamDetail}>
          {!stats && !loadingStats && (
            <p style={S.muted}>Select a team member to view the same stats they see on their own dashboard.</p>
          )}
          {loadingStats && <p style={S.muted}>Loading stats…</p>}
          {stats && (
            <>
              <h3 style={S.panelTitle}>{stats.name}</h3>
              <div style={S.statsGrid}>
                <StatCard title="Performance Score" value={stats.performanceScore} suffix="/100" color="#7b1fa2" />
                <StatCard title="Bonus Earned" value={stats.bonusAmount} prefix="$" color="#1a237e" />
                <StatCard title="Commits" value={stats.totalCommits} color="#1a237e" />
                <StatCard title="PRs Merged" value={stats.totalPrMerged} color="#7b1fa2" />
                <StatCard title="PR Conflicts" value={stats.totalPrConflicts} color="#7b1fa2" />
                <StatCard title="Teams Responsiveness" value={stats.teamsResponsivenessScore} suffix="/100" color="#1a237e" />
              </div>
              <div style={S.profileRow}>
                <Chip label={`✉️ ${stats.email}`} />
                <Chip label={`🌴 Casual leaves: ${stats.casualLeaves}`} />
                <Chip label={`💰 Paid leaves: ${stats.paidLeaves}`} />
                <Chip on={stats.githubConnected} label={stats.githubConnected ? `🐙 ${stats.githubUsername || 'GitHub linked'}` : '🐙 GitHub not linked'} />
                <Chip on={stats.teamsConnected} label={stats.teamsConnected ? '💬 Teams linked' : '💬 Teams not linked'} />
              </div>
              {stats.lastUpdated && (
                <p style={S.muted}>Last updated: {new Date(stats.lastUpdated).toLocaleString()}</p>
              )}
            </>
          )}
        </div>
      </div>
    </>
  );
}

/* ---------- Shell ---------- */
function Sidebar() {
  const location = useLocation();
  const tabs = [
    { path: '/manager/dashboard', label: '📊 Dashboard' },
    { path: '/manager/approvals', label: '✅ Approvals' },
    { path: '/manager/team', label: '👥 My Team :' },
  ];
  return (
    <aside style={S.sidebar}>
      <BrandLockup subtitle="Manager Workspace" />
      <nav style={S.nav}>
        {tabs.map(t => (
          <Link key={t.path} to={t.path}
            style={{ ...S.navLink, ...(location.pathname === t.path ? S.navLinkActive : {}) }}>
            {t.label}
          </Link>
        ))}
      </nav>
    </aside>
  );
}

export default function ManagerDashboard() {
  return (
    <div style={S.layout}>
      <Sidebar />
      <main style={S.main}>
        <Routes>
          <Route path="dashboard" element={<Overview />} />
          <Route path="approvals" element={<Approvals />} />
          <Route path="team" element={<MyTeam />} />
          <Route path="*" element={<Navigate to="dashboard" replace />} />
        </Routes>
      </main>
    </div>
  );
}

/* ---------- styles ---------- */
const S = {
  layout: { display: 'flex', minHeight: '100vh', background: '#faf8f5' },
  sidebar: { width: 260, background: '#1a237e', color: '#fff', padding: 24, position: 'fixed', height: '100vh', overflowY: 'auto' },
  nav: { display: 'flex', flexDirection: 'column', gap: 8 },
  navLink: { padding: '12px 16px', borderRadius: 8, color: 'rgba(255,255,255,0.8)', textDecoration: 'none', fontSize: 15, fontWeight: 500 },
  navLinkActive: { background: 'rgba(255,255,255,0.15)', color: '#fff' },
  main: { flex: 1, marginLeft: 260, padding: 32 },
  header: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 32 },
  pageTitle: { fontSize: 32, fontWeight: 700, color: '#1a237e', marginBottom: 4 },
  welcome: { color: '#7b1fa2', fontSize: 16 },
  logoutBtn: { padding: '10px 24px', background: 'transparent', border: '2px solid #1a237e', color: '#1a237e', borderRadius: 8, fontWeight: 600, cursor: 'pointer' },
  statsGrid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: 24, marginBottom: 32 },
  statCard: { background: '#fff', padding: 24, borderRadius: 12, boxShadow: '0 2px 8px rgba(26,35,126,0.08)' },
  statTitle: { fontSize: 14, color: '#666', marginBottom: 8, fontWeight: 500 },
  statValue: { fontSize: 34, fontWeight: 700 },
  hintCard: { background: '#fff', padding: 20, borderRadius: 12, color: '#444', boxShadow: '0 2px 8px rgba(26,35,126,0.08)' },
  muted: { color: '#888', fontSize: 15 },
  cardGrid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 24 },
  reqCard: { background: '#fff', padding: 24, borderRadius: 12, boxShadow: '0 2px 8px rgba(26,35,126,0.08)', display: 'flex', flexDirection: 'column', gap: 8 },
  badge: { alignSelf: 'flex-start', padding: '4px 12px', borderRadius: 20, fontSize: 11, fontWeight: 700, letterSpacing: '0.08em', color: '#fff' },
  badgeSignup: { background: '#7b1fa2' },
  badgeLeave: { background: '#1a237e' },
  reqName: { fontSize: 20, fontWeight: 700, color: '#1a237e' },
  reqMeta: { fontSize: 13.5, color: '#666' },
  reqReason: { fontSize: 14, color: '#444', fontStyle: 'italic', background: '#f4f1fa', padding: '8px 12px', borderRadius: 8 },
  reqActions: { display: 'flex', gap: 12, marginTop: 12 },
  approveBtn: { flex: 1, padding: '10px 0', background: '#1a237e', color: '#fff', border: 'none', borderRadius: 8, fontWeight: 600, cursor: 'pointer' },
  rejectBtn: { flex: 1, padding: '10px 0', background: '#fff', color: '#d32f2f', border: '2px solid #d32f2f', borderRadius: 8, fontWeight: 600, cursor: 'pointer' },
  teamWrap: { display: 'flex', gap: 24, alignItems: 'flex-start' },
  teamList: { width: 260, flexShrink: 0, display: 'flex', flexDirection: 'column', gap: 10 },
  memberBox: { padding: '14px 18px', background: '#fff', border: '2px solid transparent', borderRadius: 10, textAlign: 'left', fontSize: 15, fontWeight: 600, color: '#1a237e', boxShadow: '0 2px 8px rgba(26,35,126,0.08)', cursor: 'pointer', transition: 'all .2s' },
  memberBoxActive: { borderColor: '#7b1fa2', background: '#f6f1fb' },
  teamDetail: { flex: 1, background: '#fff', borderRadius: 12, padding: 28, boxShadow: '0 2px 8px rgba(26,35,126,0.08)', minHeight: 300 },
  panelTitle: { fontSize: 24, fontWeight: 700, color: '#1a237e', marginBottom: 20 },
  profileRow: { display: 'flex', flexWrap: 'wrap', gap: 10, marginTop: 20 },
  chip: { padding: '8px 14px', borderRadius: 20, background: '#eef0f8', color: '#555', fontSize: 13, fontWeight: 600 },
  chipOn: { background: '#e8f5e9', color: '#2e7d32' },
  toast: { position: 'fixed', bottom: 30, left: '50%', transform: 'translateX(-50%)', background: '#1a237e', color: '#fff', padding: '12px 24px', borderRadius: 8, fontSize: 14, zIndex: 1000 },
};