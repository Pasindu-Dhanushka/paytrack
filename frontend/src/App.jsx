
import { useState } from 'react';

function App() {
  const [password, setPassword] = useState('');
  const [authHeader, setAuthHeader] = useState('');
  const [expenses, setExpenses] = useState([]);
  const [error, setError] = useState('');

  const [title, setTitle] = useState('');
  const [amount, setAmount] = useState('');
  const [category, setCategory] = useState('');

  async function login(event) {
    event.preventDefault();
    setError('');

    const credentials = 'Basic ' + btoa('admin:' + password);

    try {
      const response = await fetch('/api/expenses', {
        headers: { Authorization: credentials }
      });

      if (!response.ok) {
        throw new Error('Invalid username or password');
      }

      setExpenses(await response.json());
      setAuthHeader(credentials);
      setPassword('');
    } catch (err) {
      setError(err.message);
    }
  }

  async function createExpense(event) {
    event.preventDefault();
    setError('');

    try {
      const response = await fetch('/api/expenses', {
        method: 'POST',
        headers: {
          Authorization: authHeader,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          title: title.trim(),
          amount: Number(amount),
          category: category.trim()
        })
      });

      if (!response.ok) throw new Error('Failed to create expense');

      const created = await response.json();
      setExpenses(previous => [...previous, created]);
      setTitle('');
      setAmount('');
      setCategory('');
    } catch (err) {
      setError(err.message);
    }
  }

  async function reviewExpense(id, action) {
    setError('');

    try {
      const response = await fetch(
        `/api/expenses/${id}/${action}`,
        {
          method: 'PATCH',
          headers: { Authorization: authHeader }
        }
      );

      if (!response.ok) throw new Error('Failed to update expense');

      const updated = await response.json();
      setExpenses(previous =>
        previous.map(expense =>
          expense.id === id ? updated : expense
        )
      );
    } catch (err) {
      setError(err.message);
    }
  }

  if (!authHeader) {
    return (
      <main style={{ padding: 30 }}>
        <h1>PayTrack 💰</h1>
        <h2>Admin Login</h2>

        <form onSubmit={login}>
          <input
            type="password"
            placeholder="Admin password"
            value={password}
            onChange={e => setPassword(e.target.value)}
            required
          />
          <button type="submit">Login</button>
        </form>

        {error && <p>{error}</p>}
      </main>
    );
  }

  return (
    <main style={{ padding: 30 }}>
      <h1>PayTrack 💰</h1>
      <p>Expense Management Dashboard</p>

      <button onClick={() => {
        setAuthHeader('');
        setExpenses([]);
      }}>
        Leave Dashboard
      </button>

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
          min="0.01"
          step="0.01"
          value={amount}
          onChange={e => setAmount(e.target.value)}
          required
        />

        <input
          placeholder="Category"
          value={category}
          onChange={e => setCategory(e.target.value)}
          required
        />

        <button type="submit">Add Expense</button>
      </form>

      <h2>Expenses</h2>
      {error && <p>{error}</p>}

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
