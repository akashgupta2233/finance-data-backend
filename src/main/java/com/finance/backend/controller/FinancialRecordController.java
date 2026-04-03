package com.finance.backend.controller;

import com.finance.backend.dto.FinancialRecordDto;
import com.finance.backend.dto.FinancialSummaryDto;
import com.finance.backend.entity.FinancialRecord;
import com.finance.backend.entity.FinancialType;
import com.finance.backend.service.FinancialRecordService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/financial-records")
public class FinancialRecordController {

    private final FinancialRecordService financialRecordService;

    public FinancialRecordController(FinancialRecordService financialRecordService) {
        this.financialRecordService = financialRecordService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FinancialRecordDto> createRecord(@Valid @RequestBody FinancialRecordDto request) {
        FinancialRecord created = financialRecordService.createRecord(toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public Page<FinancialRecordDto> listRecords(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate,
            @RequestParam Optional<String> category,
            @RequestParam Optional<FinancialType> type,
            @RequestParam Optional<String> createdBy,
            @RequestParam Optional<String> keyword,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return financialRecordService.listRecords(startDate, endDate, category, type, createdBy, keyword, pageable)
                .map(this::toDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FinancialRecordDto> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody FinancialRecordDto request
    ) {
        FinancialRecord updated = financialRecordService.updateRecord(id, toEntity(request));
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        financialRecordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public ResponseEntity<FinancialSummaryDto> getSummary() {
        return ResponseEntity.ok(financialRecordService.getSummary());
    }

    private FinancialRecord toEntity(FinancialRecordDto dto) {
        FinancialRecord record = new FinancialRecord();
        record.setAmount(dto.getAmount());
        record.setType(dto.getType());
        record.setCategory(dto.getCategory());
        record.setDate(dto.getDate());
        record.setDescription(dto.getDescription());
        return record;
    }

    private FinancialRecordDto toDto(FinancialRecord record) {
        FinancialRecordDto dto = new FinancialRecordDto();
        dto.setId(record.getId());
        dto.setAmount(record.getAmount());
        dto.setType(record.getType());
        dto.setCategory(record.getCategory());
        dto.setDate(record.getDate());
        dto.setDescription(record.getDescription());
        return dto;
    }
}
