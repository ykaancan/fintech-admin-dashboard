package com.ykaancan.fintechadmindashboard.repository;

import com.ykaancan.fintechadmindashboard.entity.Transaction;
import com.ykaancan.fintechadmindashboard.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Page<Transaction> findByPortfolioId(UUID portfolioId, Pageable pageable);
    Page<Transaction> findByPortfolioIdAndType(UUID portfolioId, TransactionType type, Pageable pageable);
    Page<Transaction> findByPortfolioIdAndAssetId(UUID portfolioId, UUID assetId, Pageable pageable);
}
