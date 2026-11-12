import { useState } from 'react';

const cellClasses = 'border-b border-zinc-800 px-3.5 py-4 text-zinc-300 sm:px-5';
const inputClasses = 'h-9 w-full min-w-24 border border-zinc-700 bg-zinc-950 px-2.5 text-sm text-zinc-100 outline-none focus:border-indigo-500';
const draftFrom = item => ({ title: item.title, currentIssue: item.currentIssue?.slice(0, 16) ?? '', price: item.price, copies: item.copies, orderQty: item.orderQty, hasDisc: item.hasDisc });

export default function DiscMag({ discMag, onDelete, onUpdate }) {
  const [editing, setEditing] = useState(false);
  const [draft, setDraft] = useState(() => draftFrom(discMag));
  const [saving, setSaving] = useState(false);
  const [saveError, setSaveError] = useState('');
  function updateField(event) { const { checked, name, type, value } = event.target; setDraft(current => ({ ...current, [name]: type === 'checkbox' ? checked : value })); }
  function cancelEditing() { setDraft(draftFrom(discMag)); setSaveError(''); setEditing(false); }
  async function saveDiscMag() {
    setSaveError(''); setSaving(true);
    try {
      const saved = await onUpdate(discMag.id, { ...discMag, ...draft, price: Number(draft.price), copies: Number(draft.copies), orderQty: Number(draft.orderQty) });
      setDraft(draftFrom(saved)); setEditing(false);
    } catch (err) { setSaveError(err.message); } finally { setSaving(false); }
  }
  const fields = [['title', 'Disc magazine title', 'text'], ['currentIssue', 'Current issue', 'datetime-local'], ['price', 'Price', 'number'], ['copies', 'Copies', 'number'], ['orderQty', 'Order quantity', 'number']];
  const values = [discMag.title, discMag.currentIssue?.replace('T', ' ').slice(0, 16), `$${Number(discMag.price).toFixed(2)}`, discMag.copies, discMag.orderQty];
  return <tr className="transition hover:bg-zinc-800/75 [&:last-child>td]:border-b-0">
    {fields.map(([name, label, type], index) => <td className={`${cellClasses} ${index === 0 ? 'font-semibold text-zinc-100' : ''}`} key={name}>{editing ? <input aria-label={label} className={inputClasses} min={type === 'number' ? '0' : undefined} name={name} step={name === 'price' ? '0.01' : undefined} type={type} value={draft[name]} onChange={updateField} /> : values[index]}</td>)}
    <td className={cellClasses}>{editing ? <input aria-label="Includes disc" className="size-4 accent-indigo-600" name="hasDisc" type="checkbox" checked={draft.hasDisc} onChange={updateField} /> : (discMag.hasDisc ? 'Yes' : 'No')}</td>
    <td className={`${cellClasses} text-right`}><div className="flex justify-end gap-2">{editing ? <><button className="cursor-pointer border border-zinc-700 px-3 py-1.5 text-xs" type="button" onClick={cancelEditing} disabled={saving}>Cancel</button><button className="cursor-pointer border border-indigo-600 bg-indigo-600 px-3 py-1.5 text-xs" type="button" onClick={saveDiscMag} disabled={saving}>{saving ? 'Saving...' : 'Save'}</button></> : <><button className="cursor-pointer border border-indigo-500/50 px-3 py-1.5 text-xs text-indigo-400" type="button" onClick={() => setEditing(true)}>Edit</button><button className="cursor-pointer border border-red-500/40 px-3 py-1.5 text-xs text-red-400" type="button" onClick={() => onDelete(discMag)}>Delete</button></>}</div>{saveError && <p className="mt-2 text-xs text-red-400" role="alert">{saveError}</p>}</td>
  </tr>;
}
