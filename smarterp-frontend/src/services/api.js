import axios from 'axios';

const API_BASE_URL = 'http://localhost:8081';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add JWT token to every request automatically
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Auto logout on 401/403
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.clear();
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth
export const login = (data) => api.post('/api/auth/login', data);
export const register = (data) => api.post('/api/auth/register', data);

// Company
export const createCompany = (data) => api.post('/api/companies', data);
export const getCompanies = () => api.get('/api/companies');

// Ledger Groups
export const createLedgerGroup = (params) => api.post(`/api/ledger-groups`, null, { params });
export const getLedgerGroups = (companyId) => api.get(`/api/ledger-groups/company/${companyId}`);

// Ledgers
export const createLedger = (data) => api.post('/api/ledgers', data);
export const getLedgers = (companyId) => api.get(`/api/ledgers/company/${companyId}`);

// Stock Items
export const createStockItem = (data) => api.post('/api/stock-items', data);
export const getStockItems = (companyId) => api.get(`/api/stock-items/company/${companyId}`);

// Vouchers
export const createPurchaseVoucher = (data) => api.post('/api/vouchers/purchase', data);
export const createSalesVoucher = (data) => api.post('/api/vouchers/sales', data);
export const getPurchaseVouchers = (companyId) => api.get(`/api/vouchers/purchase/company/${companyId}`);
export const getSalesVouchers = (companyId) => api.get(`/api/vouchers/sales/company/${companyId}`);
export const downloadInvoicePdf = (voucherId) => api.get(`/api/vouchers/sales/${voucherId}/pdf`, { responseType: 'blob' });

// Reports
export const getTrialBalance = (companyId) => api.get(`/api/reports/trial-balance/${companyId}`);
export const getStockSummary = (companyId) => api.get(`/api/reports/stock-summary/${companyId}`);
export const getBalanceSheet = (companyId) => api.get(`/api/reports/balance-sheet/${companyId}`);
export const getProfitLoss = (companyId) => api.get(`/api/reports/profit-loss/${companyId}`);

export default api;