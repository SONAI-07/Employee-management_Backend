import React, { createContext, useState, useContext } from 'react';

const AuthContext = createContext();

const readUser = () => {
  const raw = localStorage.getItem('employeger_user') || sessionStorage.getItem('employeger_user');
  return raw ? JSON.parse(raw) : null;
};

export function AuthProvider({ children }) {
  const [user, setUser] = useState(readUser);

  const login = (userData, remember = true) => {
    setUser(userData);
    (remember ? localStorage : sessionStorage).setItem('employeger_user', JSON.stringify(userData));
    (remember ? sessionStorage : localStorage).removeItem('employeger_user');
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('employeger_user');
    sessionStorage.removeItem('employeger_user');
  };

  return <AuthContext.Provider value={{ user, login, logout }}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}