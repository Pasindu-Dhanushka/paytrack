package com.paytrack.expense;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
}