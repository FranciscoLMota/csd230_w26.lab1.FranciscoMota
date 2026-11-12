import { useEffect } from 'react';

export default function FormModal({ isOpen, onClose, children }) {
  useEffect(() => {
    if (!isOpen) return undefined;
    function handleKeyDown(event) {
      if (event.key === 'Escape') onClose();
    }
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [isOpen, onClose]);

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 grid place-items-center overflow-y-auto bg-black/70 p-4 backdrop-blur-[2px]" role="presentation" onMouseDown={event => { if (event.target === event.currentTarget) onClose(); }}>
      <div className="relative my-auto w-full max-w-xl shadow-2xl" role="dialog" aria-modal="true">
        <button className="absolute top-4 right-4 z-10 grid size-8 cursor-pointer place-items-center border border-zinc-700 bg-zinc-900 text-lg text-zinc-400 transition hover:border-zinc-500 hover:text-white" type="button" onClick={onClose} aria-label="Close form">×</button>
        {children}
      </div>
    </div>
  );
}
