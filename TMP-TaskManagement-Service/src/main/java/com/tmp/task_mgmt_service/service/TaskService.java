package com.tmp.task_mgmt_service.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.tmp.task_mgmt_service.dto.AssignTaskRequest;
import com.tmp.task_mgmt_service.dto.CreateTaskRequest;
import com.tmp.task_mgmt_service.dto.UpdateTaskRequest;
import com.tmp.task_mgmt_service.dto.UpdateTaskStatusRequest;
import com.tmp.task_mgmt_service.enums.TaskStatus;

public interface TaskService
{
    ResponseEntity<Object> createTask(
            UUID projectId,
            CreateTaskRequest request);

    ResponseEntity<Object> getTasks(
            UUID projectId,
            TaskStatus status);

    ResponseEntity<Object> getTaskById(
            UUID projectId,
            UUID taskId);

    ResponseEntity<Object> updateTask(
            UUID projectId,
            UUID taskId,
            UpdateTaskRequest request);

    ResponseEntity<Object> assignTask(
            UUID projectId,
            UUID taskId,
            AssignTaskRequest request);

    ResponseEntity<Object> updateTaskStatus(
            UUID projectId,
            UUID taskId,
            UpdateTaskStatusRequest request);

    ResponseEntity<Object> deleteTask(
            UUID projectId,
            UUID taskId);

    ResponseEntity<Object> getMyTasks();
}