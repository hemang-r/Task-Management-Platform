package com.tmp.task_mgmt_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig
{
    @Bean
    public OpenAPI taskManagementOpenAPI()
    {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Management Service API")
                        .description("Task Management Platform APIs")
                        .version("1.0"));
    }
}