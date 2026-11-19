import { useCallback, useEffect, useState } from "react";
import api from "../../api.js";
import useAuth from "../../useAuth.js";
import ConfirmModal from "../ConfirmModal.jsx";
import DiscMag from "./DiscMag.jsx";

export default function DiscMagList({ onAdd, onAddToCart, cartPending }) {
  const { token } = useAuth();
  const isCurrent = useCallback(() => localStorage.getItem("token") === token, [token]);
  const [discMags, setDiscMags] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [itemToDelete, setItemToDelete] = useState(null);
  const [deleting, setDeleting] = useState(false);
  useEffect(() => {
    let active = true;
    api
      .get("/discmags")
      .then((response) => {
        if (active && isCurrent()) setDiscMags(response.data);
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
  async function handleDeleteDiscMag(id) {
    setError("");
    setDeleting(true);
    try {
      await api.delete(`/discmags/${id}`);
      if (!isCurrent()) return;
      setDiscMags((previous) => previous.filter((item) => item.id !== id));
      setItemToDelete(null);
    } catch (err) {
      if (isCurrent()) setError(err.message);
    } finally {
      setDeleting(false);
    }
  }
  async function handleUpdateDiscMag(id, discMag) {
    const response = await api.put(`/discmags/${id}`, discMag);
    if (!isCurrent()) return;
    const savedDiscMag = response.data;
    setDiscMags((previous) =>
      previous.map((current) => (current.id === id ? savedDiscMag : current)),
    );
    return savedDiscMag;
  }
  return (
    <section
      className="min-w-0 border border-zinc-800 bg-zinc-900"
      aria-labelledby="discmag-inventory-title"
    >
      <div className="flex items-center justify-between gap-4 border-b border-zinc-800 px-5.5 py-5">
        <div>
          <h2
            className="text-[1.05rem] font-semibold"
            id="discmag-inventory-title"
          >
            Disc Mag Inventory
          </h2>
          <p className="mt-2 text-sm text-zinc-400">
            {loading
              ? "Loading collection..."
              : `${discMags.length} ${discMags.length === 1 ? "disc mag" : "disc mags"} in the catalog`}
          </p>
        </div>
        <button
          className="shrink-0 cursor-pointer border border-indigo-600 bg-indigo-600 px-4 py-2 text-sm font-semibold"
          type="button"
          onClick={onAdd}
        >
          + Add Disc Mag
        </button>
      </div>
      {error && (
        <p
          className="px-5.5 py-9 text-center text-sm text-red-400"
          role="alert"
        >
          {error}
        </p>
      )}
      {!error && !loading && discMags.length === 0 && (
        <p className="px-5.5 py-9 text-center text-zinc-400">
          No disc mags yet.
        </p>
      )}
      {!error && discMags.length > 0 && (
        <div className="overflow-x-auto">
          <table className="w-full border-collapse text-left text-sm">
            <thead>
              <tr>
                {[
                  "Title",
                  "Current Issue",
                  "Price",
                  "Copies",
                  "Order Qty",
                  "Disc",
                ].map((heading) => (
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
          {discMags.map((discMag) => (
            <DiscMag
              discMag={discMag}
              key={discMag.id}
                  onDelete={setItemToDelete}
                  onUpdate={handleUpdateDiscMag}
                  onAddToCart={onAddToCart}
                  cartPending={cartPending}
                />
              ))}
            </tbody>
          </table>
        </div>
      )}
      <ConfirmModal
        isOpen={itemToDelete !== null}
        title="Delete disc mag"
        message={`Are you sure you want to delete "${itemToDelete?.title}"? This action cannot be undone.`}
        confirmLabel="Delete Disc Mag"
        busy={deleting}
        onCancel={() => setItemToDelete(null)}
        onConfirm={() => handleDeleteDiscMag(itemToDelete.id)}
      />
    </section>
  );
}
