package com.tmp.task_mgmt_service.service.Impl;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tmp.task_mgmt_service.dao.ProjectDao;
import com.tmp.task_mgmt_service.dao.TaskDao;
import com.tmp.task_mgmt_service.dto.AssignTaskRequest;
import com.tmp.task_mgmt_service.dto.CreateTaskRequest;
import com.tmp.task_mgmt_service.dto.JwtUserDetails;
import com.tmp.task_mgmt_service.dto.TaskResponse;
import com.tmp.task_mgmt_service.dto.UpdateTaskRequest;
import com.tmp.task_mgmt_service.dto.UpdateTaskStatusRequest;
import com.tmp.task_mgmt_service.entity.Projects;
import com.tmp.task_mgmt_service.entity.Tasks;
import com.tmp.task_mgmt_service.enums.TaskStatus;
import com.tmp.task_mgmt_service.exception_handler.BadRequestException;
import com.tmp.task_mgmt_service.exception_handler.ResourceNotFoundException;
import com.tmp.task_mgmt_service.feign.UserInterface;
import com.tmp.task_mgmt_service.service.TaskService;
import com.tmp.task_mgmt_service.utills.ApplicationConstants;
import com.tmp.task_mgmt_service.utills.CommonServices;

@Service
public class TaskServiceImpl implements TaskService
{
    private static final Logger LOGGER =
            LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private UserInterface userInterface;

    @Autowired
    private CommonServices commonServices;

    @Override
    public ResponseEntity<Object> createTask(
            UUID projectId,
            CreateTaskRequest request)
    {
        LOGGER.info(ApplicationConstants.ENTER_LABEL);

        Projects project = getProject(projectId);

        if (request.getAssigneeUserId() != null)
        {
            validateAssignee(request.getAssigneeUserId());
        }

        Tasks task = new Tasks();

        task.setProjectsId(project);
        task.setTitle(request.getTitle());
        task.setDiscription(request.getDiscription());
        task.setPriority(request.getPriority());
        task.setStatus(TaskStatus.TODO);
        task.setAssigneeUserId(request.getAssigneeUserId());
        task.setIsActive(true);

        taskDao.save(task);

        LOGGER.info(ApplicationConstants.EXIT_LABEL);

        return ResponseEntity.ok(
                commonServices.generateGenericSuccessResponse(
                        "Task created successfully"));
    }

    @Override
    public ResponseEntity<Object> getTasks(
            UUID projectId,
            TaskStatus status)
    {
        LOGGER.info(ApplicationConstants.ENTER_LABEL);

        getProject(projectId);

        List<Tasks> tasks;

        if (status != null)
        {
            tasks = taskDao.findByProjectsIdIdAndStatusAndIsActiveTrue(
                    projectId,
                    status);
        }
        else
        {
            tasks = taskDao.findByProjectsIdIdAndIsActiveTrue(
                    projectId);
        }

        List<TaskResponse> response =
                tasks.stream()
                        .map(this::buildTaskResponse)
                        .collect(Collectors.toList());

        LOGGER.info(ApplicationConstants.EXIT_LABEL);

        return ResponseEntity.ok(
                commonServices.generateGenericSuccessResponse(
                        response));
    }

    @Override
    public ResponseEntity<Object> getTaskById(
            UUID projectId,
            UUID taskId)
    {
        LOGGER.info(ApplicationConstants.ENTER_LABEL);

        Tasks task = getTask(projectId, taskId);

        LOGGER.info(ApplicationConstants.EXIT_LABEL);

        return ResponseEntity.ok(
                commonServices.generateGenericSuccessResponse(
                        buildTaskResponse(task)));
    }

    @Override
    public ResponseEntity<Object> updateTask(
            UUID projectId,
            UUID taskId,
            UpdateTaskRequest request)
    {
        LOGGER.info(ApplicationConstants.ENTER_LABEL);

        Tasks task = getTask(projectId, taskId);

        task.setTitle(request.getTitle());
        task.setDiscription(request.getDiscription());
        task.setPriority(request.getPriority());

        taskDao.save(task);

        LOGGER.info(ApplicationConstants.EXIT_LABEL);

        return ResponseEntity.ok(
                commonServices.generateGenericSuccessResponse(
                        "Task updated successfully"));
    }

