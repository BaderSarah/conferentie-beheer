package com.springboot.conferentieapp;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

//import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import domein.Evenement;
import domein.Lokaal;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import repository.EvenementRepository;
import repository.LokaalRepository;
import repository.SprekerRepository;
import service.ConferentieService;

@Slf4j
@Controller
@RequestMapping("/events")
public class EvenementController {
	
	@Autowired
	private ConferentieService confService; 
	
	@Autowired
	private EvenementRepository evenementRepo; 
	
	@Autowired
	private SprekerRepository sprekerRepo; 
	
	@Autowired
	private LokaalRepository lokaalRepo; 
	
	@GetMapping 
	public String showEventsList(Model model) {	
		model.addAttribute("evenementen", evenementRepo.findAll()); 
		
		log.info("GET /events");
		return "EvenementListView"; 
	}
	
	@GetMapping("/new")
	public String showCreateEventForm(Model model) {
        model.addAttribute("evenement", new Evenement());
        model.addAttribute("sprekersLijst", sprekerRepo.findAll());
        model.addAttribute("lokaalLijst", lokaalRepo.findAll());

		
		log.info("GET /events/new");
	    return "EvenementForm";
	}

	@PostMapping
	public String handleCreateEvent(
	        @Valid Evenement evenement,
	        BindingResult bindingResult,
	        Model model) {

	    log.info("POST /events");
	    log.info("" + bindingResult.getFieldErrorCount() + " fouten bij POST /events"); 

	    if (bindingResult.hasErrors()) {
	        log.warn("Evenement formulier bevat fouten");
	        return "EvenementForm"; 
	    } 
	    
	    confService.createEvenement(evenement); 

	    return "EvenementListView";
	}
	

	 @GetMapping("/{id}")
	 public String eventDetails(@PathVariable long id, Model model) {
	     Optional<Evenement> optionalEvenement = evenementRepo.findById(id);
		 log.info("GET /events/${:id}"); 
	     
	     if (optionalEvenement.isPresent()) {
	    	 
	         model.addAttribute("evenement", optionalEvenement.get());
	         log.info("events/${:id} -> event gevonden" + optionalEvenement.get()); 
	         return "EvenementView";
	     } else {
	    	 
	    	 log.error("events/${:id} -> geen event gevonden"); 
	         return "redirect:/events"; 
	     }
	 }

		@GetMapping("/favourites")
		public String showFavourites(Model model) {
			log.info("GET /events/favourites");
			return "FavorietenListView"; 
		}
	
//		@GetMapping("/favourites")
//		public String showFavourites(Model model, @AuthenticationPrincipal Gebruiker gebruiker) {
//		    model.addAttribute("favorieten", gebruiker.getFavorieteEvenementen());
//		    return "FavorietenListView";
//		}
//
//		@PostMapping("/{id}/favourite")
//		public String addToFavourites(@PathVariable long id, @AuthenticationPrincipal Gebruiker gebruiker, RedirectAttributes redirectAttributes) {
//		    try {
//		        confService.addFavouriteEvent(id, gebruiker.getId());
//		        redirectAttributes.addFlashAttribute("success", "Evenement toegevoegd aan je favorieten.");
//		    } catch (Exception e) {
//		        redirectAttributes.addFlashAttribute("error", e.getMessage());
//		    }
//		    return "redirect:/events/" + id;
//		}
//
//		@PostMapping("/{id}/unfavourite")
//		public String removeFromFavourites(@PathVariable long id, @AuthenticationPrincipal Gebruiker gebruiker, RedirectAttributes redirectAttributes) {
//		    try {
//		        confService.deleteFavouriteEvent(id, gebruiker.getId());
//		        redirectAttributes.addFlashAttribute("success", "Evenement verwijderd uit je favorieten.");
//		    } catch (Exception e) {
//		        redirectAttributes.addFlashAttribute("error", e.getMessage());
//		    }
//		    return "redirect:/events/" + id;
//		}

}
