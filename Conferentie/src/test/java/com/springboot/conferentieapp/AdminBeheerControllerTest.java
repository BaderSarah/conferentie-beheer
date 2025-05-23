package com.springboot.conferentieapp;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import repository.EvenementRepository;
import repository.LokaalRepository;
import repository.SprekerRepository;

@ActiveProfiles("test")
@Import(SecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
class AdminBeheerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LokaalRepository lokaalRepository;

    @MockitoBean
    private SprekerRepository sprekerRepository;

    @MockitoBean
    private EvenementRepository evenementRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testShowBeheerPaginaAlsAdmin() throws Exception {
        when(lokaalRepository.findAll()).thenReturn(List.of());
        when(sprekerRepository.findAll()).thenReturn(List.of());
        when(evenementRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/management"))
                .andExpect(status().isOk())
                .andExpect(view().name("Beheer"))
                .andExpect(model().attributeExists("lokalen"))
                .andExpect(model().attributeExists("sprekers"))
                .andExpect(model().attributeExists("evenementen"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testToegangGeweigerdVoorUserRol() throws Exception {
        mockMvc.perform(get("/management"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testRedirectNaarLoginVoorAnoniemeGebruiker() throws Exception {
        mockMvc.perform(get("/management"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
