import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { login } from '../../services/api';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
  e.preventDefault();
  setLoading(true);
  setError('');
  try {
    const res = await login({ email, password });
    console.log('LOGIN SUCCESS, response:', res.data);
    localStorage.clear();
    localStorage.setItem('token', res.data.token);
    localStorage.setItem('userName', res.data.name);
    localStorage.setItem('companyId', '1');
    console.log('TOKEN JUST SET:', localStorage.getItem('token'));
    navigate('/dashboard');
  } catch (err) {
    console.log('LOGIN FAILED, error:', err.response?.status, err.response?.data, err.message);
    setError('Invalid email or password');
  } finally {
    setLoading(false);
  }
};

  return (
    <div className="min-h-screen bg-gray-900 flex items-center justify-center">
      <div className="bg-gray-800 p-8 rounded-lg shadow-lg w-full max-w-md">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-green-400">SmartERP</h1>
          <p className="text-gray-400 mt-1">Cloud ERP — Tally for the Web</p>
        </div>

        <form onSubmit={handleLogin}>
          <div className="mb-4">
            <label className="block text-gray-300 mb-2">Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full bg-gray-700 text-white px-4 py-2 rounded border border-gray-600 focus:outline-none focus:border-green-400"
              placeholder="Enter email"
              required
            />
          </div>

          <div className="mb-6">
            <label className="block text-gray-300 mb-2">Password</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full bg-gray-700 text-white px-4 py-2 rounded border border-gray-600 focus:outline-none focus:border-green-400"
              placeholder="Enter password"
              required
            />
          </div>

          {error && <p className="text-red-400 mb-4 text-sm">{error}</p>}

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-green-500 hover:bg-green-600 text-white font-bold py-2 px-4 rounded transition"
          >
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>
        <p className="text-center text-gray-400 text-sm mt-4">
  Don't have an account?{' '}
  <Link to="/register" className="text-green-400 hover:underline">Register</Link>
</p>

        <div className="mt-6 p-3 bg-gray-700 rounded text-xs text-gray-400">
          <p className="font-bold text-gray-300 mb-1">⌨️ Keyboard Shortcuts:</p>
          <p>F8 = Sales Voucher | F9 = Purchase Voucher</p>
          <p>Alt+L = Ledger | Alt+S = Stock | Ctrl+Q = Logout</p>
        </div>
      </div>
    </div>
  );
}