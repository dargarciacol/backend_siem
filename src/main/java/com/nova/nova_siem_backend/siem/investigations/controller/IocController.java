package com.nova.nova_siem_backend.siem.investigations.controller;

import com.nova.nova_siem_backend.siem.common.response.ApiResponse;
import com.nova.nova_siem_backend.siem.investigations.dto.IocRequestDTO;
import com.nova.nova_siem_backend.siem.investigations.dto.IocResponseDTO;
import com.nova.nova_siem_backend.siem.investigations.service.IocService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class IocController {

    private final IocService iocService;

    public IocController(IocService iocService) {
        this.iocService = iocService;
    }

    @GetMapping("/investigations/{investigationId}/iocs")
    public ResponseEntity<ApiResponse<List<IocResponseDTO>>> list(@PathVariable UUID investigationId) {
        return ResponseEntity.ok(ApiResponse.success("IOCs retrieved", iocService.listByInvestigation(investigationId)));
    }

    @PostMapping("/investigations/{investigationId}/iocs")
    public ResponseEntity<ApiResponse<IocResponseDTO>> create(
            @PathVariable UUID investigationId, @Valid @RequestBody IocRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("IOC created", iocService.create(investigationId, request)));
    }

    @GetMapping("/iocs/{id}")
    public ResponseEntity<ApiResponse<IocResponseDTO>> get(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("IOC retrieved", iocService.get(id)));
    }

    @PutMapping("/iocs/{id}")
    public ResponseEntity<ApiResponse<IocResponseDTO>> update(
            @PathVariable UUID id, @Valid @RequestBody IocRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.success("IOC updated", iocService.update(id, request)));
    }

    @DeleteMapping("/iocs/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        iocService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("IOC deleted", null));
    }
}
