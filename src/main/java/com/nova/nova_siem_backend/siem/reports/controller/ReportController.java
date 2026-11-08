package com.nova.nova_siem_backend.siem.reports.controller;

import com.nova.nova_siem_backend.siem.common.exception.BusinessException;
import com.nova.nova_siem_backend.siem.common.response.ApiResponse;
import com.nova.nova_siem_backend.siem.reports.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<?>> generateReport(
            @RequestBody Map<String, String> payload,
            Authentication authentication) {

        if (payload == null) {
            throw new BusinessException("Request body is required.");
        }
        String type = payload.get("type");
        String format = payload.get("format");
        String author = authentication != null ? authentication.getName() : "System";

        Map<String, Object> generatedReport;

        if ("Incident Summary".equalsIgnoreCase(type)) {
            String rawIncidentId = payload.get("incidentId");
            if (rawIncidentId == null || rawIncidentId.isBlank()) {
                throw new BusinessException("incidentId is required for an Incident Summary report.");
            }
            UUID incidentId;
            try {
                incidentId = UUID.fromString(rawIncidentId.trim());
            } catch (IllegalArgumentException ex) {
                throw new BusinessException("incidentId is not a valid UUID.");
            }
            generatedReport = reportService.generateIncidentReport(incidentId, format, author);
        } else {
            generatedReport = reportService.generateExecutiveBrief(format);
        }

        return ResponseEntity.ok(ApiResponse.success("Report generated successfully", generatedReport));
    }
}