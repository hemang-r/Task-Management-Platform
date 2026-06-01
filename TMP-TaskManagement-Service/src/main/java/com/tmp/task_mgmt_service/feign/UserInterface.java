package com.tmp.task_mgmt_service.feign;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tmp.task_mgmt_service.config.FeignClientConfig;



//Defines methods for communicating with the User Service in a microservice architecture.

@FeignClient( name="TMP-USERAUTH-SERVICE",configuration = FeignClientConfig.class)
public interface UserInterface {
	
	@GetMapping("/api/test/user/get_test_string")
	public String getTestString();
	
	@GetMapping("/api/v1/users/{id}")
	public ResponseEntity<Object> getUserById(@PathVariable UUID id);
}
