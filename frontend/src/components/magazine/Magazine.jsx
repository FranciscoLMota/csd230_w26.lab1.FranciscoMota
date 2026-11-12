import { useState } from 'react';

const cellClasses = 'border-b border-zinc-800 px-3.5 py-4 text-zinc-300 sm:px-5';
const inputClasses = 'h-9 w-full min-w-24 border border-zinc-700 bg-zinc-950 px-2.5 text-sm text-zinc-100 outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500/10';

function draftFrom(magazine) {
  return { title: magazine.title, currentIssue: magazine.currentIssue?.slice(0, 16) ?? '', price: magazine.price, copies: magazine.copies, orderQty: magazine.orderQty };
}

export default function Magazine({ magazine, onDelete, onUpdate }) {
  const [editing, setEditing] = useState(false);
  const [draft, setDraft] = useState(() => draftFrom(magazine));
  const [saving, setSaving] = useState(false);
  const [saveError, setSaveError] = useState('');

  function updateField(event) {
    const { name, value } = event.target;
    setDraft(current => ({ ...current, [name]: value }));
  }

  function cancelEditing() {
    setDraft(draftFrom(magazine));
    setSaveError('');
    setEditing(false);
  }

  async function saveMagazine() {
    setSaveError('');
    setSaving(true);
    try {
      const savedMagazine = await onUpdate(magazine.id, {
        ...magazine, ...draft,
        price: Number(draft.price), copies: Number(draft.copies), orderQty: Number(draft.orderQty)
      });
      setDraft(draftFrom(savedMagazine));
      setEditing(false);
    } catch (err) {
      setSaveError(err.message);
    } finally {
      setSaving(false);
    }
  }

  const fields = [
    ['title', 'Magazine title', 'text'], ['currentIssue', 'Current issue', 'datetime-local'],
    ['price', 'Magazine price', 'number'], ['copies', 'Magazine copies', 'number'], ['orderQty', 'Order quantity', 'number']
  ];
  const displayValues = [magazine.title, magazine.currentIssue?.replace('T', ' ').slice(0, 16), `$${Number(magazine.price).toFixed(2)}`, magazine.copies, magazine.orderQty];

  return (
    <tr className="transition hover:bg-zinc-800/75 [&:last-child>td]:border-b-0">
      {fields.map(([name, label, type], index) => (
        <td className={`${cellClasses} ${index === 0 ? 'font-semibold text-zinc-100' : ''}`} key={name}>
          {editing ? <input aria-label={label} className={inputClasses} min={type === 'number' ? '0' : undefined} name={name} step={name === 'price' ? '0.01' : undefined} type={type} value={draft[name]} onChange={updateField} /> : displayValues[index]}
        </td>
      ))}
      <td className={`${cellClasses} text-right`}>
        <div className="flex justify-end gap-2">
          {editing ? <>
            <button className="cursor-pointer border border-zinc-700 px-3 py-1.5 text-xs font-medium text-zinc-300 transition hover:bg-zinc-800 disabled:opacity-60" type="button" onClick={cancelEditing} disabled={saving}>Cancel</button>
            <button className="cursor-pointer border border-indigo-600 bg-indigo-600 px-3 py-1.5 text-xs font-medium text-white transition hover:bg-indigo-500 disabled:opacity-60" type="button" onClick={saveMagazine} disabled={saving}>{saving ? 'Saving...' : 'Save'}</button>
          </> : <>
            <button className="cursor-pointer border border-indigo-500/50 px-3 py-1.5 text-xs font-medium text-indigo-400 transition hover:bg-indigo-500/10" type="button" onClick={() => setEditing(true)}>Edit</button>
            <button className="cursor-pointer border border-red-500/40 px-3 py-1.5 text-xs font-medium text-red-400 transition hover:bg-red-500/10" type="button" onClick={() => onDelete(magazine)}>Delete</button>
          </>}
        </div>
        {saveError && <p className="mt-2 text-xs text-red-400" role="alert">{saveError}</p>}
      </td>
    </tr>
  );
}
