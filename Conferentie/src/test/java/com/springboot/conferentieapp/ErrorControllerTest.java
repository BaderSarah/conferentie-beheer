package com.springboot.conferentieapp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.RequestDispatcher;

@ActiveProfiles("test")
@Import(SecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
class ErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testError404() throws Exception {
        mockMvc.perform(get("/error")
                .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value()))
                .andExpect(status().isOk())
                .andExpect(view().name("error/404"));
    }

    @Test
    void testError403() throws Exception {
        mockMvc.perform(get("/error")
                .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.FORBIDDEN.value()))
                .andExpect(status().isOk())
                .andExpect(view().name("error/403"));
    }

    @Test
    void testError500() throws Exception {
        mockMvc.perform(get("/error")
                .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(status().isOk())
                .andExpect(view().name("error/500"));
    }

    @Test
    void testErrorUnknownStatus() throws Exception {
        mockMvc.perform(get("/error")
                .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 999))
                .andExpect(status().isOk())
                .andExpect(view().name("error/500")); 
    }

    @Test
    void testErrorNoStatusCode() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/500")); 
    }
}
