import { useState } from 'react';

const cellClasses = 'border-b border-zinc-800 px-3.5 py-4 text-zinc-300 sm:px-5';
const inputClasses = 'h-9 w-full min-w-24 border border-zinc-700 bg-zinc-950 px-2.5 text-sm text-zinc-100 outline-none focus:border-indigo-500';
const draftFrom = ticket => ({ description: ticket.description, price: ticket.price });

export default function Ticket({ ticket, onDelete, onUpdate }) {
  const [editing, setEditing] = useState(false); const [draft, setDraft] = useState(() => draftFrom(ticket)); const [saving, setSaving] = useState(false); const [saveError, setSaveError] = useState('');
  function updateField(event) { const { name, value } = event.target; setDraft(current => ({ ...current, [name]: value })); }
  function cancelEditing() { setDraft(draftFrom(ticket)); setSaveError(''); setEditing(false); }
  async function saveTicket() { setSaveError(''); setSaving(true); try { const saved = await onUpdate(ticket.id, { ...ticket, ...draft, price: Number(draft.price) }); setDraft(draftFrom(saved)); setEditing(false); } catch (err) { setSaveError(err.message); } finally { setSaving(false); } }
  return <tr className="transition hover:bg-zinc-800/75 [&:last-child>td]:border-b-0"><td className={`${cellClasses} font-semibold text-zinc-100`}>{editing ? <input aria-label="Ticket description" className={inputClasses} name="description" value={draft.description} onChange={updateField} /> : ticket.description}</td><td className={cellClasses}>{editing ? <input aria-label="Ticket price" className={inputClasses} min="0" name="price" step="0.01" type="number" value={draft.price} onChange={updateField} /> : `$${Number(ticket.price).toFixed(2)}`}</td><td className={`${cellClasses} text-right`}><div className="flex justify-end gap-2">{editing ? <><button className="cursor-pointer border border-zinc-700 px-3 py-1.5 text-xs" type="button" onClick={cancelEditing} disabled={saving}>Cancel</button><button className="cursor-pointer border border-indigo-600 bg-indigo-600 px-3 py-1.5 text-xs" type="button" onClick={saveTicket} disabled={saving}>{saving ? 'Saving...' : 'Save'}</button></> : <><button className="cursor-pointer border border-indigo-500/50 px-3 py-1.5 text-xs text-indigo-400" type="button" onClick={() => setEditing(true)}>Edit</button><button className="cursor-pointer border border-red-500/40 px-3 py-1.5 text-xs text-red-400" type="button" onClick={() => onDelete(ticket)}>Delete</button></>}</div>{saveError && <p className="mt-2 text-xs text-red-400" role="alert">{saveError}</p>}</td></tr>;
}
