import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getTrialBalance } from '../../services/api';
import useKeyboardShortcuts from '../../hooks/useKeyboardShortcuts';

export default function TrialBalancePage() {
  const navigate = useNavigate();
  const companyId = localStorage.getItem('companyId') || 1;
  const [data, setData] = useState(null);

  useKeyboardShortcuts();

  useEffect(() => {
    getTrialBalance(companyId).then(r => setData(r.data)).catch(console.error);
  }, []);

  return (
    <div className="min-h-screen bg-gray-900 text-white p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-yellow-400">📊 Trial Balance (Alt+T)</h1>
        <button onClick={() => navigate('/dashboard')} className="text-gray-400 hover:text-white">← Dashboard</button>
      </div>

      {data && (
        <div className="bg-gray-800 rounded-lg border border-gray-700">
          <table className="w-full">
            <thead>
              <tr className="border-b border-gray-700">
                <th className="text-left p-4 text-gray-400">Ledger</th>
                <th className="text-left p-4 text-gray-400">Group</th>
                <th className="text-left p-4 text-gray-400">Type</th>
                <th className="text-right p-4 text-gray-400">Balance</th>
                <th className="text-right p-4 text-gray-400">DR/CR</th>
              </tr>
            </thead>
            <tbody>
              {data.entries.map((entry, i) => (
                <tr key={i} className="border-b border-gray-700 hover:bg-gray-700">
                  <td className="p-4">{entry.ledgerName}</td>
                  <td className="p-4 text-gray-400">{entry.groupName}</td>
                  <td className="p-4 text-gray-400">{entry.nature}</td>
                  <td className="p-4 text-right text-green-400">₹{entry.balance}</td>
                  <td className="p-4 text-right">
                    <span className={entry.balanceType === 'DR' ? 'text-blue-400' : 'text-red-400'}>
                      {entry.balanceType}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
            <tfoot>
              <tr className="bg-gray-700">
                <td colSpan={3} className="p-4 font-bold">Total</td>
                <td className="p-4 text-right font-bold text-green-400">DR: ₹{data.totalDebit}</td>
                <td className="p-4 text-right font-bold text-red-400">CR: ₹{data.totalCredit}</td>
              </tr>
            </tfoot>
          </table>
        </div>
      )}
    </div>
  );
}