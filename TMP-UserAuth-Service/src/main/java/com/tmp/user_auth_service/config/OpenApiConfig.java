package com.tmp.user_auth_service.config;

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
                        .title("User AuthService API")
                        .description("USer Auth Platform APIs")
                        .version("1.0"));
    }
}