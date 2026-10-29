import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Dashboard = () => {
  const [info, setInfo] = useState(null);
  const navigate = useNavigate();
  useEffect(() => {
    api.get('/dashboard').then((res) => setInfo(res.data))
       .catch(() => navigate('login'));
  }, [navigate]);
  const logout = () => {
    localStorage.clear();
    navigate('/login');
  }
  if (!info)
    return <p>Loading...</p>
    return (
        <main>
            <h1>Welcome, {info.fullName}!</h1>
            <p>{info.role} member since {info.memberSince}</p>
            <p>Total users: {info.totalUsers}</p>
            <button onClick={logout}>Logout</button>
        </main>
    );
}

export default Dashboard