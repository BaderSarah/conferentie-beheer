package com.springboot.conferentieapp;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import repository.EvenementRepository;
import repository.LokaalRepository;
import repository.SprekerRepository;

@Controller
@RequiredArgsConstructor
@Secured("ROLE_ADMIN")
public class AdminBeheerController {

    private final LokaalRepository lokaalRepo;
    private final SprekerRepository sprekerRepo;
    private final EvenementRepository evenementRepo;

    @GetMapping("/management")
    public String showBeheerPagina(Model model) {
        model.addAttribute("lokalen", lokaalRepo.findAll());
        model.addAttribute("sprekers", sprekerRepo.findAll());
        model.addAttribute("evenementen", evenementRepo.findAll());
        return "Beheer";
    }

    @PostMapping("/room/delete/{id}")
    public String deleteLokaal(@PathVariable long id) {
        lokaalRepo.deleteById(id);
        return "redirect:/management";
    }

    @PostMapping("/speaker/delete/{id}")
    public String deleteSpreker(@PathVariable long id) {
        sprekerRepo.deleteById(id);
        return "redirect:/management";
    }

    @PostMapping("/event/delete/{id}")
    public String deleteEvent(@PathVariable long id) {
        evenementRepo.deleteById(id);
        return "redirect:/management";
    }
}
