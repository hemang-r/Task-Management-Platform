package com.tmp.task_mgmt_service.exception_handler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tmp.task_mgmt_service.utills.ApplicationConstants;
import com.tmp.task_mgmt_service.utills.ApplicationResponseConstants;
import com.tmp.task_mgmt_service.utills.CommonServices;
import com.tmp.task_mgmt_service.utills.ErrorDataEnum;
import com.tmp.task_mgmt_service.utills.GenericResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	 private static final Logger LOGGER =
	            LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@Autowired
    private CommonServices commonServices;
	
	 @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<Map<String, String>> handleValidationException(
	            MethodArgumentNotValidException ex) {
		 	
		 LOGGER.info(ApplicationConstants.ENTER_LABEL);
	        Map<String, String> errors = new HashMap<>();

	        ex.getBindingResult().getFieldErrors()
	                .forEach(error ->
	                        errors.put(error.getField(),
	                                error.getDefaultMessage()));

	        return ResponseEntity.badRequest().body(errors);
	    }
	 
	 @ExceptionHandler(BadCredentialsException.class)
		public ResponseEntity<?> processRuntimeException(RuntimeException e) 
		{
		 LOGGER.info(ApplicationConstants.ENTER_LABEL);
			GenericResponse resp = new GenericResponse();
			if(e.getMessage().equals(commonServices.getMessageByCode(ErrorDataEnum.INVALID_TOKEN_MESSAGE.getCode()))) 
			{
				resp.setMessage(e.getMessage());
				resp.setCode(ApplicationResponseConstants.INVALID_API_TOKEN);
			}
			LOGGER.info(ApplicationConstants.EXIT_LABEL);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);

		}
	 
	 @ExceptionHandler(ResourceNotFoundException.class)
	    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
	            ResourceNotFoundException ex) {

	        LOGGER.error("Resource not found: {}", ex.getMessage());

	        ErrorResponse response =
	                new ErrorResponse(
	                        LocalDateTime.now(),
	                        HttpStatus.NOT_FOUND.value(),
	                        ex.getMessage());

	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(response);
	    }

	    @ExceptionHandler(BadRequestException.class)
	    public ResponseEntity<ErrorResponse> handleBadRequestException(
	            BadRequestException ex) {

	        LOGGER.error("Bad request: {}", ex.getMessage());

	        ErrorResponse response =
	                new ErrorResponse(
	                        LocalDateTime.now(),
	                        HttpStatus.BAD_REQUEST.value(),
	                        ex.getMessage());

	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(response);
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<ErrorResponse> handleException(
	            Exception ex) {

	        LOGGER.error("Unhandled exception", ex);

	        ErrorResponse response =
	                new ErrorResponse(
	                        LocalDateTime.now(),
	                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
	                        "Internal server error");

	        return ResponseEntity.status(
	                HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(response);
	    }
	    
	    @ExceptionHandler(AuthorizationDeniedException.class)
	    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(
	            AuthorizationDeniedException ex) {

	        LOGGER.error("Access denied: {}", ex.getMessage());

	        ErrorResponse response =
	                new ErrorResponse(
	                        LocalDateTime.now(),
	                        HttpStatus.FORBIDDEN.value(),
	                        "Access Denied");

	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                .body(response);
	    }
	    

	    @ExceptionHandler(AccessDeniedException.class)
	    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
	            AccessDeniedException ex) {

	        LOGGER.error("Access denied: {}", ex.getMessage());

	        ErrorResponse response =
	                new ErrorResponse(
	                        LocalDateTime.now(),
	                        HttpStatus.FORBIDDEN.value(),
	                        "Access Denied");

	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                .body(response);
	    }

}
