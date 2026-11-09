import { useState, useEffect } from 'react';

export default function BookList() {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    useEffect(() => {
        let active = true;
        fetch('/api/rest/books')
            .then(res => {
                if (!res.ok) throw new Error(`Load failed (${res.status})`);
                return res.json();
            })
            .then(data => { if (active) setBooks(data); })
            .catch(err => { if (active) setError(err.message); })
            .finally(() => { if (active) setLoading(false); });
        return () => { active = false; };
    }, []);
    if (loading) return <p>Loading books...</p>;
    if (error) return <p role="alert">{error}</p>;
    return <section aria-label="Book inventory">
        <h2>Books</h2>
        {books.length === 0 && <p>No books yet.</p>}
        {books.map(book => <article className="book-row" key={book.id}>
            <h3>{book.title}</h3>
            <p>{book.author} | ${Number(book.price).toFixed(2)} | Copies: {book.copies}</p>
        </article>)}
    </section>;
}