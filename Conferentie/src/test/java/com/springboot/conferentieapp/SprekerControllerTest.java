package com.springboot.conferentieapp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import domein.Spreker;
import repository.SprekerRepository;
import service.ConferentieService;


@ActiveProfiles("test")
@Import(SecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
class SprekerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SprekerRepository sprekerRepository;

    @MockitoBean
    private ConferentieService conferentieService;

    @MockitoBean
    private MessageSource messageSource;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRedirectFromSpeakersToForm() throws Exception {
        mockMvc.perform(get("/speakers"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/speakers/new"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testShowCreateSprekerForm() throws Exception {
        mockMvc.perform(get("/speakers/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("SprekerForm"))
                .andExpect(model().attributeExists("spreker"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateSpreker_GeldigeInvoer() throws Exception {
        when(sprekerRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(messageSource.getMessage(any(), any(), any())).thenReturn("Spreker opgeslagen");

        mockMvc.perform(post("/speakers")
                        .param("naam", "Test")
                        .param("voornaam", "Voornaam")
                        .param("email", "test@example.com")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("SprekerForm"))
                .andExpect(model().attributeExists("msg"));
        
        verify(sprekerRepository).save(any(Spreker.class));

    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateSpreker_FouteInvoer_LegeNaam() throws Exception {
    	mockMvc.perform(post("/speakers")
                        .param("naam", "")
                        .param("voornaam", "voornaam")
                        .param("email", "voornaam@mail.com")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("SprekerForm"))
            	.andExpect(model().attributeHasFieldErrors("spreker", "naam"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateSpreker_FouteInvoer_LegeVoornaam() throws Exception {
    	mockMvc.perform(post("/speakers")
                        .param("naam", "naam")
                        .param("voornaam", "")
                        .param("email", "voornaam@mail.com")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("SprekerForm"))
            	.andExpect(model().attributeHasFieldErrors("spreker", "voornaam"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateSpreker_FouteInvoer_LegeEmail() throws Exception {
    	mockMvc.perform(post("/speakers")
                        .param("naam", "naam")
                        .param("voornaam", "voornaam")
                        .param("email", "")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("SprekerForm"))
            	.andExpect(model().attributeHasFieldErrors("spreker", "email"));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateSpreker_EmailBestaatAl() throws Exception {
        when(sprekerRepository.existsByEmail("voorbeeld@mail.com")).thenReturn(true);

        mockMvc.perform(post("/speakers")
                        .param("email", "voorbeeld@mail.com")
                        .param("naam", "Naam")
                        .param("voornaam", "Voornaam")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("SprekerForm"))
                .andExpect(model().attributeHasFieldErrors("spreker", "email"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testEditSpreker_SprekerNotFound() throws Exception {
        when(sprekerRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/speakers/edit/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/404"));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testEditSpreker_GeldigeSpreker() throws Exception {
    	Spreker spreker = new Spreker();
    	spreker.setId(1L);
    	when(sprekerRepository.findById(1L)).thenReturn(Optional.of(spreker));
    	
    	mockMvc.perform(post("/speakers/edit/1")
    			.param("naam", "NaamBijgewerkt")
    			.param("voornaam", "VoornaamBijgewerkt")
    			.param("email", "voorbeeld@mail.com")
                .with(csrf())
    			.contentType(MediaType.APPLICATION_FORM_URLENCODED))
    	.andExpect(status().is3xxRedirection())
    	.andExpect(redirectedUrl("/management"));
    }

    
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testVerbodenToegangVoorUserRole() throws Exception {
        mockMvc.perform(get("/speakers"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testRedirectToLoginWhenAnonymous() throws Exception {
        mockMvc.perform(get("/speakers"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
