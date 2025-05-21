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

import domein.Lokaal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import repository.LokaalRepository;
import service.ConferentieService;

@Slf4j
@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/rooms")
public class LokaalController {
	
	@Autowired
	private LokaalRepository repository; 
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ConferentieService confServ; 
	
	@GetMapping       
	public String redirectToForm() {
	    return "redirect:/rooms/new";
	}
	
	@GetMapping("/new")
	public String showCreateLokaalForm(Model model, HttpServletRequest request) {
	    if (!model.containsAttribute("lokaal")) {
	        model.addAttribute("lokaal", new Lokaal());
	    }
	    log.info("GET /rooms/new");
	    String referer = request.getHeader("Referer");
	    model.addAttribute("referer", referer);
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
	
	@GetMapping("/edit/{id}")
	public String showEditLokaalForm(@PathVariable Long id, Model model, HttpServletRequest request) {
	    Optional<Lokaal> lokaal = repository.findById(id);
	    if (lokaal.isEmpty()) return "redirect:/404";

	    model.addAttribute("lokaal", lokaal.get());

	    String referer = request.getHeader("Referer");
	    model.addAttribute("referer", referer); 

	    return "LokaalForm";
	}
	
	@PostMapping("/edit/{id}")
	public String updateLokaal(
	        @PathVariable Long id,
	        @Valid Lokaal lokaal,
	        BindingResult bindingResult,
	        Model model,
	        Locale locale
	) {
	    if (bindingResult.hasErrors()) {
	        return "LokaalForm";
	    }

	    Optional<Lokaal> bestaand = repository.findById(id);
	    if (bestaand.isEmpty()) return "redirect:/404";

	    Lokaal bestaandLokaal = bestaand.get();
	    bestaandLokaal.setNaam(lokaal.getNaam());
	    bestaandLokaal.setCapaciteit(lokaal.getCapaciteit());

	    repository.save(bestaandLokaal);

	    model.addAttribute("lokaal", new Lokaal()); 

	    return "redirect:/management";
	}

	
//	@PostMapping("/delete/{id}")
//	public String deleteLokaal(@PathVariable Long id, RedirectAttributes redirectAttributes) {
//	    confServ.deleteLokaal(id); 
//	    redirectAttributes.addFlashAttribute("msg", "Lokaal succesvol verwijderd.");
//	    
//	    return "redirect:/management"; 
//	}
}
