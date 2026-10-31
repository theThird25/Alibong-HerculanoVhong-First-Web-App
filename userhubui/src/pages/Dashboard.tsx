import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/client.ts";
import { clearSession } from "../auth/session";
import { Button, Card, LinkButton, MutedText } from "../components/ui";
import type { DashboardInfo } from "../types";

export default function Dashboard() {
  const [info, setInfo] = useState<DashboardInfo | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    api
      .get<DashboardInfo>("/dashboard")
      .then((res) => setInfo(res.data))
      .catch(() => {
        clearSession();
        navigate("/login");
      });
  }, [navigate]);

  const logout = async (): Promise<void> => {
    try {
      await api.post("/auth/logout");
    } catch {
    }
    clearSession();
    navigate("/login");
  };

  if (!info) return <p className="text-slate-500">Loading…</p>;

  const isAdmin = info.role === "ADMIN";

  return (
    <Card wide>
      <h1 className="text-2xl font-bold">Welcome, {info.fullName}!</h1>
      <span
        className={`self-start rounded-full px-3.5 py-1 text-sm font-semibold text-white ${
          isAdmin ? "bg-amber-600" : "bg-teal-600"
        }`}
      >
        {info.role}
      </span>

      <div className="grid grid-cols-2 gap-3">
        <div className="flex flex-col gap-1 rounded-lg border border-slate-200 bg-slate-50 px-3.5 py-3">
          <span className="text-sm text-slate-500">Member since</span>
          <strong className="text-xl">{info.memberSince}</strong>
        </div>
        <div className="flex flex-col gap-1 rounded-lg border border-slate-200 bg-slate-50 px-3.5 py-3">
          <span className="text-sm text-slate-500">Total users</span>
          <strong className="text-xl">{info.totalUsers}</strong>
        </div>
      </div>

      <MutedText>{info.email}</MutedText>

      <nav className="mt-1.5 flex flex-wrap gap-2.5">
        <LinkButton to="/profile">Edit profile</LinkButton>
        {isAdmin && <LinkButton to="/admin/users">Manage users</LinkButton>}
        <Button variant="danger" onClick={logout}>
          Logout
        </Button>
      </nav>
    </Card>
  );
}