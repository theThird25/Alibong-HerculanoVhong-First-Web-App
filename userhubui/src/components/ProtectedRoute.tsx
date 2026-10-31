import type { ReactNode } from "react";
import { Navigate } from "react-router-dom";
import { getToken, getUser } from "../auth/session";
import type { Role } from "../types";

interface ProtectedRouteProps {
  children: ReactNode;
  role?: Role;
}

export default function ProtectedRoute({ children, role }: ProtectedRouteProps) {
  const token = getToken();
  const user = getUser();

  if (!token) return <Navigate to="/login" replace />;
  if (role && user?.role !== role) return <Navigate to="/dashboard" replace />;

  return <>{children}</>;
}