package com.oocl.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oocl.demo.controller.CompanyController;
import com.oocl.demo.model.Employee;
import com.oocl.demo.repository.CompanyRepository;
import com.oocl.demo.repository.EmployeeRepository;
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
public class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyController companyController;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void resetForTesting() {
        companyController.resetData();
    }

    private ResultActions mockMvcPerformPost(String requestBody) throws Exception {
        return mockMvc.perform(post("/companies")
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
    void should_return_company_with_employees() throws Exception {
        String requestBody = """
                    {
                        "name": "Apple"
                    }
                """;
        long id = addData(requestBody);
        Employee employee = new Employee();
        employee.setAge(30);
        employee.setGender("Male");
        employee.setSalary(80000);
        employee.setName("Tom");
        employee.setCompanyId(id);
        employeeRepository.createEmployee(employee);

        mockMvc.perform(get("/companies/{id}", id).
                        contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.employees.length()").value(1));
    }

    @Test
//    @Order(1)
    void should_create_company_when_post_given_a_valid_body() throws Exception {
        String requestBody = """
                    {
                        "name": "Apple"
                    }
                """;
        ResultActions resultActions = mockMvcPerformPost(requestBody);
        MvcResult mvcResult = resultActions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        long id = new ObjectMapper().readTree(contentAsString).get("id").asLong();
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
//    @Order(2)
    void should_return_company_when_get_given_company_id() throws Exception {
        String requestBody = """
                    {
                        "name": "Apple"
                    }
                """;
        long id = addData(requestBody);
        mockMvc.perform(get("/companies/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
//    @Order(3)
    void should_throw_404_when_get_given_company_id_not_exist() throws Exception {
        mockMvc.perform(get("/companies/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
//    @Order(4)
    void should_return_company_list_when_get_given_companies() throws Exception {
        String requestBody = """
                    {
                        "name": "Apple"
                    }
                """;
        long id1 = addData(requestBody);
        requestBody = """
                    {
                        "name": "Google"
                    }
                """;
        long id2 = addData(requestBody);
        mockMvc.perform(get("/companies/all")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(id1))
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[1].id").value(id2))
                .andExpect(jsonPath("$[1].name").value("Google"));
    }

    @Test
//    @Order(5)
    void should_update_company_when_put_given_company_update_infos() throws Exception {
        String requestBody = """
                    {
                        "name": "Apple"
                    }
                """;
        addData(requestBody);
        requestBody = """
                    {
                        "name": "Google"
                    }
                """;
        long id = addData(requestBody);
        requestBody = """
                    {
                        "id" : %d,
                        "name": "Meta"
                    }
                """.formatted(id);
        mockMvc.perform(put("/companies/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Meta"));
    }

    @Test
//    @Order(6)
    void should_throw_404_when_put_given_company_update_infos_not_exist() throws Exception {
        String requestBody = """
                    {
                        "name": "Meta"
                    }
                """;
        mockMvc.perform(put("/companies/{id}",-1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
//    @Order(7)
    void should_delete_company_when_delete_given_company_id() throws Exception {
        String requestBody = """
                    {
                        "name": "Apple"
                    }
                """;
        long id = addData(requestBody);
        mockMvc.perform(delete(("/companies/{id}"), id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
//    @Order(8)
    void should_throw_204_company_when_delete_given_company_id_not_exist() throws Exception {
        mockMvc.perform(delete(("/companies/{id}"), 2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
//    @Order(9)
    void should_return_page_query_when_page_query_given_enough_companies() throws Exception {
        String requestBody = """
                    {
                        "name": "Apple"
                    }
                """;
        long id1 = addData(requestBody);
        requestBody = """
                    {
                        "name": "Amazon"
                    }
                """;
        long id2 = addData(requestBody);
        requestBody = """
                    {
                        "name": "Netflix"
                    }
                """;
        long id3 = addData(requestBody);
        requestBody = """
                    {
                        "name": "X"
                    }
                """;
        long id4 = addData(requestBody);
        requestBody = """
                    {
                        "name": "Nvidia"
                    }
                """;
        long id5 = addData(requestBody);
        requestBody = """
                    {
                        "name": "AMD"
                    }
                """;
        mockMvc.perform(post("/companies")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        mockMvc.perform(get("/companies?page=1&size=5")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].id").value(id1))
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[1].id").value(id2))
                .andExpect(jsonPath("$[1].name").value("Amazon"))
                .andExpect(jsonPath("$[2].id").value(id3))
                .andExpect(jsonPath("$[2].name").value("Netflix"))
                .andExpect(jsonPath("$[3].id").value(id4))
                .andExpect(jsonPath("$[3].name").value("X"))
                .andExpect(jsonPath("$[4].id").value(id5))
                .andExpect(jsonPath("$[4].name").value("Nvidia"));
    }
}
