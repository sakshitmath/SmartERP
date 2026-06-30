import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { createStockItem, getStockItems } from '../../services/api';
import useKeyboardShortcuts from '../../hooks/useKeyboardShortcuts';

export default function StockPage() {
  const navigate = useNavigate();
  const companyId = localStorage.getItem('companyId') || 1;
  const [items, setItems] = useState([]);
  const [form, setForm] = useState({
    name: '', sku: '', hsnCode: '', purchasePrice: '', sellingPrice: '',
    quantity: 0, gstRate: 0, unit: 'PCS', stockGroup: '', companyId
  });
  const [message, setMessage] = useState('');

  useKeyboardShortcuts();

  useEffect(() => {
    getStockItems(companyId).then(r => setItems(r.data)).catch(console.error);
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await createStockItem({ ...form, companyId: Number(companyId) });
      setMessage('✅ Stock item created!');
      getStockItems(companyId).then(r => setItems(r.data));
      setForm({ name: '', sku: '', hsnCode: '', purchasePrice: '', sellingPrice: '', quantity: 0, gstRate: 0, unit: 'PCS', stockGroup: '', companyId });
    } catch (err) {
      setMessage('❌ Error: ' + (err.response?.data?.message || 'Failed'));
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-green-400">📦 Stock Items</h1>
        <button onClick={() => navigate('/dashboard')} className="text-gray-400 hover:text-white">← Dashboard</button>
      </div>

      <div className="grid grid-cols-2 gap-6">
        <div className="bg-gray-800 rounded-lg p-6 border border-gray-700">
          <h2 className="text-lg font-semibold mb-4">Create Stock Item</h2>
          <form onSubmit={handleSubmit} className="space-y-3">
            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="Item Name *"
              value={form.name} onChange={e => setForm({...form, name: e.target.value})} required />
            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="SKU"
              value={form.sku} onChange={e => setForm({...form, sku: e.target.value})} />
            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="HSN Code"
              value={form.hsnCode} onChange={e => setForm({...form, hsnCode: e.target.value})} />
            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="Purchase Price *"
              type="number" value={form.purchasePrice} onChange={e => setForm({...form, purchasePrice: e.target.value})} required />
            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="Selling Price *"
              type="number" value={form.sellingPrice} onChange={e => setForm({...form, sellingPrice: e.target.value})} required />
            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="Opening Quantity"
              type="number" value={form.quantity} onChange={e => setForm({...form, quantity: e.target.value})} />
            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="GST Rate %"
              type="number" value={form.gstRate} onChange={e => setForm({...form, gstRate: e.target.value})} />
            <select className="w-full bg-gray-700 px-3 py-2 rounded text-white"
              value={form.unit} onChange={e => setForm({...form, unit: e.target.value})}>
              <option>PCS</option><option>BOX</option><option>KG</option><option>LTR</option>
            </select>
            <input className="w-full bg-gray-700 px-3 py-2 rounded text-white" placeholder="Stock Group"
              value={form.stockGroup} onChange={e => setForm({...form, stockGroup: e.target.value})} />
            {message && <p className="text-sm">{message}</p>}
            <button type="submit" className="w-full bg-green-500 hover:bg-green-600 py-2 rounded font-bold">
              Create Stock Item
            </button>
          </form>
        </div>

        <div className="bg-gray-800 rounded-lg p-6 border border-gray-700">
          <h2 className="text-lg font-semibold mb-4">All Stock Items ({items.length})</h2>
          <div className="space-y-2 max-h-96 overflow-y-auto">
            {items.map(item => (
              <div key={item.id} className="bg-gray-700 px-4 py-3 rounded">
                <div className="flex justify-between">
                  <p className="font-semibold">{item.name}</p>
                  <p className="text-green-400">Qty: {item.quantity} {item.unit}</p>
                </div>
                <p className="text-xs text-gray-400">SKU: {item.sku} | GST: {item.gstRate}% | ₹{item.sellingPrice}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}