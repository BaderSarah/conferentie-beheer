package com.springboot.conferentieapp.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import domein.Lokaal;
import service.LokaalService;

@RestController
@RequestMapping(value = "/rest")
public class LokaalRestController {

	@Autowired
	private LokaalService lokaalService;
	    
	    @GetMapping(value = "/rooms/{id}") 
	    public Lokaal getLokaal(@PathVariable("id") Long id) {
	    	return lokaalService.getLokaal(id);
	    }

	    @GetMapping(value = "/rooms")
	    public List<Lokaal> getAllLokaals() {
	        return lokaalService.getAllLokaals(); 
	    }
	    
	    @PostMapping(value = "/rooms/create")
	    public Lokaal createLokaal(@RequestBody Lokaal lokaal) { 
	        return lokaalService.createLokaal(lokaal); 
	    }

	    @GetMapping(value = "/rooms/{id}/capacity")
	    public int getCapaciteit(@PathVariable("id") Long id) {
	        return lokaalService.getCapaciteitVanLokaal(id);
	    }
	    
	    @PutMapping(value = "/rooms/{id}")
	    public Lokaal updateLokaal(@PathVariable("id") Long id, @RequestBody Lokaal lokaal) {
	        return lokaalService.updateLokaal(id, lokaal);
	    }
}

