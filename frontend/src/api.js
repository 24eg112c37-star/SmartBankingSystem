import axios from 'axios';
const api = axios.create({ baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api' });
api.interceptors.request.use(config => { const token = localStorage.getItem('smartbank_token'); if (token) config.headers.Authorization = `Bearer ${token}`; return config; });
api.interceptors.response.use(response => response, error => {
  const status = error.response?.status;
  const url = error.config?.url || '';
  const protectedResource = /^\/(accounts|transactions|notifications|me)(\/|$)/.test(url);
  if (status === 401 || (status === 403 && protectedResource)) {
    localStorage.removeItem('smartbank_token');
    localStorage.removeItem('smartbank_user');
    if (window.location.pathname !== '/login') window.location.assign('/login');
  }
  return Promise.reject(error);
});
export default api;
