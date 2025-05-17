package com.springboot.conferentieapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import domein.Evenement;
import domein.Gebruiker;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import repository.GebruikerRepository;
import util.Rol;

@Slf4j
@Controller
public class RegistratieController {
	
	@Autowired
	private GebruikerRepository gebruikerRepo;
	
	@GetMapping("/registration")
	public String showCreateUserForm(Model model) {
        model.addAttribute("gebruiker", new Gebruiker());

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
	        log.warn("Gebruikers formulier bevat fouten" + bindingResult.getAllErrors());
	        return "Registreer";
	    } 
  
        String encryptedWachtwoord = new BCryptPasswordEncoder().encode(gebruiker.getWachtwoord());
        gebruiker.setWachtwoord(encryptedWachtwoord);
        gebruiker.setBevestigWachtwoord(encryptedWachtwoord);

        gebruiker.setRol(Rol.USER); 
        
        gebruikerRepo.save(gebruiker);
        log.info("Nieuwe gebruiker opgeslagen: {}", gebruiker.getEmail());

        return "redirect:/login";
        
	}

}
