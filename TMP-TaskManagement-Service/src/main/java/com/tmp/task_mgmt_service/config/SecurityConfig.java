package com.tmp.task_mgmt_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tmp.task_mgmt_service.utills.JwtTokenFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig  {
	
	@Autowired
	private JwtTokenFilter jwtTokenFilter;
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
    	
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                		"/api/v1/auth/register",
                        "/api/v1/auth/login",
                        "/swagger-ui/index.html",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/v3/api-docs"
                ).permitAll()

                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
            	    .accessDeniedHandler((request, response, accessDeniedException) -> {

            	        response.setStatus(HttpStatus.FORBIDDEN.value());
            	        response.setContentType("application/json");

            	        response.getWriter().write("""
            	            {
            	                "status":403,
            	                "message":"Access Denied"
            	            }
            	            """);
            	    })
            	)
            .addFilterBefore(
            		jwtTokenFilter,
                    UsernamePasswordAuthenticationFilter.class
            );
        System.err.println("in security");

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return web -> web.ignoring()
                .requestMatchers("/");
    }

    
    
}