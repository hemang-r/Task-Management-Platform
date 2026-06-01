package com.tmp.task_mgmt_service.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tmp.task_mgmt_service.dto.AssignTaskRequest;
import com.tmp.task_mgmt_service.dto.CreateTaskRequest;
import com.tmp.task_mgmt_service.dto.UpdateTaskRequest;
import com.tmp.task_mgmt_service.dto.UpdateTaskStatusRequest;
import com.tmp.task_mgmt_service.enums.TaskStatus;
import com.tmp.task_mgmt_service.service.TaskService;
import com.tmp.task_mgmt_service.utills.ApplicationURIConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping(ApplicationURIConstants.PROJECTS)
public class TaskController
{
	@Autowired
    private TaskService taskService;

    @PostMapping(ApplicationURIConstants.PID_PATH + ApplicationURIConstants.TASKS)
    public ResponseEntity<Object> createTask(
            @PathVariable UUID pid,
            @Valid @RequestBody CreateTaskRequest request)
    {
        return taskService.createTask(pid, request);
    }

    @GetMapping(ApplicationURIConstants.PID_PATH+ApplicationURIConstants.TASKS)
    public ResponseEntity<Object> getTasks(
            @PathVariable UUID pid,
            @RequestParam(required = false) TaskStatus status)
    {
        return taskService.getTasks(pid, status);
    }

    @GetMapping(ApplicationURIConstants.PID_PATH+ApplicationURIConstants.TASKS+ApplicationURIConstants.TID_PATH)
    public ResponseEntity<Object> getTaskById(
            @PathVariable UUID pid,
            @PathVariable UUID tid)
    {
        return taskService.getTaskById(pid, tid);
    }

    @PutMapping(ApplicationURIConstants.PID_PATH + ApplicationURIConstants.TASKS + ApplicationURIConstants.TID_PATH)
    public ResponseEntity<Object> updateTask(
            @PathVariable UUID pid,
            @PathVariable UUID tid,
            @Valid @RequestBody UpdateTaskRequest request)
    {
        return taskService.updateTask(pid, tid, request);
    }

    @PatchMapping(ApplicationURIConstants.PID_PATH + ApplicationURIConstants.TASKS + ApplicationURIConstants.TID_PATH+
    		ApplicationURIConstants.ASSIGN)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> assignTask(
            @PathVariable UUID pid,
            @PathVariable UUID tid,
            @Valid @RequestBody AssignTaskRequest request)
    {
        return taskService.assignTask(pid, tid, request);
    }

    @PatchMapping( ApplicationURIConstants.PID_PATH + ApplicationURIConstants.TASKS + ApplicationURIConstants.TID_PATH+
    		ApplicationURIConstants.STATUS)
    public ResponseEntity<Object> updateTaskStatus(
            @PathVariable UUID pid,
            @PathVariable UUID tid,
            @Valid @RequestBody UpdateTaskStatusRequest request)
    {
        return taskService.updateTaskStatus(
                pid,
                tid,
                request);
    }

    @DeleteMapping(ApplicationURIConstants.PID_PATH + ApplicationURIConstants.TASKS+ApplicationURIConstants.TID_PATH)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteTask(
            @PathVariable UUID pid,
            @PathVariable UUID tid)
    {
        return taskService.deleteTask(pid, tid);
    }

    @GetMapping(ApplicationURIConstants.TASKS +ApplicationURIConstants.MY_TASKS)
    public ResponseEntity<Object> getMyTasks()
    {
        return taskService.getMyTasks();
    }
}