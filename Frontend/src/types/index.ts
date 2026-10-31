export type Role = "USER" | "ADMIN";

export interface User {
    id: number;
    fullName: string;
    email: string;
    role: Role;
}

export interface RegisterForm {
    fullName: string;
    email: string;
    password: string;
}

export interface LoginForm {
    email: string;
    password: string;
}

export interface LoginResponse {
    token: string;
    user: User;
}

export interface DashboardInfo {
    fullName: string;
    email: string;
    role: Role;
    memberSince: string;
    totalUsers: number;
}

export interface ApiErrorBody {
    error?: string;
}