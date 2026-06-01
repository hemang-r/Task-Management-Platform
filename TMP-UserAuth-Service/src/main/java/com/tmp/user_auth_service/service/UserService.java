package com.tmp.user_auth_service.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.tmp.user_auth_service.dto.LoginRequest;
import com.tmp.user_auth_service.dto.UserRegistrationRequest;

import jakarta.validation.Valid;


public interface UserService 
{

	ResponseEntity<Object> userRegister(UserRegistrationRequest userRegistrationRequest);

	ResponseEntity<Object> userLogin(LoginRequest loginRequest);
	
	ResponseEntity<Object> getMyProfile();
	
	ResponseEntity<Object> getUserById(UUID id);

	ResponseEntity<Object> getAllUsers();
	
}
