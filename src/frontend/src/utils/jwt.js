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

export function getUsernameFromToken() {
    const token = localStorage.getItem('token');
    if (!token) return null;

    try {
        const decoded = jwtDecode(token);
        return decoded.sub || null; // sub is where the username is stored
    } catch (error) {
        console.error('Failed to decode token:', error);
        return null;
    }
}

export function isAdminFromToken() {
    const token = localStorage.getItem('token');
    if (!token) return false;

    try {
        const decoded = jwtDecode(token);
        return decoded.roles?.includes('ROLE_ADMIN') || false;
    } catch (error) {
        console.error('Failed to decode token for admin check:', error);
        return false;
    }
}
