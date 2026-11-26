import { useCallback, useEffect, useState } from "react";
import api from "../../api.js";
import useAuth from "../../useAuth.js";
import ConfirmModal from "../ConfirmModal.jsx";
import Ticket from "./Ticket.jsx";

export default function TicketList({ onAdd, onAddToCart, cartPending, canManage }) {
  const { token } = useAuth();
  const isCurrent = useCallback(() => localStorage.getItem("token") === token, [token]);
  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [itemToDelete, setItemToDelete] = useState(null);
  const [deleting, setDeleting] = useState(false);
  useEffect(() => {
    let active = true;
    api
      .get("/tickets")
      .then((response) => {
        if (active && isCurrent()) setTickets(response.data);
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
  async function handleDeleteTicket(id) {
    setError("");
    setDeleting(true);
    try {
      await api.delete(`/tickets/${id}`);
      if (!isCurrent()) return;
      setTickets((previous) => previous.filter((item) => item.id !== id));
      setItemToDelete(null);
    } catch (err) {
      if (isCurrent()) setError(err.message);
    } finally {
      setDeleting(false);
    }
  }
  async function handleUpdateTicket(id, ticket) {
    const response = await api.put(`/tickets/${id}`, ticket);
    if (!isCurrent()) return;
    const savedTicket = response.data;
    setTickets((previous) =>
      previous.map((current) => (current.id === id ? savedTicket : current)),
    );
    return savedTicket;
  }
  return (
    <section
      className="min-w-0 border border-zinc-800 bg-zinc-900"
      aria-labelledby="ticket-inventory-title"
    >
      <div className="flex items-center justify-between border-b border-zinc-800 px-5.5 py-5">
        <div>
          <h2
            className="text-[1.05rem] font-semibold"
            id="ticket-inventory-title"
          >
            Ticket Inventory
          </h2>
          <p className="mt-2 text-sm text-zinc-400">
            {loading
              ? "Loading collection..."
              : `${tickets.length} ${tickets.length === 1 ? "ticket" : "tickets"} in the catalog`}
          </p>
        </div>
        {canManage && <button
          className="shrink-0 cursor-pointer border border-indigo-600 bg-indigo-600 px-4 py-2 text-sm font-semibold"
          type="button"
          onClick={onAdd}
        >
          '+ Add Ticket'
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
      {!error && !loading && tickets.length === 0 && (
        <p className="px-5.5 py-9 text-center text-zinc-400">No tickets yet.</p>
      )}
      {!error && tickets.length > 0 && (
        <div className="overflow-x-auto">
          <table className="w-full border-collapse text-left text-sm">
            <thead>
              <tr>
                {["Description", "Price"].map((heading) => (
                  <th
                    className="border-b border-zinc-800 px-3.5 py-4 text-xs font-medium text-zinc-400 sm:px-5"
                    key={heading}
                  >
                    {heading}
                  </th>
                ))}
                <th className="border-b border-zinc-800 px-3.5 py-4 text-right text-xs font-medium text-zinc-400 sm:px-5">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody>
          {tickets.map((ticket) => (
            <Ticket
              ticket={ticket}
              key={ticket.id}
                  onDelete={setItemToDelete}
                  onUpdate={handleUpdateTicket}
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
        isOpen={itemToDelete !== null}
        title="Delete ticket"
        message={`Are you sure you want to delete "${itemToDelete?.description}"? This action cannot be undone.`}
        confirmLabel="Delete Ticket"
        busy={deleting}
        onCancel={() => setItemToDelete(null)}
        onConfirm={() => handleDeleteTicket(itemToDelete.id)}
      />
    </section>
  );
}
