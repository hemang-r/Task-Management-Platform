package com.tmp.task_mgmt_service.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import com.tmp.task_mgmt_service.entity.Projects;

@Repository
public interface ProjectDao extends DataTablesRepository<Projects, UUID>
{
	List<Projects> findByIsActiveTrue();

    boolean existsByNameIgnoreCaseAndIsActiveTrue(String name);

    boolean existsByNameIgnoreCaseAndIsActiveTrueAndIdNot(
            String name,
            UUID id);
}