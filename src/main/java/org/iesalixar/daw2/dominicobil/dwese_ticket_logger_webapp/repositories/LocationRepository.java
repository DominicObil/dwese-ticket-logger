package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.repositories;

import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends JpaRepository<Location, Long> {

    boolean existsLocationByAddress(String address);

    @Query("SELECT COUNT(l) > 0 FROM Location l WHERE l.address = :address AND l.id != :id")
    boolean existsLocationByAddressndNotId(@Param("address") String code, @Param("id") Long id);

}
