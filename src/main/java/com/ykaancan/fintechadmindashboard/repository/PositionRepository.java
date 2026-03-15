package com.ykaancan.fintechadmindashboard.repository;

import com.ykaancan.fintechadmindashboard.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PositionRepository extends JpaRepository<Position, UUID> {
    List<Position> findByPortfolioId(UUID portfolioId);
    Optional<Position> findByPortfolioIdAndAssetId(UUID portfolioId, UUID assetId);
    void deleteAllByPortfolioId(UUID portfolioId);
}
