package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.repositories;

import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entities.Supermarket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SupermarketRepository extends JpaRepository<Supermarket, Long> {

    // JpaRepository ya proporciona los métodos básicos como save() y findById().

    @Override
    Optional<Supermarket> findById(Long id);

    @Query("SELECT COUNT(s) > 0 FROM Supermarket s WHERE s.name = :name AND s.id != :id")
    boolean existsSupermarketByNameAndNotId(@Param("name") String name, @Param("id") Long id);

    boolean existsSupermarketByName(String name);
}
