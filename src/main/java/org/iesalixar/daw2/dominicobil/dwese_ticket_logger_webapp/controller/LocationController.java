package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.controller;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.repositories.LocationRepository;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.repositories.ProvinceRepository;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.repositories.SupermarketRepository;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entities.Location;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entities.Province;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entities.Supermarket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/locations")
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private SupermarketRepository supermarketRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listLocations(Model model) {
        logger.info("Requesting the list of all locations...");
        List<Location> listLocations = locationRepository.findAll();
        logger.info("Loaded {} locations.", listLocations.size());
        model.addAttribute("listLocations", listLocations);
        return "location";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Displaying form to create a new location.");
        model.addAttribute("location", new Location());
        model.addAttribute("listProvinces", provinceRepository.findAll());
        model.addAttribute("listSupermarkets", supermarketRepository.findAll());
        return "location-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Displaying edit form for the location with ID {}", id);
        Optional<Location> location = locationRepository.findById(id);

        if (location.isEmpty()) {
            logger.warn("Location with ID {} not found.", id);
            return "redirect:/locations";
        }

        model.addAttribute("location", location.get());
        model.addAttribute("listProvinces", provinceRepository.findAll());
        model.addAttribute("listSupermarkets", supermarketRepository.findAll());
        return "location-form";
    }

    @PostMapping("/insert")
    public String insertLocation(@Valid @ModelAttribute("location") Location location, BindingResult result,
                                 RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Inserting a new location: {}", location.getAddress());

        if (result.hasErrors()) {
            return "location-form";
        }

        if (locationRepository.existsLocationByAddress(location.getAddress())) {
            logger.warn("Location code {} already exists.", location.getAddress());
            String errorMessage = messageSource.getMessage("msg.location-controller.insert.CodeExists", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/locations/new";
        }

        locationRepository.save(location);
        logger.info("Location {} inserted successfully.", location.getAddress());
        redirectAttributes.addFlashAttribute("successMessage", "Location created successfully.");
        return "redirect:/locations";
    }

    @PostMapping("/update")
    public String updateLocation(@Valid @ModelAttribute("location") Location location, BindingResult result,
                                 RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Updating location with ID {}", location.getId());

        if (result.hasErrors()) {
            return "location-form";
        }

        if (locationRepository.existsLocationByAddressndNotId(location.getAddress(), location.getId())) {
            logger.warn("Location code {} already exists for another location.", location.getAddress());
            String errorMessage = messageSource.getMessage("msg.location-controller.update.CodeExists", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/locations/edit?id=" + location.getId();
        }

        locationRepository.save(location);
        logger.info("Location with ID {} updated successfully.", location.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Location updated successfully.");
        return "redirect:/locations";
    }

    @PostMapping("/delete")
    public String deleteLocation(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Deleting location with ID {}", id);
        Optional<Location> location = locationRepository.findById(id);

        if (location.isEmpty()) {
            logger.warn("Location with ID {} not found.", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Location not found.");
            return "redirect:/locations";
        }

        locationRepository.deleteById(id);
        logger.info("Location with ID {} deleted successfully.", id);
        redirectAttributes.addFlashAttribute("successMessage", "Location deleted successfully.");
        return "redirect:/locations";
    }
}
