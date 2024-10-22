package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Location;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz LocationDAO que define las operaciones CRUD para la entidad Location.
 */
public interface LocationDAO {

    /**
     * Lista todas las ubicaciones de la base de datos.
     * @return Lista de ubicaciones
     */
    List<Location> listAllLocations() throws SQLException;

    /**
     * Inserta una nueva ubicación en la base de datos.
     * @param location Ubicación a insertar
     */
    void insertLocation(Location location) throws SQLException;

    /**
     * Actualiza una ubicación existente en la base de datos.
     * @param location Ubicación a actualizar
     */
    void updateLocation(Location location) throws SQLException;

    /**
     * Elimina una ubicación de la base de datos.
     * @param id ID de la ubicación a eliminar
     */
    void deleteLocation(int id) throws SQLException;

    /**
     * Obtiene una ubicación por su ID.
     * @param id ID de la ubicación
     * @return Ubicación correspondiente al ID
     */
    Location getLocationById(int id) throws SQLException;


    boolean existsLocationByCode(String address) throws SQLException;
    boolean existsLocationByCodeAndNotId(String address, int id) throws SQLException;
}

