package com.ykaancan.fintechadmindashboard.service;

import com.ykaancan.fintechadmindashboard.dto.common.PagedResponse;
import com.ykaancan.fintechadmindashboard.dto.portfoliosnapshot.CreatePortfolioSnapshotRequest;
import com.ykaancan.fintechadmindashboard.dto.portfoliosnapshot.PortfolioSnapshotResponse;
import com.ykaancan.fintechadmindashboard.entity.Portfolio;
import com.ykaancan.fintechadmindashboard.entity.PortfolioSnapshot;
import com.ykaancan.fintechadmindashboard.exception.DuplicateResourceException;
import com.ykaancan.fintechadmindashboard.exception.ResourceNotFoundException;
import com.ykaancan.fintechadmindashboard.repository.PortfolioRepository;
import com.ykaancan.fintechadmindashboard.repository.PortfolioSnapshotRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PortfolioSnapshotService {

    private final PortfolioSnapshotRepository snapshotRepository;
    private final PortfolioRepository portfolioRepository;

    public PortfolioSnapshotService(PortfolioSnapshotRepository snapshotRepository,
                                    PortfolioRepository portfolioRepository) {
        this.snapshotRepository = snapshotRepository;
        this.portfolioRepository = portfolioRepository;
    }

    @Transactional
    public PortfolioSnapshotResponse createSnapshot(CreatePortfolioSnapshotRequest request) {
        Portfolio portfolio = portfolioRepository.findById(request.portfolioId())
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", request.portfolioId()));

        snapshotRepository.findByPortfolioIdAndSnapshotDate(request.portfolioId(), request.snapshotDate())
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("PortfolioSnapshot", "portfolio+date",
                            request.portfolioId() + "+" + request.snapshotDate());
                });

        PortfolioSnapshot snapshot = PortfolioSnapshot.builder()
                .portfolio(portfolio)
                .totalValue(request.totalValue())
                .totalCost(request.totalCost())
                .profitLoss(request.profitLoss())
                .snapshotDate(request.snapshotDate())
                .build();

        return mapToResponse(snapshotRepository.save(snapshot));
    }

    @Transactional(readOnly = true)
    public PortfolioSnapshotResponse getSnapshotById(UUID id) {
        PortfolioSnapshot snapshot = snapshotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PortfolioSnapshot", "id", id));
        return mapToResponse(snapshot);
    }

    @Transactional(readOnly = true)
    public PagedResponse<PortfolioSnapshotResponse> getSnapshotsByPortfolioId(UUID portfolioId, Pageable pageable) {
        Page<PortfolioSnapshot> page = snapshotRepository.findByPortfolioId(portfolioId, pageable);
        return PagedResponse.from(page, page.getContent().stream().map(this::mapToResponse).toList());
    }

    @Transactional(readOnly = true)
    public List<PortfolioSnapshotResponse> getSnapshotsByPortfolioIdAndDateRange(
            UUID portfolioId, LocalDate start, LocalDate end) {
        return snapshotRepository.findByPortfolioIdAndSnapshotDateBetween(portfolioId, start, end)
                .stream().map(this::mapToResponse).toList();
    }

    private PortfolioSnapshotResponse mapToResponse(PortfolioSnapshot snapshot) {
        return new PortfolioSnapshotResponse(
                snapshot.getId(),
                snapshot.getPortfolio().getId(),
                snapshot.getTotalValue(),
                snapshot.getTotalCost(),
                snapshot.getProfitLoss(),
                snapshot.getSnapshotDate(),
                snapshot.getCreatedAt()
        );
    }
}
