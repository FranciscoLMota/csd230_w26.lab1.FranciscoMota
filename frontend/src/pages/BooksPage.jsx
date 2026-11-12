import { useState } from 'react';
import AddBookForm from '../components/book/AddBookForm.jsx';
import BookList from '../components/book/BookList.jsx';
import FormModal from '../components/FormModal.jsx';

export default function BooksPage() {
  const [refreshKey, setRefreshKey] = useState(0);
  const [showAddForm, setShowAddForm] = useState(false);

  function handleBookAdded() {
    setRefreshKey(previous => previous + 1);
    setShowAddForm(false);
  }

  return (
    <>
      <BookList key={refreshKey} onAdd={() => setShowAddForm(true)} />
      <FormModal isOpen={showAddForm} onClose={() => setShowAddForm(false)}>
        <AddBookForm onBookAdded={handleBookAdded} />
      </FormModal>
    </>
  );
}
