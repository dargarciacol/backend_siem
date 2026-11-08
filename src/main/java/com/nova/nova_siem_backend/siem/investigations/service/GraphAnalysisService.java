package com.nova.nova_siem_backend.siem.investigations.service;

import com.nova.nova_siem_backend.siem.common.exception.ResourceNotFoundException;
import com.nova.nova_siem_backend.siem.incidents.model.SecurityIncident;
import com.nova.nova_siem_backend.siem.investigations.dto.GraphNodeDTO;
import com.nova.nova_siem_backend.siem.investigations.model.IOC;
import com.nova.nova_siem_backend.siem.investigations.model.Investigation;
import com.nova.nova_siem_backend.siem.investigations.model.InvestigationEvidence;
import com.nova.nova_siem_backend.siem.investigations.repository.EvidenceRepository;
import com.nova.nova_siem_backend.siem.investigations.repository.InvestigationRepository;
import com.nova.nova_siem_backend.siem.investigations.repository.IocRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GraphAnalysisService {

    private final InvestigationRepository investigationRepository;
    private final IocRepository iocRepository;
    private final EvidenceRepository evidenceRepository;

    public GraphAnalysisService(InvestigationRepository investigationRepository,
                                IocRepository iocRepository,
                                EvidenceRepository evidenceRepository) {
        this.investigationRepository = investigationRepository;
        this.iocRepository = iocRepository;
        this.evidenceRepository = evidenceRepository;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> generateIncidentGraph(UUID investigationId) {
        Investigation inv = investigationRepository.findById(investigationId)
                .orElseThrow(() -> new ResourceNotFoundException("Investigation not found with ID: " + investigationId));

        SecurityIncident incident = inv.getIncident();
        List<GraphNodeDTO> nodes = new ArrayList<>();
        List<Map<String, Object>> edges = new ArrayList<>();

        Map<String, Object> incMeta = new HashMap<>();
        incMeta.put("severity", String.valueOf(incident.getSeverity()));
        incMeta.put("status", String.valueOf(incident.getStatus()));
        nodes.add(node(incident.getId().toString(), incident.getTitle(), "INCIDENT", incMeta));

        Map<String, Object> invMeta = new HashMap<>();
        invMeta.put("verdict", inv.getVerdict() != null ? inv.getVerdict().name() : "PENDING");
        nodes.add(node(inv.getId().toString(), "Investigation Workspace", "INVESTIGATION", invMeta));
        edges.add(edge(incident.getId().toString(), inv.getId().toString(), "HAS_WORKSPACE"));

        for (IOC ioc : iocRepository.findByInvestigationId(investigationId)) {
            Map<String, Object> m = new HashMap<>();
            m.put("type", ioc.getIocType());
            m.put("malicious", ioc.isMalicious());
            m.put("context", ioc.getThreatContext());
            String id = ioc.getId().toString();
            nodes.add(node(id, ioc.getValue(), ioc.isMalicious() ? "IOC_MALICIOUS" : "IOC", m));
            edges.add(edge(inv.getId().toString(), id, "HAS_IOC"));
        }

        for (InvestigationEvidence ev : evidenceRepository.findByInvestigationId(investigationId)) {
            Map<String, Object> m = new HashMap<>();
            m.put("type", ev.getEvidenceType());
            m.put("description", ev.getDescription());
            m.put("reference", ev.getArtifactReference());
            String id = ev.getId().toString();
            nodes.add(node(id, ev.getEvidenceType(), "EVIDENCE", m));
            edges.add(edge(inv.getId().toString(), id, "HAS_EVIDENCE"));
        }

        Map<String, Object> graph = new HashMap<>();
        graph.put("nodes", nodes);
        graph.put("edges", edges);
        return graph;
    }

    private GraphNodeDTO node(String id, String label, String group, Map<String, Object> meta) {
        return GraphNodeDTO.builder().id(id).label(label).group(group).metadata(meta).build();
    }

    private Map<String, Object> edge(String source, String target, String rel) {
        Map<String, Object> e = new LinkedHashMap<>();
        e.put("source", source);
        e.put("target", target);
        e.put("relationship", rel);
        return e;
    }
}
