import { NavLink } from 'react-router';

export default function SideMenu({ pages }) {
  return (
    <aside className="sticky top-0 z-10 border-b border-zinc-800 bg-[#111113] px-4 py-2.5 min-[801px]:static min-[801px]:border-r min-[801px]:border-b-0 min-[801px]:px-3.5 min-[801px]:py-7" aria-label="Catalog pages">
      <div className="hidden gap-0.5 border-b border-zinc-800 px-3 pb-5 min-[801px]:grid">
        <span className="text-sm font-semibold text-zinc-200">Catalog</span>
        <small className="text-[0.72rem] text-zinc-500">Browse products</small>
      </div>
      <nav className="flex gap-1 min-[801px]:grid min-[801px]:pt-4">
        {pages.map(page => (
          <NavLink
            className={({ isActive }) => `flex min-w-[110px] items-center gap-3 border px-3 py-2.5 text-left text-sm transition min-[801px]:w-full ${isActive ? 'border-indigo-500/50 bg-indigo-600/10 text-indigo-300' : 'border-transparent text-zinc-400 hover:border-zinc-800 hover:bg-zinc-900 hover:text-zinc-100'}`}
            key={page.id}
            to={page.path}
          >
            <span className="grid size-6 place-items-center border border-current text-[0.68rem] font-bold" aria-hidden="true">{page.id.charAt(0).toUpperCase()}</span>
            <span>{page.label}</span>
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}
