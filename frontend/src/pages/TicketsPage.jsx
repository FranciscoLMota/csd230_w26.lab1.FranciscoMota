import { useState } from 'react';
import AddTicketForm from '../components/ticket/AddTicketForm.jsx';
import FormModal from '../components/FormModal.jsx';
import TicketList from '../components/ticket/TicketList.jsx';

export default function TicketsPage() {
  const [refreshKey, setRefreshKey] = useState(0);
  const [showAddForm, setShowAddForm] = useState(false);

  function handleTicketAdded() {
    setRefreshKey(previous => previous + 1);
    setShowAddForm(false);
  }

  return <>
    <TicketList key={refreshKey} onAdd={() => setShowAddForm(true)} />
    <FormModal isOpen={showAddForm} onClose={() => setShowAddForm(false)}>
      <AddTicketForm onTicketAdded={handleTicketAdded} />
    </FormModal>
  </>;
}
