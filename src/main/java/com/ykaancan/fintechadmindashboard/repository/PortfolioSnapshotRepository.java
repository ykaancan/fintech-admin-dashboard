package com.ykaancan.fintechadmindashboard.repository;

import com.ykaancan.fintechadmindashboard.entity.PortfolioSnapshot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PortfolioSnapshotRepository extends JpaRepository<PortfolioSnapshot, UUID> {
    Page<PortfolioSnapshot> findByPortfolioId(UUID portfolioId, Pageable pageable);
    List<PortfolioSnapshot> findByPortfolioIdAndSnapshotDateBetween(
            UUID portfolioId, LocalDate startDate, LocalDate endDate);
    List<PortfolioSnapshot> findByPortfolioIdAndSnapshotDateBetweenOrderBySnapshotDateAsc(
            UUID portfolioId, LocalDate startDate, LocalDate endDate);
    Optional<PortfolioSnapshot> findByPortfolioIdAndSnapshotDate(
            UUID portfolioId, LocalDate snapshotDate);
}
