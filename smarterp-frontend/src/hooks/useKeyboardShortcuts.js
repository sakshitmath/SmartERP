import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const useKeyboardShortcuts = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const handleKeyDown = (e) => {
      // Don't fire if user is typing in input
      if (e.target.tagName === 'INPUT' || e.target.tagName === 'TEXTAREA') return;

      // F1 = Company Selection
if (e.key === 'F1') {
  e.preventDefault();
  navigate('/masters/company');
}

      // F8 = Sales Voucher
      if (e.key === 'F8' && !e.altKey) {
        e.preventDefault();
        navigate('/vouchers/sales');
      }

      // F9 = Purchase Voucher
      if (e.key === 'F9' && !e.altKey) {
        e.preventDefault();
        navigate('/vouchers/purchase');
      }

      if (e.key === 'F5') {
  e.preventDefault();
  navigate('/vouchers/payment');
}
if (e.key === 'F6') {
  e.preventDefault();
  navigate('/vouchers/receipt');
}

      // Alt+L = Create Ledger
      if (e.altKey && e.key === 'l') {
        e.preventDefault();
        navigate('/masters/ledger');
      }

      // Alt+S = Create Stock Item
      if (e.altKey && e.key === 's') {
        e.preventDefault();
        navigate('/masters/stock');
      }

      // Alt+B = Balance Sheet
      if (e.altKey && e.key === 'b') {
        e.preventDefault();
        navigate('/reports/balance-sheet');
      }

      // Alt+T = Trial Balance
      if (e.altKey && e.key === 't') {
        e.preventDefault();
        navigate('/reports/trial-balance');
      }

      // Alt+R = Stock Summary
      if (e.altKey && e.key === 'r') {
        e.preventDefault();
        navigate('/reports/stock-summary');
      }

      // Alt+P = P&L
      if (e.altKey && e.key === 'p') {
        e.preventDefault();
        navigate('/reports/profit-loss');
      }

      // Ctrl+Q = Logout
      if (e.ctrlKey && e.key === 'q') {
        e.preventDefault();
        localStorage.clear();
        navigate('/login');
      }

      // Ctrl+H = Dashboard
      if (e.ctrlKey && e.key === 'h') {
        e.preventDefault();
        navigate('/dashboard');
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [navigate]);
};

export default useKeyboardShortcuts;