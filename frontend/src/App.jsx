import { Link, Route, Routes, useLocation, useNavigate } from 'react-router';
import { useCallback, useEffect, useState } from 'react';
import api from './api.js';
import AuthProvider from './AuthProvider.jsx';
import Login from './Login.jsx';
import ProtectedRoute from './ProtectedRoute.jsx';
import useAuth from './useAuth.js';
import SideMenu from './components/SideMenu.jsx';
import AlbumsPage from './pages/AlbumsPage.jsx';
import BooksPage from './pages/BooksPage.jsx';
import CartPage from './pages/CartPage.jsx';
import DiscMagsPage from './pages/DiscMagsPage.jsx';
import HomePage from './pages/HomePage.jsx';
import MagazinesPage from './pages/MagazinesPage.jsx';
import NotFoundPage from './pages/NotFoundPage.jsx';
import TicketsPage from './pages/TicketsPage.jsx';

const inventoryPages = [
  { id: 'album', label: 'Albums', path: '/inventory/album' },
  { id: 'book', label: 'Books', path: '/inventory/book' },
  { id: 'discMag', label: 'Disc Mags', path: '/inventory/discmag' },
  { id: 'magazine', label: 'Magazines', path: '/inventory/magazine' },
  { id: 'ticket', label: 'Tickets', path: '/inventory/ticket' }
];

const pageDetails = {
  '/': { label: 'Product Catalog', description: 'Choose a product type to browse and manage.' },
  '/inventory/album': { label: 'Albums', description: 'Manage your musical album collection.' },
  '/inventory/book': { label: 'Books', description: 'Manage your book collection.' },
  '/inventory/discmag': { label: 'Disc Mags', description: 'Manage your disc magazine collection.' },
  '/inventory/magazine': { label: 'Magazines', description: 'Manage your magazine collection.' },
  '/inventory/ticket': { label: 'Tickets', description: 'Manage your ticket collection.' },
  '/cart': { label: 'Shopping Cart', description: 'Review the products in your cart.' }
};

export default function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route element={<ProtectedRoute />}>
          <Route path="*" element={<SignedInCatalogApp />} />
        </Route>
      </Routes>
    </AuthProvider>
  );
}

function SignedInCatalogApp() {
  const { token } = useAuth();

  return <CatalogApp key={token} />;
}

function CatalogApp() {
  const { pathname } = useLocation();
  const navigate = useNavigate();
  const { logout, token, isAdmin } = useAuth();
  const isCurrent = useCallback(() => localStorage.getItem('token') === token, [token]);
  const [cart, setCart] = useState(null);
  const [cartLoading, setCartLoading] = useState(true);
  const [cartError, setCartError] = useState('');
  const [cartPending, setCartPending] = useState(false);
  const page = pageDetails[pathname] ?? { label: 'Page Not Found', description: 'The page you requested does not exist.' };

  useEffect(() => {
    let active = true;
    api.get('/carts/mine')
      .then(response => { if (active && isCurrent()) setCart(response.status === 204 ? null : response.data); })
      .catch(err => { if (active && isCurrent()) setCartError(err.message); })
      .finally(() => { if (active && isCurrent()) setCartLoading(false); });
    return () => { active = false; };
  }, [isCurrent]);

  async function changeCart(productId, remove = false) {
    if (cartLoading || cartPending || !isCurrent()) return;
    setCartError('');
    setCartPending(true);
    try {
      const path = `/carts/mine/products/${productId}`;
      const response = remove ? await api.delete(path) : await api.post(path);
      if (isCurrent()) setCart(response.status === 204 ? null : response.data);
    } catch (err) {
      if (isCurrent()) setCartError(err.message);
    } finally {
      if (isCurrent()) setCartPending(false);
    }
  }

  return (
    <div className="min-h-screen bg-zinc-950 text-zinc-100">
      <header className="grid min-h-[73px] grid-cols-[1fr_auto] items-center border-b border-zinc-800 px-5 py-4 sm:grid-cols-3 sm:px-6 lg:px-16">
        <Link className="justify-self-start font-semibold text-zinc-200 transition hover:text-indigo-400" to="/">The Catalog</Link>
        <p className="hidden justify-self-center text-sm text-zinc-400 sm:block">Product management</p>
        <div className="flex items-center justify-self-end gap-2">
          {isAdmin && <Link className="border border-zinc-700 px-3 py-1.5 text-sm text-zinc-300 transition hover:border-indigo-500 hover:bg-indigo-500/10 hover:text-indigo-400" to="/add">Add Book</Link>}
          <Link className="border border-zinc-700 px-3 py-1.5 text-sm text-zinc-300 transition hover:border-indigo-500 hover:bg-indigo-500/10 hover:text-indigo-400" to="/cart">Cart ({cart?.products?.length ?? 0})</Link>
          <button className="cursor-pointer border border-zinc-700 px-3 py-1.5 text-sm text-zinc-300 transition hover:border-red-500/60 hover:bg-red-500/10 hover:text-red-400" type="button" onClick={() => { logout(); navigate('/login', { replace: true }); }}>Logout</button>
        </div>
      </header>
      <div className="grid min-h-[calc(100vh-73px)] grid-cols-1 min-[801px]:grid-cols-[220px_minmax(0,1fr)]">
        <SideMenu pages={inventoryPages} />
        <main className="mx-auto w-full max-w-[1180px] px-4 py-9 sm:px-6 sm:py-11" id="main-content">
          <div className="mb-8 flex items-start justify-between gap-6 min-[561px]:items-end">
            <div>
              <p className="mb-1.5 text-[0.72rem] font-bold tracking-[0.14em] text-indigo-400 uppercase">Product catalog</p>
              <h1 className="m-0 text-4xl leading-[1.1] font-bold tracking-[-0.035em] sm:text-[2.5rem]">{page.label}</h1>
              <p className="mt-2 text-sm text-zinc-400">{page.description}</p>
            </div>
          </div>
          <Routes>
            <Route path="/" element={<HomePage products={inventoryPages} />} />
            <Route path="/inventory/album" element={<AlbumsPage onAddToCart={changeCart} cartPending={cartLoading || cartPending} canManage={isAdmin} />} />
            <Route path="/inventory/book" element={<BooksPage onAddToCart={changeCart} cartPending={cartLoading || cartPending} canManage={isAdmin} />} />
            <Route path="/add" element={isAdmin ? <BooksPage directAdd canManage={isAdmin} /> : <p role="alert">Administrator access is required.</p>} />
            <Route path="/inventory/discmag" element={<DiscMagsPage onAddToCart={changeCart} cartPending={cartLoading || cartPending} canManage={isAdmin} />} />
            <Route path="/inventory/magazine" element={<MagazinesPage onAddToCart={changeCart} cartPending={cartLoading || cartPending} canManage={isAdmin} />} />
            <Route path="/inventory/ticket" element={<TicketsPage onAddToCart={changeCart} cartPending={cartLoading || cartPending} canManage={isAdmin} />} />
            <Route path="/cart" element={<CartPage cart={cart} loading={cartLoading} error={cartError} pending={cartPending} onRemove={productId => changeCart(productId, true)} />} />
            <Route path="*" element={<NotFoundPage />} />
          </Routes>
        </main>
      </div>
    </div>
  );
}
