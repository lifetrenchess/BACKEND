package com.travel.userservice.exception;

import java.util.Date;
import java.util.List;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.travel.userservice.model.ApiException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(value = UserIdIsNotFoundException.class)
	public ApiException handleUserIdIsNotAvailable(UserIdIsNotFoundException e,
			HttpServletRequest request,HttpServletResponse response) {
			return ApiException.builder()
				.code(response.getStatus())
				.message(e.getMessage())
				.path(request.getRequestURI())
				.when(new Date())
				.build();
	}
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ApiException handleUserAlreadyExistsException(UserAlreadyExistsException e, HttpServletRequest request, HttpServletResponse response) {
        return ApiException.builder()
                .code(response.getStatus())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .when(new Date())
                .build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ApiException handleUserNotFoundException(UserNotFoundException e, HttpServletRequest request, HttpServletResponse response) {
        return ApiException.builder()
                .code(response.getStatus())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .when(new Date())
                .build();
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ApiException handleInvalidCredentialsException(InvalidCredentialsException e, HttpServletRequest request, HttpServletResponse response) {
        return ApiException.builder()
                .code(response.getStatus())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .when(new Date())
                .build();
    }
	
	@ExceptionHandler(value=UserUpdateFailureException.class)
	public ApiException handleUserIdIsNotAvailable(UserUpdateFailureException e,
			HttpServletRequest request,HttpServletResponse response) {
			return ApiException.builder()
					.code(response.getStatus())
					.message(e.getMessage())
					.path(request.getRequestURI())
					.when(new Date())
					.build();
		
	}
	
	
	@ExceptionHandler(value =  MethodArgumentNotValidException.class)
	public ApiException handleMethodArgumnetNotValidException(MethodArgumentNotValidException e,
			HttpServletRequest request,HttpServletResponse response) {
		List<FieldError> listFieldErrors=e.getFieldErrors();
		StringBuilder sb=new StringBuilder();
		for (FieldError fieldError : listFieldErrors) {
			sb.append(fieldError.getField()+" : "+fieldError.getDefaultMessage());
			sb.append(System.lineSeparator());
		}
		
		return ApiException.builder()
				.code(response.getStatus())
				.path(request.getRequestURI())
				.when(new Date())
				.message(sb.toString())
				.build();
		
	}
}
