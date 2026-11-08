package com.nova.nova_siem_backend.siem.investigations.controller;

import com.nova.nova_siem_backend.siem.common.exception.BusinessException;
import com.nova.nova_siem_backend.siem.common.response.ApiResponse;
import com.nova.nova_siem_backend.siem.investigations.model.Investigation;
import com.nova.nova_siem_backend.siem.investigations.model.InvestigationVerdict;
import com.nova.nova_siem_backend.siem.investigations.dto.TimelineDTO;
import com.nova.nova_siem_backend.siem.investigations.service.GraphAnalysisService;
import com.nova.nova_siem_backend.siem.investigations.service.InvestigationService;
import com.nova.nova_siem_backend.siem.investigations.service.TimelineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/investigations")
public class InvestigationController {

    private final InvestigationService investigationService;

    private final GraphAnalysisService graphAnalysisService;
    private final TimelineService timelineService;

    public InvestigationController(InvestigationService investigationService,
                                   GraphAnalysisService graphAnalysisService,
                                   TimelineService timelineService) {
        this.investigationService = investigationService;
        this.graphAnalysisService = graphAnalysisService;
        this.timelineService = timelineService;
    }

    @GetMapping("/{id}/graph")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getGraph(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Investigation graph generated",
                graphAnalysisService.generateIncidentGraph(id)));
    }

    @GetMapping("/{id}/timeline")
    public ResponseEntity<ApiResponse<List<TimelineDTO>>> getTimeline(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Investigation timeline generated",
                timelineService.generateInvestigationTimeline(id)));
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

        if (payload == null
                || payload.get("verdict") == null
                || payload.get("confidenceScore") == null
                || payload.get("recommendedAction") == null) {
            throw new BusinessException(
                    "verdict, confidenceScore and recommendedAction are required.");
        }

        InvestigationVerdict verdict;
        Integer confidenceScore;

        try {
            verdict = InvestigationVerdict.valueOf(
                    payload.get("verdict").toString().trim().toUpperCase());
            confidenceScore = Integer.parseInt(
                    payload.get("confidenceScore").toString().trim());
        } catch (IllegalArgumentException ex) {
            throw new BusinessException("Invalid verdict or confidenceScore value.");
        }

        if (confidenceScore < 0 || confidenceScore > 100) {
            throw new BusinessException("confidenceScore must be between 0 and 100.");
        }

        String recommendedAction = payload.get("recommendedAction").toString();

        Investigation updated = investigationService.submitVerdict(id, verdict, confidenceScore, recommendedAction);
        return ResponseEntity.ok(ApiResponse.success("Verdict submitted successfully", updated));
    }
}