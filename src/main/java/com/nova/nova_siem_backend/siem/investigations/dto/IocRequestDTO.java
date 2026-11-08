package com.nova.nova_siem_backend.siem.investigations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class IocRequestDTO {

    @NotBlank
    @Size(max = 50)
    private String iocType;

    @NotBlank
    @Size(max = 255)
    private String value;

    private Boolean malicious;

    @Size(max = 4000)
    private String threatContext;
}
