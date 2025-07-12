package cts.tpm.exception;

import java.util.Date;
import java.util.List;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cts.tpm.model.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ApiException handleResponseNotFound(ResourceNotFoundException e, HttpServletRequest request,
			HttpServletResponse response) {
		ApiException apiException = ApiException.builder().code(response.getStatus()).message(e.getMessage())
				.path(request.getRequestURI()).when(new Date()).build();
		return apiException;

	}

	@ExceptionHandler(Exception.class)
	public ApiException handleGeneric(Exception e, HttpServletRequest request, HttpServletResponse response) {
		ApiException apiException = ApiException.builder().code(response.getStatus()).message(e.getMessage())
				.path(request.getRequestURI()).when(new Date()).build();
		return apiException;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ApiException handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
			HttpServletRequest request, HttpServletResponse response) {
		List<FieldError> listFiledErrors = e.getFieldErrors();
		StringBuilder sb = new StringBuilder();
		for (FieldError fieldError : listFiledErrors) {
			sb.append(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			sb.append(System.lineSeparator());
		}
		ApiException apiException = ApiException.builder().code(response.getStatus()).path(request.getRequestURI())
				.when(new Date()).message(sb.toString()).build();
		return apiException;

	}

}
