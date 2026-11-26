import DisplayCart from '../DisplayCart.jsx';

export default function CartPage({ cart, loading, error, pending, onRemove }) {
  return (
    <DisplayCart
      cart={cart}
      loading={loading}
      error={error}
      pending={pending}
      onRemove={onRemove}
    />
  );
}
