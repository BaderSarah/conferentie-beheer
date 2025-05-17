package com.springboot.conferentieapp;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import domein.Lokaal;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import repository.LokaalRepository;

@Slf4j
@Controller
@RequestMapping("/rooms")
public class LokaalController {
	
	@Autowired
	private LokaalRepository repository; 
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping       
	public String redirectToForm() {
	    return "redirect:/rooms/new";
	}
	
	@GetMapping("/new")
	public String showCreateLokaalForm(Model model) {
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
	        Model model,
	        Locale locale) {

	    log.info("POST /rooms");
	    if (!bindingResult.hasFieldErrors("naam") 
	        && repository.existsByNaam(lokaal.getNaam())) {

	        bindingResult.rejectValue(
	            "naam",
	            "lokaal.err.name.unique",
	            "This name is already registered");
	    }

	    if (bindingResult.hasErrors()) {
	        return "LokaalForm";
	    }

	    try {
	        repository.save(lokaal);   
	    } catch (DataIntegrityViolationException ex) {
	        bindingResult.rejectValue(
	            "naam",
	            "lokaal.err.name.unique",
	            "This name is already registered");
	        return "LokaalForm";
	    }
	    
	    String msg = messageSource.getMessage(
	            "lokaal.success",
	            new Object[]{lokaal.getNaam(), lokaal.getCapaciteit()},
	            locale
	        );

	    model.addAttribute("msg", msg);
	    model.addAttribute("lokaal", new Lokaal());

	    return "LokaalForm";

	}
}
