package com.finance.backend.repository;

import com.finance.backend.entity.FinancialRecord;
import com.finance.backend.entity.FinancialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialRecordRepository extends BaseRepository<FinancialRecord, Long> {

    Optional<FinancialRecord> findByIdAndDeletedFalse(Long id);

    List<FinancialRecord> findAllByDeletedFalse();

    List<FinancialRecord> findTop5ByDeletedFalseOrderByDateDescIdDesc();

    @Query(
            value = """
            select fr
            from FinancialRecord fr
            join fr.createdBy creator
            where fr.deleted = false
              and (:startDate is null or fr.date >= :startDate)
              and (:endDate is null or fr.date <= :endDate)
              and (:category is null or lower(fr.category) = lower(cast(:category as string)))
              and (:type is null or fr.type = :type)
              and (:createdBy is null or lower(creator.username) = lower(cast(:createdBy as string)))
              and (
                    :keyword is null
                    or lower(fr.category) like lower(concat('%', cast(:keyword as string), '%'))
                    or lower(coalesce(fr.notes, '')) like lower(concat('%', cast(:keyword as string), '%'))
              )
            order by fr.date desc, fr.id desc
            """,
            countQuery = """
            select count(fr)
            from FinancialRecord fr
            join fr.createdBy creator
            where fr.deleted = false
              and (:startDate is null or fr.date >= :startDate)
              and (:endDate is null or fr.date <= :endDate)
              and (:category is null or lower(fr.category) = lower(cast(:category as string)))
              and (:type is null or fr.type = :type)
              and (:createdBy is null or lower(creator.username) = lower(cast(:createdBy as string)))
              and (
                    :keyword is null
                    or lower(fr.category) like lower(concat('%', cast(:keyword as string), '%'))
                    or lower(coalesce(fr.notes, '')) like lower(concat('%', cast(:keyword as string), '%'))
              )
            """
    )
    Page<FinancialRecord> findAllByFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("category") String category,
            @Param("type") FinancialType type,
            @Param("createdBy") String createdBy,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
