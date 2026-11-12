import { useState } from 'react';

const initialTicket = { description: '', price: '' };
const inputClasses = 'h-10.5 w-full min-w-0 border border-zinc-700 bg-zinc-950 px-3 py-2 text-zinc-100 outline-none transition focus:border-indigo-500 focus:ring-3 focus:ring-indigo-500/10';

export default function AddTicketForm({ onTicketAdded }) {
  const [ticket, setTicket] = useState(initialTicket);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);
  function updateField(event) { const { name, value } = event.target; setTicket(current => ({ ...current, [name]: value })); }
  async function handleSubmit(event) {
    event.preventDefault(); setError(''); setSaving(true);
    try { const response = await fetch('/api/rest/tickets', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ ...ticket, price: Number(ticket.price) }) }); if (!response.ok) throw new Error(`Save failed (${response.status})`); onTicketAdded(await response.json()); setTicket(initialTicket); } catch (err) { setError(err.message); } finally { setSaving(false); }
  }
  return <section className="border border-zinc-800 bg-zinc-900" aria-labelledby="add-ticket-title"><div className="border-b border-zinc-800 px-5.5 py-5"><h2 className="text-[1.05rem] font-semibold" id="add-ticket-title">Add New Ticket</h2><p className="mt-2 text-sm text-zinc-400">Enter the information for the new ticket.</p></div><form className="grid gap-4.5 p-5.5" onSubmit={handleSubmit}><label className="grid gap-2 text-sm font-medium text-zinc-300"><span>Description</span><input className={inputClasses} name="description" value={ticket.description} onChange={updateField} required /></label><label className="grid gap-2 text-sm font-medium text-zinc-300"><span>Price</span><input className={inputClasses} name="price" type="number" min="0" step="0.01" value={ticket.price} onChange={updateField} required /></label>{error && <p className="text-sm text-red-400" role="alert">{error}</p>}<button className="w-fit cursor-pointer border border-indigo-600 bg-indigo-600 px-4 py-2 text-sm font-semibold disabled:opacity-65" disabled={saving}>{saving ? 'Adding Ticket...' : 'Add Ticket'}</button></form></section>;
}
