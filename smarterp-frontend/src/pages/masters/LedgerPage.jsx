import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { createLedger, getLedgers, getLedgerGroups, createLedgerGroup } from '../../services/api';
import useKeyboardShortcuts from '../../hooks/useKeyboardShortcuts';

export default function LedgerPage() {
  const navigate = useNavigate();
  const companyId = localStorage.getItem('companyId') || 1;
  const [ledgers, setLedgers] = useState([]);
  const [groups, setGroups] = useState([]);
  const [form, setForm] = useState({
    name: '', type: 'CUSTOMER', groupId: '', companyId,
    openingBalance: 0, gstNumber: '', contactNumber: '', email: '', address: ''
  });
  const [message, setMessage] = useState('');

  useKeyboardShortcuts();

  useEffect(() => {
    getLedgers(companyId).then(r => setLedgers(r.data)).catch(console.error);
    getLedgerGroups(companyId).then(r => setGroups(r.data)).catch(console.error);
  }, []);

  const [groupName, setGroupName] = useState('');
  const [groupNature, setGroupNature] = useState('ASSETS');

  const handleCreateGroup = async (e) => {
    e.preventDefault();
    try {
      await createLedgerGroup({ companyId: Number(companyId), name: groupName, nature: groupNature });
      setGroupName('');
      getLedgerGroups(companyId).then(r => setGroups(r.data));
    } catch (err) {
      alert('Failed to create group: ' + (err.response?.data?.message || 'Error'));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await createLedger({ ...form, companyId: Number(companyId), groupId: Number(form.groupId) });
      setMessage('✅ Ledger created!');
      getLedgers(companyId).then(r => setLedgers(r.data));
      setForm({ name: '', type: 'CUSTOMER', groupId: '', companyId, openingBalance: 0, gstNumber: '', contactNumber: '', email: '', address: '' });
    } catch (err) {
      setMessage('❌ Error: ' + (err.response?.data?.message || 'Failed'));
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-green-400">📒 Ledger Management</h1>
        <button onClick={() => navigate('/dashboard')} className="text-gray-400 hover:text-white">
          ← Dashboard (ESC)
        </button>
      </div>

      <div className="grid grid-cols-2 gap-6">
        {/* Form */}
        <div className="bg-gray-800 rounded-lg p-6 border border-gray-700">
          <h2 className="text-lg font-semibold mb-4">Create Ledger</h2>
          <form onSubmit={handleSubmit} className="space-y-3">
            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="Ledger Name *"
              value={form.name} onChange={e => setForm({...form, name: e.target.value})} required />

              <div className="bg-gray-900 p-3 rounded border border-gray-600 mb-3">
              <p className="text-xs text-gray-400 mb-2">Quick-create a Group first (if needed):</p>
              <div className="flex gap-2">
                <input className="flex-1 bg-gray-700 px-2 py-1 rounded text-white text-sm" placeholder="Group name e.g. Sundry Debtors"
                  value={groupName} onChange={e => setGroupName(e.target.value)} />
                <select className="bg-gray-700 px-2 py-1 rounded text-white text-sm"
                  value={groupNature} onChange={e => setGroupNature(e.target.value)}>
                  <option value="ASSETS">Assets</option>
                  <option value="LIABILITIES">Liabilities</option>
                  <option value="INCOME">Income</option>
                  <option value="EXPENSES">Expenses</option>
                </select>
                <button type="button" onClick={handleCreateGroup}
                  className="bg-blue-600 hover:bg-blue-700 px-3 py-1 rounded text-sm">+ Add</button>
              </div>
            </div>

            <select className="w-full bg-gray-700 px-3 py-2 rounded text-white"
              value={form.type} onChange={e => setForm({...form, type: e.target.value})}>
              <option value="CUSTOMER">Customer</option>
              <option value="SUPPLIER">Supplier</option>
              <option value="BANK">Bank</option>
              <option value="CASH">Cash</option>
              <option value="INCOME">Income</option>
              <option value="EXPENSE">Expense</option>
            </select>

            <select className="w-full bg-gray-700 px-3 py-2 rounded text-white"
              value={form.groupId} onChange={e => setForm({...form, groupId: e.target.value})} required>
              <option value="">Select Group *</option>
              {groups.map(g => <option key={g.id} value={g.id}>{g.name}</option>)}
            </select>

            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="Opening Balance"
              type="number" value={form.openingBalance} onChange={e => setForm({...form, openingBalance: e.target.value})} />

            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="GST Number"
              value={form.gstNumber} onChange={e => setForm({...form, gstNumber: e.target.value})} />

            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="Contact Number"
              value={form.contactNumber} onChange={e => setForm({...form, contactNumber: e.target.value})} />

            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="Email"
              value={form.email} onChange={e => setForm({...form, email: e.target.value})} />

            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="Address"
              value={form.address} onChange={e => setForm({...form, address: e.target.value})} />

            {message && <p className="text-sm">{message}</p>}

            <button type="submit" className="w-full bg-green-500 hover:bg-green-600 py-2 rounded font-bold">
              Create Ledger
            </button>
          </form>
        </div>

        {/* List */}
        <div className="bg-gray-800 rounded-lg p-6 border border-gray-700">
          <h2 className="text-lg font-semibold mb-4">All Ledgers ({ledgers.length})</h2>
          <div className="space-y-2 max-h-96 overflow-y-auto">
            {ledgers.map(l => (
              <div key={l.id} className="bg-gray-700 px-4 py-3 rounded flex justify-between">
                <div>
                  <p className="font-semibold">{l.name}</p>
                  <p className="text-xs text-gray-400">{l.type} | {l.groupName}</p>
                </div>
                <div className="text-right">
                  <p className="text-green-400 font-semibold">₹{l.currentBalance}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}