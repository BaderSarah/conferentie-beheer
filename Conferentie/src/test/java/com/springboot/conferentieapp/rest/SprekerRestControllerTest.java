package com.springboot.conferentieapp.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import domein.Spreker;
import exceptions.DuplicateSprekerException;
import exceptions.SprekerNotFoundException;
import service.SprekerService;

@SpringBootTest
class SprekerRestControllerTest {

    @Mock
    private SprekerService mock;

    private SprekerRestController controller;
    private MockMvc mockMvc;

    private final Long ID = 1L;
    private final String VOORNAAM = "Jan";
    private final String NAAM = "Jansen";
    private final String EMAIL = "jan.jansen@example.com";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        controller = new SprekerRestController();
        mockMvc = standaloneSetup(controller)
                .setControllerAdvice(new SprekerErrorAdvice())
                .build();
        ReflectionTestUtils.setField(controller, "sprekerService", mock);
    }

    private Spreker aSpreker(Long id, String voornaam, String naam, String email) {
        Spreker spreker = new Spreker(voornaam, naam, email);
        ReflectionTestUtils.setField(spreker, "id", id);
        return spreker;
    }

    @Test
    void testGetSpreker_isOk() throws Exception {
        Mockito.when(mock.getSpreker(ID)).thenReturn(aSpreker(ID, NAAM, VOORNAAM, EMAIL));

        mockMvc.perform(get("/rest/speakers/" + ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.naam").value(NAAM))
                .andExpect(jsonPath("$.voornaam").value(VOORNAAM))
                .andExpect(jsonPath("$.email").value(EMAIL));

        Mockito.verify(mock).getSpreker(ID);
    }

    @Test
    void testGetSpreker_notFound() throws Exception {
        Mockito.when(mock.getSpreker(ID)).thenThrow(new SprekerNotFoundException(ID));

        mockMvc.perform(get("/rest/speakers/" + ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find speaker " + ID));

        Mockito.verify(mock).getSpreker(ID);
    }

    @Test
    void testGetAllSprekers_emptyList() throws Exception {
        Mockito.when(mock.getAllSprekers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/rest/speakers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        Mockito.verify(mock).getAllSprekers();
    }

    @Test
    void testGetAllSprekers_nonEmptyList() throws Exception {
        List<Spreker> sprekers = List.of(
                aSpreker(ID, NAAM, VOORNAAM,  EMAIL),
                aSpreker(2L, "De Vries", "Anna", "anna@voorbeeld.nl")
        );

        Mockito.when(mock.getAllSprekers()).thenReturn(sprekers);

        mockMvc.perform(get("/rest/speakers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].voornaam").value(VOORNAAM))
                .andExpect(jsonPath("$[1].naam").value("De Vries"));

        Mockito.verify(mock).getAllSprekers();
    }

    @Test
    void testCreateSpreker_isOk() throws Exception {
        Spreker spreker = aSpreker(ID, NAAM, VOORNAAM, EMAIL);
        String json = new ObjectMapper().writeValueAsString(spreker);

        Mockito.when(mock.createSpreker(Mockito.any(Spreker.class))).thenReturn(spreker);

        mockMvc.perform(post("/rest/speakers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.naam").value(NAAM))
                .andExpect(jsonPath("$.voornaam").value(VOORNAAM))
                .andExpect(jsonPath("$.email").value(EMAIL));

        Mockito.verify(mock).createSpreker(Mockito.any(Spreker.class));
    }

    @Test
    void testCreateSpreker_duplicate() throws Exception {
        Spreker spreker = new Spreker(VOORNAAM, NAAM, EMAIL);
        String json = new ObjectMapper().writeValueAsString(spreker);

        Mockito.when(mock.createSpreker(Mockito.any(Spreker.class)))
                .thenThrow(new DuplicateSprekerException(ID));

        mockMvc.perform(post("/rest/speakers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(content().string("Duplicate speaker " +  ID));

        Mockito.verify(mock).createSpreker(Mockito.any(Spreker.class));
    }
}
