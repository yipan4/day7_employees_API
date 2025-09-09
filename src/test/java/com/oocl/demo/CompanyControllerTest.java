package com.oocl.demo;

import com.oocl.demo.model.Company;

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
}
