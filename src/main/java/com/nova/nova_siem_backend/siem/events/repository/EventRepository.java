
package com.nova.nova_siem_backend.siem.events.repository;

import com.nova.nova_siem_backend.siem.events.model.SecurityEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<SecurityEvent, UUID> {

    List<SecurityEvent> findByEventTypeAndActionOrderByTimestampAsc(
            String eventType,
            String action
    );
}
