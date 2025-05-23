package com.springboot.conferentieapp;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import domein.Gebruiker;
import repository.GebruikerRepository;

@ActiveProfiles("test")
@Import(SecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistratieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GebruikerRepository gebruikerRepo;

    @Test
    void testShowCreateUserForm() throws Exception {
        mockMvc.perform(get("/registration")
                .header("Referer", "/some-previous-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("Registreer"))
                .andExpect(model().attributeExists("gebruiker"))
                .andExpect(model().attribute("referer", "/some-previous-page"));
    }

    @Test
    void testCreateUser_GeldigeInvoer() throws Exception {
        mockMvc.perform(post("/registration")
                        .param("email", "test@mail.com")
                        .param("wachtwoord", "SterkWachtwoord123")
                        .param("bevestigWachtwoord", "SterkWachtwoord123")
                        .param("voornaam", "Test")
                        .param("naam", "Gebruiker")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(gebruikerRepo).save(any(Gebruiker.class));
    }

    @Test
    void testCreateUser_FouteInvoer_LegeVelden() throws Exception {
        mockMvc.perform(post("/registration")
                        .param("email", "")
                        .param("wachtwoord", "")
                        .param("bevestigWachtwoord", "")
                        .param("voornaam", "")
                        .param("naam", "")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("Registreer"))
                .andExpect(model().attributeHasErrors("gebruiker"));
    }

    @Test
    void testCreateUser_VerkeerdEmail() throws Exception {
        mockMvc.perform(post("/registration")
                        .param("email", "hallo")
                        .param("wachtwoord", "Password123")
                        .param("bevestigWachtwoord", "Password123")
                        .param("voornaam", "Test")
                        .param("naam", "Gebruiker")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("Registreer"))
                .andExpect(model().attributeHasFieldErrors("gebruiker", "email"));
    }
    
    @Test
    void testCreateUser_WachtwoordenKomenNietOvereen() throws Exception {
        mockMvc.perform(post("/registration")
                        .param("email", "test@mail.com")
                        .param("wachtwoord", "Password123")
                        .param("bevestigWachtwoord", "AnderWachtwoord")
                        .param("voornaam", "Test")
                        .param("naam", "Gebruiker")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("Registreer"))
                .andExpect(model().attributeHasFieldErrors("gebruiker", "bevestigWachtwoord"));
    }
}
