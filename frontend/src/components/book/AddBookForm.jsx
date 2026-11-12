import { useState } from 'react';

const initialBook = { title: '', author: '', price: '', copies: '10', isbn: '' };

export default function AddBookForm({ onBookAdded }) {
  const [book, setBook] = useState(initialBook);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  function updateField(event) {
    const { name, value } = event.target;
    setBook(current => ({ ...current, [name]: value }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError('');
    setSaving(true);
    try {
      const response = await fetch('/api/rest/books', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ ...book, price: Number(book.price), copies: Number(book.copies) })
      });
      if (!response.ok) throw new Error(`Save failed (${response.status})`);
      const savedBook = await response.json();
      onBookAdded(savedBook);
      setBook(initialBook);
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <section className="order-2 border border-zinc-800 bg-zinc-900 min-[801px]:order-none" aria-labelledby="add-book-title">
      <div className="border-b border-zinc-800 px-5.5 py-5"><h2 className="text-[1.05rem] font-semibold tracking-tight" id="add-book-title">Add New Book</h2><p className="mt-2 text-sm text-zinc-400">Enter the information for the new book.</p></div>
      <form className="grid gap-4.5 p-5.5" onSubmit={handleSubmit}>
        <label className="grid min-w-0 gap-2 text-sm font-medium text-zinc-300"><span>Title</span><input className="h-10.5 w-full min-w-0 border border-zinc-700 bg-zinc-950 px-3 py-2 text-zinc-100 outline-none transition focus:border-indigo-500 focus:ring-3 focus:ring-indigo-500/10" name="title" value={book.title} onChange={updateField} required /></label>
        <label className="grid min-w-0 gap-2 text-sm font-medium text-zinc-300"><span>Author</span><input className="h-10.5 w-full min-w-0 border border-zinc-700 bg-zinc-950 px-3 py-2 text-zinc-100 outline-none transition focus:border-indigo-500 focus:ring-3 focus:ring-indigo-500/10" name="author" value={book.author} onChange={updateField} required /></label>
        <div className="grid grid-cols-1 gap-3.5 min-[561px]:grid-cols-2">
          <label className="grid min-w-0 gap-2 text-sm font-medium text-zinc-300"><span>Price</span><input className="h-10.5 w-full min-w-0 border border-zinc-700 bg-zinc-950 px-3 py-2 text-zinc-100 outline-none transition focus:border-indigo-500 focus:ring-3 focus:ring-indigo-500/10" name="price" type="number" min="0" step="0.01" value={book.price} onChange={updateField} required /></label>
          <label className="grid min-w-0 gap-2 text-sm font-medium text-zinc-300"><span>Copies</span><input className="h-10.5 w-full min-w-0 border border-zinc-700 bg-zinc-950 px-3 py-2 text-zinc-100 outline-none transition focus:border-indigo-500 focus:ring-3 focus:ring-indigo-500/10" name="copies" type="number" min="0" step="1" value={book.copies} onChange={updateField} required /></label>
        </div>
        <label className="grid min-w-0 gap-2 text-sm font-medium text-zinc-300"><span>ISBN</span><input className="h-10.5 w-full min-w-0 border border-zinc-700 bg-zinc-950 px-3 py-2 text-zinc-100 outline-none transition focus:border-indigo-500 focus:ring-3 focus:ring-indigo-500/10" name="isbn" value={book.isbn} onChange={updateField} required /></label>
        {error && <p className="text-sm text-red-400" role="alert">{error}</p>}
        <button className="w-fit cursor-pointer border border-indigo-600 bg-indigo-600 px-4 py-2 text-sm font-semibold text-white transition hover:border-indigo-500 hover:bg-indigo-500 disabled:cursor-wait disabled:opacity-65" disabled={saving}>{saving ? 'Adding Book...' : 'Add Book'}</button>
      </form>
    </section>
  );
}
