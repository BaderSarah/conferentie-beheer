package com.springboot.conferentieapp;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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

import domein.Lokaal;
import domein.Spreker;
import repository.LokaalRepository;
import service.ConferentieService;

@ActiveProfiles("test")
@Import(SecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
class LokaalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LokaalRepository lokaalRepository;

    @MockitoBean
    private ConferentieService conferentieService;

    @MockitoBean
    private MessageSource messageSource;

    @BeforeEach
    void setup() {
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testRedirectFromRoomsToForm() throws Exception {
        mockMvc.perform(get("/rooms"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rooms/new"));
    }
    

    @Test
    @WithMockUser(roles = "ADMIN")
    void testShowCreateLokaalForm() throws Exception {
        mockMvc.perform(get("/rooms/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("LokaalForm"))
                .andExpect(model().attributeExists("lokaal"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateLokaal_GeldigeInvoer() throws Exception {
        Lokaal lokaal = new Lokaal("D101", 50);

        when(lokaalRepository.existsByNaam("D101")).thenReturn(false);
        when(messageSource.getMessage(any(), any(), any())).thenReturn("Succes");

        mockMvc.perform(post("/rooms")
                        .param("naam", "D101")
                        .param("capaciteit", "50")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("LokaalForm"))
                .andExpect(model().attributeExists("msg"));

        verify(lokaalRepository).save(any(Lokaal.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateLokaal_FouteInvoer_OngeldigeNaam() throws Exception {
    	mockMvc.perform(post("/rooms")
    			.param("naam", "hallo")
    			.param("capaciteit", "10")
    			.with(csrf())
    			.contentType(MediaType.APPLICATION_FORM_URLENCODED))
    	.andExpect(status().isOk())
    	.andExpect(view().name("LokaalForm"))
    	.andExpect(model().attributeHasFieldErrors("lokaal", "naam"));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateLokaal_FouteInvoer_LegeNaam() throws Exception {
    	mockMvc.perform(post("/rooms")
    			.param("naam", "")
    			.param("capaciteit", "10")
    			.with(csrf())
    			.contentType(MediaType.APPLICATION_FORM_URLENCODED))
    	.andExpect(status().isOk())
    	.andExpect(view().name("LokaalForm"))
    	.andExpect(model().attributeHasFieldErrors("lokaal", "naam"));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateLokaal_FouteInvoer_OngeldigeCapaciteit() throws Exception {
    	mockMvc.perform(post("/rooms")
    			.param("naam", "A101")
    			.param("capaciteit", "-10")
    			.with(csrf())
    			.contentType(MediaType.APPLICATION_FORM_URLENCODED))
    	.andExpect(status().isOk())
    	.andExpect(view().name("LokaalForm"))
    	.andExpect(model().attributeHasFieldErrors("lokaal", "capaciteit"));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateLokaal_NaamBestaatAl() throws Exception {
        when(lokaalRepository.existsByNaam("A101")).thenReturn(true);

        mockMvc.perform(post("/rooms")
                        .param("naam", "A101")
                        .param("capaciteit", "25")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("LokaalForm"))
                .andExpect(model().attributeHasFieldErrors("lokaal", "naam"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testEditLokaal_LokaalNotFound() throws Exception {
        when(lokaalRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/rooms/edit/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/404"));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testEditLokaalForm_GeldigeLokaal() throws Exception {
    	Lokaal lokaal = new Lokaal();
    	lokaal.setId(1L);
    	when(lokaalRepository.findById(1L)).thenReturn(Optional.of(lokaal));
    	
    	mockMvc.perform(post("/rooms/edit/1")
    			.param("naam", "A123")
    			.param("capaciteit", "10")
                .with(csrf())
    			.contentType(MediaType.APPLICATION_FORM_URLENCODED))
    	.andExpect(status().is3xxRedirection())
    	.andExpect(redirectedUrl("/management"));
    }

    
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testVerbodenToegangVoorUserRole() throws Exception {
        mockMvc.perform(get("/rooms"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testRedirectToLoginWhenAnonymous() throws Exception {
        mockMvc.perform(get("/rooms"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

}
