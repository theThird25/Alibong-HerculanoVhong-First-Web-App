import React, { useState } from 'react'
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';

const Register = () => {
  const [form, setForm] = useState(
    { fullName: '', email: '', password: '' }
  );

  const [error, setError] = useState('');
  const navigate = useNavigate();
  const onChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const onSubmit = async (e) => {
    e.preventDefault();
    try {
        await api.post('/auth/register', form);
        navigate('/login'); 
    } catch (err) {
        setError(err.response?.data?.error ?? "Check your inputs and try again.");
    }  
  };

    return (
        <form onSubmit={onSubmit} className="card">
            <h2>Create an account</h2>
            <input name="fullName" placeholder="Full Name"
                value={form.fullName} onChange={onChange} required />
            <input name="email" type="email"placeholder="Email"
                value={form.email} onChange={onChange} required />
            <input name="password" placeholder="Password"
                value={form.password} onChange={onChange} required />
            {error && <p className="error">{error}</p>}
            <button type="submit">Register</button>
            <p>Have an account? <Link to="/login">Login</Link></p>
        </form>
    )
}

export default Register