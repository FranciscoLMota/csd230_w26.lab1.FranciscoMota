import { useState } from 'react';
import { Navigate, useNavigate } from 'react-router';
import api from './api.js';
import useAuth from './useAuth.js';

export default function Login() {
    const { token, login } = useAuth();
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [pending, setPending] = useState(false);
    const reauthenticated = new URLSearchParams(window.location.search).get('reauth') === 'true';
    async function handleSubmit(event) {
        event.preventDefault(); setError(''); setPending(true);
        try {
            const response = await api.post('/auth/login', { username, password });
            login(response.data.token); // Storage is synchronized before navigating.
            navigate('/inventory/book', { replace: true });
        } catch (err) { setError(err.response?.data?.message || err.message); }
        finally { setPending(false); }
    }
    if (token) return <Navigate to="/inventory/book" replace />;
    return (
        <main className="grid min-h-screen place-items-center bg-zinc-950 px-4 py-10 text-zinc-100">
            <section className="w-full max-w-md border border-zinc-800 bg-zinc-900 shadow-2xl" aria-labelledby="sign-in-title">
                <div className="border-b border-zinc-800 px-6 py-6 sm:px-8">
                    <p className="mb-2 text-[0.72rem] font-bold tracking-[0.14em] text-indigo-400 uppercase">The Catalog</p>
                    <h1 className="text-2xl font-bold tracking-tight text-zinc-100" id="sign-in-title">Sign in</h1>
                    <p className="mt-2 text-sm leading-6 text-zinc-400">Sign in to manage your product collection.</p>
                </div>
                <form className="grid gap-5 px-6 py-6 sm:px-8" onSubmit={handleSubmit}>
                    {reauthenticated && <p className="border border-red-500/30 bg-red-500/10 px-3 py-2 text-sm text-red-300" role="alert">You were logged out because your session expired. Please sign in again.</p>}
                    <label className="grid gap-2 text-sm font-medium text-zinc-300">
                        <span>Username</span>
                        <input className="h-10.5 w-full border border-zinc-700 bg-zinc-950 px-3 py-2 text-zinc-100 outline-none transition focus:border-indigo-500 focus:ring-3 focus:ring-indigo-500/10" autoComplete="username" value={username} onChange={e => setUsername(e.target.value)} required />
                    </label>
                    <label className="grid gap-2 text-sm font-medium text-zinc-300">
                        <span>Password</span>
                        <input className="h-10.5 w-full border border-zinc-700 bg-zinc-950 px-3 py-2 text-zinc-100 outline-none transition focus:border-indigo-500 focus:ring-3 focus:ring-indigo-500/10" type="password" autoComplete="current-password" value={password} onChange={e => setPassword(e.target.value)} required />
                    </label>
                    {error && <p className="border border-red-500/30 bg-red-500/10 px-3 py-2 text-sm text-red-400" role="alert">{error}</p>}
                    <button className="w-full cursor-pointer border border-indigo-600 bg-indigo-600 px-4 py-2.5 text-sm font-semibold text-white transition hover:border-indigo-500 hover:bg-indigo-500 disabled:cursor-wait disabled:opacity-65" disabled={pending}>
                        {pending ? 'Signing in...' : 'Sign in'}
                    </button>
                </form>
            </section>
        </main>
    );
}


