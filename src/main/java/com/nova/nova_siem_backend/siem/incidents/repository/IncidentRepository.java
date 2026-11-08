package com.nova.nova_siem_backend.siem.incidents.repository;

import com.nova.nova_siem_backend.siem.incidents.model.IncidentStatus; // Asegúrate de importar el Enum
import com.nova.nova_siem_backend.siem.incidents.model.SecurityIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IncidentRepository extends JpaRepository<SecurityIncident, UUID> {

    // NUEVO MÉTODO PARA EL DASHBOARD
    long countByStatusNot(IncidentStatus status);
}