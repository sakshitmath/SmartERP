import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getBalanceSheet } from '../../services/api';
import useKeyboardShortcuts from '../../hooks/useKeyboardShortcuts';

export default function BalanceSheetPage() {
  const navigate = useNavigate();
  const companyId = localStorage.getItem('companyId') || 1;
  const [data, setData] = useState(null);

  useKeyboardShortcuts();

  useEffect(() => {
    getBalanceSheet(companyId).then(r => setData(r.data)).catch(console.error);
  }, []);

  return (
    <div className="min-h-screen bg-gray-900 text-white p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-cyan-400">💰 Balance Sheet (Alt+B)</h1>
        <button onClick={() => navigate('/dashboard')} className="text-gray-400 hover:text-white">← Dashboard</button>
      </div>

      {data && (
        <div className="grid grid-cols-2 gap-6">
          <div className="bg-gray-800 rounded-lg p-6 border border-gray-700">
            <h2 className="text-lg font-bold text-green-400 mb-4">ASSETS</h2>
            {data.assets.map((a, i) => (
              <div key={i} className="flex justify-between py-2 border-b border-gray-700">
                <div>
                  <p>{a.ledgerName}</p>
                  <p className="text-xs text-gray-400">{a.groupName}</p>
                </div>
                <p className="text-green-400 font-bold">₹{a.balance}</p>
              </div>
            ))}
            <div className="flex justify-between pt-3 font-bold text-lg">
              <p>Total Assets</p>
              <p className="text-green-400">₹{data.totalAssets}</p>
            </div>
          </div>

          <div className="bg-gray-800 rounded-lg p-6 border border-gray-700">
            <h2 className="text-lg font-bold text-red-400 mb-4">LIABILITIES</h2>
            {data.liabilities.map((l, i) => (
              <div key={i} className="flex justify-between py-2 border-b border-gray-700">
                <div>
                  <p>{l.ledgerName}</p>
                  <p className="text-xs text-gray-400">{l.groupName}</p>
                </div>
                <p className="text-red-400 font-bold">₹{l.balance}</p>
              </div>
            ))}
            <div className="flex justify-between pt-3 font-bold text-lg">
              <p>Total Liabilities</p>
              <p className="text-red-400">₹{data.totalLiabilities}</p>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}