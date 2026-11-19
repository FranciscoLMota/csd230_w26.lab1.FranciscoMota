import { useCallback, useEffect, useState } from "react";
import api from "../../api.js";
import ConfirmModal from "../ConfirmModal.jsx";
import Magazine from "./Magazine.jsx";
import useAuth from "../../useAuth.js";

export default function MagazineList({ onAdd, onAddToCart, cartPending, canManage }) {
  const { token, isAdmin } = useAuth();
  const isCurrent = useCallback(
    () => localStorage.getItem("token") === token,
    [token],
  );
  const [magazines, setMagazines] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [magazineToDelete, setMagazineToDelete] = useState(null);
  const [deleting, setDeleting] = useState(false);

  useEffect(() => {
    let active = true;
    api
      .get("/magazines")
      .then((response) => {
        if (active && isCurrent()) setMagazines(response.data);
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

  async function handleDeleteMagazine(id) {
    setError("");
    setDeleting(true);
    try {
      await api.delete(`/magazines/${id}`);
      if (!isCurrent()) return;
      setMagazines((previous) =>
        previous.filter((magazine) => magazine.id !== id),
      );
      setMagazineToDelete(null);
    } catch (err) {
      if (isCurrent()) setError(err.message);
    } finally {
      setDeleting(false);
    }
  }

  async function handleUpdateMagazine(id, updatedMagazine) {
    const response = await api.put(`/magazines/${id}`, updatedMagazine);
    if (!isCurrent()) return;
    const savedMagazine = response.data;
    setMagazines((previous) =>
      previous.map((magazine) =>
        magazine.id === id ? savedMagazine : magazine,
      ),
    );
    return savedMagazine;
  }

  return (
    <section
      className="min-w-0 border border-zinc-800 bg-zinc-900"
      aria-labelledby="magazine-inventory-title"
    >
      <div className="flex items-center justify-between gap-4 border-b border-zinc-800 px-5.5 py-5">
        <div>
          <h2
            className="text-[1.05rem] font-semibold tracking-tight"
            id="magazine-inventory-title"
          >
            Magazine Inventory
          </h2>
          <p className="mt-2 text-sm text-zinc-400">
            {loading
              ? "Loading collection..."
              : `${magazines.length} ${magazines.length === 1 ? "magazine" : "magazines"} in the catalog`}
          </p>
        </div>
        {isAdmin && <button
          className="shrink-0 cursor-pointer border border-indigo-600 bg-indigo-600 px-4 py-2 text-sm font-semibold text-white transition hover:border-indigo-500 hover:bg-indigo-500"
          type="button"
          onClick={onAdd}
        >
          + Add Magazine
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
      {!error && !loading && magazines.length === 0 && (
        <p className="px-5.5 py-9 text-center text-zinc-400">
          No magazines yet.
        </p>
      )}
      {!error && magazines.length > 0 && (
        <div className="overflow-x-auto">
          <table className="w-full border-collapse text-left text-sm">
            <thead>
              <tr>
                {["Title", "Current Issue", "Price", "Copies", "Order Qty"].map(
                  (heading) => (
                    <th
                      className="border-b border-zinc-800 px-3.5 py-4 text-xs font-medium text-zinc-400 sm:px-5"
                      key={heading}
                    >
                      {heading}
                    </th>
                  ),
                )}
                <th className="border-b border-zinc-800 px-3.5 py-4 text-right text-xs font-medium text-zinc-400 sm:px-5">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody>
              {magazines.map((magazine) => (
                <Magazine
                  key={magazine.id}
                  magazine={magazine}
                  onDelete={setMagazineToDelete}
                  onUpdate={handleUpdateMagazine}
                  onAddToCart={onAddToCart}
                  cartPending={cartPending}
                  canManage={isAdmin}
                />
              ))}
            </tbody>
          </table>
        </div>
      )}
      <ConfirmModal
        isOpen={magazineToDelete !== null}
        title="Delete magazine"
        message={`Are you sure you want to delete "${magazineToDelete?.title}"? This action cannot be undone.`}
        confirmLabel="Delete Magazine"
        busy={deleting}
        onCancel={() => setMagazineToDelete(null)}
        onConfirm={() => handleDeleteMagazine(magazineToDelete.id)}
      />
    </section>
  );
}
