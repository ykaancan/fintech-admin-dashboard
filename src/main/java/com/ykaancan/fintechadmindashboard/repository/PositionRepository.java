package com.ykaancan.fintechadmindashboard.repository;

import com.ykaancan.fintechadmindashboard.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PositionRepository extends JpaRepository<Position, UUID> {
    List<Position> findByPortfolioId(UUID portfolioId);

    @Query("SELECT p FROM Position p JOIN FETCH p.asset WHERE p.portfolio.id = :portfolioId")
    List<Position> findByPortfolioIdWithAsset(@Param("portfolioId") UUID portfolioId);
    Optional<Position> findByPortfolioIdAndAssetId(UUID portfolioId, UUID assetId);
    void deleteAllByPortfolioId(UUID portfolioId);
}
