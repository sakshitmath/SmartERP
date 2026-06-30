import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getStockSummary } from '../../services/api';
import useKeyboardShortcuts from '../../hooks/useKeyboardShortcuts';

export default function StockSummaryPage() {
  const navigate = useNavigate();
  const companyId = localStorage.getItem('companyId') || 1;
  const [data, setData] = useState(null);

  useKeyboardShortcuts();

  useEffect(() => {
    getStockSummary(companyId).then(r => setData(r.data)).catch(console.error);
  }, []);

  return (
    <div className="min-h-screen bg-gray-900 text-white p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-pink-400">📈 Stock Summary (Alt+R)</h1>
        <button onClick={() => navigate('/dashboard')} className="text-gray-400 hover:text-white">← Dashboard</button>
      </div>

      {data && (
        <>
          <div className="bg-gray-800 rounded-lg border border-gray-700 mb-4">
            <table className="w-full">
              <thead>
                <tr className="border-b border-gray-700">
                  <th className="text-left p-4 text-gray-400">Item</th>
                  <th className="text-left p-4 text-gray-400">SKU</th>
                  <th className="text-right p-4 text-gray-400">Qty</th>
                  <th className="text-left p-4 text-gray-400">Unit</th>
                  <th className="text-right p-4 text-gray-400">Purchase Price</th>
                  <th className="text-right p-4 text-gray-400">Total Value</th>
                  <th className="text-center p-4 text-gray-400">Status</th>
                </tr>
              </thead>
              <tbody>
                {data.items.map((item, i) => (
                  <tr key={i} className="border-b border-gray-700 hover:bg-gray-700">
                    <td className="p-4 font-semibold">{item.itemName}</td>
                    <td className="p-4 text-gray-400">{item.sku}</td>
                    <td className="p-4 text-right text-green-400">{item.quantity}</td>
                    <td className="p-4 text-gray-400">{item.unit}</td>
                    <td className="p-4 text-right">₹{item.purchasePrice}</td>
                    <td className="p-4 text-right font-bold text-green-400">₹{item.totalValue}</td>
                    <td className="p-4 text-center">
                      <span className={item.status === 'LOW STOCK' ? 'text-red-400 font-bold' : 'text-green-400'}>
                        {item.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
              <tfoot>
                <tr className="bg-gray-700">
                  <td colSpan={5} className="p-4 font-bold">Total Stock Value</td>
                  <td className="p-4 text-right font-bold text-green-400">₹{data.totalValue}</td>
                  <td></td>
                </tr>
              </tfoot>
            </table>
          </div>
        </>
      )}
    </div>
  );
}