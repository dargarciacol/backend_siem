package com.nova.nova_siem_backend.siem.reports.service;

import com.nova.nova_siem_backend.siem.common.exception.ResourceNotFoundException;
import com.nova.nova_siem_backend.siem.incidents.model.SecurityIncident;
import com.nova.nova_siem_backend.siem.incidents.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ReportService {

    private final IncidentRepository incidentRepository;

    public ReportService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public Map<String, Object> generateIncidentReport(UUID incidentId, String format, String author) {
        SecurityIncident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found"));

        Map<String, Object> report = new HashMap<>();
        report.put("reportId", "REP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        report.put("title", "Incident Report: " + incident.getTitle());
        report.put("type", "Incident Summary");
        report.put("format", format);
        report.put("author", author);
        report.put("generatedAt", LocalDateTime.now());
        report.put("incidentDetails", incident);

        return report;
    }

    public Map<String, Object> generateExecutiveBrief(String format) {
        Map<String, Object> report = new HashMap<>();
        report.put("reportId", "REP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        report.put("title", "SOC Executive Brief - " + LocalDateTime.now().toLocalDate().toString());
        report.put("type", "Executive Brief");
        report.put("format", format);
        report.put("author", "System");
        report.put("generatedAt", LocalDateTime.now());

        return report;
    }
}