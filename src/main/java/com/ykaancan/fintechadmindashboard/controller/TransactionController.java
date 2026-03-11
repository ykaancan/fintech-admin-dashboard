package com.ykaancan.fintechadmindashboard.controller;

import com.ykaancan.fintechadmindashboard.dto.common.PagedResponse;
import com.ykaancan.fintechadmindashboard.dto.transaction.CreateTransactionRequest;
import com.ykaancan.fintechadmindashboard.dto.transaction.TransactionResponse;
import com.ykaancan.fintechadmindashboard.enums.TransactionType;
import com.ykaancan.fintechadmindashboard.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTransaction(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable UUID id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<TransactionResponse>> getTransactions(
            @RequestParam UUID portfolioId,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) UUID assetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "executedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(page, size, sort);

        if (type != null) {
            return ResponseEntity.ok(transactionService.getTransactionsByPortfolioIdAndType(portfolioId, type, pageable));
        }
        if (assetId != null) {
            return ResponseEntity.ok(transactionService.getTransactionsByPortfolioIdAndAssetId(portfolioId, assetId, pageable));
        }
        return ResponseEntity.ok(transactionService.getTransactionsByPortfolioId(portfolioId, pageable));
    }
}
