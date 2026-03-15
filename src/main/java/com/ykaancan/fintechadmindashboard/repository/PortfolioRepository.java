package com.ykaancan.fintechadmindashboard.repository;

import com.ykaancan.fintechadmindashboard.entity.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {
    Page<Portfolio> findByUserId(UUID userId, Pageable pageable);
    List<Portfolio> findAllByUserId(UUID userId);
}
