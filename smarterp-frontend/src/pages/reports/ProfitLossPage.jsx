import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getProfitLoss } from '../../services/api';
import useKeyboardShortcuts from '../../hooks/useKeyboardShortcuts';

export default function ProfitLossPage() {
  const navigate = useNavigate();
  const companyId = localStorage.getItem('companyId') || 1;
  const [data, setData] = useState(null);

  useKeyboardShortcuts();

  useEffect(() => {
    getProfitLoss(companyId).then(r => setData(r.data)).catch(console.error);
  }, []);

  return (
    <div className="min-h-screen bg-gray-900 text-white p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-red-400">📉 Profit & Loss (Alt+P)</h1>
        <button onClick={() => navigate('/dashboard')} className="text-gray-400 hover:text-white">← Dashboard</button>
      </div>

      {data && (
        <>
          <div className="grid grid-cols-2 gap-6 mb-6">
            <div className="bg-gray-800 rounded-lg p-6 border border-gray-700">
              <h2 className="text-lg font-bold text-green-400 mb-4">INCOME</h2>
              {data.incomes.length === 0 && <p className="text-gray-400">No income entries</p>}
              {data.incomes.map((item, i) => (
                <div key={i} className="flex justify-between py-2 border-b border-gray-700">
                  <p>{item.ledgerName}</p>
                  <p className="text-green-400">₹{item.balance}</p>
                </div>
              ))}
              <div className="flex justify-between pt-3 font-bold">
                <p>Total Income</p>
                <p className="text-green-400">₹{data.totalIncome}</p>
              </div>
            </div>

            <div className="bg-gray-800 rounded-lg p-6 border border-gray-700">
              <h2 className="text-lg font-bold text-red-400 mb-4">EXPENSES</h2>
              {data.expenses.length === 0 && <p className="text-gray-400">No expense entries</p>}
              {data.expenses.map((item, i) => (
                <div key={i} className="flex justify-between py-2 border-b border-gray-700">
                  <p>{item.ledgerName}</p>
                  <p className="text-red-400">₹{item.balance}</p>
                </div>
              ))}
              <div className="flex justify-between pt-3 font-bold">
                <p>Total Expenses</p>
                <p className="text-red-400">₹{data.totalExpense}</p>
              </div>
            </div>
          </div>

          <div className={`p-6 rounded-lg text-center text-2xl font-bold border ${
            data.result === 'NET PROFIT' ? 'bg-green-900 border-green-500 text-green-400' : 'bg-red-900 border-red-500 text-red-400'
          }`}>
            {data.result}: ₹{data.netProfitOrLoss}
          </div>
        </>
      )}
    </div>
  );
}