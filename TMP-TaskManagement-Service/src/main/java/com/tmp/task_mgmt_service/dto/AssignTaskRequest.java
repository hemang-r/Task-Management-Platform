package com.tmp.task_mgmt_service.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignTaskRequest
{
    @NotNull(message = "Assignee user id is required")
    private UUID assigneeUserId;
}