import { useEffect } from 'react';

export default function ConfirmModal({ isOpen, title, message, confirmLabel, busy, onConfirm, onCancel }) {
  useEffect(() => {
    if (!isOpen) return undefined;
    function handleKeyDown(event) {
      if (event.key === 'Escape' && !busy) onCancel();
    }
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [isOpen, busy, onCancel]);

  if (!isOpen) return null;

  return (
    <div
      className="fixed inset-0 z-50 grid place-items-center bg-black/70 p-4 backdrop-blur-[2px]"
      role="presentation"
      onMouseDown={event => { if (event.target === event.currentTarget && !busy) onCancel(); }}
    >
      <section className="w-full max-w-md border border-zinc-700 bg-zinc-900 shadow-2xl" role="alertdialog" aria-modal="true" aria-labelledby="confirm-title" aria-describedby="confirm-message">
        <div className="border-b border-zinc-800 px-6 py-5">
          <h2 className="text-lg font-semibold text-zinc-100" id="confirm-title">{title}</h2>
        </div>
        <div className="px-6 py-5">
          <p className="text-sm leading-6 text-zinc-400" id="confirm-message">{message}</p>
        </div>
        <div className="flex justify-end gap-3 border-t border-zinc-800 px-6 py-4">
          <button className="cursor-pointer border border-zinc-700 px-4 py-2 text-sm font-semibold text-zinc-300 transition hover:border-zinc-500 hover:bg-zinc-800 hover:text-white disabled:cursor-wait disabled:opacity-60" type="button" onClick={onCancel} disabled={busy}>Cancel</button>
          <button className="cursor-pointer border border-red-600 bg-red-600 px-4 py-2 text-sm font-semibold text-white transition hover:border-red-500 hover:bg-red-500 disabled:cursor-wait disabled:opacity-60" type="button" onClick={onConfirm} disabled={busy}>{busy ? 'Deleting...' : confirmLabel}</button>
        </div>
      </section>
    </div>
  );
}
