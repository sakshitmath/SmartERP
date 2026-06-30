import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getCompanies, getStockItems, getPurchaseVouchers, getSalesVouchers } from '../../services/api';
import useKeyboardShortcuts from '../../hooks/useKeyboardShortcuts';

export default function Dashboard() {
  const navigate = useNavigate();
  const [stats, setStats] = useState({
    companies: 0, stockItems: 0, purchases: 0, sales: 0
  });
  const userName = localStorage.getItem('userName');
  const companyId = localStorage.getItem('companyId') || 1;

  useKeyboardShortcuts();

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [companies, stock, purchases, sales] = await Promise.all([
          getCompanies(),
          getStockItems(companyId),
          getPurchaseVouchers(companyId),
          getSalesVouchers(companyId),
        ]);
        setStats({
          companies: companies.data.length,
          stockItems: stock.data.length,
          purchases: purchases.data.length,
          sales: sales.data.length,
        });
      } catch (err) {
        console.error(err);
      }
    };
    fetchStats();
  }, []);

  const handleLogout = () => {
    localStorage.clear();
    navigate('/login');
  };

  const modules = [
    { title: '🏢 Company', desc: 'F1', path: '/masters/company', color: 'indigo' },
    { title: '📒 Ledger', desc: 'Alt+L', path: '/masters/ledger', color: 'blue' },
    { title: '📦 Stock Items', desc: 'Alt+S', path: '/masters/stock', color: 'purple' },
    { title: '🛒 Purchase Voucher', desc: 'F9', path: '/vouchers/purchase', color: 'orange' },
    { title: '🧾 Sales Voucher', desc: 'F8', path: '/vouchers/sales', color: 'green' },
    { title: '📊 Trial Balance', desc: 'Alt+T', path: '/reports/trial-balance', color: 'yellow' },
    { title: '📈 Stock Summary', desc: 'Alt+R', path: '/reports/stock-summary', color: 'pink' },
    { title: '💰 Balance Sheet', desc: 'Alt+B', path: '/reports/balance-sheet', color: 'cyan' },
    { title: '📉 Profit & Loss', desc: 'Alt+P', path: '/reports/profit-loss', color: 'red' },
  ];

  return (
    <div className="min-h-screen bg-gray-900 text-white">
      {/* Header */}
      <div className="bg-gray-800 px-6 py-4 flex justify-between items-center border-b border-gray-700">
        <div>
          <h1 className="text-xl font-bold text-green-400">SmartERP</h1>
          <p className="text-gray-400 text-sm">Gateway of SmartERP</p>
        </div>
        <div className="flex items-center gap-4">
          <span className="text-gray-300">👤 {userName}</span>
          <button
            onClick={handleLogout}
            className="bg-red-600 hover:bg-red-700 px-3 py-1 rounded text-sm"
          >
            Logout (Ctrl+Q)
          </button>
        </div>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-4 gap-4 p-6">
        {[
          { label: 'Companies', value: stats.companies, color: 'green' },
          { label: 'Stock Items', value: stats.stockItems, color: 'blue' },
          { label: 'Purchases', value: stats.purchases, color: 'orange' },
          { label: 'Sales', value: stats.sales, color: 'purple' },
        ].map((stat) => (
          <div key={stat.label} className="bg-gray-800 rounded-lg p-4 border border-gray-700">
            <p className="text-gray-400 text-sm">{stat.label}</p>
            <p className="text-3xl font-bold text-white mt-1">{stat.value}</p>
          </div>
        ))}
      </div>

      {/* Modules */}
      <div className="px-6">
        <h2 className="text-gray-400 text-sm mb-4">MODULES — use keyboard shortcuts or click</h2>
        <div className="grid grid-cols-4 gap-4">
          {modules.map((mod) => (
            <button
              key={mod.path}
              onClick={() => navigate(mod.path)}
              className="bg-gray-800 hover:bg-gray-700 border border-gray-700 rounded-lg p-4 text-left transition"
            >
              <p className="text-white font-semibold">{mod.title}</p>
              <p className="text-green-400 text-xs mt-1">{mod.desc}</p>
            </button>
          ))}
        </div>
      </div>

      {/* Shortcuts reminder */}
      <div className="px-6 mt-6">
        <div className="bg-gray-800 border border-gray-700 rounded-lg p-4">
          <p className="text-gray-400 text-xs font-bold mb-2">⌨️ KEYBOARD SHORTCUTS</p>
          <div className="grid grid-cols-4 gap-2 text-xs text-gray-400">
            <span>F8 = Sales Voucher</span>
            <span>F9 = Purchase Voucher</span>
            <span>Alt+L = Ledger</span>
            <span>Alt+S = Stock Item</span>
            <span>Alt+B = Balance Sheet</span>
            <span>Alt+T = Trial Balance</span>
            <span>Alt+R = Stock Summary</span>
            <span>Ctrl+Q = Logout</span>
          </div>
        </div>
      </div>
    </div>
  );
}