
package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.controller;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entities.Supermarket;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.repositories.SupermarketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/supermarkets")
public class SupermarketController {

    @Autowired
    private MessageSource messageSource;

    private static final Logger logger = LoggerFactory.getLogger(SupermarketController.class);

    @Autowired
    private SupermarketRepository supermarketRepository;

    @GetMapping
    public String listSupermarkets(Model model) {
        logger.info("Requesting the list of all supermarkets...");
        List<Supermarket> listSupermarkets = supermarketRepository.findAll();
        logger.info("Loaded {} supermarkets.", listSupermarkets.size());
        model.addAttribute("listSupermarkets", listSupermarkets);
        return "supermarket";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Displaying form to create a new supermarket.");
        model.addAttribute("supermarket", new Supermarket());
        return "supermarket-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        logger.info("Displaying edit form for the supermarket with ID {}", id);
        Optional<Supermarket> supermarketOptional = supermarketRepository.findById(id);

        if (supermarketOptional.isEmpty()) {
            logger.warn("Supermarket with ID {} not found.", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Supermarket not found.");
            return "redirect:/supermarkets";
        }

        model.addAttribute("supermarket", supermarketOptional.get());
        return "supermarket-form";
    }

    @PostMapping("/insert")
    public String insertSupermarket(
            @Valid @ModelAttribute("supermarket") Supermarket supermarket,
            BindingResult result,
            RedirectAttributes redirectAttributes, Locale locale) {

        if (result.hasErrors()) {
            return "supermarket-form";
        }

        logger.info("Inserting new supermarket with name {}", supermarket.getName());

        if (supermarketRepository.existsSupermarketByName(supermarket.getName())) {
            logger.warn("Supermarket name {} already exists.", supermarket.getName());
            String errorMessage = messageSource.getMessage("msg.supermarket-controller.insert.nameExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/supermarkets/new";
        }

        supermarketRepository.save(supermarket);
        logger.info("Supermarket {} inserted successfully.", supermarket.getName());
        return "redirect:/supermarkets";
    }

    @PostMapping("/update")
    public String updateSupermarket(
            @Valid @ModelAttribute("supermarket") Supermarket supermarket,
            BindingResult result,
            RedirectAttributes redirectAttributes, Locale locale) {

        logger.info("Updating supermarket with ID {}", supermarket.getId());

        if (result.hasErrors()) {
            return "supermarket-form";
        }

        if (supermarketRepository.existsSupermarketByNameAndNotId(supermarket.getName(), supermarket.getId())) {
            logger.warn("Supermarket name {} already exists for another supermarket.", supermarket.getName());
            String errorMessage = messageSource.getMessage("msg.supermarket-controller.update.nameExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/supermarkets/edit?id=" + supermarket.getId();
        }

        supermarketRepository.save(supermarket);
        logger.info("Supermarket with ID {} updated successfully.", supermarket.getId());
        return "redirect:/supermarkets";
    }

    @PostMapping("/delete")
    public String deleteSupermarket(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Deleting supermarket with ID {}", id);
        Optional<Supermarket> supermarketOptional = supermarketRepository.findById(id);

        if (supermarketOptional.isEmpty()) {
            logger.warn("Supermarket with ID {} not found.", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Supermarket not found.");
            return "redirect:/supermarkets";
        }

        try {
            supermarketRepository.deleteById(id);
            logger.info("Supermarket with ID {} deleted successfully.", id);
        } catch (DataIntegrityViolationException e) {
            logger.error("Error deleting supermarket with ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting supermarket.");
        }

        return "redirect:/supermarkets";
    }
}