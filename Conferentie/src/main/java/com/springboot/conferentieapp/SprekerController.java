package com.springboot.conferentieapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import domein.Spreker;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import repository.LokaalRepository;
import repository.SprekerRepository;

@Slf4j
@Controller
@RequestMapping("/speakers")
public class SprekerController {
	
	@Autowired
	private SprekerRepository repository; 

	@GetMapping("/new")
	public String showCreateEventForm(Model model) {
        model.addAttribute("spreker", new Spreker());
		log.info("GET /speakers/new");
	    return "SprekerForm";
	}
	
	@PostMapping
	public String handleCreateLokaal(
	        @Valid Spreker spreker,
	        BindingResult bindingResult,
	        Model model) {

	    log.info("POST /speakers");
	    log.info("" + bindingResult.getFieldErrorCount() + " fouten bij POST /speakers"); 

	    if (bindingResult.hasErrors()) {
	        log.warn("Spreker formulier bevat fouten");
	        return "SprekerForm";
	    } 

	    repository.save(spreker); 
        return "redirect:/events/new"; 
	}
}

