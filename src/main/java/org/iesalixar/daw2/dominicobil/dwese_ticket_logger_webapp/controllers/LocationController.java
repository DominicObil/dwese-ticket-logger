package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.dao.LocationDAO;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.dao.ProvinceDAO;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.dao.SupermarketDAO;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Location;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Province;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Supermarket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/**
 * Controlador que maneja las operaciones CRUD para la entidad `Location`.
 * Utiliza `LocationDAO` para interactuar con la base de datos.
 */
@Controller
@RequestMapping("/locations")
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);


    // DAO para gestionar las operaciones de las Localizacion en la base de datos
    @Autowired
    private LocationDAO locationDAO;

    @Autowired
    private SupermarketDAO supermarketDAO;

    // DAO para gestionar las operaciones de las provincias en la base de datos
    @Autowired
    private ProvinceDAO provinceDAO;


    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todas las ubicaciones y las pasa como atributo al modelo para que sean
     * accesibles en la vista `location.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de ubicaciones.
     */
    @GetMapping
    public String listLocations(Model model) {
        logger.info("Solicitando la lista de todas las ubicaciones...");
        List<Location> listLocations = null;
        try {
            listLocations = locationDAO.listAllLocations();
            logger.info("Se han cargado {} ubicaciones.", listLocations.size());
        } catch (SQLException e) {
            logger.error("Error al listar las ubicaciones: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al listar las ubicaciones.");
        }
        model.addAttribute("listLocations", listLocations);
        return "location";  // Vista para listar locations
    }


    /**
     * Muestra el formulario para crear una nueva ubicación.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva ubicación.");
        model.addAttribute("location", new Location());


        // Cargar provincias y supermercados para seleccionar
        List<Province> listProvinces = null;
        List<Supermarket> listSupermarkets = null;
        try {
            listProvinces = provinceDAO.listAllProvinces();
            listSupermarkets = supermarketDAO.listAllSupermarkets();
            logger.info("Se han cargado {} provincias y {} supermercados.", listProvinces.size(), listSupermarkets.size());
        } catch (SQLException e) {
            logger.error("Error al listar provincias o supermercados: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al listar provincias o supermercados.");
        }
        model.addAttribute("listProvinces", listProvinces);
        model.addAttribute("listSupermarkets", listSupermarkets);


        return "location-form";  // Vista para el formulario
    }


    /**
     * Muestra el formulario para editar una ubicación existente.
     *
     * @param id    ID de la ubicación a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        logger.info("Mostrando formulario de edición para la ubicación con ID {}", id);
        Location location = null;
        try {
            location = locationDAO.getLocationById(id);
            if (location == null) {
                logger.warn("No se encontró la ubicación con ID {}", id);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener la ubicación con ID {}: {}", id, e.getMessage());
            model.addAttribute("errorMessage", "Error al obtener la ubicación.");
        }


        // Cargar provincias y supermercados para seleccionar
        List<Province> listProvinces = null;
        List<Supermarket> listSupermarkets = null;
        try {
            listProvinces = provinceDAO.listAllProvinces();
            listSupermarkets = supermarketDAO.listAllSupermarkets();
            logger.info("Se han cargado {} provincias y {} supermercados.", listProvinces.size(), listSupermarkets.size());
        } catch (SQLException e) {
            logger.error("Error al listar provincias o supermercados: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al listar provincias o supermercados.");
        }


        model.addAttribute("location", location);
        model.addAttribute("listProvinces", listProvinces);
        model.addAttribute("listSupermarkets", listSupermarkets);
        return "location-form";
    }


    /**
     * Inserta una nueva ubicación en la base de datos.
     *
     * @param location           Objeto que contiene los datos del formulario.
     * @param result             Resultado de la validación.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @param locale             Locale para los mensajes de error.
     * @return Redirección a la lista de ubicaciones.
     */
    @PostMapping("/insert")
    public String insertLocation(@Valid @ModelAttribute("location") Location location, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Insertando nueva ubicación en {}", location.getAddress());


        if (result.hasErrors()) {
            return "location-form";  // Retorna al formulario si hay errores
        }


        try {
            locationDAO.insertLocation(location);
            logger.info("Ubicación en {} insertada con éxito.", location.getAddress());
            redirectAttributes.addFlashAttribute("successMessage", "Ubicación creada con éxito.");  // Mensaje de éxito
        } catch (SQLException e) {
            logger.error("Error al insertar la ubicación: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al insertar la ubicación.");
        }
        return "redirect:/locations";  // Redirigir a la lista de locations
    }



    /**
     * Actualiza una ubicación existente en la base de datos.
     *
     * @param location           Objeto que contiene los datos del formulario.
     * @param result             Resultado de la validación.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @param locale             Locale para los mensajes de error.
     * @return Redirección a la lista de ubicaciones.
     */
    @PostMapping("/update")
    public String updateLocation(@Valid @ModelAttribute("location") Location location, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando ubicación con ID {}", location.getId());


        if (result.hasErrors()) {
            return "location-form";
        }


        try {
            locationDAO.updateLocation(location);
            logger.info("Ubicación con ID {} actualizada con éxito.", location.getId());
        } catch (SQLException e) {
            logger.error("Error al actualizar la ubicación con ID {}: {}", location.getId(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar la ubicación.");
        }
        return "redirect:/locations"; // Redirigir a la lista de locations
    }




    /**
     * Elimina una ubicación de la base de datos.
     *
     * @param id                 ID de la ubicación a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de ubicaciones.
     */
    @PostMapping("/delete")
    public String deleteLocation(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando ubicación con ID {}", id);


        try {
            locationDAO.deleteLocation(id);
            logger.info("Ubicación con ID {} eliminada con éxito.", id);
        } catch (SQLException e) {
            logger.error("Error al eliminar la ubicación con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la ubicación.");
        }
        return "redirect:/locations"; // Redirigir a la lista de locations
    }



}



