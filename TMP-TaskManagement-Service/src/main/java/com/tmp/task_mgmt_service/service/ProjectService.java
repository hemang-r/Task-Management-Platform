package com.tmp.task_mgmt_service.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.tmp.task_mgmt_service.dto.CreateProjectRequest;
import com.tmp.task_mgmt_service.dto.UpdateProjectRequest;

public interface ProjectService
{
    ResponseEntity<Object> createProject(CreateProjectRequest request);

    ResponseEntity<Object> getAllProjects();

    ResponseEntity<Object> getProjectById(UUID id);

    ResponseEntity<Object> updateProject(UUID id,UpdateProjectRequest request);

    ResponseEntity<Object> deleteProject(UUID id);
}
