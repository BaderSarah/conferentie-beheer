package com.springboot.conferentieapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import domein.evenement.Lokaal;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/rooms")
public class LokaalController {
	
	@ModelAttribute("lokaal")
	public Lokaal createLokaal(){
		return new Lokaal(); 
	}

	@GetMapping("/new")
	public String showCreateEventForm(Model model) {
//        model.addAttribute("lokaal", new Lokaal());
		log.info("GET /rooms/new");
	    return "LokaalForm";
	}
	
	@PostMapping
	public String handleCreateLokaal(
	        @Valid Lokaal lokaal,
	        BindingResult bindingResult,
	        Model model) {

	    log.info("POST /rooms");
	    log.info("" + bindingResult.getFieldErrorCount() + " fouten bij POST /rooms"); 

	    if (bindingResult.hasErrors()) {
	        log.warn("Lokaal formulier bevat fouten");
	        return "LokaalForm"; // #TODO bug:behoud de ingevulde waarden niet
	    } 

	    return "EvenementForm";
	}
}
