import { useState } from 'react';
import AddMagazineForm from '../components/magazine/AddMagazineForm.jsx';
import FormModal from '../components/FormModal.jsx';
import MagazineList from '../components/magazine/MagazineList.jsx';

export default function MagazinesPage() {
  const [refreshKey, setRefreshKey] = useState(0);
  const [showAddForm, setShowAddForm] = useState(false);

  function handleMagazineAdded() {
    setRefreshKey(previous => previous + 1);
    setShowAddForm(false);
  }

  return (
    <>
      <MagazineList key={refreshKey} onAdd={() => setShowAddForm(true)} />
      <FormModal isOpen={showAddForm} onClose={() => setShowAddForm(false)}>
        <AddMagazineForm onMagazineAdded={handleMagazineAdded} />
      </FormModal>
    </>
  );
}
