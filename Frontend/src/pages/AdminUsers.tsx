import { useEffect, useState } from "react";
import api, { getErrorMessage } from "../api/client";
import { Card, ErrorText, MutedText, PageLink } from "../components/ui";
import type { User } from "../types";

export default function AdminUsers() {
  const [users, setUsers] = useState<User[]>([]);
  const [error, setError] = useState<string>("");

  useEffect(() => {
    api
      .get<User[]>("/dashboard/users")
      .then((res) => setUsers(res.data))
      .catch((err) => setError(getErrorMessage(err, "Could not load users")));
  }, []);

  return (
    <Card wide>
      <h1 className="text-2xl font-bold">All users</h1>
      {error && <ErrorText>{error}</ErrorText>}
      <div className="overflow-x-auto">
        <table className="w-full border-collapse text-left">
          <thead>
            <tr className="text-sm text-slate-500">
              <th className="border-b border-slate-200 px-3 py-2.5 font-semibold">ID</th>
              <th className="border-b border-slate-200 px-3 py-2.5 font-semibold">Name</th>
              <th className="border-b border-slate-200 px-3 py-2.5 font-semibold">Email</th>
              <th className="border-b border-slate-200 px-3 py-2.5 font-semibold">Role</th>
            </tr>
          </thead>
          <tbody>
            {users.map((u) => (
              <tr key={u.id}>
                <td className="border-b border-slate-200 px-3 py-2.5">{u.id}</td>
                <td className="border-b border-slate-200 px-3 py-2.5">{u.fullName}</td>
                <td className="border-b border-slate-200 px-3 py-2.5">{u.email}</td>
                <td className="border-b border-slate-200 px-3 py-2.5">{u.role}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <MutedText>
        <PageLink to="/dashboard">Back to dashboard</PageLink>
      </MutedText>
    </Card>
  );
}