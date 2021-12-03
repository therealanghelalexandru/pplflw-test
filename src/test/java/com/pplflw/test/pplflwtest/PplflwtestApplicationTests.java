package com.pplflw.test.pplflwtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pplflw.test.pplflwtest.model.Employee;
import com.pplflw.test.pplflwtest.model.EmployeeState;
import com.pplflw.test.pplflwtest.service.EmployeeService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
class PplflwtestApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EmployeeService employeeService;

    @Test
    @SneakyThrows
    public void employeeCreatedWithAddedStatusAndContractId() {
        mvc.perform(post("/api/employee/add")
                        .content(new ObjectMapper().writeValueAsString(getDefEmployee()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(EmployeeState.ADDED.name()))
                .andExpect(jsonPath("$.contractId").exists());
    }

    @Test
    @SneakyThrows
    void employeeWasNotCreatedDueToInputMismatch() {
        Employee employee = new Employee();

        mvc.perform(post("/api/employee/add")
                        .content(new ObjectMapper().writeValueAsString(employee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )


                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.firstName").value("First Name must be provided"))
                .andExpect(jsonPath("$.errors.lastName").value("Last Name must be provided"))
                .andExpect(jsonPath("$.errors.email").value("Email must be provided"))
                .andExpect(jsonPath("$.errors.age").value("Employee age  must be above 18"));
    }

    @Test
    @SneakyThrows
    public void employeeUpdateToInCheck() {
        UUID contractId = employeeService.save(getDefEmployee()).getContractId();
        mvc.perform(patch("/api/employee/{contractId}/state-change/{state}", contractId.toString(), EmployeeState.IN_CHECK)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.contractId").value(contractId.toString()))
                .andExpect(jsonPath("$.state").value(EmployeeState.IN_CHECK.name()));
    }

    @Test
    @SneakyThrows
    public void employeeUpdateWithWrongContractIdResultsInNotFound() {
        UUID contractId = UUID.randomUUID();
        mvc.perform(patch("/api/employee/{contractId}/state-change/{state}", contractId.toString(), EmployeeState.IN_CHECK)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Employee with contractId " + contractId + " not found"));
    }

    @Test
    @SneakyThrows
    public void employeeStateUpdateToApprovedResultInBadRequest() {
        UUID contractId = employeeService.save(getDefEmployee()).getContractId();
        mvc.perform(patch("/api/employee/{contractId}/state-change/{state}", contractId.toString(), EmployeeState.ADDED)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("State transition to ADDED for contract " + contractId + " not allowed"));
    }

    @Test
    @SneakyThrows
    public void employeeStateUpdateTwiceToInCheckResultsInBadRequest() {
        UUID contractId = employeeService.save(getDefEmployee()).getContractId();
        mvc.perform(patch("/api/employee/{contractId}/state-change/{state}", contractId.toString(), EmployeeState.IN_CHECK)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.contractId").value(contractId.toString()))
                .andExpect(jsonPath("$.state").value(EmployeeState.IN_CHECK.name()));

        mvc.perform(patch("/api/employee/{contractId}/state-change/{state}", contractId.toString(), EmployeeState.IN_CHECK)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("State transition to IN_CHECK for contract " + contractId + " not allowed"));
    }

    private Employee getDefEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("firstName");
        employee.setLastName("lastName");
        employee.setAge(18);
        employee.setEmail("test@email.com");
        return employee;
    }
}
