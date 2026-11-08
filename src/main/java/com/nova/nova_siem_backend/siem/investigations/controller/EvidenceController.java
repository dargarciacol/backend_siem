package com.nova.nova_siem_backend.siem.investigations.controller;

import com.nova.nova_siem_backend.siem.common.response.ApiResponse;
import com.nova.nova_siem_backend.siem.investigations.dto.EvidenceRequestDTO;
import com.nova.nova_siem_backend.siem.investigations.dto.EvidenceResponseDTO;
import com.nova.nova_siem_backend.siem.investigations.service.EvidenceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class EvidenceController {

    private final EvidenceService evidenceService;

    public EvidenceController(EvidenceService evidenceService) {
        this.evidenceService = evidenceService;
    }

    @GetMapping("/investigations/{investigationId}/evidences")
    public ResponseEntity<ApiResponse<List<EvidenceResponseDTO>>> list(@PathVariable UUID investigationId) {
        return ResponseEntity.ok(ApiResponse.success("Evidences retrieved", evidenceService.listByInvestigation(investigationId)));
    }

    @PostMapping("/investigations/{investigationId}/evidences")
    public ResponseEntity<ApiResponse<EvidenceResponseDTO>> create(
            @PathVariable UUID investigationId, @Valid @RequestBody EvidenceRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Evidence created", evidenceService.create(investigationId, request)));
    }

    @GetMapping("/evidences/{id}")
    public ResponseEntity<ApiResponse<EvidenceResponseDTO>> get(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Evidence retrieved", evidenceService.get(id)));
    }

    @DeleteMapping("/evidences/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        evidenceService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Evidence deleted", null));
    }
}
