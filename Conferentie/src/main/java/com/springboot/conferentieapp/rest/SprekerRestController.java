package com.springboot.conferentieapp.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import domein.Spreker;
import service.SprekerService;

@RestController
@RequestMapping(value = "/rest")
public class SprekerRestController {

	@Autowired
	private SprekerService sprekerService;
	    
	    @GetMapping(value = "/speakers/{id}") 
	    public Spreker getSpreker(@PathVariable("id") Long id) {
	    	return sprekerService.getSpreker(id);
	    }

	    @GetMapping(value = "/speakers")
	    public List<Spreker> getAllSprekers() {
	        return sprekerService.getAllSprekers(); 
	    }
	    
	    @PostMapping(value = "/speakers/create")
	    public Spreker createSpreker(@RequestBody Spreker spreker) { 
	        return sprekerService.createSpreker(spreker); 
	    }
	    
//	    @DeleteMapping(value = "/rooms/delete/{id}")
//	    public void deleteSpreker(@PathVariable("id") Long id) {
//	    	sprekerService.deleteSpreker(id);
//	    }

	}

