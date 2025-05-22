package com.springboot.conferentieapp;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import domein.Evenement;
import repository.EvenementRepository;
import repository.LokaalRepository;
import repository.SprekerRepository;
import service.ConferentieService;

@ActiveProfiles("test")
@Import(SecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
class EvenementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EvenementRepository evenementRepository;

    @MockitoBean
    private SprekerRepository sprekerRepository;

    @MockitoBean
    private LokaalRepository lokaalRepository;

    @MockitoBean
    private ConferentieService conferentieService;

    @Test
    void testShowEventsList() throws Exception {
        when(evenementRepository.findAllByOrderByDatumAscBegintijdstipAsc())
            .thenReturn(List.of());

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(view().name("EvenementListView"))
                .andExpect(model().attributeExists("evenementen"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testShowCreateEventForm() throws Exception {
        when(sprekerRepository.findAll()).thenReturn(List.of());
        when(lokaalRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/events/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("EvenementForm"))
                .andExpect(model().attributeExists("evenement"))
                .andExpect(model().attributeExists("sprekersLijst"))
                .andExpect(model().attributeExists("lokaalLijst"));
    }

    
	@Test
	@WithMockUser(roles = "ADMIN")
	void testCreateEvenement_FouteInvoer_LegeNaam() throws Exception {
	    mockMvc.perform(post("/events")
	            .param("naam", "") 
	            .param("prijs", "20.00")
	            .param("beamercode", "1234")
	            .param("beamercheck", "70")
	            .param("datum", "2025-05-02")
	            .param("begintijdstip", "10:00")
	            .param("eindtijdstip", "11:00")
	            .param("lokaal.id", "1")
	            .param("sprekers[0].id", "1")
	            .with(csrf())
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
	        .andExpect(status().isOk())
	        .andExpect(view().name("EvenementForm"))
	        .andExpect(model().attributeHasFieldErrors("evenement", "naam"));
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void testCreateEvenement_FouteInvoer_PrijsTeLaag() throws Exception {
		mockMvc.perform(post("/events")
				.param("naam", "Testevent")
				.param("prijs", "5.00")
				.param("beamercode", "1234")
				.param("beamercheck", "70")
				.param("datum", "2025-05-02")
				.param("begintijdstip", "10:00")
				.param("eindtijdstip", "11:00")
				.param("lokaal.id", "1")
				.param("sprekers[0].id", "1")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
		.andExpect(status().isOk())
		.andExpect(view().name("EvenementForm"))
		.andExpect(model().attributeHasFieldErrors("evenement", "prijs"));
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void testCreateEvenement_FouteInvoer_BeamercodeOngeldig() throws Exception {
	    mockMvc.perform(post("/events")
	            .param("naam", "Testevent")
	            .param("prijs", "20.00")
	            .param("beamercode", "999")
	            .param("beamercheck", "29")
	            .param("datum", "2025-05-02")
	            .param("begintijdstip", "10:00")
	            .param("eindtijdstip", "11:00")
	            .param("lokaal.id", "1")
	            .param("sprekers[0].id", "1")
	            .with(csrf())
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
	        .andExpect(status().isOk())
	        .andExpect(view().name("EvenementForm"))
	        .andExpect(model().attributeHasFieldErrors("evenement", "beamercode"));
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void testCreateEvenement_FouteInvoer_BeamercheckOngeldig() throws Exception {
	    mockMvc.perform(post("/events")
	            .param("naam", "Testevent")
	            .param("prijs", "20.00")
	            .param("beamercode", "1234")
	            .param("beamercheck", "0")
	            .param("datum", "2025-05-02")
	            .param("begintijdstip", "10:00")
	            .param("eindtijdstip", "11:00")
	            .param("lokaal.id", "1")
	            .param("sprekers[0].id", "1")
	            .with(csrf())
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
	        .andExpect(status().isOk())
	        .andExpect(view().name("EvenementForm"))
	        .andExpect(model().attributeHasFieldErrors("evenement", "beamercheck"));
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void testCreateEvenement_FouteInvoer_GeenDatum() throws Exception {
	    mockMvc.perform(post("/events")
	            .param("naam", "Testevent")
	            .param("prijs", "20.00")
	            .param("beamercode", "1234")
	            .param("beamercheck", "70")
	            .param("begintijdstip", "10:00")
	            .param("eindtijdstip", "11:00")
	            .param("lokaal.id", "1")
	            .param("sprekers[0].id", "1")
	            .with(csrf())
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
	        .andExpect(status().isOk())
	        .andExpect(view().name("EvenementForm"))
	        .andExpect(model().attributeHasFieldErrors("evenement", "datum"));
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void testCreateEvenement_FouteInvoer_GeenBeginTijdstip() throws Exception {
	    mockMvc.perform(post("/events")
	            .param("naam", "Testevent")
	            .param("prijs", "20.00")
	            .param("beamercode", "1234")
	            .param("beamercheck", "70")
	            .param("datum", "2025-05-02")
	            .param("eindtijdstip", "11:00")
	            .param("lokaal.id", "1")
	            .param("sprekers[0].id", "1")
	            .with(csrf())
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
	        .andExpect(status().isOk())
	        .andExpect(view().name("EvenementForm"))
	        .andExpect(model().attributeHasFieldErrors("evenement", "datum"));
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void testCreateEvenement_FouteInvoer_GeenEindTijdstip() throws Exception {
	    mockMvc.perform(post("/events")
	            .param("naam", "Testevent")
	            .param("prijs", "20.00")
	            .param("beamercode", "1234")
	            .param("beamercheck", "70")
	            .param("datum", "2025-05-02")
	            .param("begintijdstip", "10:00")
	            .param("eindtijdstip", "11:00")
	            .param("sprekers[0].id", "1")
	            .with(csrf())
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
	        .andExpect(status().isOk())
	        .andExpect(view().name("EvenementForm"))
	        .andExpect(model().attributeHasFieldErrors("evenement", "datum"));
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void testCreateEvenement_FouteInvoer_GeenLokaal() throws Exception {
	    mockMvc.perform(post("/events")
	            .param("naam", "Testevent")
	            .param("prijs", "20.00")
	            .param("beamercode", "1234")
	            .param("beamercheck", "70")
	            .param("datum", "2025-05-02")
	            .param("begintijdstip", "10:00")

	            .param("sprekers[0].id", "1")
	            .with(csrf())
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
	        .andExpect(status().isOk())
	        .andExpect(view().name("EvenementForm"))
	        .andExpect(model().attributeHasFieldErrors("evenement", "datum"));
	}
	
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void testCreateEvenement_FouteInvoer_GeenSprekers() throws Exception {
	    mockMvc.perform(post("/events")
	            .param("naam", "Testevent")
	            .param("prijs", "20.00")
	            .param("beamercode", "1234")
	            .param("beamercheck", "0")
	            .param("datum", "2025-05-02")
	            .param("begintijdstip", "10:00")
	            .param("eindtijdstip", "11:00")
	            .param("lokaal.id", "1")
	            .with(csrf())
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
	        .andExpect(status().isOk())
	        .andExpect(view().name("EvenementForm"))
	        .andExpect(model().attributeHasFieldErrors("evenement", "sprekers"));
	}


    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteEvent_Succesvol() throws Exception {
        doNothing().when(conferentieService).deleteEvenement(1L);

        mockMvc.perform(post("/events/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/management"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testShowFavouritesAlsUser() throws Exception {
        when(conferentieService.getFavorieten(anyLong())).thenReturn(List.of());

        mockMvc.perform(get("/events/favourites"))
                .andExpect(status().isOk())
                .andExpect(view().name("FavorietenListView"))
                .andExpect(model().attributeExists("favorieten"));
    }

    @Test
    @WithAnonymousUser
    void testRedirectNaarLoginVoorFavourites() throws Exception {
        mockMvc.perform(get("/events/favourites"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testEditEventNietGevonden() throws Exception {
        when(evenementRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/events/edit/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/404"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateEvenement_FoutieveInvoer() throws Exception {
        mockMvc.perform(post("/events/edit/1")
                .param("naam", "") // vereist veld
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("EvenementForm"))
                .andExpect(model().attributeHasErrors("evenement"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAddToFavourites() throws Exception {
        mockMvc.perform(post("/events/1/favourite").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events/1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteAllFavourites() throws Exception {
        mockMvc.perform(post("/events/favourites/delete-all").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events/favourites"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testRemoveFromFavourites() throws Exception {
        mockMvc.perform(post("/events/1/unfavourite").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events/1"));
    }

}

