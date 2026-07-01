import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import useKeyboardShortcuts from '../../hooks/useKeyboardShortcuts';
import { getLedgers } from '../../services/api';
import api from '../../services/api';

export default function ReceiptVoucherPage() {
  const navigate = useNavigate();
  const companyId = Number(localStorage.getItem('companyId') || 1);
  const [customers, setCustomers] = useState([]);
  const [vouchers, setVouchers] = useState([]);
  const [form, setForm] = useState({
    companyId, ledgerId: '', amount: '', voucherDate: new Date().toISOString().split('T')[0], notes: ''
  });
  const [message, setMessage] = useState('');

  useKeyboardShortcuts();

  const loadVouchers = () => {
    api.get(`/api/vouchers/receipt/company/${companyId}`).then(r => setVouchers(r.data)).catch(console.error);
  };

  useEffect(() => {
    getLedgers(companyId).then(r => setCustomers(r.data.filter(l => l.type === 'CUSTOMER'))).catch(console.error);
    loadVouchers();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/api/vouchers/receipt', { ...form, ledgerId: Number(form.ledgerId), amount: Number(form.amount) });
      setMessage('✅ Receipt Voucher created!');
      loadVouchers();
      setForm({ companyId, ledgerId: '', amount: '', voucherDate: new Date().toISOString().split('T')[0], notes: '' });
    } catch (err) {
      setMessage('❌ ' + (err.response?.data?.message || 'Failed'));
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-cyan-400">🧾 Receipt Voucher (F6)</h1>
        <button onClick={() => navigate('/dashboard')} className="text-gray-400 hover:text-white">← Dashboard</button>
      </div>
      <div className="grid grid-cols-2 gap-6">
        <div className="bg-gray-800 rounded-lg p-6 border border-gray-700">
          <h2 className="text-sm text-gray-400 mb-4">Receive from customer — reduces their outstanding balance</h2>
          <form onSubmit={handleSubmit} className="space-y-3">
            <select className="w-full bg-gray-700 px-3 py-2 rounded text-white"
              value={form.ledgerId} onChange={e => setForm({...form, ledgerId: e.target.value})} required>
              <option value="">Select Customer *</option>
              {customers.map(c => <option key={c.id} value={c.id}>{c.name} (₹{c.currentBalance} outstanding)</option>)}
            </select>
            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="Amount *"
              type="number" value={form.amount} onChange={e => setForm({...form, amount: e.target.value})} required />
            <input type="date" className="w-full bg-gray-700 px-3 py-2 rounded text-white"
              value={form.voucherDate} onChange={e => setForm({...form, voucherDate: e.target.value})} />
            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="Notes"
              value={form.notes} onChange={e => setForm({...form, notes: e.target.value})} />
            {message && <p className="text-sm">{message}</p>}
            <button type="submit" className="w-full bg-cyan-500 hover:bg-cyan-600 py-2 rounded font-bold">
              Create Receipt Voucher
            </button>
          </form>
        </div>
        <div className="bg-gray-800 rounded-lg p-6 border border-gray-700">
          <h2 className="text-lg font-semibold mb-4">Receipt History ({vouchers.length})</h2>
          <div className="space-y-2 max-h-96 overflow-y-auto">
            {vouchers.length === 0 && <p className="text-gray-400">No receipts yet</p>}
            {vouchers.map(v => (
              <div key={v.id} className="bg-gray-700 px-4 py-3 rounded">
                <div className="flex justify-between">
                  <p className="font-semibold">{v.voucherNumber}</p>
                  <p className="text-cyan-400 font-bold">₹{v.grandTotal}</p>
                </div>
                <p className="text-xs text-gray-400">{v.ledgerName} | {v.voucherDate}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}