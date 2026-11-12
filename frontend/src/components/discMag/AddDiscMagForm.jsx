import { useState } from 'react';

const initialDiscMag = { title: '', currentIssue: '', price: '', copies: '10', orderQty: '10', hasDisc: false };
const inputClasses = 'h-10.5 w-full min-w-0 border border-zinc-700 bg-zinc-950 px-3 py-2 text-zinc-100 outline-none transition focus:border-indigo-500 focus:ring-3 focus:ring-indigo-500/10';
const labelClasses = 'grid min-w-0 gap-2 text-sm font-medium text-zinc-300';

export default function AddDiscMagForm({ onDiscMagAdded }) {
  const [discMag, setDiscMag] = useState(initialDiscMag);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  function updateField(event) {
    const { checked, name, type, value } = event.target;
    setDiscMag(current => ({ ...current, [name]: type === 'checkbox' ? checked : value }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError(''); setSaving(true);
    try {
      const response = await fetch('/api/rest/discmags', {
        method: 'POST', headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ ...discMag, price: Number(discMag.price), copies: Number(discMag.copies), orderQty: Number(discMag.orderQty) })
      });
      if (!response.ok) throw new Error(`Save failed (${response.status})`);
      onDiscMagAdded(await response.json());
      setDiscMag(initialDiscMag);
    } catch (err) { setError(err.message); } finally { setSaving(false); }
  }

  return <section className="border border-zinc-800 bg-zinc-900" aria-labelledby="add-discmag-title">
    <div className="border-b border-zinc-800 px-5.5 py-5"><h2 className="text-[1.05rem] font-semibold" id="add-discmag-title">Add New Disc Mag</h2><p className="mt-2 text-sm text-zinc-400">Enter the information for the new disc magazine.</p></div>
    <form className="grid gap-4.5 p-5.5" onSubmit={handleSubmit}>
      <label className={labelClasses}><span>Title</span><input className={inputClasses} name="title" value={discMag.title} onChange={updateField} required /></label>
      <label className={labelClasses}><span>Current Issue</span><input className={inputClasses} name="currentIssue" type="datetime-local" value={discMag.currentIssue} onChange={updateField} required /></label>
      <div className="grid grid-cols-1 gap-3.5 min-[561px]:grid-cols-2">
        <label className={labelClasses}><span>Price</span><input className={inputClasses} name="price" type="number" min="0" step="0.01" value={discMag.price} onChange={updateField} required /></label>
        <label className={labelClasses}><span>Copies</span><input className={inputClasses} name="copies" type="number" min="0" step="1" value={discMag.copies} onChange={updateField} required /></label>
      </div>
      <label className={labelClasses}><span>Order Quantity</span><input className={inputClasses} name="orderQty" type="number" min="0" step="1" value={discMag.orderQty} onChange={updateField} required /></label>
      <label className="flex items-center gap-3 text-sm font-medium text-zinc-300"><input className="size-4 accent-indigo-600" name="hasDisc" type="checkbox" checked={discMag.hasDisc} onChange={updateField} /><span>Includes disc</span></label>
      {error && <p className="text-sm text-red-400" role="alert">{error}</p>}
      <button className="w-fit cursor-pointer border border-indigo-600 bg-indigo-600 px-4 py-2 text-sm font-semibold text-white disabled:opacity-65" disabled={saving}>{saving ? 'Adding Disc Mag...' : 'Add Disc Mag'}</button>
    </form>
  </section>;
}
