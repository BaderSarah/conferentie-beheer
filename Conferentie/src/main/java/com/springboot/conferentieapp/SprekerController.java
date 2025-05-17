package com.springboot.conferentieapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import domein.Lokaal;
import domein.Spreker;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import repository.SprekerRepository;

@Slf4j
@Controller
@RequestMapping("/speakers")
public class SprekerController {
	
	@Autowired
	private SprekerRepository repository; 
	
	@GetMapping       
	public String redirectToForm() {
	    return "redirect:/speakers/new";
	}


	@GetMapping("/new")
	public String showCreateSprekerForm(Model model) {
        model.addAttribute("spreker", new Spreker());
		log.info("GET /speakers/new");
	    return "SprekerForm";
	}
	
	@PostMapping
	public String handleCreateSpreker(
	        @Valid Spreker spreker,
	        BindingResult bindingResult,
	        Model model) {

	    if (!bindingResult.hasFieldErrors("email") 
	        && repository.existsByEmail(spreker.getEmail())) {

	        bindingResult.rejectValue(
	            "email",
	            "spreker.err.email.unique",
	            "This e-mail address is already registered");
	    }

	    if (bindingResult.hasErrors()) {
	        return "SprekerForm";
	    }

	    try {
	        repository.save(spreker);   
	    } catch (DataIntegrityViolationException ex) {
	        bindingResult.rejectValue(
	            "email",
	            "spreker.err.email.unique",
	            "This e-mail address is already registered");
	        return "SprekerForm";
	    }

	    String msg = String.format("Spreker met email '%s' werd toegevoegd.",
                spreker.getEmail());

		model.addAttribute("msg", msg);
		model.addAttribute("spreker", new Spreker());
		
		return "SprekerForm";

	}

}

