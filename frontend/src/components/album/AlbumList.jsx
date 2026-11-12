import { useEffect, useState } from 'react';
import Album from './Album.jsx';
import ConfirmModal from '../ConfirmModal.jsx';

export default function AlbumList({ onAdd }) {
  const [albums, setAlbums] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [albumToDelete, setAlbumToDelete] = useState(null);
  const [deleting, setDeleting] = useState(false);

  useEffect(() => {
    let active = true;
    fetch('/api/rest/albums')
      .then(response => {
        if (!response.ok) throw new Error(`Load failed (${response.status})`);
        return response.json();
      })
      .then(data => { if (active) setAlbums(data); })
      .catch(err => { if (active) setError(err.message); })
      .finally(() => { if (active) setLoading(false); });
    return () => { active = false; };
  }, []);

  async function handleDeleteAlbum(id) {
    setError('');
    setDeleting(true);
    try {
      const response = await fetch(`/api/rest/albums/${id}`, { method: 'DELETE' });
      if (!response.ok) throw new Error(`Delete failed (${response.status})`);
      setAlbums(previous => previous.filter(album => album.id !== id));
      setAlbumToDelete(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setDeleting(false);
    }
  }

  async function handleUpdateAlbum(id, updatedAlbum) {
    const response = await fetch(`/api/rest/albums/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(updatedAlbum)
    });
    if (!response.ok) throw new Error(`Save failed (${response.status})`);
    const savedAlbum = await response.json();
    setAlbums(previous => previous.map(album => album.id === id ? savedAlbum : album));
    return savedAlbum;
  }

  return (
    <section className="min-w-0 border border-zinc-800 bg-zinc-900" aria-labelledby="album-inventory-title">
      <div className="flex items-center justify-between border-b border-zinc-800 px-5.5 py-5">
        <div>
          <h2 className="text-[1.05rem] font-semibold tracking-tight" id="album-inventory-title">Album Inventory</h2>
          <p className="mt-2 text-sm text-zinc-400">{loading ? 'Loading collection...' : `${albums.length} ${albums.length === 1 ? 'album' : 'albums'} in the catalog`}</p>
        </div>
        <button className="shrink-0 cursor-pointer border border-indigo-600 bg-indigo-600 px-4 py-2 text-sm font-semibold text-white transition hover:border-indigo-500 hover:bg-indigo-500" type="button" onClick={onAdd}>+ Add Album</button>
      </div>
      {error && <p className="px-5.5 py-9 text-center text-sm text-red-400" role="alert">{error}</p>}
      {!error && !loading && albums.length === 0 && <p className="px-5.5 py-9 text-center text-zinc-400">No albums yet.</p>}
      {!error && albums.length > 0 && (
        <div className="overflow-x-auto">
          <table className="w-full border-collapse text-left text-sm">
            <thead><tr>{['Title', 'Artist', 'Genre', 'Year', 'Price', 'Copies'].map(heading => <th className="border-b border-zinc-800 px-3.5 py-4 text-xs font-medium text-zinc-400 sm:px-5" key={heading}>{heading}</th>)}<th className="border-b border-zinc-800 px-3.5 py-4 text-right text-xs font-medium text-zinc-400 sm:px-5">Actions</th></tr></thead>
            <tbody>
              {albums.map(album => <Album album={album} key={album.id} onDelete={setAlbumToDelete} onUpdate={handleUpdateAlbum} />)}
            </tbody>
          </table>
        </div>
      )}
      <ConfirmModal
        isOpen={albumToDelete !== null}
        title="Delete album"
        message={`Are you sure you want to delete “${albumToDelete?.title}”? This action cannot be undone.`}
        confirmLabel="Delete Album"
        busy={deleting}
        onCancel={() => setAlbumToDelete(null)}
        onConfirm={() => handleDeleteAlbum(albumToDelete.id)}
      />
    </section>
  );
}
