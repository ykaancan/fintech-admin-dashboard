package com.ykaancan.fintechadmindashboard.repository;

import com.ykaancan.fintechadmindashboard.entity.PriceHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, UUID> {
    Page<PriceHistory> findByAssetId(UUID assetId, Pageable pageable);
    List<PriceHistory> findByAssetIdAndRecordedAtBetween(
            UUID assetId, LocalDateTime start, LocalDateTime end);
}
