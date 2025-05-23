package com.springboot.conferentieapp.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.time.LocalDate;
import java.time.LocalTime;
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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import domein.Evenement;
import domein.Lokaal;
import domein.Spreker;
import exceptions.EvenementNotFoundException;
import service.EvenementenService;

@SpringBootTest
class EvenementRestControllerTest {

    @Mock
    private EvenementenService mock;

    private EvenementRestController controller;
    private MockMvc mockMvc;

    private final Long ID = 1L;
    private final String NAAM = "TechConf";
    private final LocalDate DATUM = LocalDate.of(2025, 6, 15);
    private final LocalTime BEGIN = LocalTime.of(10, 0);
    private final LocalTime EIND = LocalTime.of(12, 0);

    private Lokaal lokaal;
    private Spreker spreker;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        controller = new EvenementRestController();
        mockMvc = standaloneSetup(controller)
                .setControllerAdvice(new EvenementErrorAdvice())
                .build();
        ReflectionTestUtils.setField(controller, "eventService", mock);

        lokaal = new Lokaal("B101", 40);
        spreker = new Spreker("Jan", "Jansen", "jan@event.nl");
    }

    private Evenement createEvenement(Long id) {
        Evenement event = new Evenement();
        event.setNaam(NAAM);
        event.setBeschrijving("Tech conference");
        event.setBeamercode(1234);
        event.setBeamercheck(1);
        event.setPrijs(49.99);
        event.setDatum(DATUM);
        event.setBegintijdstip(BEGIN);
        event.setEindtijdstip(EIND);
        event.setLokaal(lokaal);
        event.setSprekers(List.of(spreker));
        ReflectionTestUtils.setField(event, "id", id);
        return event;
    }

    @Test
    void testGetEvenement_isOk() throws Exception {
        Evenement event = createEvenement(ID);
        Mockito.when(mock.getEvenement(ID)).thenReturn(event);

        mockMvc.perform(get("/rest/events/" + ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.naam").value(NAAM));

        Mockito.verify(mock).getEvenement(ID);
    }

    @Test
    void testGetEvenement_notFound() throws Exception {
        Mockito.when(mock.getEvenement(ID)).thenThrow(new EvenementNotFoundException(ID));

        mockMvc.perform(get("/rest/events/" + ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find event " + ID));

        Mockito.verify(mock).getEvenement(ID);
    }

    @Test
    void testGetAllEvenementen_isOk() throws Exception {
        List<Evenement> events = List.of(createEvenement(ID), createEvenement(2L));
        Mockito.when(mock.getAllEvenements()).thenReturn(events);

        mockMvc.perform(get("/rest/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        Mockito.verify(mock).getAllEvenements();
    }

    @Test
    void testCreateEvenement_isOk() throws Exception {
        Evenement event = createEvenement(ID);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);  
        
        String json = mapper.writeValueAsString(event);

        Mockito.when(mock.createEvenement(Mockito.any(Evenement.class))).thenReturn(event);

        mockMvc.perform(post("/rest/events/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.naam").value(NAAM));

        Mockito.verify(mock).createEvenement(Mockito.any(Evenement.class));
    }


    @Test
    void testDeleteEvenement_isOk() throws Exception {
        mockMvc.perform(delete("/rest/events/delete/" + ID))
                .andExpect(status().isOk());

        Mockito.verify(mock).deleteEvenement(ID);
    }

    @Test
    void testGetEventsByDate_isOk() throws Exception {
        Evenement event = createEvenement(ID);
        Mockito.when(mock.getEvenementenByDatum(DATUM)).thenReturn(List.of(event));

        mockMvc.perform(get("/rest/events/bydate/" + DATUM))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].naam").value(NAAM));

        Mockito.verify(mock).getEvenementenByDatum(DATUM);
    }
}
