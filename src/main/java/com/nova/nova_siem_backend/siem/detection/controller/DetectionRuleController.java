
package com.nova.nova_siem_backend.siem.detection.controller;

import com.nova.nova_siem_backend.siem.common.response.ApiResponse;
import com.nova.nova_siem_backend.siem.detection.dto.DetectionRuleRequestDTO;
import com.nova.nova_siem_backend.siem.detection.model.DetectionRule;
import com.nova.nova_siem_backend.siem.detection.service.DetectionRuleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/detection/rules")
public class DetectionRuleController {

    private final DetectionRuleService ruleService;

    public DetectionRuleController(DetectionRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SOC_L2', 'SOC_L3', 'COORDINATOR', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<DetectionRule>>> getAllRules() {
        List<DetectionRule> rules = ruleService.getAllRules();

        return ResponseEntity.ok(
                ApiResponse.success("Detection rules retrieved", rules)
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SOC_L2', 'SOC_L3', 'COORDINATOR', 'MANAGER')")
    public ResponseEntity<ApiResponse<DetectionRule>> createRule(
            @Valid @RequestBody DetectionRuleRequestDTO request) {

        DetectionRule newRule = ruleService.createRule(request);

        return ResponseEntity.ok(
                ApiResponse.success("Rule created successfully", newRule)
        );
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasAnyRole('SOC_L2', 'SOC_L3', 'COORDINATOR', 'MANAGER')")
    public ResponseEntity<ApiResponse<DetectionRule>> toggleRule(
            @PathVariable UUID id) {

        DetectionRule updated = ruleService.toggleRuleStatus(id);

        return ResponseEntity.ok(
                ApiResponse.success("Rule status updated", updated)
        );
    }

    @PostMapping("/reprocess")
    @PreAuthorize("hasAnyRole('SOC_L3', 'COORDINATOR', 'MANAGER')")
    public ResponseEntity<ApiResponse<String>> reprocessHistoricalEvents() {

        ruleService.reprocessHistoricalEvents();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Historical event reprocessing completed",
                        "Reprocessing requested successfully"
                )
        );
    }
}
