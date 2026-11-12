import { useState } from 'react';

const cellClasses = 'border-b border-zinc-800 px-3.5 py-4 text-zinc-300 sm:px-5';
const inputClasses = 'h-9 w-full min-w-24 border border-zinc-700 bg-zinc-950 px-2.5 text-sm text-zinc-100 outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500/10';

function draftFrom(album) {
  return { title: album.title, artist: album.artist, genre: album.genre, releaseYear: album.releaseYear, price: album.price, copies: album.copies };
}

export default function Album({ album, onDelete, onUpdate }) {
  const [editing, setEditing] = useState(false);
  const [draft, setDraft] = useState(() => draftFrom(album));
  const [saving, setSaving] = useState(false);
  const [saveError, setSaveError] = useState('');

  function updateField(event) {
    const { name, value } = event.target;
    setDraft(current => ({ ...current, [name]: value }));
  }

  function cancelEditing() {
    setDraft(draftFrom(album));
    setSaveError('');
    setEditing(false);
  }

  async function saveAlbum() {
    setSaveError('');
    setSaving(true);
    try {
      const savedAlbum = await onUpdate(album.id, {
        ...album,
        ...draft,
        releaseYear: Number(draft.releaseYear),
        price: Number(draft.price),
        copies: Number(draft.copies)
      });
      setDraft(draftFrom(savedAlbum));
      setEditing(false);
    } catch (err) {
      setSaveError(err.message);
    } finally {
      setSaving(false);
    }
  }

  const fields = [
    ['title', 'Album title', 'text'], ['artist', 'Album artist', 'text'], ['genre', 'Album genre', 'text'],
    ['releaseYear', 'Album release year', 'number'], ['price', 'Album price', 'number'], ['copies', 'Album copies', 'number']
  ];
  const displayValues = [album.title, album.artist, album.genre, album.releaseYear, `$${Number(album.price).toFixed(2)}`, album.copies];

  return (
    <tr className="transition hover:bg-zinc-800/75 [&:last-child>td]:border-b-0">
      {fields.map(([name, label, type], index) => (
        <td className={`${cellClasses} ${index === 0 ? 'font-semibold text-zinc-100' : ''}`} key={name}>
          {editing ? <input aria-label={label} className={inputClasses} min={type === 'number' ? '0' : undefined} name={name} step={name === 'price' ? '0.01' : undefined} type={type} value={draft[name]} onChange={updateField} /> : displayValues[index]}
        </td>
      ))}
      <td className={`${cellClasses} text-right`}>
        <div className="flex justify-end gap-2">
          {editing ? (
            <>
              <button className="cursor-pointer border border-zinc-700 px-3 py-1.5 text-xs font-medium text-zinc-300 transition hover:bg-zinc-800 disabled:cursor-wait disabled:opacity-60" type="button" onClick={cancelEditing} disabled={saving}>Cancel</button>
              <button className="cursor-pointer border border-indigo-600 bg-indigo-600 px-3 py-1.5 text-xs font-medium text-white transition hover:bg-indigo-500 disabled:cursor-wait disabled:opacity-60" type="button" onClick={saveAlbum} disabled={saving}>{saving ? 'Saving...' : 'Save'}</button>
            </>
          ) : (
            <>
              <button className="cursor-pointer border border-indigo-500/50 px-3 py-1.5 text-xs font-medium text-indigo-400 transition hover:bg-indigo-500/10" type="button" onClick={() => setEditing(true)}>Edit</button>
              <button className="cursor-pointer border border-red-500/40 px-3 py-1.5 text-xs font-medium text-red-400 transition hover:bg-red-500/10" type="button" onClick={() => onDelete(album)}>Delete</button>
            </>
          )}
        </div>
        {saveError && <p className="mt-2 text-xs text-red-400" role="alert">{saveError}</p>}
      </td>
    </tr>
  );
}
