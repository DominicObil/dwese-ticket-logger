package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Province;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz para las operaciones CRUD de la entidad `Province`.
 */
public interface ProvinceDAO {

    /**
     * Lista todas las provincias de la base de datos.
     * @return Lista de provincias.
     */
    List<Province> listAllProvinces() throws SQLException;

    /**
     * Inserta una nueva provincia en la base de datos.
     * @param province Provincia a insertar.
     */
    void insertProvince(Province province) throws SQLException;

    /**
     * Actualiza una provincia existente en la base de datos.
     * @param province Provincia a actualizar.
     */
    void updateProvince(Province province) throws SQLException;

    /**
     * Elimina una provincia de la base de datos.
     * @param id ID de la provincia a eliminar.
     */
    void deleteProvince(int id) throws SQLException;

    /**
     * Obtiene una provincia por su ID.
     * @param id ID de la provincia.
     * @return Provincia correspondiente al ID.
     */
    Province getProvinceById(int id) throws SQLException;

    /**
     * Verifica si una provincia con el código especificado ya existe en la base de datos.
     * @param code el código de la provincia a verificar.
     * @return true si una provincia con el código ya existe, false de lo contrario.
     */
    boolean existsProvinceByCode(String code) throws SQLException;

    /**
     * Verifica si una provincia con el código especificado ya existe en la base de datos,
     * excluyendo una provincia con un ID específico.
     * @param code el código de la provincia a verificar.
     * @param id el ID de la provincia a excluir de la verificación.
     * @return true si una provincia con el código ya existe (y no es la provincia con el ID dado),
     *         false de lo contrario.
     */
    boolean existsProvinceByCodeAndNotId(String code, int id) throws SQLException;
}
