package com.nova.nova_siem_backend.siem.investigations.service;

import com.nova.nova_siem_backend.siem.common.exception.ResourceNotFoundException;
import com.nova.nova_siem_backend.siem.incidents.model.SecurityIncident;
import com.nova.nova_siem_backend.siem.investigations.dto.GraphNodeDTO;
import com.nova.nova_siem_backend.siem.investigations.model.Investigation;
import com.nova.nova_siem_backend.siem.investigations.repository.InvestigationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GraphAnalysisService {

    private final InvestigationRepository investigationRepository;

    public GraphAnalysisService(InvestigationRepository investigationRepository) {
        this.investigationRepository = investigationRepository;
    }

    public Map<String, Object> generateIncidentGraph(UUID investigationId) {
        Investigation investigation = investigationRepository.findById(investigationId)
                .orElseThrow(() -> new ResourceNotFoundException("Investigation not found with ID: " + investigationId));

        SecurityIncident incident = investigation.getIncident();

        List<GraphNodeDTO> nodes = new ArrayList<>();
        List<Map<String, Object>> edges = new ArrayList<>();

        nodes.add(GraphNodeDTO.builder()
                .id(incident.getId().toString())
                .label(incident.getTitle())
                .group("INCIDENT")
                .metadata(Map.of("severity", incident.getSeverity(), "status", incident.getStatus()))
                .build());

        nodes.add(GraphNodeDTO.builder()
                .id(investigation.getId().toString())
                .label("Investigation Workspace")
                .group("INVESTIGATION")
                .metadata(Map.of("verdict", investigation.getVerdict() != null ? investigation.getVerdict() : "PENDING"))
                .build());

        edges.add(Map.of(
                "source", incident.getId().toString(),
                "target", investigation.getId().toString(),
                "relationship", "HAS_WORKSPACE"
        ));

        Map<String, Object> graphData = new HashMap<>();
        graphData.put("nodes", nodes);
        graphData.put("edges", edges);

        return graphData;
    }
}