package com.tmp.task_mgmt_service.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.tmp.task_mgmt_service.enums.TaskPriority;
import com.tmp.task_mgmt_service.enums.TaskStatus;
import com.tmp.task_mgmt_service.utills.ApplicationConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = ApplicationConstants.TASK_TABLE)
public class Tasks 
{
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@ManyToOne
	@JoinColumn(name = "projects_id")
	private Projects projectsId;
	
	@Column(nullable = false)
	private String title;
	
	@Column(columnDefinition = ApplicationConstants.TEXT)
	private String discription;
	
	@Enumerated(EnumType.STRING)
	private TaskStatus status;  //  TODO | IN_PROGRESS | DONE
	 
	@Enumerated(EnumType.STRING)
	private TaskPriority priority; //   LOW | MEDIUM | HIGH
	
	private UUID assigneeUserId;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	private Boolean isActive;
	
	@PrePersist
	public void setDefaults()
	{
		if(isActive == null)
		{
			isActive = Boolean.TRUE;
		}
	}
}
