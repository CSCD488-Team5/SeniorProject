import { jwtDecode } from 'jwt-decode';

export function isTokenValid() {
    const token = localStorage.getItem('token');
    if (!token) return false;

    try {
        const decoded = jwtDecode(token);
        const now = Math.floor(Date.now() / 1000);
        return decoded.exp > now;
    } catch (error) {
        console.error('Invalid token:', error);
        return false;
    }
}