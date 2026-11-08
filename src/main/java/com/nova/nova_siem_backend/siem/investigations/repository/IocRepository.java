package com.nova.nova_siem_backend.siem.investigations.repository;

import com.nova.nova_siem_backend.siem.investigations.model.IOC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IocRepository extends JpaRepository<IOC, UUID> {
    List<IOC> findByInvestigationId(UUID investigationId);

    boolean existsByInvestigationIdAndIocTypeIgnoreCaseAndValueIgnoreCase(UUID investigationId, String iocType, String value);
}
