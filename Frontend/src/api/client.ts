import type { ApiErrorBody } from "@/types";
import axios from "axios";
import { getToken } from "../auth/session";

const api = axios.create({
    baseURL: "http://localhost:8080/api",
});

api.interceptors.request.use((config) => {
    const token = getToken();
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
});

export function getErrorMessage(err: unknown, fallback: string): string {
    if (axios.isAxiosError<ApiErrorBody>(err)) {
        return err.response?.data?.error ?? fallback;
    }
    return fallback;
}

export default api;