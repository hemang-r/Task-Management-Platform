package com.tmp.user_auth_service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class UserProfileResponse {

    private UUID id;
    private String email;
    private String fullName;
    private String role;
    private LocalDateTime createdAt;
    private Boolean isActive;

}
