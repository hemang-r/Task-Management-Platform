package com.tmp.task_mgmt_service.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.tmp.task_mgmt_service.utills.ApplicationConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = ApplicationConstants.PROJECT_TABLE)
public class Projects 
{
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(columnDefinition = ApplicationConstants.TEXT)
	private String discription;
	
	private UUID ownerUserId;
	
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
