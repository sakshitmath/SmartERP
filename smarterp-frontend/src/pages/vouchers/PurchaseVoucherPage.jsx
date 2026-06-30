import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { createPurchaseVoucher, getLedgers, getStockItems, getPurchaseVouchers } from '../../services/api';
import useKeyboardShortcuts from '../../hooks/useKeyboardShortcuts';

export default function PurchaseVoucherPage() {
  const navigate = useNavigate();
  const companyId = Number(localStorage.getItem('companyId') || 1);
  const [suppliers, setSuppliers] = useState([]);
  const [stockItems, setStockItems] = useState([]);
  const [vouchers, setVouchers] = useState([]);
  const [form, setForm] = useState({
    companyId, supplierLedgerId: '', voucherDate: new Date().toISOString().split('T')[0],
    notes: '', items: [{ stockItemId: '', quantity: '', rate: '' }]
  });
  const [message, setMessage] = useState('');

  useKeyboardShortcuts();

  useEffect(() => {
    getLedgers(companyId).then(r => setSuppliers(r.data.filter(l => l.type === 'SUPPLIER'))).catch(console.error);
    getStockItems(companyId).then(r => setStockItems(r.data)).catch(console.error);
    getPurchaseVouchers(companyId).then(r => setVouchers(r.data)).catch(console.error);
  }, []);

  const addItem = () => setForm({...form, items: [...form.items, { stockItemId: '', quantity: '', rate: '' }]});

  const updateItem = (index, field, value) => {
    const items = [...form.items];
    items[index][field] = value;
    setForm({...form, items});
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await createPurchaseVoucher({
        ...form,
        supplierLedgerId: Number(form.supplierLedgerId),
        items: form.items.map(i => ({ stockItemId: Number(i.stockItemId), quantity: Number(i.quantity), rate: Number(i.rate) }))
      });
      setMessage('✅ Purchase Voucher created!');
      getPurchaseVouchers(companyId).then(r => setVouchers(r.data));
    } catch (err) {
      setMessage('❌ ' + (err.response?.data?.message || 'Failed'));
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-orange-400">🛒 Purchase Voucher (F9)</h1>
        <button onClick={() => navigate('/dashboard')} className="text-gray-400 hover:text-white">← Dashboard</button>
      </div>

      <div className="grid grid-cols-2 gap-6">
        <div className="bg-gray-800 rounded-lg p-6 border border-gray-700">
          <form onSubmit={handleSubmit} className="space-y-3">
            <select className="w-full bg-gray-700 px-3 py-2 rounded text-white"
              value={form.supplierLedgerId} onChange={e => setForm({...form, supplierLedgerId: e.target.value})} required>
              <option value="">Select Supplier *</option>
              {suppliers.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
            </select>

            <input type="date" className="w-full bg-gray-700 px-3 py-2 rounded text-white"
              value={form.voucherDate} onChange={e => setForm({...form, voucherDate: e.target.value})} />

            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="Notes"
              value={form.notes} onChange={e => setForm({...form, notes: e.target.value})} />

            <div>
              <p className="text-sm text-gray-400 mb-2">Items:</p>
              {form.items.map((item, i) => (
                <div key={i} className="grid grid-cols-3 gap-2 mb-2">
                  <select className="bg-gray-700 px-2 py-1 rounded text-white text-sm"
                    value={item.stockItemId} onChange={e => updateItem(i, 'stockItemId', e.target.value)}>
                    <option value="">Item</option>
                    {stockItems.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
                  </select>
                  <input className="bg-gray-700 px-2 py-1 rounded text-white text-sm" placeholder="Qty"
                    type="number" value={item.quantity} onChange={e => updateItem(i, 'quantity', e.target.value)} />
                  <input className="bg-gray-700 px-2 py-1 rounded text-white text-sm" placeholder="Rate"
                    type="number" value={item.rate} onChange={e => updateItem(i, 'rate', e.target.value)} />
                </div>
              ))}
              <button type="button" onClick={addItem} className="text-green-400 text-sm hover:underline">+ Add Item</button>
            </div>

            {message && <p className="text-sm">{message}</p>}
            <button type="submit" className="w-full bg-orange-500 hover:bg-orange-600 py-2 rounded font-bold">
              Create Purchase Voucher
            </button>
          </form>
        </div>

        <div className="bg-gray-800 rounded-lg p-6 border border-gray-700">
          <h2 className="text-lg font-semibold mb-4">Purchase History ({vouchers.length})</h2>
          <div className="space-y-2 max-h-96 overflow-y-auto">
            {vouchers.map(v => (
              <div key={v.id} className="bg-gray-700 px-4 py-3 rounded">
                <div className="flex justify-between">
                  <p className="font-semibold">{v.voucherNumber}</p>
                  <p className="text-orange-400 font-bold">₹{v.grandTotal}</p>
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