package com.nova.nova_siem_backend.siem.incidents.controller;

import com.nova.nova_siem_backend.siem.common.response.ApiResponse;
import com.nova.nova_siem_backend.siem.incidents.dto.IncidentRequestDTO;
import com.nova.nova_siem_backend.siem.incidents.model.SecurityIncident;
import jakarta.validation.Valid;
import com.nova.nova_siem_backend.siem.incidents.service.IncidentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SecurityIncident>>> getAllIncidents(
            @PageableDefault(size = 200, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<SecurityIncident> incidents = incidentService.getAllIncidents(pageable);
        return ResponseEntity.ok(ApiResponse.success("Incidents retrieved successfully", incidents));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SecurityIncident>> createIncident(
            @Valid @RequestBody IncidentRequestDTO request,
            Authentication authentication) {

        String username = authentication != null ? authentication.getName() : "System";
        SecurityIncident createdIncident = incidentService.createIncident(request, username);

        return ResponseEntity.ok(ApiResponse.success("Incident created successfully", createdIncident));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SecurityIncident>> getIncidentById(@PathVariable UUID id) {
        SecurityIncident incident = incidentService.getIncidentById(id);
        return ResponseEntity.ok(ApiResponse.success("Incident retrieved successfully", incident));
    }
}