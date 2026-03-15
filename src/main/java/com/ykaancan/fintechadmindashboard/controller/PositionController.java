package com.ykaancan.fintechadmindashboard.controller;

import com.ykaancan.fintechadmindashboard.dto.position.CreatePositionRequest;
import com.ykaancan.fintechadmindashboard.dto.position.PositionResponse;
import com.ykaancan.fintechadmindashboard.dto.position.UpdatePositionRequest;
import com.ykaancan.fintechadmindashboard.service.PositionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/positions")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping
    public ResponseEntity<PositionResponse> createPosition(@Valid @RequestBody CreatePositionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(positionService.createPosition(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PositionResponse> getPositionById(@PathVariable UUID id) {
        return ResponseEntity.ok(positionService.getPositionById(id));
    }

    @GetMapping
    public ResponseEntity<List<PositionResponse>> getPositionsByPortfolioId(
            @RequestParam UUID portfolioId) {
        return ResponseEntity.ok(positionService.getPositionsByPortfolioId(portfolioId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PositionResponse> updatePosition(@PathVariable UUID id,
                                                           @Valid @RequestBody UpdatePositionRequest request) {
        return ResponseEntity.ok(positionService.updatePosition(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable UUID id) {
        positionService.deletePosition(id);
        return ResponseEntity.noContent().build();
    }
}
