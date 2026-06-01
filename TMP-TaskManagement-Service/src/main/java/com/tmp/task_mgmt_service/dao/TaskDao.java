package com.tmp.task_mgmt_service.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import com.tmp.task_mgmt_service.entity.Tasks;
import com.tmp.task_mgmt_service.enums.TaskStatus;

@Repository
public interface TaskDao extends DataTablesRepository<Tasks, UUID>
{
    List<Tasks> findByProjectsIdIdAndIsActiveTrue(UUID projectId);

    List<Tasks> findByProjectsIdIdAndStatusAndIsActiveTrue(
            UUID projectId,
            TaskStatus status);
    
    Optional<Tasks> findByIdAndProjectsIdIdAndIsActiveTrue(
            UUID taskId,
            UUID projectId);

    List<Tasks> findByAssigneeUserIdAndIsActiveTrue(
            UUID assigneeUserId);

}