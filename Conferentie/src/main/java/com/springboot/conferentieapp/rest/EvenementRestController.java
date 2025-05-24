package com.springboot.conferentieapp.rest;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import domein.Evenement;
import domein.Spreker;
import service.EvenementenService;
import service.SprekerService;

@RestController
@RequestMapping(value = "/rest")
public class EvenementRestController {

	@Autowired
	private EvenementenService eventService;
	    
	    @GetMapping(value = "/events/{id}") 
	    public Evenement getSpreker(@PathVariable("id") Long id) {
	    	return eventService.getEvenement(id);
	    }

	    @GetMapping(value = "/events")
	    public List<Evenement> getAllEvents() {
	        return eventService.getAllEvenements(); 
	    }
	    
	    @PostMapping(value = "/events/create")
	    public Evenement createEvenement(@RequestBody Evenement event) { 
	        return eventService.createEvenement(event); 
	    }
	    
	    @DeleteMapping(value = "/events/delete/{id}")
	    public void deleteEvenement(@PathVariable("id") Long id) {
	    	eventService.deleteEvenement(id);
	    }
	    
	    @GetMapping(value = "/events/bydate/{datum}")
	    public List<Evenement> getEventsByDate(@PathVariable("datum") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datum) {
	        return eventService.getEvenementenByDatum(datum);
	    }

	    @PutMapping(value = "/events/{id}")
	    public Evenement updateEvenement(@PathVariable("id") Long id, @RequestBody Evenement nieuwEvenement) {
	        return eventService.updateEvenement(id, nieuwEvenement);
	    }
}
