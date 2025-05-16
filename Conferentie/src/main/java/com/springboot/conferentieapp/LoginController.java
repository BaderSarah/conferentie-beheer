package com.springboot.conferentieapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {
    
    @GetMapping
    public String login(String error, String logout, Model model) {

        if (error != null) {
    	    log.warn("user tried logging in, invalid email and/or password");
            model.addAttribute("error", "Invalid email and password!");
        }
        if (logout != null) {
    	    log.info("user logged out");
            model.addAttribute("msg", "You've been logged out successfully.");
        }
        
        return "login";
    }

}