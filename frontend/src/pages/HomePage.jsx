import { Link } from 'react-router';

const descriptions = {
  album: 'Browse artists, genres, release years, and available musical albums.',
  book: 'Browse titles, authors, prices, and available copies in the book inventory.',
  discMag: 'Browse current disc magazine issues, included discs, stock, and order quantities.',
  magazine: 'Browse current magazine issues, prices, stock, and order quantities.',
  ticket: 'Browse ticket descriptions and prices in the ticket inventory.'
};

export default function HomePage({ products }) {
  return (
    <div>
     
      <section className="grid gap-5 sm:grid-cols-2" aria-label="Product types">
      {products.map(product => (
        <Link className="group border border-zinc-800 bg-zinc-900 p-7 transition hover:border-indigo-500 hover:bg-zinc-800" key={product.id} to={product.path}>
          <div className="mb-8 flex items-start justify-between gap-4">
            <span className="grid size-11 place-items-center border border-zinc-700 text-sm font-bold text-zinc-300 transition group-hover:border-indigo-500 group-hover:text-indigo-400" aria-hidden="true">{product.id.charAt(0).toUpperCase()}</span>
              {product.id === 'album' && (
                  <span className="mb-5 inline-flex border border-purple-500/50 bg-purple-500/15 px-2.5 py-1 text-xs font-semibold tracking-wide text-purple-300 uppercase">Niche</span>
              )}

          </div>
          <h2 className="text-xl font-semibold text-zinc-100">{product.label}</h2>
          <p className="mt-2 text-sm leading-6 text-zinc-400">{descriptions[product.id]}</p>
        </Link>
      ))}
      </section>
    </div>
  );
}
