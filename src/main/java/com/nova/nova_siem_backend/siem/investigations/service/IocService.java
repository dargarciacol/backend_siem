package com.nova.nova_siem_backend.siem.investigations.service;

import com.nova.nova_siem_backend.siem.common.exception.BusinessException;
import com.nova.nova_siem_backend.siem.common.exception.ConflictException;
import com.nova.nova_siem_backend.siem.common.exception.ResourceNotFoundException;
import com.nova.nova_siem_backend.siem.investigations.dto.IocRequestDTO;
import com.nova.nova_siem_backend.siem.investigations.dto.IocResponseDTO;
import com.nova.nova_siem_backend.siem.investigations.model.IOC;
import com.nova.nova_siem_backend.siem.investigations.model.Investigation;
import com.nova.nova_siem_backend.siem.investigations.repository.InvestigationRepository;
import com.nova.nova_siem_backend.siem.investigations.repository.IocRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class IocService {

    private static final Set<String> TYPES = Set.of("IP", "DOMAIN", "URL", "HASH", "EMAIL", "FILE");

    private final IocRepository iocRepository;
    private final InvestigationRepository investigationRepository;

    public IocService(IocRepository iocRepository, InvestigationRepository investigationRepository) {
        this.iocRepository = iocRepository;
        this.investigationRepository = investigationRepository;
    }

    @Transactional(readOnly = true)
    public List<IocResponseDTO> listByInvestigation(UUID investigationId) {
        requireInvestigation(investigationId);
        return iocRepository.findByInvestigationId(investigationId).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public IocResponseDTO get(UUID id) {
        return toDto(find(id));
    }

    @Transactional
    public IocResponseDTO create(UUID investigationId, IocRequestDTO req) {
        Investigation inv = requireInvestigation(investigationId);
        String type = normalizeType(req.getIocType());
        String value = normalizeValue(req.getValue());

        if (iocRepository.existsByInvestigationIdAndIocTypeIgnoreCaseAndValueIgnoreCase(investigationId, type, value)) {
            throw new ConflictException("This IOC already exists in the investigation.");
        }

        IOC ioc = new IOC();
        ioc.setInvestigation(inv);
        ioc.setIocType(type);
        ioc.setValue(value);
        ioc.setMalicious(Boolean.TRUE.equals(req.getMalicious()));
        ioc.setThreatContext(req.getThreatContext());
        return toDto(iocRepository.save(ioc));
    }

    @Transactional
    public IocResponseDTO update(UUID id, IocRequestDTO req) {
        IOC ioc = find(id);
        String type = normalizeType(req.getIocType());
        String value = normalizeValue(req.getValue());

        boolean changed = !type.equalsIgnoreCase(ioc.getIocType()) || !value.equalsIgnoreCase(ioc.getValue());
        if (changed && iocRepository.existsByInvestigationIdAndIocTypeIgnoreCaseAndValueIgnoreCase(
                ioc.getInvestigation().getId(), type, value)) {
            throw new ConflictException("This IOC already exists in the investigation.");
        }

        ioc.setIocType(type);
        ioc.setValue(value);
        ioc.setMalicious(Boolean.TRUE.equals(req.getMalicious()));
        ioc.setThreatContext(req.getThreatContext());
        return toDto(iocRepository.save(ioc));
    }

    @Transactional
    public void delete(UUID id) {
        iocRepository.delete(find(id));
    }

    private IOC find(UUID id) {
        return iocRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IOC not found with ID: " + id));
    }

    private Investigation requireInvestigation(UUID id) {
        return investigationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investigation not found with ID: " + id));
    }

    private String normalizeType(String raw) {
        String type = raw == null ? "" : raw.trim().toUpperCase(Locale.ROOT);
        if (!TYPES.contains(type)) {
            throw new BusinessException("iocType must be one of: " + String.join(", ", new java.util.TreeSet<>(TYPES)));
        }
        return type;
    }

    private String normalizeValue(String raw) {
        String value = raw == null ? "" : raw.trim();
        if (value.isEmpty()) {
            throw new BusinessException("value is required.");
        }
        return value;
    }

    private IocResponseDTO toDto(IOC ioc) {
        return IocResponseDTO.builder()
                .id(ioc.getId())
                .investigationId(ioc.getInvestigation().getId())
                .iocType(ioc.getIocType())
                .value(ioc.getValue())
                .malicious(ioc.isMalicious())
                .threatContext(ioc.getThreatContext())
                .createdAt(ioc.getCreatedAt())
                .build();
    }
}
