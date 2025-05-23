package com.springboot.conferentieapp;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domein.Evenement;
import exceptions.MaxFavouritesReachedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import repository.EvenementRepository;
import repository.LokaalRepository;
import repository.SprekerRepository;
import service.EvenementenService;
import service.GebruikerDetails;

@Slf4j
@Controller
@RequestMapping("/events")
@RequiredArgsConstructor   
public class EvenementController {

    private final EvenementenService   eventService;
    
    private final EvenementRepository  evenementRepo;
    private final SprekerRepository    sprekerRepo;
    private final LokaalRepository     lokaalRepo;

    @GetMapping
    public String showEventsList(Model model) {
        model.addAttribute(
            "evenementen",
            evenementRepo.findAllByOrderByDatumAscBegintijdstipAsc()
        );
        
	    log.info("GET /events");

        return "EvenementListView";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String showCreateEventForm(Model model, HttpServletRequest request) {
        model.addAttribute("evenement", new Evenement());
        model.addAttribute("sprekersLijst", sprekerRepo.findAll());
        model.addAttribute("lokaalLijst",   lokaalRepo.findAll());
        
	    log.info("GET /events/new");
	    String referer = request.getHeader("Referer");
	    model.addAttribute("referer", referer);
	
        return "EvenementForm";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/delete/{id}")
	public String deleteEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
	    try {
	    	eventService.deleteEvenement(id);
	        redirectAttributes.addFlashAttribute("msg", "Evenement succesvol verwijderd.");
	    } catch (IllegalStateException e) {
	        redirectAttributes.addFlashAttribute("error", "Kan evenement niet verwijderen: " + e.getMessage());
	    }
	    return "redirect:/management";
	}
    

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String handleCreateEvent(@Valid Evenement evenement,
                                    BindingResult bindingResult,
                                    Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("sprekersLijst", sprekerRepo.findAll());
            model.addAttribute("lokaalLijst", lokaalRepo.findAll());
            return "EvenementForm";
        }

        try {
            eventService.createEvenement(evenement);
        } catch (IllegalArgumentException ex) {
            bindingResult.reject(null, ex.getMessage());
            model.addAttribute("sprekersLijst", sprekerRepo.findAll());
            model.addAttribute("lokaalLijst", lokaalRepo.findAll());
            return "EvenementForm";
        }

        return "redirect:/events";
    }


    @GetMapping("/{id}")
    public String eventDetails(@PathVariable long id, Model model, Authentication auth) {
        return evenementRepo.findById(id)
            .map(e -> {
                model.addAttribute("evenement", e);

                GebruikerDetails user = currentUser(auth);
                List<Evenement> favorieten = (user == null)
                    ? List.of()
                    : eventService.getFavorieten(user.getGebruiker().getId());
                model.addAttribute("favorieten", favorieten);
                
                log.info("GET /events/{}", id);
                log.info("favorieten: {}", favorieten);
                return "EvenementView";
            })
            .orElseGet(() -> "redirect:/events");
    }


    private GebruikerDetails currentUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()
            || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return (GebruikerDetails) auth.getPrincipal();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/favourites")
    public String showFavourites(Model model, Authentication auth) {

        GebruikerDetails user = currentUser(auth);

        model.addAttribute("favorieten",
            user == null
              ? List.of() 
              : eventService.getFavorieten(user.getGebruiker().getId())
        );
        
	    log.info("GET /events/favourites");
        
        return "FavorietenListView";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/favourites/delete-all")
    public String deleteAllFavourites(Authentication auth,
                                      RedirectAttributes ra) {

        GebruikerDetails user = currentUser(auth);
        if (user == null) {
            ra.addFlashAttribute("error",
                   "You have to be logged in to change your favourites.");
        } else {
            eventService.deleteAllFavouriteEvents(user.getGebruiker().getId());
            ra.addFlashAttribute("success", "All favourites are deleted.");
        }
        
	    log.info("USER deleted all favourites");
        
        return "redirect:/events/favourites";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/favourite")
    public String addToFavourites(@PathVariable long id,
                                  Authentication auth,
                                  RedirectAttributes ra) {

        GebruikerDetails user = currentUser(auth);
        if (user == null) {
            ra.addFlashAttribute("error", "Login to add a favourite.");
        } else {
            try {
                eventService.addFavouriteEvent(id, user.getGebruiker().getId());
                ra.addFlashAttribute("success",
                        "Event added to favourites.");
                log.info("USER added an event to favourites");
            } catch (MaxFavouritesReachedException ex) {
            	ra.addFlashAttribute("favLimitMsgKey", "flash.limitReached");
                log.info("USER reached favourites limit");
            } catch (Exception ex) {
                ra.addFlashAttribute("error", ex.getMessage());
            }
        }
        return "redirect:/events/" + id;
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/unfavourite")
    public String removeFromFavourites(@PathVariable long id,
                                       Authentication auth,
                                       RedirectAttributes ra) {

        GebruikerDetails user = currentUser(auth);
        if (user == null) {
            ra.addFlashAttribute("error", "Login to delete a favourite.");
        } else {
            try {
                eventService.deleteFavouriteEvent(id, user.getGebruiker().getId());
                ra.addFlashAttribute("success",
                        "Event deleted from favourites.");
            } catch (Exception ex) {
                ra.addFlashAttribute("error", ex.getMessage());
            }
        }
        
	    log.info("USER removed a event to favourites");
        
        return "redirect:/events/" + id;
    }
    
    @GetMapping("/edit/{id}")
    public String showEditEvenementForm(@PathVariable Long id, Model model, HttpServletRequest request) {
        Optional<Evenement> optEvenement = evenementRepo.findById(id);
        if (optEvenement.isEmpty()) return "redirect:/404";

        System.out.println(optEvenement.get().getDatum());
        System.out.println(optEvenement.get().getBegintijdstip());
        System.out.println(optEvenement.get().getEindtijdstip());

        
        model.addAttribute("evenement", optEvenement.get());

        model.addAttribute("sprekersLijst", sprekerRepo.findAll());
        model.addAttribute("lokaalLijst", lokaalRepo.findAll());

        String referer = request.getHeader("Referer");
        model.addAttribute("referer", referer);

        return "EvenementForm";  
    }

    @PostMapping("/edit/{id}")
    public String updateEvenement(
            @PathVariable Long id,
            @Valid @ModelAttribute("evenement") Evenement nieuwEvenement,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest request
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("evenement", nieuwEvenement);
            model.addAttribute("sprekersLijst", sprekerRepo.findAll());
            model.addAttribute("lokaalLijst", lokaalRepo.findAll());
            return "EvenementForm";
        }

        Optional<Evenement> opt = evenementRepo.findById(id);
        if (opt.isEmpty()) return "redirect:/404";

        Evenement bestaand = opt.get();

        bestaand.setNaam(nieuwEvenement.getNaam());
        bestaand.setBeschrijving(nieuwEvenement.getBeschrijving());
        bestaand.setDatum(nieuwEvenement.getDatum());
        bestaand.setBegintijdstip(nieuwEvenement.getBegintijdstip());
        bestaand.setEindtijdstip(nieuwEvenement.getEindtijdstip());
        bestaand.setBeamercode(nieuwEvenement.getBeamercode());
        bestaand.setBeamercheck(nieuwEvenement.getBeamercheck());
        bestaand.setPrijs(nieuwEvenement.getPrijs());
        bestaand.setLokaal(nieuwEvenement.getLokaal());
        bestaand.setSprekers(nieuwEvenement.getSprekers());

        evenementRepo.save(bestaand);

        return "redirect:/management";
    }

    
}

