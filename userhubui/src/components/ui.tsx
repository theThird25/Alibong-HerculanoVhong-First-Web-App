import type {
    ButtonHTMLAttributes,
    InputHTMLAttributes,
    ReactNode,
} from "react";
import type { LinkProps } from "react-router-dom";
import { Link } from "react-router-dom";
type Variant = "primary" | "secondary" | "danger";

const base =
    "rounded-lg px-4 py-2.5 text-center font-semibold text-white transition " +
    "hover:brightness-95 focus-visible:outline-2 focus-visible:outline-offset-2 " +
    "focus-visible:outline-teal-600 cursor-pointer";

const variants: Record<Variant, string> = {
    primary: "bg-green-600",
    secondary: "bg-teal-600",
    danger: "bg-red-600",
};

interface CardProps {
    children: ReactNode;
    wide?: boolean;
}

export function Card({ children, wide = false }: CardProps) {
    return (
        <div
        className={`flex w-full flex-col gap-3.5 rounded-xl border border-slate-200 bg-white p-7 ${
            wide ? "max-w-2xl" : "max-w-sm"
        }`}
        >
        {children}
        </div>
    );
}

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
    variant?: Variant;
}

export function Button({ variant = "primary", ...props }: ButtonProps) {
    return <button className={`${base} ${variants[variant]}`} {...props} />;
}

interface LinkButtonProps extends LinkProps {
    variant?: Variant;
}

export function LinkButton({ variant = "secondary", ...props }: LinkButtonProps) {
    return <Link className={`${base} ${variants[variant]}`} {...props} />;
}

export function TextInput(props: InputHTMLAttributes<HTMLInputElement>) {
    return (
        <input
        className="rounded-lg border border-slate-200 bg-slate-50 px-3 py-2.5 focus-visible:outline-2 focus-visible:outline-teal-600"
        {...props}
        />
    );
}

export function ErrorText({ children }: { children: ReactNode }) {
    return <p className="text-red-600">{children}</p>;
}

export function SuccessText({ children }: { children: ReactNode }) {
    return <p className="text-green-600">{children}</p>;
}

export function MutedText({ children }: { children: ReactNode }) {
    return <p className="text-center text-slate-500">{children}</p>;
}

export function Label({ children, htmlFor }: { children: ReactNode; htmlFor?: string }) {
    return (
        <label htmlFor={htmlFor} className="text-sm text-slate-500">
        {children}
        </label>
    );
}

export function PageLink({ to, children }: { to: string; children: ReactNode }) {
    return (
        <Link to={to} className="text-teal-600 hover:underline">
        {children}
        </Link>
    );
}