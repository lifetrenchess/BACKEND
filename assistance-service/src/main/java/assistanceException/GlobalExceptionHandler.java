package assistanceException;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Date;
import java.util.List;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RequestIdNotFoundException.class)
    public ApiException handleRequestIdIsNotFound(RequestIdNotFoundException e,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) {
        return ApiException.builder()
                .code(HttpServletResponse.SC_NOT_FOUND)
                .message(e.getMessage())
                .path(request.getRequestURI())
                .when(new Date())
                .build();
    }

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
	public ApiException handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
			HttpServletRequest request, HttpServletResponse response) {
			List<FieldError> listFieldErrors=e.getFieldErrors();
			StringBuilder sb=new StringBuilder();
			for (FieldError fieldError : listFieldErrors) {
				sb.append(fieldError.getField()+" : "+fieldError.getDefaultMessage());
				sb.append(System.lineSeparator());
				
			}
			ApiException apiException=ApiException.builder().code(response.getStatus())
			.path(request.getRequestURI())
			.when(new Date()).message(sb.toString()).build();
		return apiException;
	}
}

    