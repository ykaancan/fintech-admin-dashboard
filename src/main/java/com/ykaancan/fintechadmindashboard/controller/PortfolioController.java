package com.ykaancan.fintechadmindashboard.controller;

import com.ykaancan.fintechadmindashboard.dto.common.PagedResponse;
import com.ykaancan.fintechadmindashboard.dto.portfolio.CreatePortfolioRequest;
import com.ykaancan.fintechadmindashboard.dto.portfolio.PortfolioResponse;
import com.ykaancan.fintechadmindashboard.dto.portfolio.UpdatePortfolioRequest;
import com.ykaancan.fintechadmindashboard.service.PortfolioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @PostMapping
    public ResponseEntity<PortfolioResponse> createPortfolio(@Valid @RequestBody CreatePortfolioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(portfolioService.createPortfolio(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioResponse> getPortfolioById(@PathVariable UUID id) {
        return ResponseEntity.ok(portfolioService.getPortfolioById(id));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<PortfolioResponse>> getAllPortfolios(
            @RequestParam(required = false) UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(page, size, sort);

        if (userId != null) {
            return ResponseEntity.ok(portfolioService.getPortfoliosByUserId(userId, pageable));
        }
        return ResponseEntity.ok(portfolioService.getAllPortfolios(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PortfolioResponse> updatePortfolio(@PathVariable UUID id,
                                                             @Valid @RequestBody UpdatePortfolioRequest request) {
        return ResponseEntity.ok(portfolioService.updatePortfolio(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable UUID id) {
        portfolioService.deletePortfolio(id);
        return ResponseEntity.noContent().build();
    }
}
