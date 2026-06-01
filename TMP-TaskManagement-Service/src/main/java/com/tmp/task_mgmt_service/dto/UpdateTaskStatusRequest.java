package com.tmp.task_mgmt_service.dto;


import com.tmp.task_mgmt_service.enums.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTaskStatusRequest
{
	@NotNull(message = "Status is required")
	private TaskStatus status;
}
