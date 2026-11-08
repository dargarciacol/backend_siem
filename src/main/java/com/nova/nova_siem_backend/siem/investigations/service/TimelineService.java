package com.nova.nova_siem_backend.siem.investigations.service;

import com.nova.nova_siem_backend.siem.common.exception.ResourceNotFoundException;
import com.nova.nova_siem_backend.siem.investigations.dto.TimelineDTO;
import com.nova.nova_siem_backend.siem.investigations.model.Investigation;
import com.nova.nova_siem_backend.siem.investigations.model.InvestigationEvidence;
import com.nova.nova_siem_backend.siem.investigations.repository.EvidenceRepository;
import com.nova.nova_siem_backend.siem.investigations.repository.InvestigationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TimelineService {

    private final InvestigationRepository investigationRepository;

    private final EvidenceRepository evidenceRepository;

    public TimelineService(InvestigationRepository investigationRepository, EvidenceRepository evidenceRepository) {
        this.investigationRepository = investigationRepository;
        this.evidenceRepository = evidenceRepository;
    }

    public List<TimelineDTO> generateInvestigationTimeline(UUID investigationId) {
        Investigation investigation = investigationRepository.findById(investigationId)
                .orElseThrow(() -> new ResourceNotFoundException("Investigation not found with ID: " + investigationId));

        List<TimelineDTO> timeline = new ArrayList<>();

        timeline.add(TimelineDTO.builder()
                .timestamp(investigation.getCreatedAt())
                .category("INVESTIGATION_OPENED")
                .title("Investigation Initialized")
                .description("SOC L2 Analyst opened investigation workspace.")
                .source("System")
                .build());

        if (investigation.getVerdict() != null) {
            timeline.add(TimelineDTO.builder()
                    .timestamp(investigation.getUpdatedAt())
                    .category("VERDICT_SUBMITTED")
                    .title("Verdict Reached: " + investigation.getVerdict())
                    .description("Action recommended: " + investigation.getRecommendedAction())
                    .source("Analyst")
                    .build());
        }

        for (InvestigationEvidence ev : evidenceRepository.findByInvestigationId(investigationId)) {
            timeline.add(TimelineDTO.builder()
                    .timestamp(ev.getCreatedAt())
                    .category("EVIDENCE_ADDED")
                    .title("Evidence: " + ev.getEvidenceType())
                    .description(ev.getDescription())
                    .source("Analyst")
                    .build());
        }

        return timeline.stream()
                .sorted(Comparator.comparing(TimelineDTO::getTimestamp, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }
}