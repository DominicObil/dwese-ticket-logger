package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase `Supermarket` representa una entidad de un supermercado con un identificador y un nombre.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supermarket {

    // Identificador único del supermercado
    private Integer id;

    // Nombre del supermercado
    @NotEmpty(message = "{msg.supermarket.name.notEmpty}")
    @Size(max = 100, message = "{msg.supermarket.name.size}")
    private String name;

    /**
     * Constructor que no incluye el campo `id`.
     * Se utiliza cuando el `id` no es necesario o no está disponible.
     * @param name Nombre del supermercado.
     */
    public Supermarket(String name) {
        this.name = name;
    }
}