    @Override
    public ResponseEntity<Object> assignTask(
            UUID projectId,
            UUID taskId,
            AssignTaskRequest request)
    {
        LOGGER.info(ApplicationConstants.ENTER_LABEL);

        Tasks task = getTask(projectId, taskId);

        validateAssignee(
                request.getAssigneeUserId());

        task.setAssigneeUserId(
                request.getAssigneeUserId());

        taskDao.save(task);

        LOGGER.info(ApplicationConstants.EXIT_LABEL);

        return ResponseEntity.ok(
                commonServices.generateGenericSuccessResponse(
                        "Task assigned successfully"));
    }

    @Override
    public ResponseEntity<Object> updateTaskStatus(
            UUID projectId,
            UUID taskId,
            UpdateTaskStatusRequest request)
    {
        LOGGER.info(ApplicationConstants.ENTER_LABEL);

        Tasks task = getTask(projectId, taskId);

        validateStatusTransition(
                task.getStatus(),
                request.getStatus());

        task.setStatus(request.getStatus());

        taskDao.save(task);

        LOGGER.info(ApplicationConstants.EXIT_LABEL);

        return ResponseEntity.ok(
                commonServices.generateGenericSuccessResponse(
                        "Task status updated successfully"));
    }

    @Override
    public ResponseEntity<Object> deleteTask(
            UUID projectId,
            UUID taskId)
    {
        LOGGER.info(ApplicationConstants.ENTER_LABEL);

        Tasks task = getTask(projectId, taskId);

        task.setIsActive(false);

        taskDao.save(task);

        LOGGER.info(ApplicationConstants.EXIT_LABEL);

        return ResponseEntity.ok(
                commonServices.generateGenericSuccessResponse(
                        "Task deleted successfully"));
    }

    @Override
    public ResponseEntity<Object> getMyTasks()
    {
        LOGGER.info(ApplicationConstants.ENTER_LABEL);

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        JwtUserDetails userDetail =
                (JwtUserDetails) authentication.getPrincipal();

        UUID userId =
                userDetail.getId();

        List<TaskResponse> tasks =
                taskDao.findByAssigneeUserIdAndIsActiveTrue(userId)
                        .stream()
                        .map(this::buildTaskResponse)
                        .collect(Collectors.toList());

        LOGGER.info(ApplicationConstants.EXIT_LABEL);

        return ResponseEntity.ok(
                commonServices.generateGenericSuccessResponse(
                        tasks));
    }

    private Projects getProject(UUID projectId)
    {
        Projects project =
                projectDao.findById(projectId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Project not found"));

        if (!Boolean.TRUE.equals(project.getIsActive()))
        {
            throw new ResourceNotFoundException(
                    "Project not found");
        }

        return project;
    }

    private Tasks getTask(
            UUID projectId,
            UUID taskId)
    {
        getProject(projectId);

        return taskDao
                .findByIdAndProjectsIdIdAndIsActiveTrue(
                        taskId,
                        projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found"));
    }

    private void validateAssignee(UUID userId)
    {
        if (userId == null)
        {
            throw new BadRequestException(
                    "Assignee user id is required");
        }

        try
        {
            ResponseEntity<Object> response =
                    userInterface.getUserById(userId);

            if (!response.getStatusCode()
                    .is2xxSuccessful())
            {
                throw new BadRequestException(
                        "Assignee user not found");
            }
        }
        catch (Exception ex)
        {
            throw new BadRequestException(
                    "Assignee user not found");
        }
    }

    private void validateStatusTransition(
            TaskStatus currentStatus,
            TaskStatus newStatus)
    {
        if (currentStatus == TaskStatus.TODO
                && newStatus == TaskStatus.IN_PROGRESS)
        {
            return;
        }

        if (currentStatus == TaskStatus.IN_PROGRESS
                && newStatus == TaskStatus.DONE)
        {
            return;
        }

        if (currentStatus == TaskStatus.DONE
                && newStatus == TaskStatus.IN_PROGRESS)
        {
            return;
        }

        throw new BadRequestException(
                "Invalid status transition from "
                        + currentStatus
                        + " to "
                        + newStatus);
    }

    private TaskResponse buildTaskResponse(
            Tasks task)
    {
        TaskResponse response =
                new TaskResponse();

        BeanUtils.copyProperties(
                task,
                response);

        response.setProjectId(
                task.getProjectsId().getId());

        return response;
    }
}