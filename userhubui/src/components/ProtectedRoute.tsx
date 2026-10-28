import React from 'react'
import { Navigate } from 'react-router';

interface ProtectedRouteProps {
  children: React.ReactNode;
  role?: string;
}

const ProtectedRoute = ({ children, role }: ProtectedRouteProps) => {
  const token = localStorage.getItem('token');
  const user = JSON.parse(localStorage.getItem('user') || 'null');

  if (!token) return <Navigate to='/login' replace />;
  if (role && user?.role !== role)
    return <Navigate to='/dashboard' replace />;

  return children;
}

export default ProtectedRoute