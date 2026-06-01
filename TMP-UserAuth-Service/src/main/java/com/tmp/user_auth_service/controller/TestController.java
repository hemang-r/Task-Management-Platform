package com.tmp.user_auth_service.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/user")
public class TestController {
	
	@GetMapping("/get_test_string")
	@PreAuthorize("hasRole('ADMIN')")
	public String getTestString() 
	{
		return "Test String from user which is required for task mgmt.";
	}

}
