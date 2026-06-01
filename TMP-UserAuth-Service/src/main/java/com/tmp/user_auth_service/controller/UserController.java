package com.tmp.user_auth_service.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmp.user_auth_service.service.UserService;
import com.tmp.user_auth_service.utills.ApplicationConstants;
import com.tmp.user_auth_service.utills.ApplicationURIConstants;
import com.tmp.user_auth_service.utills.CommonServices;

@RestController
@RequestMapping(ApplicationURIConstants.USER)
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private CommonServices commonServices;

	@Autowired
	private UserService userService;

	@GetMapping(ApplicationURIConstants.ME)
	public ResponseEntity<Object> getMyProfile() {
		return userService.getMyProfile();
	}

	@GetMapping(ApplicationURIConstants.ID_PATH)
	public ResponseEntity<Object> getUserById(@PathVariable UUID id) {

		return userService.getUserById(id);
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Object> getAllUsers() {

		return userService.getAllUsers();
	}

}
