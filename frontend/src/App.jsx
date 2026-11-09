import { useState } from 'react';
import AddBookForm from './AddBookForm.jsx';
import BookList from './BookList.jsx';
import './App.css';

export default function App() {
  const [refreshKey, setRefreshKey] = useState(0);
  return <main className="app-container">
    <h1>My Bookstore Frontend</h1>
    <AddBookForm onBookAdded={() => setRefreshKey(previous => previous + 1)} />
    <BookList key={refreshKey} />
  </main>;
}