package com.tmp.task_mgmt_service.exception_handler;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorResponse {

    private LocalDateTime timestamp;
    private Integer status;
    private String message;

    public ErrorResponse(
            LocalDateTime timestamp,
            Integer status,
            String message) {

        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
    }
}
