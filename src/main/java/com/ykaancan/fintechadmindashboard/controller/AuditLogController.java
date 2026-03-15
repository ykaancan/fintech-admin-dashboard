package com.ykaancan.fintechadmindashboard.controller;

import com.ykaancan.fintechadmindashboard.dto.auditlog.AuditLogResponse;
import com.ykaancan.fintechadmindashboard.dto.common.PagedResponse;
import com.ykaancan.fintechadmindashboard.service.AuditLogService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLogResponse> getAuditLogById(@PathVariable UUID id) {
        return ResponseEntity.ok(auditLogService.getAuditLogById(id));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<AuditLogResponse>> getAuditLogs(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) UUID entityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(page, size, sort);

        if (userId != null) {
            return ResponseEntity.ok(auditLogService.getAuditLogsByUserId(userId, pageable));
        }
        if (entityType != null && entityId != null) {
            return ResponseEntity.ok(auditLogService.getAuditLogsByEntity(entityType, entityId, pageable));
        }
        return ResponseEntity.ok(auditLogService.getAllAuditLogs(pageable));
    }
}
