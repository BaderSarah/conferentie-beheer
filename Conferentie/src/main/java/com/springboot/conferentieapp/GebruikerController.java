package com.springboot.conferentieapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import domein.evenement.Evenement;
import domein.gebruiker.Gebruiker;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class GebruikerController {
	
	@GetMapping("/login")
	public String showLoginPage(Model model) {
		log.info("GET /login");
		return "Login"; 
	}
	
	@GetMapping("/registration")
	public String showCreateUserForm(Model model) {
        model.addAttribute("registration", new Gebruiker());

		log.info("GET /registration");
	    return "Registreer";
	}
	
	@PostMapping("/registration")
	public String handleCreateUser(
			 @Valid Gebruiker gebruiker,
		     BindingResult bindingResult,
		     Model model) {

		log.info("POST /registration");
	    log.info("" + bindingResult.getFieldErrorCount() + " fouten bij POST /registration"); 
        
        if (bindingResult.hasErrors()) {
	        log.warn("Gebruikers formulier bevat fouten");
	        return "Registreer"; // #TODO bug:behoud de ingevulde waarden niet
	    } 

	    return "EvenementListView";
        
	}

}
