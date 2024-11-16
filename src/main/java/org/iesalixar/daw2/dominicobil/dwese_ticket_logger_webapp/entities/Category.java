package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entities;

import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * La clase `Category` representa una entidad que modela una categoría dentro de la base de datos.
 * Contiene cuatro campos: `id`, `name`, `image` y `parent`, donde `id` es el identificador único de la categoría,
 * `name` es el nombre de la categoría, `image` es una URL o ruta de la imagen asociada, y `parent` es la categoría principal
 * en una relación jerárquica de categorías (opcional).
 *
 * Las anotaciones de Lombok ayudan a reducir el código repetitivo al generar automáticamente
 * métodos comunes como getters, setters, constructores, y otros métodos estándar de los objetos.
 */
@Entity // Marca esta clase como una entidad gestionada por JPA.
@Table(name = "categories") // Especifica el nombre de la tabla asociada a esta entidad.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    // Campo que almacena el identificador único de la categoría.
    // Es una clave primaria autogenerada por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campo que almacena el nombre de la categoría, que no puede estar vacío y tiene una longitud máxima de 255 caracteres.
    @NotEmpty(message = "{msg.category.name.notEmpty}")
    @Size(max = 255, message = "{msg.category.name.size}")
    @Column(name = "name", nullable = false, length = 255) // Define la columna correspondiente en la tabla.
    private String name;

    // Campo que almacena la URL o ruta de la imagen asociada a la categoría.
    // Puede ser nulo y tiene una longitud máxima de 500 caracteres.
    @Size(max = 500, message = "{msg.category.image.size}")
    @Column(name = "image", nullable = true, length = 500) // Define la columna correspondiente en la tabla.
    private String image;

    // Relación muchos a uno con la misma entidad `Category`, representando la categoría principal (padre).
    // Una categoría puede tener una categoría principal (padre).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true)
    private Category parent;

    // Relación uno a muchos con la misma entidad `Category`, representando las categorías hijas.
    // Una categoría puede tener muchas categorías hijas.
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Mapeo inverso de la relación.
    private List<Category> categories; // Lista de categorías hijas.

    /**
     * Este es un constructor personalizado que permite crear instancias de `Category` especificando solo el nombre,
     * la imagen y la categoría principal (padre), sin necesidad de conocer el `id` (que es autogenerado).
     * @param name Nombre de la categoría.
     * @param image URL o ruta de la imagen asociada a la categoría.
     * @param parent Categoría principal (padre) de la categoría.
     */
    public Category(String name, String image, Category parent) {
        this.name = name;
        this.image = image;
        this.parent = parent;
    }
}
