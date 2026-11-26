import api from "../../api.js";
import { useCallback, useEffect, useState } from "react";
import Book from "./Book.jsx";
import ConfirmModal from "../ConfirmModal.jsx";
import useAuth from "../../useAuth.js";

export default function BookList({ onAdd, onAddToCart, cartPending, canManage }) {
  const { token } = useAuth();
  const isCurrent = useCallback(() => localStorage.getItem("token") === token, [token]);
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [bookToDelete, setBookToDelete] = useState(null);
  const [deleting, setDeleting] = useState(false);

  useEffect(() => {
    let active = true;
    api
      .get("/books")
      .then((response) => {
        if (active && isCurrent()) setBooks(response.data);
      })
      .catch((err) => {
        if (active && isCurrent()) setError(err.message);
      })
      .finally(() => {
        if (active && isCurrent()) setLoading(false);
      });
    return () => {
      active = false;
    };
  }, [isCurrent]);

  async function handleDeleteBook(id) {
    setError("");
    setDeleting(true);
    try {
      await api.delete(`/books/${id}`);
      if (!isCurrent()) return;
      setBooks((previous) => previous.filter((book) => book.id !== id));
      setBookToDelete(null);
    } catch (err) {
      if (isCurrent()) setError(err.message);
    } finally {
      setDeleting(false);
    }
  }

  async function handleUpdateBook(id, data) {
    const response = await api.put(`/books/${id}`, data);
    if (!isCurrent()) return;
    const savedBook = response.data;
    setBooks((previous) =>
      previous.map((book) => (book.id === id ? savedBook : book)),
    );
    return savedBook;
  }

  return (
    <section
      className="min-w-0 border border-zinc-800 bg-zinc-900"
      aria-labelledby="inventory-title"
    >
      <div className="flex items-center justify-between border-b border-zinc-800 px-5.5 py-5">
        <div>
          <h2
            className="text-[1.05rem] font-semibold tracking-tight"
            id="inventory-title"
          >
            Book Inventory
          </h2>
          <p className="mt-2 text-sm text-zinc-400">
            {loading
              ? "Loading collection..."
              : `${books.length} ${books.length === 1 ? "book" : "books"} in the catalog`}
          </p>
        </div>
        {canManage && <button
          className="shrink-0 cursor-pointer border border-indigo-600 bg-indigo-600 px-4 py-2 text-sm font-semibold text-white transition hover:border-indigo-500 hover:bg-indigo-500"
          type="button"
          onClick={onAdd}
        >
          + Add Book
        </button>}
      </div>

      {error && (
        <p
          className="px-5.5 py-9 text-center text-sm text-red-400"
          role="alert"
        >
          {error}
        </p>
      )}
      {!error && !loading && books.length === 0 && (
        <p className="px-5.5 py-9 text-center text-zinc-400">No books yet.</p>
      )}

      {!error && books.length > 0 && (
        <div className="overflow-x-auto">
          <table className="w-full border-collapse text-left text-sm">
            <thead>
              <tr>
                <th className="border-b border-zinc-800 px-3.5 py-4 text-xs font-medium text-zinc-400 sm:px-5">
                  Title
                </th>
                <th className="border-b border-zinc-800 px-3.5 py-4 text-xs font-medium text-zinc-400 sm:px-5">
                  Author
                </th>
                <th className="border-b border-zinc-800 px-3.5 py-4 text-xs font-medium text-zinc-400 sm:px-5">
                  Price
                </th>
                <th className="border-b border-zinc-800 px-3.5 py-4 text-xs font-medium text-zinc-400 sm:px-5">
                  Copies
                </th>
                <th className="border-b border-zinc-800 px-3.5 py-4 text-right text-xs font-medium text-zinc-400 sm:px-5">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody>
              {books.map((book) => (
                <Book
                  book={book}
                  key={book.id}
                  onDelete={setBookToDelete}
                  onUpdate={handleUpdateBook}
                  onAddToCart={onAddToCart}
                  cartPending={cartPending}
                  canManage={canManage}
                />
              ))}
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
