package com.nova.nova_siem_backend.siem.alerts.controller;

import com.nova.nova_siem_backend.siem.alerts.model.AlertStatus;
import com.nova.nova_siem_backend.siem.alerts.model.SecurityAlert;
import com.nova.nova_siem_backend.siem.alerts.service.AlertService;
import com.nova.nova_siem_backend.siem.common.exception.BusinessException;
import com.nova.nova_siem_backend.siem.common.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SecurityAlert>>> getAllAlerts(
            @PageableDefault(size = 200, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<SecurityAlert> alerts = alertService.getAllAlerts(pageable);
        return ResponseEntity.ok(ApiResponse.success("Alerts retrieved successfully", alerts));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<SecurityAlert>> updateAlertStatus(
            @PathVariable UUID id,
            @RequestBody Map<String, String> payload,
            Authentication authentication) {

        String rawStatus = payload != null ? payload.get("status") : null;

        if (rawStatus == null || rawStatus.isBlank()) {
            throw new BusinessException("The 'status' field is required.");
        }

        AlertStatus status;

        try {
            status = AlertStatus.valueOf(rawStatus.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessException("Unsupported alert status: " + rawStatus);
        }
        String username = authentication != null ? authentication.getName() : "System";

        SecurityAlert updatedAlert = alertService.updateAlertStatus(id, status, username);
        return ResponseEntity.ok(ApiResponse.success("Alert status updated", updatedAlert));
    }
}