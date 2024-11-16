package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.controller;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entities.Region;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.repositories.RegionRepository;
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
@RequestMapping("/regions")
public class RegionController {

    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listRegions(Model model) {
        logger.info("Requesting the list of all regions...");
        List<Region> listRegions = regionRepository.findAll();
        logger.info("Loaded {} regions.", listRegions.size());
        model.addAttribute("listRegions", listRegions);
        return "region";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Displaying form to create a new region.");
        model.addAttribute("region", new Region());
        return "region-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        logger.info("Displaying edit form for the region with ID {}", id);
        Optional<Region> regionOptional = regionRepository.findById(id);

        if (regionOptional.isEmpty()) {
            logger.warn("Region with ID {} not found.", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Region not found.");
            return "redirect:/regions";
        }

        model.addAttribute("region", regionOptional.get());
        return "region-form";
    }

    @PostMapping("/insert")
    public String insertRegion(
            @Valid @ModelAttribute("region") Region region,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        if (result.hasErrors()) {
            return "region-form";
        }

        logger.info("Inserting new region with code {}", region.getCode());

        if (regionRepository.existsRegionByCode(region.getCode())) {
            logger.warn("Region code {} already exists.", region.getCode());
            String errorMessage = messageSource.getMessage("msg.region-controller.insert.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/regions/new";
        }

        regionRepository.save(region);
        logger.info("Region {} inserted successfully.", region.getCode());
        return "redirect:/regions";
    }

    @PostMapping("/update")
    public String updateRegion(
            @Valid @ModelAttribute("region") Region region,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Updating region with ID {}", region.getId());

        if (result.hasErrors()) {
            return "region-form";
        }

        if (regionRepository.existsRegionByCodeAndNotId(region.getCode(), region.getId())) {
            logger.warn("Region code {} already exists for another region.", region.getCode());
            String errorMessage = messageSource.getMessage("msg.region-controller.update.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/regions/edit?id=" + region.getId();
        }

        regionRepository.save(region);
        logger.info("Region with ID {} updated successfully.", region.getId());
        return "redirect:/regions";
    }

    @PostMapping("/delete")
    public String deleteRegion(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Deleting region with ID {}", id);
        Optional<Region> regionOptional = regionRepository.findById(id);

        if (regionOptional.isEmpty()) {
            logger.warn("Region with ID {} not found.", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Region not found.");
            return "redirect:/regions";
        }

        regionRepository.deleteById(id);
        logger.info("Region with ID {} deleted successfully.", id);
        return "redirect:/regions";
    }
}