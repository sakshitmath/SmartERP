import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { createCompany, getCompanies } from '../../services/api';
import useKeyboardShortcuts from '../../hooks/useKeyboardShortcuts';

export default function CompanyPage() {
  const navigate = useNavigate();
  const [companies, setCompanies] = useState([]);
  const [form, setForm] = useState({
    name: '', gstNumber: '', address: '', state: '', pinCode: '',
    contactNumber: '', email: '', financialYearStart: '2025-04-01', financialYearEnd: '2026-03-31'
  });
  const [message, setMessage] = useState('');

  useKeyboardShortcuts();

  const loadCompanies = () => {
    getCompanies().then(r => setCompanies(r.data)).catch(console.error);
  };

  useEffect(() => { loadCompanies(); }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await createCompany(form);
      setMessage('✅ Company created! Selecting it now...');
      localStorage.setItem('companyId', res.data.id.toString());
      loadCompanies();
      setForm({ name: '', gstNumber: '', address: '', state: '', pinCode: '', contactNumber: '', email: '', financialYearStart: '2025-04-01', financialYearEnd: '2026-03-31' });
    } catch (err) {
      setMessage('❌ ' + (err.response?.data?.message || 'Failed'));
    }
  };

  const selectCompany = (id) => {
    localStorage.setItem('companyId', id.toString());
    setMessage('✅ Company selected!');
  };

  const currentCompanyId = localStorage.getItem('companyId');

  return (
    <div className="min-h-screen bg-gray-900 text-white p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-blue-400">🏢 Company Selection (F1)</h1>
        <button onClick={() => navigate('/dashboard')} className="text-gray-400 hover:text-white">← Dashboard</button>
      </div>

      <div className="grid grid-cols-2 gap-6">
        <div className="bg-gray-800 rounded-lg p-6 border border-gray-700">
          <h2 className="text-lg font-semibold mb-4">Create Company</h2>
          <form onSubmit={handleSubmit} className="space-y-3">
            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="Company Name *"
              value={form.name} onChange={e => setForm({...form, name: e.target.value})} required />
            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="GST Number"
              value={form.gstNumber} onChange={e => setForm({...form, gstNumber: e.target.value})} />
            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="Address *"
              value={form.address} onChange={e => setForm({...form, address: e.target.value})} required />
            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="State *"
              value={form.state} onChange={e => setForm({...form, state: e.target.value})} required />
            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="Pin Code"
              value={form.pinCode} onChange={e => setForm({...form, pinCode: e.target.value})} />
            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="Contact Number"
              value={form.contactNumber} onChange={e => setForm({...form, contactNumber: e.target.value})} />
            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="Email"
              value={form.email} onChange={e => setForm({...form, email: e.target.value})} />
            <div className="grid grid-cols-2 gap-2">
              <div>
                <label className="text-xs text-gray-400">FY Start</label>
                <input type="date" className="w-full bg-gray-700 px-3 py-2 rounded text-white"
                  value={form.financialYearStart} onChange={e => setForm({...form, financialYearStart: e.target.value})} />
              </div>
              <div>
                <label className="text-xs text-gray-400">FY End</label>
                <input type="date" className="w-full bg-gray-700 px-3 py-2 rounded text-white"
                  value={form.financialYearEnd} onChange={e => setForm({...form, financialYearEnd: e.target.value})} />
              </div>
            </div>
            {message && <p className="text-sm">{message}</p>}
            <button type="submit" className="w-full bg-blue-500 hover:bg-blue-600 py-2 rounded font-bold">
              Create Company
            </button>
          </form>
        </div>

        <div className="bg-gray-800 rounded-lg p-6 border border-gray-700">
          <h2 className="text-lg font-semibold mb-4">Your Companies ({companies.length}) — click to select</h2>
          <div className="space-y-2 max-h-96 overflow-y-auto">
            {companies.length === 0 && <p className="text-gray-400">No companies yet — create one!</p>}
            {companies.map(c => (
              <button key={c.id} onClick={() => selectCompany(c.id)}
                className={`w-full text-left bg-gray-700 hover:bg-gray-600 px-4 py-3 rounded ${
                  currentCompanyId === c.id.toString() ? 'border-2 border-green-400' : ''
                }`}>
                <div className="flex justify-between">
                  <p className="font-semibold">{c.name}</p>
                  {currentCompanyId === c.id.toString() && <span className="text-green-400 text-xs">✓ ACTIVE</span>}
                </div>
                <p className="text-xs text-gray-400">{c.state} | GST: {c.gstNumber || 'N/A'}</p>
              </button>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}