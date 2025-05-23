package com.springboot.conferentieapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import domein.Evenement;
import domein.Gebruiker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import repository.GebruikerRepository;
import util.Rol;
import validator.OnCreate;

@Slf4j
@Controller
public class RegistratieController {

    @Autowired
    private GebruikerRepository gebruikerRepo;
    
    @GetMapping("/registration")
    public String showCreateUserForm(Model model, HttpServletRequest request) {
        model.addAttribute("gebruiker", new Gebruiker());
        log.info("GET /registration");
        
	    String referer = request.getHeader("Referer");
	    model.addAttribute("referer", referer);
        return "Registreer";
    }

    @PostMapping("/registration")
    public String handleCreateUser(
            @ModelAttribute("gebruiker") @Valid Gebruiker gebruiker,
            BindingResult bindingResult,
            Model model) {

        log.info("POST /registration – {} fouten", bindingResult.getFieldErrorCount());

        if (bindingResult.hasErrors()) {
            log.warn("Fouten in formulier: {}", bindingResult.getAllErrors());
            return "Registreer";
        }

        String enc = new BCryptPasswordEncoder().encode(gebruiker.getWachtwoord());
        gebruiker.setWachtwoord(enc);

        gebruiker.setBevestigWachtwoord(null);

        gebruiker.setRol(Rol.USER);

        gebruikerRepo.save(gebruiker);
        log.info("Nieuwe gebruiker opgeslagen: {}", gebruiker.getEmail());

        return "redirect:/login";
    }
}
