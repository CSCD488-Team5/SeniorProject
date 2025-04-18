import {jwtDecode} from 'jwt-decode'

class TokenService {
    constructor() {
        this.tokenKey = 'token'; // Key for localStorage
    }

    // Save the JWT token to localStorage
    saveToken(token) {
        localStorage.setItem(this.tokenKey, token);
        console.log('Token saved:', token);
    }

    // Get the JWT token from localStorage
    getToken() {
        const token = localStorage.getItem(this.tokenKey);
        console.log('Retrieved token:', token);
        return token;
    }

    // Check if the token is valid (exists and not expired)
    isTokenValid() {
        const token = this.getToken();
        if (!token) {
            console.log('No token found');
            return false;
        }

        try { //Checks to see if the token is not expired
            const decoded = jwtDecode(token);
            const now = Math.floor(Date.now() / 1000);
            const isValid = decoded.exp > now;
            console.log('Token valid:', isValid, 'Expires:', decoded.exp);
            return isValid;
        } catch (error) {//Clears the token if it is already expired
            console.error('Invalid token:', error);
            this.clearToken()
            return false;
        }
    }

    // Clear the token from localStorage (e.g., logout)
    clearToken() {
        localStorage.removeItem(this.tokenKey);
        console.log('Token cleared');
    }
}

export default new TokenService(); // Export a singleton instance