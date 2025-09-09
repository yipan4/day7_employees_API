package com.oocl.demo;

import com.oocl.demo.model.Company;

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
public class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void should_create_company_when_post_given_a_valid_body() throws Exception {
        String requestBody = """
                    {
                        "name": "Apple"
                    }
                """;
        mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @Order(2)
    void should_return_company_when_get_given_company_id() throws Exception {
        mockMvc.perform(get("/companies/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
    @Order(3)
    void should_throw_404_when_get_given_company_id_not_exist() throws Exception {
        mockMvc.perform(get("/companies/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(4)
    void should_return_company_list_when_get_given_companies() throws Exception {
        String requestBody = """
                    {
                        "name": "Google"
                    }
                """;
        mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody));

        mockMvc.perform(get("/companies/all")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Google"));
    }

    @Test
    @Order(5)
    void should_update_company_when_put_given_company_update_infos() throws Exception {
        String requestBody = """
                    {
                        "name": "Meta"
                    }
                """;
        mockMvc.perform(put("/companies/{id}",2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Meta"));
    }

    @Test
    @Order(6)
    void should_throw_404_when_put_given_company_update_infos_not_exist() throws Exception {
        String requestBody = """
                    {
                        "name": "Meta"
                    }
                """;
        mockMvc.perform(put("/companies/{id}",3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    void should_delete_company_when_delete_given_company_id() throws Exception {
        mockMvc.perform(delete(("/companies/{id}"), 2)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Meta"));
    }

    @Test
    @Order(8)
    void should_throw_404_company_when_delete_given_company_id_not_exist() throws Exception {
        mockMvc.perform(delete(("/companies/{id}"), 2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }
}
