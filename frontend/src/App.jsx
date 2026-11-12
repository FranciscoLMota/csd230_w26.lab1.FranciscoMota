import { Link, Route, Routes, useLocation } from 'react-router';
import SideMenu from './components/SideMenu.jsx';
import AlbumsPage from './pages/AlbumsPage.jsx';
import BooksPage from './pages/BooksPage.jsx';
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
  '/inventory/ticket': { label: 'Tickets', description: 'Manage your ticket collection.' }
};

export default function App() {
  const { pathname } = useLocation();
  const page = pageDetails[pathname] ?? { label: 'Page Not Found', description: 'The page you requested does not exist.' };

  return (
    <div className="min-h-screen bg-zinc-950 text-zinc-100">
      <header className="grid min-h-[73px] grid-cols-[1fr_auto] items-center border-b border-zinc-800 px-5 py-4 sm:grid-cols-3 sm:px-6 lg:px-16">
        <Link className="justify-self-start font-semibold text-zinc-200 transition hover:text-indigo-400" to="/">The Catalog</Link>
        <p className="hidden justify-self-center text-sm text-zinc-400 sm:block">Product management</p>
        <a className="justify-self-end border border-zinc-700 px-3 py-1.5 text-sm text-zinc-300 transition hover:border-indigo-500 hover:bg-indigo-500/10 hover:text-indigo-400" href="/cart">Cart</a>
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
            <Route path="/inventory/album" element={<AlbumsPage />} />
            <Route path="/inventory/book" element={<BooksPage />} />
            <Route path="/inventory/discmag" element={<DiscMagsPage />} />
            <Route path="/inventory/magazine" element={<MagazinesPage />} />
            <Route path="/inventory/ticket" element={<TicketsPage />} />
            <Route path="*" element={<NotFoundPage />} />
          </Routes>
        </main>
      </div>
    </div>
  );
}
