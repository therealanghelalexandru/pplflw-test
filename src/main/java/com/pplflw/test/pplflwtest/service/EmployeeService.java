package com.pplflw.test.pplflwtest.service;

import com.pplflw.test.pplflwtest.model.Employee;
import com.pplflw.test.pplflwtest.model.EmployeeState;
import com.pplflw.test.pplflwtest.repository.EmployeeRepository;
import com.pplflw.test.pplflwtest.service.exceptions.EmployeeNotFoundException;
import com.pplflw.test.pplflwtest.service.exceptions.EmployeeStateChangeNotAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee save(Employee employee) {
        employee.setState(EmployeeState.ADDED);
        employee.setContractId(UUID.randomUUID());
        return employeeRepository.save(employee);
    }

    public Employee changeState(String contractId, EmployeeState state) {
        Employee employee = employeeRepository.findByContractId(UUID.fromString(contractId)).orElseThrow(() -> new EmployeeNotFoundException(contractId));

        if (EmployeeState.ADDED == state || employee.getState() == EmployeeState.IN_CHECK) {
            throw new EmployeeStateChangeNotAllowed(contractId, state);
        }
        employee.setState(EmployeeState.IN_CHECK);
        return employeeRepository.save(employee);
    }

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }
}
