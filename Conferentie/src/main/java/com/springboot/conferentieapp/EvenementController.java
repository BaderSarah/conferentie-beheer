package com.springboot.conferentieapp;

//import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import domein.evenement.Evenement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/events")
public class EvenementController {
	
//	@Autowired // moet dit autowired want ik heb er maar één van en het is al een implementatie geen bean
	// mss als je gebruiker een BeheerderBean en dan de juiste geeft maar in mij JPA moet ik voor
	// mijn gebruikercontroller de twee beheerders hebben
	//	private EvenementBeheer eb;  || private BeheerBean bb;
	// als je de tweede neemt dan moet in ConferentieApplication.java:
	//@Bean // voor de injectie
	//BeheerBean beheerBean(){
	//  return new EvenementBeheer();
	// }

//	@ModelAttribute("eventsList")
//	public List<Evenement> populateEvents(){
//		// #TODO
//		return null; 
//	}
	
	@GetMapping 
	public String showEventsList(Model model) {
		log.info("get /events");
		return "EvenementListView"; 
	}
	
	@GetMapping("/new")
	public String showCreateEventForm(Model model) {
        model.addAttribute("evenement", new Evenement());
		
		log.info("GET /events/new");
	    return "EvenementForm";
	}

	@PostMapping
	public String handleCreateEvent(
	        @Valid Evenement evenement,
	        BindingResult bindingResult,
	        Model model) {

	    log.info("POST /events");
	    log.info("" + bindingResult.getFieldErrorCount()); 

	    if (bindingResult.hasErrors()) {
	        log.warn("Formulier bevat fouten");
	        return "EvenementForm"; // #TODO behoud de ingevulde waarden niet
	    } 

	    return "EvenementListView";
	}
	
//	@PostMapping("/{id}")
//  public String showEventById(@PathVariable("id") long id, Model model) {
//		return "EvenementView"; 
//	}
	
}
