package com.oocl.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.oocl.demo.controller.EmployeeController;
import com.oocl.demo.model.Company;
import com.oocl.demo.model.Employee;

import com.oocl.demo.repository.CompanyRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

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

    @Autowired
    private CompanyRepository companyRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

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

    private ResultActions mockMvcPerformPost(String requestBody) throws Exception {
        return mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));
    }

    private long addData(String requestBody) throws Exception {
        ResultActions resultActions = mockMvcPerformPost(requestBody);
        MvcResult mvcResult = resultActions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        return new ObjectMapper().readTree(contentAsString).get("id").asLong();
    }

    @Test
//    @Order(1)
    void should_create_employee_when_post_given_a_valid_body() throws Exception {
//        Company company = new Company();
//        long companyId = companyRepository.createCompany(company).getId();
//        ResultActions resultActions = mockMvcPerformPost(requestBody);
        Company savedCompany = companyRepository.createCompany(new Company("TestCompany"));
        String requestBody = """
                    {
                        "name": "John Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 60000,
                        "company_id": %d
                    }
                """.formatted(savedCompany.getId());
        JsonNode bodyNode = new ObjectMapper().readTree(requestBody);
        if (bodyNode.isObject()) {
            ((ObjectNode) bodyNode).put("companyId", savedCompany.getId());
            requestBody = objectMapper.writeValueAsString(bodyNode);
        }
        ResultActions resultActions = mockMvcPerformPost(requestBody);
        MvcResult mvcResult = resultActions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        long id = objectMapper.readTree(contentAsString).get("id").asLong();

        resultActions.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void should_not_create_employee_when_post_given_invalid_age() throws Exception {
        String requestBody = """
                    {
                        "name": "John Smith",
                        "age": 17,
                        "gender": "MALE",
                        "salary": 60000
                    }
                """;
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    private String employeeStringWithInitCompany(Company company, String requestBody) throws Exception {
        requestBody = requestBody.formatted(company.getId());
        JsonNode bodyNode = new ObjectMapper().readTree(requestBody);
        if (bodyNode.isObject()) {
            ((ObjectNode) bodyNode).put("companyId", company.getId());
            requestBody = objectMapper.writeValueAsString(bodyNode);
        }
        return requestBody;
    }
    @Test
//    @Order(2)
    void should_return_employee_when_get_given_employee_id() throws Exception {
        Company company = companyRepository.createCompany(new Company("Apple"));
        String requestBody = """
            {
                "name": "John Smith",
                "age": 30,
                "gender": "MALE",
                "salary": 60000,
                "company_id": %d
            }
        """;
        requestBody = employeeStringWithInitCompany(company,requestBody);
        long id = addData(requestBody);
        mockMvc.perform(get("/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.salary").value(60000));
    }

    @Test
//    @Order(3)
    void should_return_male_employee_when_get_given_gender_male() throws Exception {
        Company company = companyRepository.createCompany(new Company("Apple"));
        String requestBody = """
                    {
                        "name": "John Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 60000,
                        "company_id": %d
                    }
                """;
        long id = addData(employeeStringWithInitCompany(company, requestBody));
        requestBody = """
                    {
                        "name": "Mary Jane",
                        "age": 28,
                        "gender": "FEMALE",
                        "salary": 65000,
                        "company_id": %d
                    }
                """;
        addData(employeeStringWithInitCompany(company,requestBody));
        Employee expectedEmployee = johnSmith();

        mockMvc.perform(get("/employees?gender=male")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(expectedEmployee.getName()))
                .andExpect(jsonPath("$[0].age").value(expectedEmployee.getAge()))
                .andExpect(jsonPath("$[0].gender").value(expectedEmployee.getGender()))
                .andExpect(jsonPath("$[0].salary").value(expectedEmployee.getSalary()));
    }

    @Test
//    @Order(4)
    void should_return_female_employee_when_get_given_gender_female() throws Exception {
        Company company = companyRepository.createCompany(new Company("Apple"));
        String requestBody = """
                    {
                        "name": "John Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 60000,
                        "company_id": %d
                    }
                """;
        addData(employeeStringWithInitCompany(company,requestBody));
        requestBody = """
                    {
                        "name": "Mary Jane",
                        "age": 28,
                        "gender": "FEMALE",
                        "salary": 65000,
                        "company_id": %d
                    }
                """;
        long id = addData(employeeStringWithInitCompany(company,requestBody));
        Employee expectedEmployee = maryJane();
        mockMvc.perform(get("/employees?gender=female")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(expectedEmployee.getName()))
                .andExpect(jsonPath("$[0].age").value(expectedEmployee.getAge()))
                .andExpect(jsonPath("$[0].gender").value(expectedEmployee.getGender()))
                .andExpect(jsonPath("$[0].salary").value(expectedEmployee.getSalary()));
    }
    @Test
//    @Order(5)
    void should_return_employee_list_when_get_given_employees() throws Exception {
        Company company = companyRepository.createCompany(new Company("Apple"));
        String requestBody = """
                    {
                        "name": "John Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 60000,
                        "company_id": %d
                    }
                """;
        long id1 = addData(employeeStringWithInitCompany(company, requestBody));
        requestBody = """
                    {
                        "name": "Mary Jane",
                        "age": 28,
                        "gender": "FEMALE",
                        "salary": 65000,
                        "company_id": %d
                    }
                """;
        long id2 = addData(employeeStringWithInitCompany(company, requestBody));
        Employee expectedEmployee_1 = johnSmith();
        Employee expectedEmployee_2 = maryJane();
        mockMvc.perform(get("/employees/all")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(id1))
                .andExpect(jsonPath("$[0].name").value(expectedEmployee_1.getName()))
                .andExpect(jsonPath("$[0].age").value(expectedEmployee_1.getAge()))
                .andExpect(jsonPath("$[0].gender").value(expectedEmployee_1.getGender()))
                .andExpect(jsonPath("$[0].salary").value(expectedEmployee_1.getSalary()))
                .andExpect(jsonPath("$[1].id").value(id2))
                .andExpect(jsonPath("$[1].name").value(expectedEmployee_2.getName()))
                .andExpect(jsonPath("$[1].age").value(expectedEmployee_2.getAge()))
                .andExpect(jsonPath("$[1].gender").value(expectedEmployee_2.getGender()))
                .andExpect(jsonPath("$[1].salary").value(expectedEmployee_2.getSalary()));
    }
    @Test
//    @Order(6)
    void should_update_employee_when_put_given_employee_update_infos() throws Exception {
        Company company = companyRepository.createCompany(new Company("Apple"));
        String requestBody = """
                    {
                        "name": "John Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 60000,
                        "company_id": %d
                    }
                """;
        long id = addData(employeeStringWithInitCompany(company, requestBody));
        requestBody = """
                    {
                        "name": "Mary Jane",
                        "age": 28,
                        "gender": "FEMALE",
                        "salary": 65000,
                        "company_id": %d
                    }
                """;
        addData(employeeStringWithInitCompany(company, requestBody));
        Employee updatedEmployee = newJohnSmith();
        requestBody = """
                    {
                        "id": %d,
                        "name": "John Smith",
                        "gender": "MALE",
                        "age": 31,
                        "salary": 70000
                    }
                """.formatted(id);
        mockMvc.perform(put("/employees/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(updatedEmployee.getName()))
                .andExpect(jsonPath("$.age").value(updatedEmployee.getAge()))
                .andExpect(jsonPath("$.gender").value(updatedEmployee.getGender()))
                .andExpect(jsonPath("$.salary").value(updatedEmployee.getSalary()));
    }

    @Test
//    @Order(7)
    void should_delete_employee_when_delete_given_employee_exists() throws Exception {
        Company company = companyRepository.createCompany(new Company("Apple"));
        String requestBody = """
                    {
                        "name": "John Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 60000,
                        "company_id": %d
                    }
                """;
        addData(employeeStringWithInitCompany(company, requestBody));
        requestBody = """
                    {
                        "name": "Mary Jane",
                        "age": 28,
                        "gender": "FEMALE",
                        "salary": 65000,
                        "company_id": %d
                    }
                """;
        long id = addData(employeeStringWithInitCompany(company, requestBody));
        Employee updatedEmployee = maryJane();
        mockMvc.perform(delete("/employees/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(updatedEmployee.getName()))
                .andExpect(jsonPath("$.age").value(updatedEmployee.getAge()))
                .andExpect(jsonPath("$.gender").value(updatedEmployee.getGender()))
                .andExpect(jsonPath("$.salary").value(updatedEmployee.getSalary()));
    }

    @Test
//    @Order(8)
    void should_return_204_employee_when_delete_given_employee_not_exists() throws Exception {
        Company company = companyRepository.createCompany(new Company("Apple"));
        String requestBody = """
                    {
                        "name": "John Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 60000,
                        "company_id": %d
                    }
                """;
        addData(employeeStringWithInitCompany(company, requestBody));
        mockMvc.perform(delete("/employees/{id}",2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
//    @Order(9)
    void should_return_page_query_when_page_query_given_enough_employees() throws Exception {
        Company company = companyRepository.createCompany(new Company("Apple"));
        String requestBody = """
                    {
                        "name": "John Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 60000,
                        "company_id": %d
                    }
                """;
        long id1 = addData(employeeStringWithInitCompany(company, requestBody));
        requestBody = """
                    {
                        "name": "Mary Jane",
                        "age": 28,
                        "gender": "FEMALE",
                        "salary": 65000,
                        "company_id": %d
                    }
                """;
        long id2 = addData(employeeStringWithInitCompany(company, requestBody));
        requestBody = """
                    {
                        "name": "Ben Smith",
                        "age": 30,
                        "gender": "MALE",
                        "salary": 50000,
                        "company_id": %d
                    }
                """;
        long id3 = addData(employeeStringWithInitCompany(company, requestBody));
        requestBody = """
                    {
                        "name": "Amy Smith",
                        "age": 30,
                        "gender": "FEMALE",
                        "salary": 80000,
                        "company_id": %d
                    }
                """;
        long id4 = addData(employeeStringWithInitCompany(company, requestBody));
        requestBody = """
                    {
                        "name": "Ann Clarkson",
                        "age": 28,
                        "gender": "FEMALE",
                        "salary": 55000,
                        "company_id": %d
                    }
                """;
        long id5 = addData(employeeStringWithInitCompany(company, requestBody));
        requestBody = """
                    {
                        "name": "Clara Smith",
                        "age": 40,
                        "gender": "FEMALE",
                        "salary": 85000,
                        "company_id": %d
                    }
                """;
        addData(employeeStringWithInitCompany(company, requestBody));
        requestBody = """
                    {
                        "name": "Daniel Clarkson",
                        "age": 32,
                        "gender": "MALE",
                        "salary": 65000,
                        "company_id": %d
                    }
                """;
        addData(employeeStringWithInitCompany(company, requestBody));
        Employee expectedEmployee_1 = johnSmith();
        Employee expectedEmployee_2 = maryJane();
        Employee expectedEmployee_3 = benSmith();
        Employee expectedEmployee_4 = amySmith();
        Employee expectedEmployee_5 = annClarkson();
        mockMvc.perform(get("/employees?page=1&size=5")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].id").value(id1))
                .andExpect(jsonPath("$[0].age").value(expectedEmployee_1.getAge()))
                .andExpect(jsonPath("$[0].gender").value(expectedEmployee_1.getGender()))
                .andExpect(jsonPath("$[0].name").value(expectedEmployee_1.getName()))
                .andExpect(jsonPath("$[0].salary").value(expectedEmployee_1.getSalary()))

                .andExpect(jsonPath("$[1].id").value(id2))
                .andExpect(jsonPath("$[1].age").value(expectedEmployee_2.getAge()))
                .andExpect(jsonPath("$[1].gender").value(expectedEmployee_2.getGender()))
                .andExpect(jsonPath("$[1].name").value(expectedEmployee_2.getName()))
                .andExpect(jsonPath("$[1].salary").value(expectedEmployee_2.getSalary()))

                .andExpect(jsonPath("$[2].id").value(id3))
                .andExpect(jsonPath("$[2].age").value(expectedEmployee_3.getAge()))
                .andExpect(jsonPath("$[2].gender").value(expectedEmployee_3.getGender()))
                .andExpect(jsonPath("$[2].name").value(expectedEmployee_3.getName()))
                .andExpect(jsonPath("$[2].salary").value(expectedEmployee_3.getSalary()))

                .andExpect(jsonPath("$[3].id").value(id4))
                .andExpect(jsonPath("$[3].age").value(expectedEmployee_4.getAge()))
                .andExpect(jsonPath("$[3].gender").value(expectedEmployee_4.getGender()))
                .andExpect(jsonPath("$[3].name").value(expectedEmployee_4.getName()))
                .andExpect(jsonPath("$[3].salary").value(expectedEmployee_4.getSalary()))

                .andExpect(jsonPath("$[4].id").value(id5    ))
                .andExpect(jsonPath("$[4].age").value(expectedEmployee_5.getAge()))
                .andExpect(jsonPath("$[4].gender").value(expectedEmployee_5.getGender()))
                .andExpect(jsonPath("$[4].name").value(expectedEmployee_5.getName()))
                .andExpect(jsonPath("$[4].salary").value(expectedEmployee_5.getSalary()));

    }
}
