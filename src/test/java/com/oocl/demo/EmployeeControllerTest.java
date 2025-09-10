package com.oocl.demo;

import com.oocl.demo.controller.EmployeeController;
import com.oocl.demo.model.Employee;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeController employeeController;

    @BeforeEach
    public void resetData() {
        employeeController.resetData();
    }

    private Employee johnSmith() {
        return new Employee(1, "John Smith", 30, "MALE", 60000);
    }

    private Employee maryJane() {
        return new Employee(2, "Mary Jane", 28, "FEMALE", 65000);
    }

    private Employee newJohnSmith() {
        return new Employee(1, "John Smith", 31, "MALE", 70000);
    }

    private Employee benSmith() {
        return new Employee(3, "Ben Smith", 30, "MALE", 50000);
    }

    private Employee amySmith() {
        return new Employee(4, "Amy Smith", 30, "FEMALE", 80000);
    }

    private Employee annClarkson() {
        return new Employee(5, "Ann Clarkson", 28, "FEMALE", 55000);
    }

    private Employee claraSmith() {
        return new Employee(6, "Clara Smith", 40, "FEMALE", 85000);
    }

    private Employee danielClarkson() {
        return new Employee(7, "Daniel Clarkson", 32, "MALE", 65000);
    }

    private void addData(String requestBody) throws Exception {
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));
    }

    @Test
