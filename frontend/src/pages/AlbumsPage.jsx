import { useState } from 'react';
import AddAlbumForm from '../components/album/AddAlbumForm.jsx';
import AlbumList from '../components/album/AlbumList.jsx';
import FormModal from '../components/FormModal.jsx';

export default function AlbumsPage() {
  const [refreshKey, setRefreshKey] = useState(0);
  const [showAddForm, setShowAddForm] = useState(false);

  function handleAlbumAdded() {
    setRefreshKey(previous => previous + 1);
    setShowAddForm(false);
  }

  return (
    <>
      <AlbumList key={refreshKey} onAdd={() => setShowAddForm(true)} />
      <FormModal isOpen={showAddForm} onClose={() => setShowAddForm(false)}>
        <AddAlbumForm onAlbumAdded={handleAlbumAdded} />
      </FormModal>
    </>
  );
}
