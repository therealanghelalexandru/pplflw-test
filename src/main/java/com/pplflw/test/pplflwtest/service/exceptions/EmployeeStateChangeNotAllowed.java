package com.pplflw.test.pplflwtest.service.exceptions;

import com.pplflw.test.pplflwtest.model.EmployeeState;

public class EmployeeStateChangeNotAllowed extends RuntimeException{

    public EmployeeStateChangeNotAllowed(String contractId, EmployeeState employeeState) {
        super("State transition to " + employeeState + " for contract " + contractId + " not allowed");
    }
}
