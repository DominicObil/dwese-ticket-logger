package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.controller;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.repositories.ProvinceRepository;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.repositories.RegionRepository;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entities.Province;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entities.Region;
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
@RequestMapping("/provinces")
public class ProvinceController {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listProvinces(Model model) {
        logger.info("Solicitando la lista de todas las provincias...");
        List<Province> listProvinces = provinceRepository.findAll();
        logger.info("Se han cargado {} provincias.", listProvinces.size());
        model.addAttribute("listProvinces", listProvinces);
        return "province";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva provincia.");
        model.addAttribute("province", new Province());
        model.addAttribute("listRegions", regionRepository.findAll());
        return "province-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para la provincia con ID {}", id);
        Optional<Province> province = provinceRepository.findById(id);

        if (province.isEmpty()) {
            logger.warn("No se encontró la provincia con ID {}", id);
            return "redirect:/provinces";
        }

        model.addAttribute("province", province.get());
        model.addAttribute("listRegions", regionRepository.findAll());
        return "province-form";
    }

    @PostMapping("/insert")
    public String insertProvince(@Valid @ModelAttribute("province") Province province, BindingResult result,
                                 RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Insertando nueva provincia con código {}", province.getCode());

        if (result.hasErrors()) {
            logger.warn("Errores de validación encontrados.");
            return "province-form";
        }

        if (provinceRepository.existsProvinceByCode(province.getCode())) {
            logger.warn("El código de la provincia {} ya existe.", province.getCode());
            String errorMessage = messageSource.getMessage("msg.province-controller.insert.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/provinces/new";
        }

        provinceRepository.save(province);
        logger.info("Provincia {} insertada con éxito.", province.getCode());
        redirectAttributes.addFlashAttribute("successMessage", "Provincia creada con éxito.");
        return "redirect:/provinces";
    }

    @PostMapping("/update")
    public String updateProvince(@Valid @ModelAttribute("province") Province province, BindingResult result,
                                 RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando provincia con ID {}", province.getId());

        if (result.hasErrors()) {
            logger.warn("Errores de validación encontrados.");
            return "province-form";
        }

        if (provinceRepository.existsProvinceByCodeAndNotId(province.getCode(), province.getId())) {
            logger.warn("El código de la provincia {} ya existe para otra provincia.", province.getCode());
            String errorMessage = messageSource.getMessage("msg.province-controller.update.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/provinces/edit?id=" + province.getId();
        }

        provinceRepository.save(province);
        logger.info("Provincia con ID {} actualizada con éxito.", province.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Provincia actualizada con éxito.");
        return "redirect:/provinces";
    }

    @PostMapping("/delete")
    public String deleteProvince(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando provincia con ID {}", id);

        if (!provinceRepository.existsById(id)) {
            logger.warn("No se encontró la provincia con ID {}", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Provincia no encontrada.");
            return "redirect:/provinces";
        }

        provinceRepository.deleteById(id);
        logger.info("Provincia con ID {} eliminada con éxito.", id);
        redirectAttributes.addFlashAttribute("successMessage", "Provincia eliminada con éxito.");
        return "redirect:/provinces";
    }
}
