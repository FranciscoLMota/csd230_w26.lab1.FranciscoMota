import { useEffect, useState } from 'react';
import { hasRole, isTokenExpired } from './tokenRoles.js';
import { AuthContext } from './AuthContext.js';


export default function AuthProvider({ children }) {
    const [token, setToken] = useState(() => {
        const stored = localStorage.getItem('token');
        if (isTokenExpired(stored)) {
            localStorage.removeItem('token');
            return null;
        }
        return stored;
    });

    useEffect(() => {
        if (!token) return undefined;
        const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/').padEnd(Math.ceil(token.split('.')[1].length / 4) * 4, '=')));
        const timeout = window.setTimeout(() => {
            localStorage.removeItem('token');
            setToken(null);
            window.dispatchEvent(new Event('auth:invalid'));
            if (window.location.pathname !== '/login') {
                window.location.replace('/login?reauth=true');
            }
        }, Math.max(0, payload.exp * 1000 - Date.now()));
        return () => window.clearTimeout(timeout);
    }, [token]);

    useEffect(() => {
        function invalidSession() { setToken(null); }
        window.addEventListener('auth:invalid', invalidSession);
        return () => window.removeEventListener('auth:invalid', invalidSession);
    }, []);

    function login(nextToken) {
        if (typeof nextToken !== 'string' || !nextToken.trim()) throw new Error('Login did not return a token.');
        localStorage.setItem('token', nextToken);
        setToken(nextToken);
    }
    function logout() {
        localStorage.removeItem('token');
        setToken(null);
    }

    return <AuthContext.Provider value={{ token, login, logout, isAdmin: hasRole(token, 'ROLE_ADMIN') }}>{children}</AuthContext.Provider>;

}
