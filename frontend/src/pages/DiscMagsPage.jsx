import { useState } from 'react';
import AddDiscMagForm from '../components/discMag/AddDiscMagForm.jsx';
import DiscMagList from '../components/discMag/DiscMagList.jsx';
import FormModal from '../components/FormModal.jsx';

export default function DiscMagsPage() {
  const [refreshKey, setRefreshKey] = useState(0);
  const [showAddForm, setShowAddForm] = useState(false);

  function handleDiscMagAdded() {
    setRefreshKey(previous => previous + 1);
    setShowAddForm(false);
  }

  return <>
    <DiscMagList key={refreshKey} onAdd={() => setShowAddForm(true)} />
    <FormModal isOpen={showAddForm} onClose={() => setShowAddForm(false)}>
      <AddDiscMagForm onDiscMagAdded={handleDiscMagAdded} />
    </FormModal>
  </>;
}