//    @Order(1)
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
//    @Order(2)
    void should_return_employee_when_get_given_employee_id() throws Exception {
        String requestBody = """
                    {
                        "name": "John Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 60000
                    }
                """;
        addData(requestBody);
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
//    @Order(3)
    void should_return_male_employee_when_get_given_gender_male() throws Exception {
        String requestBody = """
                    {
                        "name": "John Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 60000
                    }
                """;
        addData(requestBody);
        requestBody = """
                    {
                        "name": "Mary Jane",
                        "age": 28,
                        "gender": "FEMALE",
                        "salary": 65000
                    }
                """;
        addData(requestBody);
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
//    @Order(4)
    void should_return_female_employee_when_get_given_gender_female() throws Exception {
        String requestBody = """
                    {
                        "name": "John Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 60000
                    }
                """;
        addData(requestBody);
        requestBody = """
                    {
                        "name": "Mary Jane",
                        "age": 28,
                        "gender": "FEMALE",
                        "salary": 65000
                    }
                """;
        addData(requestBody);
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
//    @Order(5)
    void should_return_employee_list_when_get_given_employees() throws Exception {
        String requestBody = """
                    {
                        "name": "John Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 60000
                    }
                """;
        addData(requestBody);
        requestBody = """
                    {
                        "name": "Mary Jane",
                        "age": 28,
                        "gender": "FEMALE",
                        "salary": 65000
                    }
                """;
        addData(requestBody);
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
//    @Order(6)
    void should_update_employee_when_put_given_employee_update_infos() throws Exception {
        String requestBody = """
                    {
                        "name": "John Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 60000
                    }
                """;
        addData(requestBody);
        requestBody = """
                    {
                        "name": "Mary Jane",
                        "age": 28,
                        "gender": "FEMALE",
                        "salary": 65000
                    }
                """;
        addData(requestBody);
        Employee updatedEmployee = newJohnSmith();
        requestBody = """
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
//    @Order(7)
    void should_delete_employee_when_delete_given_employee_exists() throws Exception {
        String requestBody = """
                    {
                        "name": "John Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 60000
                    }
                """;
        addData(requestBody);
        requestBody = """
                    {
                        "name": "Mary Jane",
                        "age": 28,
                        "gender": "FEMALE",
                        "salary": 65000
                    }
                """;
        addData(requestBody);
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

    @Test
//    @Order(8)
    void should_return_204_employee_when_delete_given_employee_not_exists() throws Exception {
        String requestBody = """
                    {
                        "name": "John Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 60000
                    }
                """;
        addData(requestBody);
        mockMvc.perform(delete("/employees/{id}",2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
//    @Order(9)
    void should_return_page_query_when_page_query_given_enough_employees() throws Exception {
        String requestBody = """
                    {
                        "name": "John Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 60000
                    }
                """;
        addData(requestBody);
        requestBody = """
                    {
                        "name": "Mary Jane",
                        "age": 28,
                        "gender": "FEMALE",
                        "salary": 65000
                    }
                """;
        addData(requestBody);
        requestBody = """
                    {
                        "name": "Ben Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 50000
                    }
                """;
        addData(requestBody);
        requestBody = """
                    {
                        "name": "Amy Smith",
                        "age": 30,
                        "gender": "FEMALE",
                        "salary": 80000
                    }
                """;
        addData(requestBody);
        requestBody = """
                    {
                        "name": "Ann Clarkson",
                        "age": 28,
                        "gender": "FEMALE",
                        "salary": 55000
                    }
                """;
        addData(requestBody);
        requestBody = """
                    {
                        "name": "Clara Smith",
                        "age": 40,
                        "gender": "FEMALE",
                        "salary": 85000
                    }
                """;
        addData(requestBody);
        requestBody = """
                    {
                        "name": "Daniel Clarkson",
                        "age": 32,
                        "gender": "MALE",
                        "salary": 65000
                    }
                """;
        addData(requestBody);
        Employee expectedEmployee_1 = johnSmith();
        Employee expectedEmployee_2 = maryJane();
        Employee expectedEmployee_3 = benSmith();
        Employee expectedEmployee_4 = amySmith();
        Employee expectedEmployee_5 = annClarkson();
        mockMvc.perform(get("/employees?page=1&size=5")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].id").value(expectedEmployee_1.getId()))
                .andExpect(jsonPath("$[0].age").value(expectedEmployee_1.getAge()))
                .andExpect(jsonPath("$[0].gender").value(expectedEmployee_1.getGender()))
                .andExpect(jsonPath("$[0].name").value(expectedEmployee_1.getName()))
                .andExpect(jsonPath("$[0].salary").value(expectedEmployee_1.getSalary()))

                .andExpect(jsonPath("$[1].id").value(expectedEmployee_2.getId()))
                .andExpect(jsonPath("$[1].age").value(expectedEmployee_2.getAge()))
                .andExpect(jsonPath("$[1].gender").value(expectedEmployee_2.getGender()))
                .andExpect(jsonPath("$[1].name").value(expectedEmployee_2.getName()))
                .andExpect(jsonPath("$[1].salary").value(expectedEmployee_2.getSalary()))

                .andExpect(jsonPath("$[2].id").value(expectedEmployee_3.getId()))
                .andExpect(jsonPath("$[2].age").value(expectedEmployee_3.getAge()))
                .andExpect(jsonPath("$[2].gender").value(expectedEmployee_3.getGender()))
                .andExpect(jsonPath("$[2].name").value(expectedEmployee_3.getName()))
                .andExpect(jsonPath("$[2].salary").value(expectedEmployee_3.getSalary()))

                .andExpect(jsonPath("$[3].id").value(expectedEmployee_4.getId()))
                .andExpect(jsonPath("$[3].age").value(expectedEmployee_4.getAge()))
                .andExpect(jsonPath("$[3].gender").value(expectedEmployee_4.getGender()))
                .andExpect(jsonPath("$[3].name").value(expectedEmployee_4.getName()))
                .andExpect(jsonPath("$[3].salary").value(expectedEmployee_4.getSalary()))

                .andExpect(jsonPath("$[4].id").value(expectedEmployee_5.getId()))
                .andExpect(jsonPath("$[4].age").value(expectedEmployee_5.getAge()))
                .andExpect(jsonPath("$[4].gender").value(expectedEmployee_5.getGender()))
                .andExpect(jsonPath("$[4].name").value(expectedEmployee_5.getName()))
                .andExpect(jsonPath("$[4].salary").value(expectedEmployee_5.getSalary()));

    }
}
