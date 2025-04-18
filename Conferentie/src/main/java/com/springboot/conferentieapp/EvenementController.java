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

@Slf4j
@Controller
@RequestMapping("/events")
public class EvenementController {
	
	@Autowired
	private EvenementRepository eveRepository; 
	
	@Autowired
	private SprekerRepository   speRepository; 
	
	@Autowired
	private LokaalRepository    lokRepository; 
	
	@GetMapping 
	public String showEventsList(Model model) {	
		model.addAttribute("evenementen", eveRepository.findAll()); 
		
		log.info("GET /events");
		return "EvenementListView"; 
	}
	
	@GetMapping("/new")
	public String showCreateEventForm(Model model) {
        model.addAttribute("evenement", new Evenement());
        model.addAttribute("sprekersLijst", speRepository.findAll());
        model.addAttribute("lokaalLijst", lokRepository.findAll());

		
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
	        return "EvenementForm"; // #TODO bug:behoud de ingevulde waarden niet
	    } 

	    return "EvenementListView";
	}
	

	 @GetMapping("/{id}")
	 public String eventDetails(@PathVariable long id, Model model) {
	     Optional<Evenement> optionalEvenement = eveRepository.findById(id);
		 log.info("GET /events/${:id}"); 
	     
	     if (optionalEvenement.isPresent()) {
	    	 
	         model.addAttribute("evenement", optionalEvenement.get());
	         log.info("events/${:id} -> got event" + optionalEvenement.get()); 
	         return "EvenementView";
	     } else {
	    	 
	    	 log.error("events/${:id} -> NO EVENT FOUND"); 
	         return "redirect:/events"; 
	     }
	 }

	 	// favourites of the logged in USER
		@GetMapping("/favourites")
		public String showFavourites(Model model) {
			log.info("GET /events/favourites");
			return "FavorietenListView"; 
		}
	
}
