package com.example.demo.controllers;

import com.example.demo.BaseControllerTest;
import com.example.demo.config.security.LoginCommand;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class SecurityTest extends BaseControllerTest {

    @Test
    public void testAuthentication() throws Exception {
        LoginCommand loginCommand = new LoginCommand();
        loginCommand.setUsername("admin");
        loginCommand.setPassword("1234567890");

        this.mvc.perform(post("/api/login")
                .header("Accept", "application/json")
                .content(toJson(loginCommand))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE2")
    public void testExcecuteActionWithRole2() throws Exception {

        this.mvc.perform(get("/api/user")
                .header("Accept", "application/json")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE1")
    public void testExcecuteActionWithRole1() throws Exception {

        this.mvc.perform(get("/api/someOtherThing")
                .header("Accept", "application/json")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }
}
