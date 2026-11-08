package com.nova.nova_siem_backend.siem.detection.model;

public enum RuleCategory {
    INITIAL_ACCESS,
    EXECUTION,
    PERSISTENCE,
    PRIVILEGE_ESCALATION,
    DEFENSE_EVASION,
    CREDENTIAL_ACCESS,
    DISCOVERY,
    LATERAL_MOVEMENT,
    COLLECTION,
    COMMAND_AND_CONTROL,
    EXFILTRATION,
    IMPACT,
    CUSTOM
}