package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.repositories;

import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // findAll() ya está incluido en JpaRepository
    // Por lo tanto, no es necesario redefinirlo.

    // save() ya está incluido en JpaRepository
    // No necesitas declarar dos métodos save() redundantes.

    // deleteById() también está incluido en JpaRepository.

    // Métodos personalizados
    @Override
    Optional<Category> findById(Long id); // Usar Optional para manejar el resultado.

    // Método con lógica especial: comprobar existencia por nombre y excluyendo un ID específico.
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.name = :name AND c.id != :id")
    boolean existsCategoryByNameAndNotId(@Param("name") String name, @Param("id") Long id);

    // Método para comprobar existencia solo por nombre.
    boolean existsCategoryByName(String name);
}
