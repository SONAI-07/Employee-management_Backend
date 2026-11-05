import React from 'react';

/* Reusable flame mark — same gradients as the login page */
export function FlameLogo({ size = 112 }) {
  return (
    <svg width={size} height={size * (140 / 120)} viewBox="0 0 120 140" fill="none">
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
}

/* Brand lockup for dark sidebars: flame tile + wordmark + role subline */
export default function BrandLockup({ subtitle = 'Workspace' }) {
  return (
    <div style={styles.wrap}>
      <div style={styles.tile}>
        <FlameLogo size={26} />
      </div>
      <div>
        <div style={styles.wordmark}>EMPLOYEGER</div>
        <div style={styles.subline}>{subtitle}</div>
      </div>
    </div>
  );
}

const styles = {
  wrap: {
    display: 'flex',
    alignItems: 'center',
    gap: '12px',
    padding: '4px 4px 20px',
    marginBottom: '28px',
    borderBottom: '1px solid rgba(255,255,255,0.14)',
  },
  tile: {
    width: '46px',
    height: '46px',
    borderRadius: '14px',
    background: 'rgba(255,255,255,0.10)',
    boxShadow: 'inset 1px 1px 0 rgba(255,255,255,0.18), 0 6px 14px rgba(0,0,0,0.28)',
    display: 'grid',
    placeItems: 'center',
    flexShrink: 0,
  },
  wordmark: {
    color: '#f7f4ec',        /* cream — a nod to your original navy/cream/purple palette */
    fontSize: '17px',
    fontWeight: 800,
    letterSpacing: '0.08em',
    lineHeight: 1.1,
  },
  subline: {
    color: '#8fa5ff',
    fontSize: '10px',
    fontWeight: 600,
    letterSpacing: '0.24em',
    textTransform: 'uppercase',
    marginTop: '3px',
  },
};