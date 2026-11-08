package com.nova.nova_siem_backend.siem.investigations.service;

import com.nova.nova_siem_backend.siem.common.exception.BusinessException;
import com.nova.nova_siem_backend.siem.common.exception.ResourceNotFoundException;
import com.nova.nova_siem_backend.siem.investigations.dto.EvidenceRequestDTO;
import com.nova.nova_siem_backend.siem.investigations.dto.EvidenceResponseDTO;
import com.nova.nova_siem_backend.siem.investigations.model.Investigation;
import com.nova.nova_siem_backend.siem.investigations.model.InvestigationEvidence;
import com.nova.nova_siem_backend.siem.investigations.repository.EvidenceRepository;
import com.nova.nova_siem_backend.siem.investigations.repository.InvestigationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Evidencias de texto/referencia. No se implementa carga de archivos: no existe almacenamiento
 * real en el proyecto; artifactReference guarda solo una referencia (ruta, hash, URL o ID externo).
 */
@Service
public class EvidenceService {

    private final EvidenceRepository evidenceRepository;
    private final InvestigationRepository investigationRepository;

    public EvidenceService(EvidenceRepository evidenceRepository, InvestigationRepository investigationRepository) {
        this.evidenceRepository = evidenceRepository;
        this.investigationRepository = investigationRepository;
    }

    @Transactional(readOnly = true)
    public List<EvidenceResponseDTO> listByInvestigation(UUID investigationId) {
        requireInvestigation(investigationId);
        return evidenceRepository.findByInvestigationId(investigationId).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public EvidenceResponseDTO get(UUID id) {
        return toDto(find(id));
    }

    @Transactional
    public EvidenceResponseDTO create(UUID investigationId, EvidenceRequestDTO req) {
        Investigation inv = requireInvestigation(investigationId);
        String type = req.getEvidenceType() == null ? "" : req.getEvidenceType().trim();
        if (type.isEmpty()) {
            throw new BusinessException("evidenceType is required.");
        }
        InvestigationEvidence ev = new InvestigationEvidence();
        ev.setInvestigation(inv);
        ev.setEvidenceType(type);
        ev.setDescription(req.getDescription());
        ev.setArtifactReference(req.getArtifactReference());
        return toDto(evidenceRepository.save(ev));
    }

    @Transactional
    public void delete(UUID id) {
        evidenceRepository.delete(find(id));
    }

    private InvestigationEvidence find(UUID id) {
        return evidenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidence not found with ID: " + id));
    }

    private Investigation requireInvestigation(UUID id) {
        return investigationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investigation not found with ID: " + id));
    }

    private EvidenceResponseDTO toDto(InvestigationEvidence ev) {
        return EvidenceResponseDTO.builder()
                .id(ev.getId())
                .investigationId(ev.getInvestigation().getId())
                .evidenceType(ev.getEvidenceType())
                .description(ev.getDescription())
                .artifactReference(ev.getArtifactReference())
                .createdAt(ev.getCreatedAt())
                .build();
    }
}
