package com.tmp.task_mgmt_service.dto;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class ProjectResponse
{
    private UUID id;

    private String name;

    private String discription;

    private UUID ownerUserId;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}