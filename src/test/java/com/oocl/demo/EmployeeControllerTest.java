package com.oocl.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.oocl.demo.model.Employee;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureMockMvc
@SpringBootTest
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private Employee johnSmith() {
        return new Employee(
                1,
                "John Smith",
                30,
                "MALE",
                60000
        );
    }

    private Employee maryJane() {
        return new Employee(
                2,
                "Mary Jane",
                28,
                "FEMALE",
                65000
        );
    }

    @Test
    void should_create_employee_when_post_given_a_valid_body() throws Exception {
        String requestBody = """
                    {
                        "name": "John Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 60000
                    }
                """;
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void should_return_employee_when_get_given_employee_id() throws Exception {
        mockMvc.perform(get("/employees/{id}",1)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.salary").value(60000));
    }

    @Test
    void should_return_male_employee_when_get_given_gender_male() throws Exception {
        String requestBody = """
                    {
                        "name": "Mary Jane",
                        "age": 28,
                        "gender": "FEMALE",
                        "salary": 65000
                    }
                """;
        Employee expectedEmployee = johnSmith();
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        mockMvc.perform(get("/employees?gender=male")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expectedEmployee.getId()))
                .andExpect(jsonPath("$[0].name").value(expectedEmployee.getName()))
                .andExpect(jsonPath("$[0].age").value(expectedEmployee.getAge()))
                .andExpect(jsonPath("$[0].gender").value(expectedEmployee.getGender()))
                .andExpect(jsonPath("$[0].salary").value(expectedEmployee.getSalary()));
    }

    @Test
    void
}
