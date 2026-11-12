import { useEffect, useState } from 'react';
import ConfirmModal from '../ConfirmModal.jsx';
import DiscMag from './DiscMag.jsx';

export default function DiscMagList({ onAdd }) {
  const [discMags, setDiscMags] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [itemToDelete, setItemToDelete] = useState(null);
  const [deleting, setDeleting] = useState(false);
  useEffect(() => { let active = true; fetch('/api/rest/discmags').then(response => { if (!response.ok) throw new Error(`Load failed (${response.status})`); return response.json(); }).then(data => { if (active) setDiscMags(data); }).catch(err => { if (active) setError(err.message); }).finally(() => { if (active) setLoading(false); }); return () => { active = false; }; }, []);
  async function handleDelete(id) { setError(''); setDeleting(true); try { const response = await fetch(`/api/rest/discmags/${id}`, { method: 'DELETE' }); if (!response.ok) throw new Error(`Delete failed (${response.status})`); setDiscMags(previous => previous.filter(item => item.id !== id)); setItemToDelete(null); } catch (err) { setError(err.message); } finally { setDeleting(false); } }
  async function handleUpdate(id, item) { const response = await fetch(`/api/rest/discmags/${id}`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(item) }); if (!response.ok) throw new Error(`Save failed (${response.status})`); const saved = await response.json(); setDiscMags(previous => previous.map(current => current.id === id ? saved : current)); return saved; }
  return <section className="min-w-0 border border-zinc-800 bg-zinc-900" aria-labelledby="discmag-inventory-title">
    <div className="flex items-center justify-between gap-4 border-b border-zinc-800 px-5.5 py-5"><div><h2 className="text-[1.05rem] font-semibold" id="discmag-inventory-title">Disc Mag Inventory</h2><p className="mt-2 text-sm text-zinc-400">{loading ? 'Loading collection...' : `${discMags.length} ${discMags.length === 1 ? 'disc mag' : 'disc mags'} in the catalog`}</p></div><button className="shrink-0 cursor-pointer border border-indigo-600 bg-indigo-600 px-4 py-2 text-sm font-semibold" type="button" onClick={onAdd}>+ Add Disc Mag</button></div>
    {error && <p className="px-5.5 py-9 text-center text-sm text-red-400" role="alert">{error}</p>}
    {!error && !loading && discMags.length === 0 && <p className="px-5.5 py-9 text-center text-zinc-400">No disc mags yet.</p>}
    {!error && discMags.length > 0 && <div className="overflow-x-auto"><table className="w-full border-collapse text-left text-sm"><thead><tr>{['Title', 'Current Issue', 'Price', 'Copies', 'Order Qty', 'Disc'].map(heading => <th className="border-b border-zinc-800 px-3.5 py-4 text-xs font-medium text-zinc-400 sm:px-5" key={heading}>{heading}</th>)}<th className="border-b border-zinc-800 px-3.5 py-4 text-right text-xs font-medium text-zinc-400 sm:px-5">Actions</th></tr></thead><tbody>{discMags.map(item => <DiscMag discMag={item} key={item.id} onDelete={setItemToDelete} onUpdate={handleUpdate} />)}</tbody></table></div>}
    <ConfirmModal isOpen={itemToDelete !== null} title="Delete disc mag" message={`Are you sure you want to delete "${itemToDelete?.title}"? This action cannot be undone.`} confirmLabel="Delete Disc Mag" busy={deleting} onCancel={() => setItemToDelete(null)} onConfirm={() => handleDelete(itemToDelete.id)} />
  </section>;
}
