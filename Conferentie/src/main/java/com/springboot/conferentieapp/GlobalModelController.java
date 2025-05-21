package com.springboot.conferentieapp;

import java.time.format.DateTimeFormatter;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelController {

    private final ConferentieEigenschappen eigenschappen;

    public GlobalModelController(ConferentieEigenschappen eigenschappen) {
        this.eigenschappen = eigenschappen;
    }

    @ModelAttribute
    public void addConferenceInfo(Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formatted = eigenschappen.getStart().format(formatter) + " - " + eigenschappen.getEinde().format(formatter);
        model.addAttribute("conferentiePeriode", formatted);
    }
}
