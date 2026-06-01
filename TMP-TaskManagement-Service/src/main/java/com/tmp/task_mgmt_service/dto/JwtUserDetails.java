package com.tmp.task_mgmt_service.dto;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Data;

@Data
public class JwtUserDetails 
{
	private UUID id;
	
	private String email;
	
	private String role;
	
	 public Collection<? extends GrantedAuthority> getAuthorities() {
	        return List.of(
	                new SimpleGrantedAuthority("ROLE_" + role));
	    }

	public JwtUserDetails(UUID id, String email, String role) {
		super();
		this.id = id;
		this.email = email;
		this.role = role;
	}
	
}
