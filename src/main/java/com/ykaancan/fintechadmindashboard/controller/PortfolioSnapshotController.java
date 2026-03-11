package com.ykaancan.fintechadmindashboard.controller;

import com.ykaancan.fintechadmindashboard.dto.common.PagedResponse;
import com.ykaancan.fintechadmindashboard.dto.portfoliosnapshot.CreatePortfolioSnapshotRequest;
import com.ykaancan.fintechadmindashboard.dto.portfoliosnapshot.PortfolioSnapshotResponse;
import com.ykaancan.fintechadmindashboard.service.PortfolioSnapshotService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portfolio-snapshots")
public class PortfolioSnapshotController {

    private final PortfolioSnapshotService snapshotService;

    public PortfolioSnapshotController(PortfolioSnapshotService snapshotService) {
        this.snapshotService = snapshotService;
    }

    @PostMapping
    public ResponseEntity<PortfolioSnapshotResponse> createSnapshot(
            @Valid @RequestBody CreatePortfolioSnapshotRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(snapshotService.createSnapshot(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioSnapshotResponse> getSnapshotById(@PathVariable UUID id) {
        return ResponseEntity.ok(snapshotService.getSnapshotById(id));
    }

    @GetMapping
    public ResponseEntity<?> getSnapshots(
            @RequestParam UUID portfolioId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "snapshotDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        if (startDate != null && endDate != null) {
            List<PortfolioSnapshotResponse> results =
                    snapshotService.getSnapshotsByPortfolioIdAndDateRange(portfolioId, startDate, endDate);
            return ResponseEntity.ok(results);
        }

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(snapshotService.getSnapshotsByPortfolioId(portfolioId, pageable));
    }
}
