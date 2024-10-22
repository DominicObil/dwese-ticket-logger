package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.dao.SupermarketDAO;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Supermarket;
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


import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
/**
 * Controlador que maneja las operaciones CRUD para la entidad `Supermarket`.
 * Utiliza `SupermarketDAO` para interactuar con la base de datos.
 */
@Controller
@RequestMapping("/supermarkets")
public class SupermarketController {


    private static final Logger logger = LoggerFactory.getLogger(SupermarketController.class);

    // DAO para gestionar las operaciones de los supermercados en la base de datos
    @Autowired
    private SupermarketDAO supermarketDAO;


    /**
     * Lista todos los supermercados y los pasa como atributo al modelo para que sean
     * accesibles en la vista `supermarket.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de supermercados.
     */
    @GetMapping
    public String listSupermarkets(Model model) {
        logger.info("Solicitando la lista de todos los supermercados...");
        List<Supermarket> listSupermarkets = null;
        try {
            listSupermarkets = supermarketDAO.listAllSupermarkets();
            logger.info("Se han cargado {} supermercados.", listSupermarkets.size());
        } catch (SQLException e) {
            logger.error("Error al listar los supermercados: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al listar los supermercados.");
        }
        model.addAttribute("listSupermarkets", listSupermarkets);
        return "supermarket"; // Nombre de la plantilla Thymeleaf a renderizar
    }


    /**
     * Muestra el formulario para crear un nuevo supermercado.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo supermercado.");
        model.addAttribute("supermarket", new Supermarket()); // Crear un nuevo objeto Supermarket
        return "supermarket-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }


    /**
     * Muestra el formulario para editar un supermercado existente.
     *
     * @param id    ID del supermercado a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        logger.info("Mostrando formulario de edición para el supermercado con ID {}", id);
        Supermarket supermarket = null;
        try {
            supermarket = supermarketDAO.getSupermarketById(id);
            if (supermarket == null) {
                logger.warn("No se encontró el supermercado con ID {}", id);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener el supermercado con ID {}: {}", id, e.getMessage());
            model.addAttribute("errorMessage", "Error al obtener el supermercado.");
        }
        model.addAttribute("supermarket", supermarket);
        return "supermarket-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    @Autowired
    private MessageSource messageSource;
    /**
     * Inserta un nuevo supermercado en la base de datos.
     *
     * @param supermarket          Objeto que contiene los datos del formulario.
     * @param redirectAttributes   Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de supermercados.
     */
    @PostMapping("/insert")
    public String insertSupermarket(@Valid @ModelAttribute("supermarket") Supermarket supermarket, BindingResult result, RedirectAttributes redirectAttributes, Locale locale, Model model) {
        logger.info("Insertando nuevo supermercado con nombre {}", supermarket.getName());
        try {
            if (result.hasErrors()) {
                return "supermarket-form";  // Devuelve el formulario para mostrar los errores de validación
            }
            if (supermarketDAO.existsSupermarketByName(supermarket.getName())) {
                logger.warn("El nombre del supermercado {} ya existe.", supermarket.getName());
                String errorMessage = messageSource.getMessage("msg.supermarket-controller.insert.nameExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/supermarkets/new";
            }
            supermarketDAO.insertSupermarket(supermarket);
            logger.info("Supermercado {} insertado con éxito.", supermarket.getName());
        } catch (SQLException e) {
            logger.error("Error al insertar el supermercado {}: {}", supermarket.getName(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.supermarket-controller.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/supermarkets"; // Redirigir a la lista de supermercados
    }


    /**
     * Actualiza un supermercado existente en la base de datos.
     *
     * @param supermarket          Objeto que contiene los datos del formulario.
     * @param redirectAttributes    Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de supermercados.
     */
    @PostMapping("/update")
    public String updateSupermarket(@Valid @ModelAttribute("supermarket") Supermarket supermarket, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando supermercado con ID {}", supermarket.getId());
        try {
            if (result.hasErrors()) {
                return "supermarket-form";  // Devuelve el formulario para mostrar los errores de validación
            }
            if (supermarketDAO.existsSupermarketByNameAndNotId(supermarket.getName(), supermarket.getId())) {
                logger.warn("El nombre del supermercado {} ya existe para otro supermercado.", supermarket.getName());
                String errorMessage = messageSource.getMessage("msg.supermarket-controller.update.nameExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/supermarkets/edit?id=" + supermarket.getId();
            }
            supermarketDAO.updateSupermarket(supermarket);
            logger.info("Supermercado con ID {} actualizado con éxito.", supermarket.getId());
        } catch (SQLException e) {
            logger.error("Error al actualizar el supermercado con ID {}: {}", supermarket.getId(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.supermarket-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/supermarkets"; // Redirigir a la lista de supermercados
    }


    /**
     * Elimina un supermercado de la base de datos.
     *
     * @param id                 ID del supermercado a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de supermercados.
     */
    @PostMapping("/delete")
    public String deleteSupermarket(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando supermercado con ID {}", id);
        try {
            supermarketDAO.deleteSupermarket(id);
            logger.info("Supermercado con ID {} eliminado con éxito.", id);
        } catch (SQLException e) {
            logger.error("No se puede eliminar el supermercado con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el supermercado.");
        } catch (DataIntegrityViolationException e) {
            logger.error("Error al eliminar el supermercado con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el supermercado.");
        }
        return "redirect:/supermarkets"; // Redirigir a la lista de supermercados
    }
}

