package com.pplflw.test.pplflwtest.controller;

import com.pplflw.test.pplflwtest.model.Employee;
import com.pplflw.test.pplflwtest.model.EmployeeState;
import com.pplflw.test.pplflwtest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/employee/add")
    Employee addEmployee(@Validated @RequestBody Employee employee) {
        employeeService.save(employee);
        return employeeService.save(employee);
    }

    @GetMapping("/employee/all")
    List<Employee> getEmployees() {
        return employeeService.getAll();
    }

    @PatchMapping("employee/{contractId}/state-change/{state}")
    Employee changeEmployeeState(@PathVariable("contractId") String contractId, @PathVariable("state") EmployeeState state) {
        return employeeService.changeState(contractId, state);
    }
}
