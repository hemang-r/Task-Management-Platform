package com.tmp.user_auth_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmp.user_auth_service.dto.LoginRequest;
import com.tmp.user_auth_service.dto.UserRegistrationRequest;
import com.tmp.user_auth_service.service.UserService;
import com.tmp.user_auth_service.utills.ApplicationConstants;
import com.tmp.user_auth_service.utills.ApplicationURIConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping(ApplicationURIConstants.AUTH)
public class AuthenticationController 
{
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@PostMapping(ApplicationURIConstants.REGISTER)
	public ResponseEntity<Object> userRegister(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) 
	{
		LOGGER.info(ApplicationConstants.ENTER_LABEL);
		return userService.userRegister(userRegistrationRequest);
	}
	
	@PostMapping(ApplicationURIConstants.LOGIN)
	public ResponseEntity<Object> userLogin(@Valid @RequestBody LoginRequest loginRequest) 
	{
		LOGGER.info(ApplicationConstants.ENTER_LABEL);
		return userService.userLogin(loginRequest);
	}
}
