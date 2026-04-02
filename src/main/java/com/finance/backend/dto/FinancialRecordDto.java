package com.finance.backend.dto;

import com.finance.backend.entity.FinancialType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class FinancialRecordDto {

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotNull
    private FinancialType type;

    @NotBlank
    private String category;

    @NotNull
    private LocalDate date;

    private String notes;

    public FinancialRecordDto() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public FinancialType getType() {
        return type;
    }

    public void setType(FinancialType type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FinancialRecordDto that = (FinancialRecordDto) o;
        return Objects.equals(amount, that.amount)
                && type == that.type
                && Objects.equals(category, that.category)
                && Objects.equals(date, that.date)
                && Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, type, category, date, notes);
    }
}
