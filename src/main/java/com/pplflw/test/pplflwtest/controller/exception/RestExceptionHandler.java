package com.pplflw.test.pplflwtest.controller.exception;

import com.pplflw.test.pplflwtest.service.exceptions.EmployeeNotFoundException;
import com.pplflw.test.pplflwtest.service.exceptions.EmployeeStateChangeNotAllowed;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    protected ResponseEntity<Object> handleEmployeeNotFoundException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, Map.of("error", ex.getMessage()),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({EmployeeStateChangeNotAllowed.class, IllegalArgumentException.class})
    protected ResponseEntity<Object> handleStateChangeNotAllowException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, Map.of("error", ex.getMessage()),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult result = ex.getBindingResult();

        List<FieldError> fieldErrors = result.getFieldErrors();

        HashMap<String, String> errorMap = new HashMap<>();

        fieldErrors.forEach(it -> errorMap.put(it.getField(), it.getDefaultMessage()));

        return ResponseEntity.badRequest().body(Map.of("errors", errorMap));
    }
}
