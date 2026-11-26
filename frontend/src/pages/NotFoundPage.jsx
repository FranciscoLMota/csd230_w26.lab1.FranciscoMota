import { Link } from 'react-router';

export default function NotFoundPage() {
  return (
    <section className="grid min-h-72 place-items-center border border-zinc-800 bg-zinc-900 px-6 py-12 text-center" aria-labelledby="not-found-title">
      <div>
        <p className="text-sm font-bold tracking-[0.14em] text-indigo-400 uppercase">404 Error</p>
        <h2 className="mt-3 text-2xl font-semibold text-zinc-100" id="not-found-title">Nothing to catalog here</h2>
        <p className="mx-auto mt-3 max-w-md text-sm leading-6 text-zinc-400">The address may be incorrect, or the page may have been moved.</p>
        <Link className="mt-7 inline-flex border border-indigo-600 bg-indigo-600 px-4 py-2 text-sm font-semibold text-white transition hover:border-indigo-500 hover:bg-indigo-500" to="/">Return to Catalog</Link>
      </div>
    </section>
  );
}
