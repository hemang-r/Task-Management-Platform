package com.tmp.user_auth_service.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("TMP-TASKMANAGEMENT-SERVICE")
public interface TaskInterface {
 
}
