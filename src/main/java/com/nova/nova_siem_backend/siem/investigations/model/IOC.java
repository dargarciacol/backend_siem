package com.nova.nova_siem_backend.siem.investigations.model;

import com.nova.nova_siem_backend.siem.common.audit.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "indicators_of_compromise")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IOC extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investigation_id", nullable = false)
    private Investigation investigation;

    @Column(name = "ioc_type", nullable = false)
    private String iocType;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "is_malicious", nullable = false)
    private boolean malicious;

    @Column(name = "threat_context", columnDefinition = "TEXT")
    private String threatContext;
}