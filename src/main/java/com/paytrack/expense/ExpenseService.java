package com.paytrack.expense;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense createExpense(
            String title,
            BigDecimal amount,
            String category) {

        Expense expense = new Expense(title, amount, category);

        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense approveExpense(Long id) {
        Expense expense = findPendingExpense(id);

        expense.approve();

        return expenseRepository.save(expense);
    }

    public Expense rejectExpense(Long id) {
        Expense expense = findPendingExpense(id);

        expense.reject();

        return expenseRepository.save(expense);
    }

    private Expense findPendingExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Expense not found"
            ));

        if (expense.getStatus() != ExpenseStatus.PENDING) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Only pending expenses can be reviewed"
            );
        }

        return expense;
    }
}