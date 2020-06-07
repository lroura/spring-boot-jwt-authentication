package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public abstract class BaseControllerTest {

    protected ObjectMapper mapper = new ObjectMapper();

    @Autowired
    protected WebApplicationContext context;

    protected MockMvc mvc;

    @BeforeEach
    public void init() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    protected byte[] toJson(Object object) {
        try {
            return this.mapper
                    .writeValueAsString(object).getBytes();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
