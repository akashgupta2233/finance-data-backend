package com.finance.backend.service;

import com.finance.backend.dto.FinancialSummaryDto;
import com.finance.backend.entity.FinancialRecord;
import com.finance.backend.entity.FinancialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface FinancialRecordService {

    FinancialRecord createRecord(FinancialRecord record);

    FinancialRecord updateRecord(Long id, FinancialRecord record);

    void deleteRecord(Long id);

    Page<FinancialRecord> listRecords(
            Optional<LocalDate> startDate,
            Optional<LocalDate> endDate,
            Optional<String> category,
            Optional<FinancialType> type,
            Optional<String> createdBy,
            Optional<String> keyword,
            Pageable pageable
    );

    FinancialSummaryDto getSummary();
}
