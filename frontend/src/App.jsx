
import { useState, useEffect } from 'react';

function App() {
  const [expenses, setExpenses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const [title, setTitle] = useState('');
  const [amount, setAmount] = useState('');
  const [category, setCategory] = useState('');
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    fetch('/api/expenses')
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to fetch expenses');
        }
        return response.json();
      })
      .then(data => setExpenses(data))
      .catch(() => setError('Could not load expenses'))
      .finally(() => setLoading(false));
  }, []);

  async function createExpense(event) {
    event.preventDefault();
    setError('');
    setSaving(true);

    try {
      const response = await fetch('/api/expenses', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          title: title.trim(),
          amount: Number(amount),
          category: category.trim(),
        }),
      });

      if (!response.ok) {
        throw new Error('Failed to create expense');
      }

      const newExpense = await response.json();

      setExpenses(previous => [...previous, newExpense]);

      setTitle('');
      setAmount('');
      setCategory('');
    } catch {
      setError('Could not create expense');
    } finally {
      setSaving(false);
    }
  }

  async function reviewExpense(id, action) {
    setError('');

    try {
      const response = await fetch(
        `/api/expenses/${id}/${action}`,
        { method: 'PATCH' }
      );

      if (!response.ok) {
        throw new Error(`Failed to ${action} expense`);
      }

      const updatedExpense = await response.json();

      setExpenses(previous =>
        previous.map(expense =>
          expense.id === id ? updatedExpense : expense
        )
      );
    } catch (error) {
      setError(error.message);
    }
  }

  return (
    <main style={{ padding: '30px' }}>
      <h1>PayTrack 💰</h1>
      <p>Expense Management Dashboard</p>

      <h2>Create Expense</h2>

      <form onSubmit={createExpense}>
        <input
          placeholder="Expense title"
          value={title}
          onChange={e => setTitle(e.target.value)}
          required
        />

        <input
          type="number"
          placeholder="Amount"
          value={amount}
          onChange={e => setAmount(e.target.value)}
          min="0.01"
          step="0.01"
          required
        />

        <input
          placeholder="Category"
          value={category}
          onChange={e => setCategory(e.target.value)}
          required
        />

        <button type="submit" disabled={saving}>
          {saving ? 'Saving...' : 'Add Expense'}
        </button>
      </form>

      <h2>Expenses</h2>

      {loading && <p>Loading expenses...</p>}
      {error && <p>{error}</p>}

      {!loading && expenses.length === 0 && (
        <p>No expenses found.</p>
      )}

      <ul>
        {expenses.map(expense => (
          <li key={expense.id}>
            {expense.title} — {expense.amount} — {expense.status}

            {expense.status === 'PENDING' && (
              <>
                <button onClick={() => reviewExpense(expense.id, 'approve')}>
                  Approve
                </button>

                <button onClick={() => reviewExpense(expense.id, 'reject')}>
                  Reject
                </button>
              </>
            )}
          </li>
        ))}
      </ul>
    </main>
  );
}

export default App;
