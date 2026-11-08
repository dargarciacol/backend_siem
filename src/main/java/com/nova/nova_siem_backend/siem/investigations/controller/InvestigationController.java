package com.nova.nova_siem_backend.siem.investigations.controller;

import com.nova.nova_siem_backend.siem.common.response.ApiResponse;
import com.nova.nova_siem_backend.siem.investigations.model.Investigation;
import com.nova.nova_siem_backend.siem.investigations.model.InvestigationVerdict;
import com.nova.nova_siem_backend.siem.investigations.service.InvestigationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/investigations")
public class InvestigationController {

    private final InvestigationService investigationService;

    public InvestigationController(InvestigationService investigationService) {
        this.investigationService = investigationService;
    }

    @GetMapping("/incident/{incidentId}")
    public ResponseEntity<ApiResponse<Investigation>> getWorkspaceByIncidentId(@PathVariable UUID incidentId) {
        Investigation inv = investigationService.getOrCreateInvestigation(incidentId);
        return ResponseEntity.ok(ApiResponse.success("Investigation workspace retrieved", inv));
    }

    @PostMapping("/{id}/verdict")
    public ResponseEntity<ApiResponse<Investigation>> submitVerdict(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> payload) {

        InvestigationVerdict verdict = InvestigationVerdict.valueOf(payload.get("verdict").toString());
        Integer confidenceScore = Integer.parseInt(payload.get("confidenceScore").toString());
        String recommendedAction = payload.get("recommendedAction").toString();

        Investigation updated = investigationService.submitVerdict(id, verdict, confidenceScore, recommendedAction);
        return ResponseEntity.ok(ApiResponse.success("Verdict submitted successfully", updated));
    }
}