package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase `Location` representa una entidad que modela la ubicación de un supermercado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    // Identificador único de la ubicación
    @NotNull
    private Integer id;

    // Dirección de la ubicación
    @NotEmpty(message = "{msg.location.address.notEmpty}")
    @Size(max = 255, message = "{msg.location.address.size}")
    private String address;

    // Ciudad de la ubicación
    @NotEmpty(message = "{msg.location.city.notEmpty}")
    @Size(max = 100, message = "{msg.location.city.size}")
    private String city;

    // Supermercado asociado a la ubicación
    @NotNull(message = "{msg.location.supermarket.notNull}")
    private Supermarket supermarket;

    // Provincia asociada a la ubicación
    @NotNull(message = "{msg.location.province.notNull}")
    private Province province;

    /**
     * Constructor que no incluye el campo `id`.
     * @param address Dirección de la ubicación.
     * @param city Ciudad de la ubicación.
     * @param province Provincia asociada.
     */
    public Location(String address, String city, Province province) {
        this.address = address;
        this.city = city;
        this.province = province;
    }
}
