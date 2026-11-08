package com.nova.nova_siem_backend.siem.detection.repository;

import com.nova.nova_siem_backend.siem.detection.model.DetectionRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DetectionRuleRepository extends JpaRepository<DetectionRule, UUID> {
    List<DetectionRule> findByActiveTrue();

    boolean existsByNameIgnoreCase(String name);

    long countByActiveTrue();
}