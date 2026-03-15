package com.ykaancan.fintechadmindashboard.controller;

import com.ykaancan.fintechadmindashboard.dto.dashboard.AssetPnlResponse;
import com.ykaancan.fintechadmindashboard.dto.dashboard.DailyPnlResponse;
import com.ykaancan.fintechadmindashboard.dto.dashboard.PortfolioDashboardResponse;
import com.ykaancan.fintechadmindashboard.service.DashboardService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portfolios/{portfolioId}/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<PortfolioDashboardResponse> getPortfolioDashboard(
            @PathVariable UUID portfolioId) {
        return ResponseEntity.ok(dashboardService.getPortfolioDashboard(portfolioId));
    }

    @GetMapping("/asset-pnl")
    public ResponseEntity<List<AssetPnlResponse>> getAssetPnlBreakdown(
            @PathVariable UUID portfolioId) {
        return ResponseEntity.ok(dashboardService.getAssetPnlBreakdown(portfolioId));
    }

    @GetMapping("/daily-pnl")
    public ResponseEntity<List<DailyPnlResponse>> getDailyPnl(
            @PathVariable UUID portfolioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(dashboardService.getDailyPnl(portfolioId, from, to));
    }
}
