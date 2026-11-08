package com.nova.nova_siem_backend.siem.detection.mapper;

import com.nova.nova_siem_backend.siem.detection.dto.DetectionRuleRequestDTO;
import com.nova.nova_siem_backend.siem.detection.dto.DetectionRuleResponseDTO;
import com.nova.nova_siem_backend.siem.detection.model.DetectionRule;
import org.springframework.stereotype.Component;

@Component
public class DetectionRuleMapper {

    public DetectionRule toEntity(DetectionRuleRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        DetectionRule rule = new DetectionRule();
        rule.setName(dto.getName());
        rule.setCategory(dto.getCategory());
        rule.setMitreTechniqueId(dto.getMitreTechniqueId());
        rule.setSeverity(dto.getSeverity());
        rule.setSigmaLogic(dto.getSigmaLogic());
        rule.setActive(dto.getActive() != null ? dto.getActive() : true);
        rule.setHitCount(0L);

        return rule;
    }

    public DetectionRuleResponseDTO toDto(DetectionRule entity) {
        if (entity == null) {
            return null;
        }

        return DetectionRuleResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .category(entity.getCategory())
                .mitreTechniqueId(entity.getMitreTechniqueId())
                .severity(entity.getSeverity())
                .sigmaLogic(entity.getSigmaLogic())
                .active(entity.isActive())
                .hitCount(entity.getHitCount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}