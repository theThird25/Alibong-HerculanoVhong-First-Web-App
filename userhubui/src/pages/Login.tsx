import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom';

const Login = () => {
 const [form, setForm] = useState({ email: '', password: ''});
 const [error, setError] = useState('')
 const navigate = useNavigate();
 const onChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });
 const onSubmit = async (e) => {
        e.preventDefault();
        try {
            const { data } = await api.post('/auth/login', form);
            localStorage.setItem('token', data.token);
            localStorage.setItem('user', JSON.stringify(data.user));
            navigate('/dashboard');
        } catch {
            setError('Invalid email or password');
        }
    };

    return (
        <form onSubmit={onSubmit} className="card">
            <h2>Login</h2>
            <input name="email" type="email"placeholder="Email"
                value={form.email} onChange={onChange} required />
            <input name="password" placeholder="Password"
                value={form.password} onChange={onChange} required />
            {error && <p className="error">{error}</p>}
            <button type="submit">Login</button>
            <p>Don't have an account? <Link to="/register">Register</Link></p>
        </form>
    )
}



export default Login