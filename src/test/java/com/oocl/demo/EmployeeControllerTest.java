package com.oocl.demo;

import com.oocl.demo.model.Employee;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private Employee johnSmith() {
        return new Employee(1, "John Smith", 30, "MALE", 60000);
    }

    private Employee maryJane() {
        return new Employee(2, "Mary Jane", 28, "FEMALE", 65000);
    }

    private Employee newJohnSmith() {
        return new Employee(1, "John Smith", 31, "MALE", 70000);
    }

    @Test
    @Order(1)
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
    @Order(2)
    void should_return_employee_when_get_given_employee_id() throws Exception {
        mockMvc.perform(get("/employees/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.salary").value(60000));
    }

    @Test
    @Order(3)
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
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(expectedEmployee.getId()))
                .andExpect(jsonPath("$[0].name").value(expectedEmployee.getName()))
                .andExpect(jsonPath("$[0].age").value(expectedEmployee.getAge()))
                .andExpect(jsonPath("$[0].gender").value(expectedEmployee.getGender()))
                .andExpect(jsonPath("$[0].salary").value(expectedEmployee.getSalary()));
    }

    @Test
    @Order(4)
    void should_return_female_employee_when_get_given_gender_female() throws Exception {
        Employee expectedEmployee = maryJane();
        mockMvc.perform(get("/employees?gender=female")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(expectedEmployee.getId()))
                .andExpect(jsonPath("$[0].name").value(expectedEmployee.getName()))
                .andExpect(jsonPath("$[0].age").value(expectedEmployee.getAge()))
                .andExpect(jsonPath("$[0].gender").value(expectedEmployee.getGender()))
                .andExpect(jsonPath("$[0].salary").value(expectedEmployee.getSalary()));
    }
    @Test
    @Order(5)
    void should_return_employee_list_when_get_given_employees() throws Exception {
        Employee expectedEmployee_1 = johnSmith();
        Employee expectedEmployee_2 = maryJane();
        mockMvc.perform(get("/employees/all")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(expectedEmployee_1.getId()))
                .andExpect(jsonPath("$[0].name").value(expectedEmployee_1.getName()))
                .andExpect(jsonPath("$[0].age").value(expectedEmployee_1.getAge()))
                .andExpect(jsonPath("$[0].gender").value(expectedEmployee_1.getGender()))
                .andExpect(jsonPath("$[0].salary").value(expectedEmployee_1.getSalary()))
                .andExpect(jsonPath("$[1].id").value(expectedEmployee_2.getId()))
                .andExpect(jsonPath("$[1].name").value(expectedEmployee_2.getName()))
                .andExpect(jsonPath("$[1].age").value(expectedEmployee_2.getAge()))
                .andExpect(jsonPath("$[1].gender").value(expectedEmployee_2.getGender()))
                .andExpect(jsonPath("$[1].salary").value(expectedEmployee_2.getSalary()));
    }
    @Test
    @Order(6)
    void should_update_employee_when_put_given_employee_update_infos() throws Exception {
        Employee updatedEmployee = newJohnSmith();
        String requestBody = """
                    {
                        "age": 31,
                        "salary": 70000
                    }
                """;
        mockMvc.perform(put("/employees/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedEmployee.getId()))
                .andExpect(jsonPath("$.name").value(updatedEmployee.getName()))
                .andExpect(jsonPath("$.age").value(updatedEmployee.getAge()))
                .andExpect(jsonPath("$.gender").value(updatedEmployee.getGender()))
                .andExpect(jsonPath("$.salary").value(updatedEmployee.getSalary()));
    }

    @Test
    void should_delete_employee_when_delete_given_employee_exists() throws Exception {
        Employee updatedEmployee = maryJane();
        mockMvc.perform(delete("/employees/{id}",2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedEmployee.getId()))
                .andExpect(jsonPath("$.name").value(updatedEmployee.getName()))
                .andExpect(jsonPath("$.age").value(updatedEmployee.getAge()))
                .andExpect(jsonPath("$.gender").value(updatedEmployee.getGender()))
                .andExpect(jsonPath("$.salary").value(updatedEmployee.getSalary()));
    }

}
