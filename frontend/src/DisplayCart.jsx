export default function DisplayCart({ cart, loading, error, pending, onRemove }) {
  const products = cart?.products ?? [];

  return (
    <section className="border border-zinc-800 bg-zinc-900" aria-labelledby="cart-title">
      <div className="border-b border-zinc-800 px-5.5 py-5">
        <h2 className="text-[1.05rem] font-semibold tracking-tight" id="cart-title">Your Cart</h2>
        <p className="mt-2 text-sm text-zinc-400">
          {loading ? 'Loading cart...' : `${products.length} ${products.length === 1 ? 'item' : 'items'} in your cart`}
        </p>
      </div>
      {error && <p className="px-5.5 py-9 text-center text-sm text-red-400" role="alert">{error}</p>}
      {!loading && !error && products.length === 0 && <p className="px-5.5 py-9 text-center text-zinc-400">Your cart is empty.</p>}
      {!loading && !error && products.length > 0 && (
        <ul className="divide-y divide-zinc-800">
          {products.map(product => (
            <li className="flex items-center justify-between gap-4 px-5.5 py-4" key={product.id}>
              <div>
                <p className="font-semibold text-zinc-100">{product.title || product.description || `${product.productType || 'Product'} #${product.id}`}</p>
                <p className="mt-1 text-sm text-zinc-400">${Number(product.price).toFixed(2)}</p>
              </div>
              <button className="cursor-pointer border border-red-500/40 px-3 py-1.5 text-xs font-medium text-red-400 transition hover:bg-red-500/10 disabled:cursor-wait disabled:opacity-60" type="button" onClick={() => onRemove(product.id)} disabled={pending}>Remove</button>
            </li>
          ))}
        </ul>
      )}
    </section>
  );
}
