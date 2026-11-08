package com.nova.nova_siem_backend.siem.investigations.service;

import com.nova.nova_siem_backend.siem.common.exception.ResourceNotFoundException;
import com.nova.nova_siem_backend.siem.incidents.model.SecurityIncident;
import com.nova.nova_siem_backend.siem.incidents.repository.IncidentRepository;
import com.nova.nova_siem_backend.siem.investigations.model.Investigation;
import com.nova.nova_siem_backend.siem.investigations.model.InvestigationVerdict;
import com.nova.nova_siem_backend.siem.investigations.repository.InvestigationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class InvestigationService {

    private final InvestigationRepository investigationRepository;
    private final IncidentRepository incidentRepository;

    public InvestigationService(InvestigationRepository investigationRepository, IncidentRepository incidentRepository) {
        this.investigationRepository = investigationRepository;
        this.incidentRepository = incidentRepository;
    }

    public Investigation getOrCreateInvestigation(UUID incidentId) {
        return investigationRepository.findByIncidentId(incidentId).orElseGet(() -> {
            SecurityIncident incident = incidentRepository.findById(incidentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Incident not found"));

            Investigation newInv = new Investigation();
            newInv.setIncident(incident);
            newInv.setFactsNotebook(new ArrayList<>());
            newInv.setInferencesNotebook(new ArrayList<>());
            newInv.setPendingNotebook(new ArrayList<>());

            return investigationRepository.save(newInv);
        });
    }

    public Investigation submitVerdict(UUID investigationId, InvestigationVerdict verdict, Integer confidenceScore, String recommendedAction) {
        Investigation inv = investigationRepository.findById(investigationId)
                .orElseThrow(() -> new ResourceNotFoundException("Investigation not found"));

        inv.setVerdict(verdict);
        inv.setConfidenceScore(confidenceScore);
        inv.setRecommendedAction(recommendedAction);

        return investigationRepository.save(inv);
    }
}