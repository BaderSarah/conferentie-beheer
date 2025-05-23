package com.springboot.conferentieapp.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;
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

import domein.Lokaal;
import exceptions.DuplicateLokaalException;
import exceptions.LokaalNotFoundException;
import service.LokaalService;

@SpringBootTest
class LokaalRestControllerTest {

    @Mock
    private LokaalService mock;

    private LokaalRestController controller;
    private MockMvc mockMvc;

    private final Long ID = 1L;
    private final String NAAM = "B101";
    private final int CAPACITEIT = 40;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        controller = new LokaalRestController();
        mockMvc = standaloneSetup(controller)
                .setControllerAdvice(new LokaalErrorAdvice())
                .build();
        ReflectionTestUtils.setField(controller, "lokaalService", mock);
    }

    private Lokaal aLokaal(Long id, String naam, int capaciteit) {
        Lokaal lokaal = new Lokaal(naam, capaciteit);
        ReflectionTestUtils.setField(lokaal, "id", id);
        return lokaal;
    }

    @Test
    void testGetLokaal_isOk() throws Exception {
        Mockito.when(mock.getLokaal(ID)).thenReturn(aLokaal(ID, NAAM, CAPACITEIT));

        mockMvc.perform(get("/rest/rooms/" + ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.naam").value(NAAM))
                .andExpect(jsonPath("$.capaciteit").value(CAPACITEIT));

        Mockito.verify(mock).getLokaal(ID);
    }

    @Test
    void testGetLokaal_notFound() throws Exception {
        Mockito.when(mock.getLokaal(ID)).thenThrow(new LokaalNotFoundException(ID));

        mockMvc.perform(get("/rest/rooms/" + ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find room " + ID ));

        Mockito.verify(mock).getLokaal(ID);
    }

    @Test
    void testGetAllLokaals_emptyList() throws Exception {
        Mockito.when(mock.getAllLokaals()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/rest/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        Mockito.verify(mock).getAllLokaals();
    }

    @Test
    void testGetAllLokaals_nonEmptyList() throws Exception {
        List<Lokaal> lijst = List.of(aLokaal(ID, NAAM, CAPACITEIT), aLokaal(2L, "C102", 30));
        Mockito.when(mock.getAllLokaals()).thenReturn(lijst);

        mockMvc.perform(get("/rest/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].naam").value(NAAM))
                .andExpect(jsonPath("$[1].naam").value("C102"));

        Mockito.verify(mock).getAllLokaals();
    }

    @Test
    void testCreateLokaal_isOk() throws Exception {
        Lokaal lokaal = aLokaal(ID, NAAM, CAPACITEIT);
        String json = new ObjectMapper().writeValueAsString(lokaal);

        Mockito.when(mock.createLokaal(Mockito.any(Lokaal.class))).thenReturn(lokaal);

        mockMvc.perform(post("/rest/rooms/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.naam").value(NAAM))
                .andExpect(jsonPath("$.capaciteit").value(CAPACITEIT));

        Mockito.verify(mock).createLokaal(Mockito.any(Lokaal.class));
    }

    @Test
    void testCreateLokaal_duplicate() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new Lokaal(NAAM, CAPACITEIT));
        Mockito.when(mock.createLokaal(Mockito.any(Lokaal.class)))
                .thenThrow(new DuplicateLokaalException(ID));

        mockMvc.perform(post("/rest/rooms/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(content().string("Duplicate room " + ID));

        Mockito.verify(mock).createLokaal(Mockito.any(Lokaal.class));
    }

    @Test
    void testGetCapaciteit_isOk() throws Exception {
        Mockito.when(mock.getCapaciteitVanLokaal(ID)).thenReturn(CAPACITEIT);

        mockMvc.perform(get("/rest/rooms/" + ID + "/capacity"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(CAPACITEIT)));

        Mockito.verify(mock).getCapaciteitVanLokaal(ID);
    }
}

