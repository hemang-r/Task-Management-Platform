package com.tmp.task_mgmt_service.dto;
import java.time.LocalDateTime;
import java.util.UUID;

import com.tmp.task_mgmt_service.enums.TaskPriority;
import com.tmp.task_mgmt_service.enums.TaskStatus;

import lombok.Data;

@Data
public class TaskResponse
{
    private UUID id;

    private UUID projectId;

    private String title;

    private String discription;

    private TaskStatus status;
    private TaskPriority priority;

    private UUID ownerUserId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}