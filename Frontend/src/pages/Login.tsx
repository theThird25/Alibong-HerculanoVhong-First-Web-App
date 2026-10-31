import type { ChangeEvent, FormEvent } from "react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/client";
import { saveSession } from "../auth/session";
import { Button, Card, ErrorText, MutedText, PageLink, TextInput } from "../components/ui";
import type { LoginForm, LoginResponse } from "../types";

export default function Login() {
    const [form, setForm] = useState<LoginForm>({ email: "", password: "" });
    const [error, setError] = useState<string>("");
    const navigate = useNavigate();

    const onChange = (e: ChangeEvent<HTMLInputElement>) =>
        setForm({ ...form, [e.target.name]: e.target.value });

    const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError("");
        try {
        const { data } = await api.post<LoginResponse>("/auth/login", form);
        saveSession(data.token, data.user);
        navigate("/dashboard");
        } catch {
        setError("Invalid email or password");
        }
    };

    return (
        <Card>
        <form onSubmit={onSubmit} className="flex flex-col gap-3.5">
            <h2 className="text-xl font-bold">Log in</h2>
            <TextInput
            name="email"
            type="email"
            placeholder="Email"
            value={form.email}
            onChange={onChange}
            required
            />
            <TextInput
            name="password"
            type="password"
            placeholder="Password"
            value={form.password}
            onChange={onChange}
            required
            />
            {error && <ErrorText>{error}</ErrorText>}
            <Button type="submit">Log in</Button>
            <MutedText>
            No account yet? <PageLink to="/register">Register</PageLink>
            </MutedText>
        </form>
        </Card>
    );
}