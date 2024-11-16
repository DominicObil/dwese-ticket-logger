package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.controller;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.repositories.CategoryRepository;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entities.Category;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.services.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private MessageSource messageSource;

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public String listCategories(Model model) {
        logger.info("Requesting the list of all categories...");
        List<Category> listCategories = categoryRepository.findAll();
        logger.info("Loaded {} categories.", listCategories.size());
        model.addAttribute("listCategories", listCategories);
        return "category";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Displaying form to create a new category.");
        model.addAttribute("category", new Category());
        model.addAttribute("listCategories", categoryRepository.findAll());
        return "category-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        logger.info("Displaying edit form for the category with ID {}", id);
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (categoryOptional.isEmpty()) {
            logger.warn("Category with ID {} not found.", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Category not found.");
            return "redirect:/categories";
        }

        model.addAttribute("category", categoryOptional.get());
        model.addAttribute("listCategories", categoryRepository.findAll());
        return "category-form";
    }

    @PostMapping("/insert")
    public String insertCategory(
            @Valid @ModelAttribute("category") Category category,
            BindingResult result,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes, Locale locale, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("listCategories", categoryRepository.findAll());
            return "category-form";
        }

        logger.info("Inserting new category with name {}", category.getName());

        if (category.getParent() != null && category.getParent().getId() == null) {
            category.setParent(null);
        }

        if (!imageFile.isEmpty()) {
            String fileName = fileStorageService.saveFile(imageFile);
            if (fileName != null) {
                category.setImage(fileName);
            }
        }

        categoryRepository.save(category);
        logger.info("Category {} inserted successfully.", category.getName());
        return "redirect:/categories";
    }

    @PostMapping("/update")
    public String updateCategory(
            @Valid @ModelAttribute("category") Category category,
            BindingResult result,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes, Locale locale) {

        logger.info("Updating category with ID {}", category.getId());

        if (result.hasErrors()) {
            return "category-form";
        }

        if (categoryRepository.existsCategoryByNameAndNotId(category.getName(), category.getId())) {
            logger.warn("Category name {} already exists for another category.", category.getName());
            String errorMessage = messageSource.getMessage("msg.categorie-controller.update.NameExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/categories/edit?id=" + category.getId();
        }

        if (!imageFile.isEmpty()) {
            String fileName = fileStorageService.saveFile(imageFile);
            if (fileName != null) {
                category.setImage(fileName);
            }
        }

        categoryRepository.save(category);
        logger.info("Category with ID {} updated successfully.", category.getId());
        return "redirect:/categories";
    }

    @PostMapping("/delete")
    public String deleteCategory(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Deleting category with ID {}", id);
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (categoryOptional.isEmpty()) {
            logger.warn("Category with ID {} not found.", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Category not found.");
            return "redirect:/categories";
        }

        Category category = categoryOptional.get();
        categoryRepository.deleteById(id);

        if (category.getImage() != null && !category.getImage().isEmpty()) {
            fileStorageService.deleteFile(category.getImage());
        }

        logger.info("Category with ID {} deleted successfully.", id);
        return "redirect:/categories";
    }
}
