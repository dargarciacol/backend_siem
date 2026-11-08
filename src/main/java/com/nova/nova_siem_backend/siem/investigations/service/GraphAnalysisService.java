package com.nova.nova_siem_backend.siem.investigations.service;

import com.nova.nova_siem_backend.siem.common.exception.ResourceNotFoundException;
import com.nova.nova_siem_backend.siem.incidents.model.SecurityIncident;
import com.nova.nova_siem_backend.siem.alerts.model.SecurityAlert; // Ajusta el paquete según tu estructura
import com.nova.nova_siem_backend.siem.events.model.SecurityEvent; // Ajusta el paquete según tu estructura
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

        // 1. Nodo del Incidente
        Map<String, Object> incMeta = new HashMap<>();
        incMeta.put("severity", String.valueOf(incident.getSeverity()));
        incMeta.put("status", String.valueOf(incident.getStatus()));
        nodes.add(node(incident.getId().toString(), incident.getTitle(), "INCIDENT", incMeta));

        // 2. Nodo del Workspace de Investigación
        Map<String, Object> invMeta = new HashMap<>();
        invMeta.put("verdict", inv.getVerdict() != null ? inv.getVerdict().name() : "PENDING");
        nodes.add(node(inv.getId().toString(), "Investigation Workspace", "INVESTIGATION", invMeta));
        edges.add(edge(incident.getId().toString(), inv.getId().toString(), "HAS_WORKSPACE"));

        // 3. Alertas asociadas al incidente (security_alerts.incident_id) y sus eventos persistidos
        java.util.Set<String> eventNodes = new java.util.HashSet<>();
        for (SecurityAlert alert : incident.getAlerts()) {
            String alertId = "alert_" + alert.getId();
            Map<String, Object> alertMeta = new HashMap<>();
            alertMeta.put("severity", String.valueOf(alert.getSeverity()));
            alertMeta.put("status", String.valueOf(alert.getStatus()));
            nodes.add(node(alertId, alert.getRuleTriggered(), "ALERT", alertMeta));
            edges.add(edge(incident.getId().toString(), alertId, "GENERATES_ALERT"));

            java.util.Set<SecurityEvent> related = new java.util.LinkedHashSet<>(alert.getEvents());
            if (alert.getSourceEvent() != null) {
                related.add(alert.getSourceEvent());
            }
            for (SecurityEvent event : related) {
                String eventId = "event_" + event.getId();
                if (eventNodes.add(eventId)) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("sourceIp", event.getSourceIp());
                    m.put("user", event.getUserIdentity());
                    m.put("timestamp", String.valueOf(event.getTimestamp()));
                    m.put("severity", String.valueOf(event.getSeverity()));
                    nodes.add(node(eventId, event.getEventType() + " / " + event.getAction(), "EVENT", m));
                }
                edges.add(edge(alertId, eventId, "TRIGGERED_BY"));
            }
        }

        // 5. Nodos de Indicadores de Compromiso (IOCs)
        for (IOC ioc : iocRepository.findByInvestigationId(investigationId)) {
            Map<String, Object> m = new HashMap<>();
            m.put("type", ioc.getIocType());
            m.put("malicious", ioc.isMalicious());
            m.put("context", ioc.getThreatContext());
            String id = ioc.getId().toString();
            nodes.add(node(id, ioc.getValue(), ioc.isMalicious() ? "IOC_MALICIOUS" : "IOC", m));
            edges.add(edge(inv.getId().toString(), id, "HAS_IOC"));
        }

        // 6. Nodos de Evidencia Recolectada
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