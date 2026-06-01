package com.tmp.task_mgmt_service.controller;


import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.tmp.task_mgmt_service.dto.CreateProjectRequest;
import com.tmp.task_mgmt_service.dto.UpdateProjectRequest;
import com.tmp.task_mgmt_service.service.ProjectService;
import com.tmp.task_mgmt_service.utills.ApplicationURIConstants;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApplicationURIConstants.PROJECTS)
@RequiredArgsConstructor
public class ProjectController
{
    private final ProjectService projectService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> createProject(
            @Valid @RequestBody CreateProjectRequest request)
    {
        return projectService.createProject(request);
    }

    @GetMapping
    public ResponseEntity<Object> getAllProjects()
    {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProjectById(
            @PathVariable UUID id)
    {
        return projectService.getProjectById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateProject(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProjectRequest request)
    {
        return projectService.updateProject(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteProject(
            @PathVariable UUID id)
    {
        return projectService.deleteProject(id);
    }
}