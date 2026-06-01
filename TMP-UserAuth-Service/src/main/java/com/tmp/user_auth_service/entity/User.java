package com.tmp.user_auth_service.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tmp.user_auth_service.utills.ApplicationConstants;

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
@Table(name = ApplicationConstants.USER_TABLE)
public class User 
{
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(unique = true)
	private String email;
	@JsonIgnore
	private String passwordHash;
	private String fullName;
	private String role;   // ADMIN / USER
	@CreationTimestamp
	private LocalDateTime createdAt;
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
