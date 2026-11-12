import { useState } from 'react';

const initialMagazine = { title: '', currentIssue: '', price: '', copies: '10', orderQty: '10' };
const inputClasses = 'h-10.5 w-full min-w-0 border border-zinc-700 bg-zinc-950 px-3 py-2 text-zinc-100 outline-none transition focus:border-indigo-500 focus:ring-3 focus:ring-indigo-500/10';
const labelClasses = 'grid min-w-0 gap-2 text-sm font-medium text-zinc-300';

export default function AddMagazineForm({ onMagazineAdded }) {
  const [magazine, setMagazine] = useState(initialMagazine);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  function updateField(event) {
    const { name, value } = event.target;
    setMagazine(current => ({ ...current, [name]: value }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError('');
    setSaving(true);
    try {
      const response = await fetch('/api/rest/magazines', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ ...magazine, price: Number(magazine.price), copies: Number(magazine.copies), orderQty: Number(magazine.orderQty) })
      });
      if (!response.ok) throw new Error(`Save failed (${response.status})`);
      const savedMagazine = await response.json();
      onMagazineAdded(savedMagazine);
      setMagazine(initialMagazine);
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <section className="order-2 border border-zinc-800 bg-zinc-900 min-[801px]:order-none" aria-labelledby="add-magazine-title">
      <div className="border-b border-zinc-800 px-5.5 py-5">
        <h2 className="text-[1.05rem] font-semibold tracking-tight" id="add-magazine-title">Add New Magazine</h2>
        <p className="mt-2 text-sm text-zinc-400">Enter the information for the new magazine.</p>
      </div>
      <form className="grid gap-4.5 p-5.5" onSubmit={handleSubmit}>
        <label className={labelClasses}><span>Title</span><input className={inputClasses} name="title" value={magazine.title} onChange={updateField} required /></label>
        <label className={labelClasses}><span>Current Issue</span><input className={inputClasses} name="currentIssue" type="datetime-local" value={magazine.currentIssue} onChange={updateField} required /></label>
        <div className="grid grid-cols-1 gap-3.5 min-[561px]:grid-cols-2">
          <label className={labelClasses}><span>Price</span><input className={inputClasses} name="price" type="number" min="0" step="0.01" value={magazine.price} onChange={updateField} required /></label>
          <label className={labelClasses}><span>Copies</span><input className={inputClasses} name="copies" type="number" min="0" step="1" value={magazine.copies} onChange={updateField} required /></label>
        </div>
        <label className={labelClasses}><span>Order Quantity</span><input className={inputClasses} name="orderQty" type="number" min="0" step="1" value={magazine.orderQty} onChange={updateField} required /></label>
        {error && <p className="text-sm text-red-400" role="alert">{error}</p>}
        <button className="w-fit cursor-pointer border border-indigo-600 bg-indigo-600 px-4 py-2 text-sm font-semibold text-white transition hover:border-indigo-500 hover:bg-indigo-500 disabled:cursor-wait disabled:opacity-65" disabled={saving}>{saving ? 'Adding Magazine...' : 'Add Magazine'}</button>
      </form>
    </section>
  );
}
