package com.springboot.conferentieapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import domein.Lokaal;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/rooms")
public class LokaalController {
	
	@GetMapping("/new")
	public String showCreateEventForm(Model model) {
	    if (!model.containsAttribute("lokaal")) {
	        model.addAttribute("lokaal", new Lokaal());
	    }
	    log.info("GET /rooms/new");
	    return "LokaalForm";
	}

	@PostMapping
	public String handleCreateLokaal(
	        @Valid Lokaal lokaal,
	        BindingResult bindingResult,
	        Model model) {

	    log.info("POST /rooms");
	    if (bindingResult.hasErrors()) {
	        model.addAttribute("lokaal", lokaal);
	        return "LokaalForm";
	    }

	    return "EvenementForm";
	}

}
