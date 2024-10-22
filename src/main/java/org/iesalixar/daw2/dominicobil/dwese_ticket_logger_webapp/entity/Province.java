package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase `Province` representa una entidad que modela una provincia dentro de la base de datos.
 * Contiene cuatro campos: `id`, `code`, `name` y `region`, donde `id` es el identificador único de la provincia,
 * `code` es un código asociado a la provincia, `name` es el nombre de la provincia, y `region` es la región asociada.
 *
 * Las anotaciones de Lombok ayudan a reducir el código repetitivo al generar automáticamente
 * métodos comunes como getters, setters, constructores, y otros métodos estándar de los objetos.
 */
@Data  // Genera automáticamente getters, setters, equals, hashCode, toString y canEqual.
@NoArgsConstructor  // Genera un constructor vacío, útil para frameworks como Hibernate.
@AllArgsConstructor  // Genera un constructor que acepta todos los campos como parámetros.

public class Province {

    // Campo que almacena el identificador único de la provincia.
    private Integer id;

    // Campo que almacena el código de la provincia, normalmente una cadena corta que identifica la provincia.
    @NotEmpty(message = "{msg.province.code.notEmpty}")
    @Size(max = 3, message = "{msg.province.code.size}")
    private String code;

    // Campo que almacena el nombre de la provincia.
    @NotEmpty(message = "{msg.province.name.notEmpty}")
    @Size(max = 100, message = "{msg.province.name.size}")
    private String name;

    // Campo que almacena la región asociada (clave foránea).
    @NotNull(message = "{msg.province.region.notNull}")
    private Region region;

    /**
     * Constructor que no incluye el campo `id`.
     * Se utiliza para crear instancias de `Province` cuando no es necesario o no se conoce el `id` de la provincia.
     * @param code Código de la provincia.
     * @param name Nombre de la provincia.
     * @param region Región asociada a la provincia.
     */
    public Province(String code, String name, Region region) {
        this.code = code;
        this.name = name;
        this.region = region;
    }
}
