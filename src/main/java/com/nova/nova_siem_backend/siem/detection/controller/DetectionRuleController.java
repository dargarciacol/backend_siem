
package com.nova.nova_siem_backend.siem.detection.controller;

import com.nova.nova_siem_backend.siem.common.response.ApiResponse;
import com.nova.nova_siem_backend.siem.detection.model.DetectionRule;
import com.nova.nova_siem_backend.siem.detection.service.DetectionRuleService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<DetectionRule>>> getAllRules() {
        List<DetectionRule> rules = ruleService.getAllRules();

        return ResponseEntity.ok(
                ApiResponse.success("Detection rules retrieved", rules)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DetectionRule>> createRule(
            @RequestBody DetectionRule rule) {

        DetectionRule newRule = ruleService.createRule(rule);

        return ResponseEntity.ok(
                ApiResponse.success("Rule created successfully", newRule)
        );
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<DetectionRule>> toggleRule(
            @PathVariable UUID id) {

        DetectionRule updated = ruleService.toggleRuleStatus(id);

        return ResponseEntity.ok(
                ApiResponse.success("Rule status updated", updated)
        );
    }

    @PostMapping("/reprocess")
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
