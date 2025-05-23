package com.springboot.conferentieapp;

import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domein.Spreker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import repository.SprekerRepository;
import service.SprekerService;

@Slf4j
@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/speakers")
public class SprekerController {
	
	@Autowired
	private SprekerRepository repository; 
	
	@Autowired
	private SprekerService sprekerService; 
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping       
	public String redirectToForm() {
	    return "redirect:/speakers/new";
	}

	@GetMapping("/new")
	public String showCreateSprekerForm(Model model, HttpServletRequest request) {
        model.addAttribute("spreker", new Spreker());
		log.info("GET /speakers/new");
	    
	    String referer = request.getHeader("Referer");
	    model.addAttribute("referer", referer);
		return "SprekerForm";
	}

	
	@PostMapping
	public String handleCreateSpreker(
	        @Valid Spreker spreker,
	        BindingResult bindingResult,
	        Model model,
	        Locale locale) {

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

	    
	    String msg = messageSource.getMessage(
	            "spreker.success",
	            new Object[]{spreker.getEmail()},
	            locale
	        );

		model.addAttribute("msg", msg);
		model.addAttribute("spreker", new Spreker());
		
		return "SprekerForm";

	}
	
	@GetMapping("/edit/{id}")
	public String showEditSpeakerForm(@PathVariable Long id, Model model, HttpServletRequest request) {
	    Optional<Spreker> spreker = repository.findById(id);
	    if (spreker.isEmpty()) return "redirect:/404";

	    model.addAttribute("spreker", spreker.get());

	    String referer = request.getHeader("Referer");
	    model.addAttribute("referer", referer); 

	    return "SprekerForm";
	}
	
	@PostMapping("/delete/{id}")
	public String deleteSpreker(@PathVariable Long id, RedirectAttributes redirectAttributes) {
	    try {
	        sprekerService.deleteSpreker(id);
	        redirectAttributes.addFlashAttribute("msg", "Spreker succesvol verwijderd.");
	    } catch (IllegalStateException e) {
	        redirectAttributes.addFlashAttribute("error", "Kan spreker niet verwijderen: " + e.getMessage());
	    }
	    return "redirect:/management";
	}
	
	@PostMapping("/edit/{id}")
	public String updateSpreker(
	        @PathVariable Long id,
	        @Valid Spreker spreker,
	        BindingResult bindingResult,
	        Model model,
	        Locale locale
	) {
	    if (bindingResult.hasErrors()) {
	        return "LokaalForm";
	    }

	    Optional<Spreker> bestaand = repository.findById(id);
	    if (bestaand.isEmpty()) return "redirect:/404";

	    Spreker bestaandSpreker = bestaand.get();
	    bestaandSpreker.setNaam(spreker.getNaam());
	    bestaandSpreker.setVoornaam(spreker.getVoornaam());
	    bestaandSpreker.setEmail(spreker.getEmail()); 
	    
	    repository.save(bestaandSpreker);

	    model.addAttribute("spreker", new Spreker()); 

	    return "redirect:/management";
	}

}

