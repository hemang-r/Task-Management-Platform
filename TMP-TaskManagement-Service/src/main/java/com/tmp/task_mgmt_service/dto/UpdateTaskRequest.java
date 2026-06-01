package com.tmp.task_mgmt_service.dto;

import com.tmp.task_mgmt_service.enums.TaskPriority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTaskRequest
{
    @NotBlank(message = "Title is required")
    private String title;

    private String discription;

    @NotNull(message = "Priority is required")
    private TaskPriority priority;
}