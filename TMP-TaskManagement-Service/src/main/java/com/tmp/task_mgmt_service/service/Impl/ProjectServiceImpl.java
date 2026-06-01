package com.tmp.task_mgmt_service.service.Impl;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tmp.task_mgmt_service.dao.ProjectDao;
import com.tmp.task_mgmt_service.dto.CreateProjectRequest;
import com.tmp.task_mgmt_service.dto.ProjectResponse;
import com.tmp.task_mgmt_service.dto.UpdateProjectRequest;
import com.tmp.task_mgmt_service.entity.Projects;
import com.tmp.task_mgmt_service.exception_handler.BadRequestException;
import com.tmp.task_mgmt_service.exception_handler.ResourceNotFoundException;
import com.tmp.task_mgmt_service.feign.UserInterface;
import com.tmp.task_mgmt_service.service.ProjectService;
import com.tmp.task_mgmt_service.utills.ApplicationConstants;
import com.tmp.task_mgmt_service.utills.CommonServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ProjectServiceImpl implements ProjectService
{
	private static final Logger LOGGER =
	        LoggerFactory.getLogger(ProjectServiceImpl.class);
	
	@Autowired
    private ProjectDao projectDao;
	
	@Autowired
    private CommonServices commonServices;
	
	@Autowired
	private UserInterface userInterface;
	
	private void validateOwnerUser(UUID ownerUserId)
	{
	    LOGGER.info(ApplicationConstants.ENTER_LABEL);

	    if (ownerUserId == null)
	    {
	        throw new BadRequestException(
	                "Owner user id is required");
	    }

	    try
	    {
	        ResponseEntity<Object> response =
	                userInterface.getUserById(ownerUserId);

	        if (!response.getStatusCode().is2xxSuccessful())
	        {
	            throw new BadRequestException(
	                    "Owner user not found");
	        }
	    }
	    catch (Exception ex)
	    {
	        throw new BadRequestException(
	                "Owner user not found");
	    }

	    LOGGER.info(ApplicationConstants.EXIT_LABEL);
	}

	@Override
	public ResponseEntity<Object> createProject(
	        CreateProjectRequest request)
	{
	    LOGGER.info(ApplicationConstants.ENTER_LABEL);

	    boolean projectExists =
	            projectDao.existsByNameIgnoreCaseAndIsActiveTrue(
	                    request.getName());

	    if (projectExists)
	    {
	        throw new BadRequestException(
	                "Project name already exists");
	    }

	    validateOwnerUser(
	            request.getOwnerUserId());

	    Projects project = new Projects();

	    BeanUtils.copyProperties(
	            request,
	            project);

	    project.setIsActive(true);

	    projectDao.save(project);

	    LOGGER.info(ApplicationConstants.EXIT_LABEL);

	    return ResponseEntity.ok(
	            commonServices.generateGenericSuccessResponse(
	                    "Project created successfully"));
	}

	@Override
	public ResponseEntity<Object> getAllProjects()
	{
	    LOGGER.info(ApplicationConstants.ENTER_LABEL);

	    List<ProjectResponse> projects =
	            projectDao.findByIsActiveTrue()
	                      .stream()
	                      .map(this::buildProjectResponse)
	                      .collect(Collectors.toList());

	    LOGGER.info(ApplicationConstants.EXIT_LABEL);

	    return ResponseEntity.ok(
	            commonServices.generateGenericSuccessResponse(
	                    projects));
	}

	@Override
	public ResponseEntity<Object> getProjectById(
	        UUID id)
	{
	    LOGGER.info(ApplicationConstants.ENTER_LABEL);

	    Projects project =
	            projectDao.findById(id)
	                      .orElseThrow(() ->
	                              new ResourceNotFoundException(
	                                      "Project not found"));

	    if (!Boolean.TRUE.equals(project.getIsActive()))
	    {
	        throw new ResourceNotFoundException(
	                "Project not found");
	    }

	    LOGGER.info(ApplicationConstants.EXIT_LABEL);

	    return ResponseEntity.ok(
	            commonServices.generateGenericSuccessResponse(
	                    buildProjectResponse(project)));
	}

	@Override
	public ResponseEntity<Object> updateProject(
	        UUID id,
	        UpdateProjectRequest request)
	{
	    LOGGER.info(ApplicationConstants.ENTER_LABEL);

	    Projects project =
	            projectDao.findById(id)
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Project not found"));

	    boolean duplicateName =
	            projectDao.existsByNameIgnoreCaseAndIsActiveTrueAndIdNot(
	                    request.getName(),
	                    id);

	    if (duplicateName)
	    {
	        throw new BadRequestException(
	                "Project name already exists");
	    }

	    validateOwnerUser(
	            request.getOwnerUserId());

	    project.setName(request.getName());
	    project.setDiscription(request.getDiscription());
	    project.setOwnerUserId(request.getOwnerUserId());

	    if (request.getIsActive() != null)
	    {
	        project.setIsActive(request.getIsActive());
	    }

	    projectDao.save(project);

	    LOGGER.info(ApplicationConstants.EXIT_LABEL);

	    return ResponseEntity.ok(
	            commonServices.generateGenericSuccessResponse(
	                    "Project updated successfully"));
	}

	@Override
	public ResponseEntity<Object> deleteProject(
	        UUID id)
	{
	    LOGGER.info(ApplicationConstants.ENTER_LABEL);

	    Projects project =
	            projectDao.findById(id)
	                      .orElseThrow(() ->
	                              new ResourceNotFoundException(
	                                      "Project not found"));

	    project.setIsActive(false);

	    projectDao.save(project);

	    LOGGER.info(ApplicationConstants.EXIT_LABEL);

	    return ResponseEntity.ok(
	            commonServices.generateGenericSuccessResponse(
	                    "Project deleted successfully"));
	}
	
	private ProjectResponse buildProjectResponse(
            Projects project)
    {
        ProjectResponse response =
                new ProjectResponse();

        BeanUtils.copyProperties(
                project,
                response);

        return response;
    }
}
