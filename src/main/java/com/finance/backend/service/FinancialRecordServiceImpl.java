package com.finance.backend.service;

import com.finance.backend.dto.FinancialRecordDto;
import com.finance.backend.dto.FinancialSummaryDto;
import com.finance.backend.entity.FinancialRecord;
import com.finance.backend.entity.FinancialType;
import com.finance.backend.entity.Role;
import com.finance.backend.entity.User;
import com.finance.backend.exception.InvalidInputException;
import com.finance.backend.exception.ResourceNotFoundException;
import com.finance.backend.repository.FinancialRecordRepository;
import com.finance.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class FinancialRecordServiceImpl implements FinancialRecordService {

    private final FinancialRecordRepository financialRecordRepository;
    private final UserRepository userRepository;

    public FinancialRecordServiceImpl(
            FinancialRecordRepository financialRecordRepository,
            UserRepository userRepository
    ) {
        this.financialRecordRepository = financialRecordRepository;
        this.userRepository = userRepository;
    }

    @Override
    public FinancialRecord createRecord(FinancialRecord record) {
        requireAdmin();
        record.setCategory(normalizeCategory(record.getCategory()));
        record.setDeleted(false);
        record.setCreatedBy(getCurrentUser());
        return financialRecordRepository.save(record);
    }

    @Override
    public FinancialRecord updateRecord(Long id, FinancialRecord record) {
        requireAdmin();
        FinancialRecord existing = financialRecordRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Financial record not found with id: " + id));

        existing.setAmount(record.getAmount());
        existing.setType(record.getType());
        existing.setCategory(normalizeCategory(record.getCategory()));
        existing.setDate(record.getDate());
        existing.setNotes(record.getNotes());

        return financialRecordRepository.save(existing);
    }

    @Override
    public void deleteRecord(Long id) {
        requireAdmin();
        FinancialRecord existing = financialRecordRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Financial record not found with id: " + id));
        existing.setDeleted(true);
        financialRecordRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FinancialRecord> listRecords(
            Optional<LocalDate> startDate,
            Optional<LocalDate> endDate,
            Optional<String> category,
            Optional<FinancialType> type,
            Optional<String> createdBy,
            Optional<String> keyword,
            Pageable pageable
    ) {
        requireReadAccess();
        validateDateRange(startDate, endDate);

        Pageable normalizedPageable = pageable.getSort().isSorted()
                ? pageable
                : PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by(Sort.Order.desc("date"), Sort.Order.desc("id"))
                );

        return financialRecordRepository.findAllByFilters(
                startDate.orElse(null),
                endDate.orElse(null),
                normalizeOptional(category),
                type.orElse(null),
                normalizeOptional(createdBy),
                normalizeOptional(keyword),
                normalizedPageable
        );
    }

    @Override
    @Transactional(readOnly = true)
    public FinancialSummaryDto getSummary() {
        requireReadAccess();

        List<FinancialRecord> allRecords = financialRecordRepository.findAllByDeletedFalse();
        BigDecimal totalIncome = sumByType(allRecords, FinancialType.INCOME);
        BigDecimal totalExpense = sumByType(allRecords, FinancialType.EXPENSE);

        Map<String, BigDecimal> categoryTotals = new LinkedHashMap<>();
        for (FinancialRecord record : allRecords) {
            BigDecimal signedAmount = record.getType() == FinancialType.INCOME
                    ? record.getAmount()
                    : record.getAmount().negate();
            categoryTotals.merge(record.getCategory(), signedAmount, BigDecimal::add);
        }

        FinancialSummaryDto summary = new FinancialSummaryDto();
        summary.setTotalIncome(totalIncome);
        summary.setTotalExpense(totalExpense);
        summary.setNetBalance(totalIncome.subtract(totalExpense));
        summary.setCategoryTotals(categoryTotals);
        summary.setRecentActivity(financialRecordRepository.findTop5ByDeletedFalseOrderByDateDescIdDesc().stream()
                .map(this::toDto)
                .toList());
        return summary;
    }

    private BigDecimal sumByType(List<FinancialRecord> records, FinancialType type) {
        return records.stream()
                .filter(record -> record.getType() == type)
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void requireReadAccess() {
        if (!(hasRole(Role.VIEWER) || hasRole(Role.ANALYST) || hasRole(Role.ADMIN))) {
            throw new AccessDeniedException("You do not have permission to view financial records");
        }
    }

    private void requireAdmin() {
        if (!hasRole(Role.ADMIN)) {
            throw new AccessDeniedException("Only administrators can modify financial records");
        }
    }

    private boolean hasRole(Role role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role.name()));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new AccessDeniedException("Authenticated user not found");
        }

        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + userDetails.getUsername()));
    }

    private FinancialRecordDto toDto(FinancialRecord record) {
        FinancialRecordDto dto = new FinancialRecordDto();
        dto.setAmount(record.getAmount());
        dto.setType(record.getType());
        dto.setCategory(record.getCategory());
        dto.setDate(record.getDate());
        dto.setNotes(record.getNotes());
        return dto;
    }

    private String normalizeCategory(String category) {
        return category == null ? null : category.trim();
    }

    private String normalizeOptional(Optional<String> value) {
        return value.map(String::trim).filter(item -> !item.isBlank()).orElse(null);
    }

    private void validateDateRange(Optional<LocalDate> startDate, Optional<LocalDate> endDate) {
        if (startDate.isPresent() && endDate.isPresent() && startDate.get().isAfter(endDate.get())) {
            throw new InvalidInputException("startDate must be before or equal to endDate");
        }
    }
}
