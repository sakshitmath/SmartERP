import { Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/auth/Login';
import Dashboard from './pages/dashboard/Dashboard';
import LedgerPage from './pages/masters/LedgerPage';
import StockPage from './pages/masters/StockPage';
import PurchaseVoucherPage from './pages/vouchers/PurchaseVoucherPage';
import SalesVoucherPage from './pages/vouchers/SalesVoucherPage';
import TrialBalancePage from './pages/reports/TrialBalancePage';
import StockSummaryPage from './pages/reports/StockSummaryPage';
import BalanceSheetPage from './pages/reports/BalanceSheetPage';
import ProfitLossPage from './pages/reports/ProfitLossPage';
import Register from './pages/auth/Register';
import CompanyPage from './pages/masters/CompanyPage';

const PrivateRoute = ({ children }) => {
  const token = localStorage.getItem('token');
  if (!token || token === 'undefined' || token === 'null') {
    return <Navigate to="/login" replace />;
  }
  return children;
};

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/dashboard" element={<PrivateRoute><Dashboard /></PrivateRoute>} />
      <Route path="/masters/company" element={<PrivateRoute><CompanyPage /></PrivateRoute>} />
      <Route path="/masters/ledger" element={<PrivateRoute><LedgerPage /></PrivateRoute>} />
      <Route path="/masters/stock" element={<PrivateRoute><StockPage /></PrivateRoute>} />
      <Route path="/vouchers/purchase" element={<PrivateRoute><PurchaseVoucherPage /></PrivateRoute>} />
      <Route path="/vouchers/sales" element={<PrivateRoute><SalesVoucherPage /></PrivateRoute>} />
      <Route path="/reports/trial-balance" element={<PrivateRoute><TrialBalancePage /></PrivateRoute>} />
      <Route path="/reports/stock-summary" element={<PrivateRoute><StockSummaryPage /></PrivateRoute>} />
      <Route path="/reports/balance-sheet" element={<PrivateRoute><BalanceSheetPage /></PrivateRoute>} />
      <Route path="/reports/profit-loss" element={<PrivateRoute><ProfitLossPage /></PrivateRoute>} />
      <Route path="/" element={<Navigate to="/login" />} />
    </Routes>
  );
}

export default App;