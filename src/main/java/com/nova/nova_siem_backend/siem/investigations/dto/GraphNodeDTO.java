package com.nova.nova_siem_backend.siem.investigations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraphNodeDTO {

    private String id;
    private String label;
    private String group;
    private Map<String, Object> metadata;
}