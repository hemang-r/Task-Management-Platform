package com.tmp.task_mgmt_service.dto;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateProjectRequest
{
    @NotBlank(message = "Project name is required")
    private String name;

    private String discription;
    
    @NotNull(message = "Owner user id is required")
    private UUID ownerUserId;

    private Boolean isActive;
}