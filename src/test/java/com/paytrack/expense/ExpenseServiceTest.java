package com.paytrack.expense;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;

class ExpenseServiceTest {

    @Test
    void shouldCreateExpenseWithPendingStatus() {

        // Create a fake repository
        ExpenseRepository repository = mock(ExpenseRepository.class);

        // Create service using fake repository
        ExpenseService service = new ExpenseService(repository);

        // Create an expense
        service.createExpense(
            "Taxi",
            new BigDecimal("2500"),
            "Travel"
        );

        // Check what was passed to the repository
        var captor = org.mockito.ArgumentCaptor.forClass(Expense.class);
        verify(repository).save(captor.capture());

        Expense expense = captor.getValue();

        // Verify the values
        assertEquals("Taxi", expense.getTitle());
        assertEquals(new BigDecimal("2500"), expense.getAmount());
        assertEquals(ExpenseStatus.PENDING, expense.getStatus());
    }

    @Test
    void shouldApprovePendingExpense() {

        // Create a fake repository
        ExpenseRepository repository = mock(ExpenseRepository.class);

        ExpenseService service = new ExpenseService(repository);

        // Create a pending expense
        Expense expense = new Expense(
            "Taxi",
            new BigDecimal("1500"),
            "TRAVEL"
        );

        // Tell Mockito what to return
        when(repository.findById(1L))
            .thenReturn(Optional.of(expense));

        when(repository.save(expense))
            .thenReturn(expense);

        // Execute approval
        Expense result = service.approveExpense(1L);

        // Verify result
        assertEquals(ExpenseStatus.APPROVED, result.getStatus());

        verify(repository).save(expense);
    }

    @Test
    void shouldRejectPendingExpense() {

        ExpenseRepository repository = mock(ExpenseRepository.class);

        ExpenseService service = new ExpenseService(repository);

        Expense expense = new Expense(
            "Lunch",
            new BigDecimal("800"),
            "FOOD"
        );

        when(repository.findById(2L))
            .thenReturn(Optional.of(expense));

        when(repository.save(expense))
            .thenReturn(expense);

        Expense result = service.rejectExpense(2L);

        assertEquals(ExpenseStatus.REJECTED, result.getStatus());

        verify(repository).save(expense);
    }

}