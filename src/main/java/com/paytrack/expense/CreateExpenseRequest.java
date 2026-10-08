package com.paytrack.expense;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public record CreateExpenseRequest(
    @NotBlank String title,
    @NotNull @DecimalMin("0.01") BigDecimal amount,
    @NotBlank String category
) {}