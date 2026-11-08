package com.nova.nova_siem_backend.siem.dashboard.controller;

import com.nova.nova_siem_backend.siem.common.response.ApiResponse;
import com.nova.nova_siem_backend.siem.dashboard.dto.DashboardSummaryDTO;
import com.nova.nova_siem_backend.siem.dashboard.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<DashboardSummaryDTO>> getDashboardSummary() {
        DashboardSummaryDTO summary = dashboardService.getSummaryMetrics();
        return ResponseEntity.ok(ApiResponse.success("Dashboard metrics retrieved", summary));
    }
}