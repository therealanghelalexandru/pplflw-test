package com.pplflw.test.pplflwtest.service.exceptions;

public class EmployeeNotFoundException extends RuntimeException{
    public EmployeeNotFoundException(String contractId) {
        super("Employee with contractId " + contractId + " not found");
    }
}
