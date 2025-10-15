package com.demo.crud.utils;
import com.demo.crud.students.dto.response.studentResponseDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    //

    // Handle validation errors (@Valid + BindingResult)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<studentResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage();

        studentResponseDTO response = new studentResponseDTO();
        response.setCode("-1");
        response.setStatus("FAILED");
        response.setMessage(errorMessage);
        response.setHttpStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle DB constraint violation
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<studentResponseDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        studentResponseDTO response = new studentResponseDTO();
        response.setCode("-1");
        response.setStatus("FAILED");
        response.setMessage("Database constraint violation: " + ex.getMostSpecificCause().getMessage());
        response.setHttpStatus(HttpStatus.CONFLICT);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Handle illegal arguments
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<studentResponseDTO> handleIllegalArgument(IllegalArgumentException ex) {
        studentResponseDTO response = new studentResponseDTO();
        response.setCode("-1");
        response.setStatus("FAILED");
        response.setMessage(ex.getMessage());
        response.setHttpStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle wrong HTTP methods
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<studentResponseDTO> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        studentResponseDTO response = new studentResponseDTO();
        response.setCode("-1");
        response.setStatus("FAILED");
        response.setMessage("Method not allowed: " + ex.getMessage());
        response.setHttpStatus(HttpStatus.METHOD_NOT_ALLOWED);

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    // Fallback: catch all runtime exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<studentResponseDTO> handleRuntime(RuntimeException ex) {
        studentResponseDTO response = new studentResponseDTO();
        response.setCode("-1");
        response.setStatus("FAILED");
        response.setMessage("Unexpected error occurred: " + ex.getMessage());
        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
