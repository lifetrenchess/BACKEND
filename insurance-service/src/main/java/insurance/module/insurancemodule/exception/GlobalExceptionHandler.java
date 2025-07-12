package insurance.module.insurancemodule.exception;

import insurance.module.insurancemodule.model.ApiException; // Ensure this import is correct
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus; // Import HttpStatus
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus; // Import ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.List;

// Assuming InsuranceIdIsNotFoundException is defined in the same package or another imported package
// If it's in the same package, no import needed. Otherwise, add:
// import insurance.module.insurancemodule.exception.InsuranceIdIsNotFoundException; 
// (or wherever it's located)

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handler for your custom InsuranceIdIsNotFoundException
    @ExceptionHandler(InsuranceIdIsNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // Explicitly set HTTP 404 Not Found
    public ApiException handleInsuranceIdIsNotFound(InsuranceIdIsNotFoundException e,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        return ApiException.builder()
                .code(HttpStatus.NOT_FOUND.value()) // Use HttpStatus for the code
                .message(e.getMessage())
                .path(request.getRequestURI())
                .when(new Date())
                .build();
    }

    // Handler for MethodArgumentNotValidException (validation errors)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Explicitly set HTTP 400 Bad Request
    public ApiException handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response) {
        // More robust way to get field errors from BindingResult
        List<FieldError> listFieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder();

        for (FieldError fieldError : listFieldErrors) {
            sb.append(fieldError.getField()).append(" : ").append(fieldError.getDefaultMessage());
            sb.append(System.lineSeparator());
        }

        ApiException apiException = ApiException.builder()
                .code(HttpStatus.BAD_REQUEST.value()) // Use 400 for validation errors
                .path(request.getRequestURI())
                .when(new Date())
                .message(sb.toString().trim()) // Trim to remove potential trailing newline
                .build();
        return apiException;
    }

    // You can add more @ExceptionHandler methods here for other exception types
    // For example:
    // @ExceptionHandler(Exception.class) // Generic handler for any other unhandled exceptions
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // public ApiException handleGenericException(Exception e, HttpServletRequest request) {
    //     return ApiException.builder()
    //             .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
    //             .message("An unexpected error occurred: " + e.getMessage())
    //             .path(request.getRequestURI())
    //             .when(new Date())
    //             .build();
    // }
}