package com.finance.backend.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FinancialSummaryDto {

    private BigDecimal totalIncome = BigDecimal.ZERO;
    private BigDecimal totalExpense = BigDecimal.ZERO;
    private BigDecimal netBalance = BigDecimal.ZERO;
    private Map<String, BigDecimal> categoryTotals = new LinkedHashMap<>();
    private List<FinancialRecordDto> recentActivity = new ArrayList<>();

    public FinancialSummaryDto() {
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getNetBalance() {
        return netBalance;
    }

    public void setNetBalance(BigDecimal netBalance) {
        this.netBalance = netBalance;
    }

    public Map<String, BigDecimal> getCategoryTotals() {
        return categoryTotals;
    }

    public void setCategoryTotals(Map<String, BigDecimal> categoryTotals) {
        this.categoryTotals = categoryTotals;
    }

    public List<FinancialRecordDto> getRecentActivity() {
        return recentActivity;
    }

    public void setRecentActivity(List<FinancialRecordDto> recentActivity) {
        this.recentActivity = recentActivity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FinancialSummaryDto that = (FinancialSummaryDto) o;
        return Objects.equals(totalIncome, that.totalIncome)
                && Objects.equals(totalExpense, that.totalExpense)
                && Objects.equals(netBalance, that.netBalance)
                && Objects.equals(categoryTotals, that.categoryTotals)
                && Objects.equals(recentActivity, that.recentActivity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalIncome, totalExpense, netBalance, categoryTotals, recentActivity);
    }
}
