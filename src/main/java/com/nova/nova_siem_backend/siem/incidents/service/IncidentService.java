package com.nova.nova_siem_backend.siem.incidents.service;

import com.nova.nova_siem_backend.siem.incidents.model.IncidentStatus;
import com.nova.nova_siem_backend.siem.incidents.model.SecurityIncident;
import com.nova.nova_siem_backend.siem.incidents.repository.IncidentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public Page<SecurityIncident> getAllIncidents(Pageable pageable) {
        return incidentRepository.findAll(pageable);
    }

    public SecurityIncident createIncident(SecurityIncident incident, String username) {
        incident.setStatus(IncidentStatus.TRIAGE);
        incident.setAssignedTo(username);
        return incidentRepository.save(incident);
    }

    public SecurityIncident getIncidentById(UUID id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found with ID: " + id));
    }
}