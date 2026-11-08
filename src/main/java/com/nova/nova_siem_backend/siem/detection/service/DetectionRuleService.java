package com.nova.nova_siem_backend.siem.detection.service;

import com.nova.nova_siem_backend.siem.common.exception.ResourceNotFoundException;
import com.nova.nova_siem_backend.siem.detection.model.DetectionRule;
import com.nova.nova_siem_backend.siem.detection.repository.DetectionRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DetectionRuleService {

    private final DetectionRuleRepository ruleRepository;

    public DetectionRuleService(DetectionRuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public List<DetectionRule> getAllRules() {
        return ruleRepository.findAll();
    }

    public DetectionRule createRule(DetectionRule rule) {
        return ruleRepository.save(rule);
    }

    public DetectionRule toggleRuleStatus(UUID id) {
        DetectionRule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rule not found"));
        rule.setActive(!rule.isActive());
        return ruleRepository.save(rule);
    }
}