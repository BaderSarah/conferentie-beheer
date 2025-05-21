package com.springboot.conferentieapp;

import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domein.Evenement;
import domein.Lokaal;
import exception.MaxFavouritesReachedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import repository.EvenementRepository;
import repository.LokaalRepository;
import repository.SprekerRepository;
import service.ConferentieService;
import service.GebruikerDetails;

@Slf4j
@Controller
@RequestMapping("/events")
@RequiredArgsConstructor   
public class EvenementController {

    private final ConferentieService   confService;
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
    
    @PostMapping("/delete/{id}")
    public String deleteEvent(@PathVariable long id) {
    	evenementRepo.deleteById(id);
        return "redirect:/management";
    }

    @PostMapping
    public String handleCreateEvent(@Valid Evenement evenement,
                                    BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "EvenementForm";
        }
        confService.createEvenement(evenement);
        return "redirect:/events";
    }

    @GetMapping("/{id}")
    public String eventDetails(@PathVariable long id, Model model, Authentication auth) {
        return evenementRepo.findById(id)
            .map(e -> {
                model.addAttribute("evenement", e);

                GebruikerDetails user = currentUser(auth);
                Set<Evenement> favorieten = (user == null)
                    ? Set.of()
                    : confService.getFavorieten(user.getGebruiker().getId());
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

    @GetMapping("/favourites")
    public String showFavourites(Model model, Authentication auth) {

        GebruikerDetails user = currentUser(auth);

        model.addAttribute("favorieten",
            user == null
              ? List.of() 
              : confService.getFavorieten(user.getGebruiker().getId())
        );
        
	    log.info("GET /events/favourites");
        
        return "FavorietenListView";
    }

    @PostMapping("/favourites/delete-all")
    public String deleteAllFavourites(Authentication auth,
                                      RedirectAttributes ra) {

        GebruikerDetails user = currentUser(auth);
        if (user == null) {
            ra.addFlashAttribute("error",
                   "You have to be logged in to change your favourites.");
        } else {
            confService.deleteAllFavouriteEvents(user.getGebruiker().getId());
            ra.addFlashAttribute("success", "All favourites are deleted.");
        }
        
	    log.info("USER deleted all favourites");
        
        return "redirect:/events/favourites";
    }

    @PostMapping("/{id}/favourite")
    public String addToFavourites(@PathVariable long id,
                                  Authentication auth,
                                  RedirectAttributes ra) {

        GebruikerDetails user = currentUser(auth);
        if (user == null) {
            ra.addFlashAttribute("error", "Login to add a favourite.");
        } else {
            try {
                confService.addFavouriteEvent(id, user.getGebruiker().getId());
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


    @PostMapping("/{id}/unfavourite")
    public String removeFromFavourites(@PathVariable long id,
                                       Authentication auth,
                                       RedirectAttributes ra) {

        GebruikerDetails user = currentUser(auth);
        if (user == null) {
            ra.addFlashAttribute("error", "Login to delete a favourite.");
        } else {
            try {
                confService.deleteFavouriteEvent(id, user.getGebruiker().getId());
                ra.addFlashAttribute("success",
                        "Event deleted from favourites.");
            } catch (Exception ex) {
                ra.addFlashAttribute("error", ex.getMessage());
            }
        }
        
	    log.info("USER removed a event to favourites");
        
        return "redirect:/events/" + id;
    }
}

