package com.tmp.task_mgmt_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmp.task_mgmt_service.feign.UserInterface;

@RestController
@RequestMapping("/api/test/task")
public class TestController {
	
	@Autowired
	private UserInterface userInterface;
	
	@GetMapping("/get_test_string")
	public String getTestString() 
	{
		String output = "First it will fetch task";
		
		output = output + userInterface.getTestString();
		
		return output;
	}

	
	

}
