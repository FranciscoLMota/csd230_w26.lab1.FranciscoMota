import { useState } from 'react';

const cellClasses = 'border-b border-zinc-800 px-3.5 py-4 text-zinc-300 sm:px-5';
const inputClasses = 'h-9 w-full min-w-24 border border-zinc-700 bg-zinc-950 px-2.5 text-sm text-zinc-100 outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500/10';

function draftFrom(book) {
  return { title: book.title, author: book.author, price: book.price, copies: book.copies };
}

export default function Book({ book, onDelete, onUpdate }) {
  const [editing, setEditing] = useState(false);
  const [draft, setDraft] = useState(() => draftFrom(book));
  const [saving, setSaving] = useState(false);
  const [saveError, setSaveError] = useState('');

  function updateField(event) {
    const { name, value } = event.target;
    setDraft(current => ({ ...current, [name]: value }));
  }

  function cancelEditing() {
    setDraft(draftFrom(book));
    setSaveError('');
    setEditing(false);
  }

  async function saveBook() {
    setSaveError('');
    setSaving(true);
    try {
      const savedBook = await onUpdate(book.id, {
        ...book,
        ...draft,
        price: Number(draft.price),
        copies: Number(draft.copies)
      });
      setDraft(draftFrom(savedBook));
      setEditing(false);
    } catch (err) {
      setSaveError(err.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <tr className="transition hover:bg-zinc-800/75 [&:last-child>td]:border-b-0">
      <td className={`${cellClasses} font-semibold text-zinc-100`}>{editing ? <input aria-label="Book title" className={inputClasses} name="title" value={draft.title} onChange={updateField} /> : book.title}</td>
      <td className={cellClasses}>{editing ? <input aria-label="Book author" className={inputClasses} name="author" value={draft.author} onChange={updateField} /> : book.author}</td>
      <td className={cellClasses}>{editing ? <input aria-label="Book price" className={inputClasses} min="0" name="price" step="0.01" type="number" value={draft.price} onChange={updateField} /> : `$${Number(book.price).toFixed(2)}`}</td>
      <td className={cellClasses}>{editing ? <input aria-label="Book copies" className={inputClasses} min="0" name="copies" step="1" type="number" value={draft.copies} onChange={updateField} /> : book.copies}</td>
      <td className={`${cellClasses} text-right`}>
        <div className="flex justify-end gap-2">
          {editing ? (
            <>
              <button className="cursor-pointer border border-zinc-700 px-3 py-1.5 text-xs font-medium text-zinc-300 transition hover:bg-zinc-800 disabled:cursor-wait disabled:opacity-60" type="button" onClick={cancelEditing} disabled={saving}>Cancel</button>
              <button className="cursor-pointer border border-indigo-600 bg-indigo-600 px-3 py-1.5 text-xs font-medium text-white transition hover:bg-indigo-500 disabled:cursor-wait disabled:opacity-60" type="button" onClick={saveBook} disabled={saving}>{saving ? 'Saving...' : 'Save'}</button>
            </>
          ) : (
            <>
              <button className="cursor-pointer border border-indigo-500/50 px-3 py-1.5 text-xs font-medium text-indigo-400 transition hover:bg-indigo-500/10" type="button" onClick={() => setEditing(true)}>Edit</button>
              <button className="cursor-pointer border border-red-500/40 px-3 py-1.5 text-xs font-medium text-red-400 transition hover:bg-red-500/10" type="button" onClick={() => onDelete(book)}>Delete</button>
            </>
          )}
        </div>
        {saveError && <p className="mt-2 text-xs text-red-400" role="alert">{saveError}</p>}
      </td>
    </tr>
  );
}
