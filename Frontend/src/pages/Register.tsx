import type { ChangeEvent, FormEvent } from "react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api, { getErrorMessage } from "../api/client";
import { Button, Card, ErrorText, MutedText, PageLink, TextInput } from "../components/ui";
import type { RegisterForm } from "../types";

export default function Register() {
    const [form, setForm] = useState<RegisterForm>({
        fullName: "",
        email: "",
        password: "",
    });
    const [error, setError] = useState<string>("");
    const navigate = useNavigate();

    const onChange = (e: ChangeEvent<HTMLInputElement>) =>
        setForm({ ...form, [e.target.name]: e.target.value });

    const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError("");
        try {
        await api.post("/auth/register", form);
        navigate("/login");
        } catch (err) {
        setError(getErrorMessage(err, "Check your inputs"));
        }
    };

    return (
        <Card>
        <form onSubmit={onSubmit} className="flex flex-col gap-3.5">
            <h2 className="text-xl font-bold">Create an account</h2>
            <TextInput
            name="fullName"
            placeholder="Full name"
            value={form.fullName}
            onChange={onChange}
            required
            />
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
            placeholder="Password (8+ characters)"
            minLength={8}
            value={form.password}
            onChange={onChange}
            required
            />
            {error && <ErrorText>{error}</ErrorText>}
            <Button type="submit">Register</Button>
            <MutedText>
            Have an account? <PageLink to="/login">Log in</PageLink>
            </MutedText>
        </form>
        </Card>
    );
}