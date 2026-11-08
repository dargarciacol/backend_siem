package com.nova.nova_siem_backend.siem.incidents.service;

import com.nova.nova_siem_backend.siem.alerts.model.SecurityAlert;
import com.nova.nova_siem_backend.siem.alerts.repository.AlertRepository;
import com.nova.nova_siem_backend.siem.common.exception.BusinessException;
import com.nova.nova_siem_backend.siem.common.exception.ResourceNotFoundException;
import com.nova.nova_siem_backend.siem.incidents.dto.IncidentRequestDTO;
import com.nova.nova_siem_backend.siem.incidents.model.IncidentStatus;
import com.nova.nova_siem_backend.siem.incidents.model.SecurityIncident;
import com.nova.nova_siem_backend.siem.incidents.repository.IncidentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final AlertRepository alertRepository;

    public IncidentService(IncidentRepository incidentRepository, AlertRepository alertRepository) {
        this.incidentRepository = incidentRepository;
        this.alertRepository = alertRepository;
    }

    public Page<SecurityIncident> getAllIncidents(Pageable pageable) {
        return incidentRepository.findAll(pageable);
    }

    @Transactional
    public SecurityIncident createIncident(IncidentRequestDTO request, String username) {
        Integer confidence = request.getConfidenceLevel();

        if (confidence != null && (confidence < 0 || confidence > 100)) {
            throw new BusinessException("confidenceLevel must be between 0 and 100.");
        }

        SecurityIncident incident = new SecurityIncident();
        incident.setTitle(request.getTitle().trim());
        incident.setDescription(request.getDescription());
        incident.setSeverity(request.getSeverity());
        incident.setPrimaryHypothesis(request.getPrimaryHypothesis());
        incident.setConfidenceLevel(confidence);
        incident.setStatus(IncidentStatus.TRIAGE);
        incident.setAssignedTo(username);
        SecurityIncident saved = incidentRepository.save(incident);

        if (request.getAlertIds() != null && !request.getAlertIds().isEmpty()) {
            List<SecurityAlert> alerts = alertRepository.findAllById(request.getAlertIds());
            if (alerts.size() != request.getAlertIds().stream().distinct().count()) {
                throw new ResourceNotFoundException("One or more alerts were not found.");
            }
            alerts.forEach(a -> a.setIncident(saved));
            alertRepository.saveAll(alerts);
        }
        return saved;
    }

    public SecurityIncident getIncidentById(UUID id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found with ID: " + id));
    }
}