import { useEffect, useState } from 'react';
import Book from './Book.jsx';
import ConfirmModal from '../ConfirmModal.jsx';

export default function BookList({ onAdd }) {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [bookToDelete, setBookToDelete] = useState(null);
    const [deleting, setDeleting] = useState(false);

    useEffect(() => {
        let active = true;
        fetch('/api/rest/books')
            .then(response => {
                if (!response.ok) throw new Error(`Load failed (${response.status})`);
                return response.json();
            })
            .then(data => { if (active) setBooks(data); })
            .catch(err => { if (active) setError(err.message); })
            .finally(() => { if (active) setLoading(false); });
        return () => { active = false; };
    }, []);

    async function handleDeleteBook(id) {
        setError('');
        setDeleting(true);
        try {
            const response = await fetch(`/api/rest/books/${id}`, { method: 'DELETE' });
            if (!response.ok) throw new Error(`Delete failed (${response.status})`);
            setBooks(previous => previous.filter(book => book.id !== id));
            setBookToDelete(null);
        } catch (err) {
            setError(err.message);
        } finally {
            setDeleting(false);
        }
    }

    async function handleUpdateBook(id, updatedBook) {
        const response = await fetch(`/api/rest/books/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedBook)
        });
        if (!response.ok) throw new Error(`Save failed (${response.status})`);
        const savedBook = await response.json();
        setBooks(previous => previous.map(book => book.id === id ? savedBook : book));
        return savedBook;
    }

    return (
        <section className="min-w-0 border border-zinc-800 bg-zinc-900" aria-labelledby="inventory-title">
            <div className="flex items-center justify-between border-b border-zinc-800 px-5.5 py-5">
                <div>
                    <h2 className="text-[1.05rem] font-semibold tracking-tight" id="inventory-title">Book Inventory</h2>
                    <p className="mt-2 text-sm text-zinc-400">{loading ? 'Loading collection...' : `${books.length} ${books.length === 1 ? 'book' : 'books'} in the catalog`}</p>
                </div>
                <button className="shrink-0 cursor-pointer border border-indigo-600 bg-indigo-600 px-4 py-2 text-sm font-semibold text-white transition hover:border-indigo-500 hover:bg-indigo-500" type="button" onClick={onAdd}>+ Add Book</button>
            </div>

            {error && <p className="px-5.5 py-9 text-center text-sm text-red-400" role="alert">{error}</p>}
            {!error && !loading && books.length === 0 && <p className="px-5.5 py-9 text-center text-zinc-400">No books yet.</p>}

            {!error && books.length > 0 && (
                <div className="overflow-x-auto">
                    <table className="w-full border-collapse text-left text-sm">
                        <thead>
                            <tr>
                                <th className="border-b border-zinc-800 px-3.5 py-4 text-xs font-medium text-zinc-400 sm:px-5">Title</th>
                                <th className="border-b border-zinc-800 px-3.5 py-4 text-xs font-medium text-zinc-400 sm:px-5">Author</th>
                                <th className="border-b border-zinc-800 px-3.5 py-4 text-xs font-medium text-zinc-400 sm:px-5">Price</th>
                                <th className="border-b border-zinc-800 px-3.5 py-4 text-xs font-medium text-zinc-400 sm:px-5">Copies</th>
                                <th className="border-b border-zinc-800 px-3.5 py-4 text-right text-xs font-medium text-zinc-400 sm:px-5">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {books.map(book => <Book book={book} key={book.id} onDelete={setBookToDelete} onUpdate={handleUpdateBook} />)}
                        </tbody>
                    </table>
                </div>
            )}
            <ConfirmModal
                isOpen={bookToDelete !== null}
                title="Delete book"
                message={`Are you sure you want to delete “${bookToDelete?.title}”? This action cannot be undone.`}
                confirmLabel="Delete Book"
                busy={deleting}
                onCancel={() => setBookToDelete(null)}
                onConfirm={() => handleDeleteBook(bookToDelete.id)}
            />
        </section>
    );
}
