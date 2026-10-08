package com.paytrack.expense;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private BigDecimal amount;

    private String category;

    @Enumerated(EnumType.STRING)
    private ExpenseStatus status;

    private LocalDateTime createdAt;


    // 🧱 Empty constructor
    // JPA/Hibernate needs this
    public Expense() {
    }


    // 🧱 Constructor for creating a new expense
    public Expense(String title, BigDecimal amount, String category) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.status = ExpenseStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }


    // 🔍 Getter for ID
    public Long getId() {
        return id;
    }


    // 🔍 Getter for title
    public String getTitle() {
        return title;
    }

    // ✏️ Setter for title
    public void setTitle(String title) {
        this.title = title;
    }


    // 🔍 Getter for amount
    public BigDecimal getAmount() {
        return amount;
    }

    // ✏️ Setter for amount
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    // 🔍 Getter for category
    public String getCategory() {
        return category;
    }

    // ✏️ Setter for category
    public void setCategory(String category) {
        this.category = category;
    }


    // 🔍 Getter for status
    public ExpenseStatus getStatus() {
        return status;
    }

    // ✏️ Setter for status
    public void setStatus(ExpenseStatus status) {
        this.status = status;
    }


    // 🔍 Getter for created time
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}