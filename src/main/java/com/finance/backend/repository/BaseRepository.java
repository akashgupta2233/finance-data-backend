package com.finance.backend.repository;

import com.finance.backend.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseRepository extends JpaRepository<BaseEntity, Long> {
    // Base repository placeholder
}
