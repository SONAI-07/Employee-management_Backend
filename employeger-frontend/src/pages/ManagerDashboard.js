import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function ManagerDashboard() {
  const { user, logout } = useAuth();
  return (
    <div style={styles.container}>
      <aside style={styles.sidebar}>
        <div style={styles.logo}>
          <h2 style={styles.logoText}>Employeger</h2>
        </div>
        <nav style={styles.nav}>
          <a href="/manager/dashboard" style={styles.navLinkActive}>
            📊 Dashboard
          </a>
        </nav>
      </aside>
      <main style={styles.main}>
        <header style={styles.header}>
          <div>
            <h1 style={styles.pageTitle}>Manager Dashboard</h1>
            <p style={styles.welcome}>Welcome, Manager {user.id}</p>
          </div>
          <button onClick={logout} style={styles.logoutBtn}>
            Logout
          </button>
        </header>
        <div style={styles.card}>
          <h2 style={styles.cardTitle}>Manager Console</h2>
          <p style={styles.cardText}>
            Manager-specific features will be displayed here.
          </p>
        </div>
      </main>
    </div>
  );
}

export default function ManagerDashboardWrapper() {
  return (
    <Routes>
      <Route path="dashboard" element={<ManagerDashboard />} />
      <Route path="*" element={<Navigate to="dashboard" replace />} />
    </Routes>
  );
}

const styles = {
  container: {
    display: 'flex',
    minHeight: '100vh',
    background: '#faf8f5',
  },
  sidebar: {
    width: '260px',
    background: '#1a237e',
    color: 'white',
    padding: '24px',
    position: 'fixed',
    height: '100vh',
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
  navLinkActive: {
    padding: '12px 16px',
    borderRadius: '8px',
    background: 'rgba(255,255,255,0.15)',
    color: 'white',
    textDecoration: 'none',
    fontSize: '15px',
    fontWeight: '500',
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
  },
  card: {
    background: 'white',
    padding: '32px',
    borderRadius: '12px',
    boxShadow: '0 2px 8px rgba(26,35,126,0.08)',
  },
  cardTitle: {
    fontSize: '24px',
    fontWeight: '700',
    color: '#1a237e',
    marginBottom: '16px',
  },
  cardText: {
    color: '#666',
    lineHeight: '1.6',
  },
};