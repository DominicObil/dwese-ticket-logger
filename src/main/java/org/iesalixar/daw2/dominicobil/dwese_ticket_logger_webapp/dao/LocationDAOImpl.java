package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Location;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Supermarket;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Province;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
public class LocationDAOImpl implements LocationDAO {

    // Logger para registrar eventos importantes en el DAO
    private static final Logger logger = LoggerFactory.getLogger(LocationDAOImpl.class);
    private final JdbcTemplate jdbcTemplate;

    // Inyección de JdbcTemplate
    public LocationDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Lista todas las localizaciones de la base de datos.
     * @return Lista de locaclizaciones
     */
    @Override
    public List<Location> listAllLocations() {
        logger.info("Listing all locations from the database.");
        String sql = "SELECT l.*, p.id AS province_id, p.code AS province_code, p.name AS province_name, " +
                "s.id AS supermarket_id, s.name AS supermarket_name " +
                "FROM locations l " +
                "JOIN provinces p ON l.province_id = p.id " +
                "JOIN supermarkets s ON l.supermarket_id = s.id";
        List<Location> locations = jdbcTemplate.query(sql, new LocationRowMapper());
        logger.info("Retrieved {} locations from the database.", locations.size());
        return locations;
    }

    /**
     * Inserta una nueva localizacion en la base de datos.
     * @param location Localizacion a insertar
     */


    @Override
    public void insertLocation(Location location) {
        logger.info("Inserting location with address: {} and city: {}", location.getAddress(), location.getCity());
        String sql = "INSERT INTO locations (address, city, province_id, supermarket_id) VALUES (?, ?, ?, ?)"; //
        int rowsAffected = jdbcTemplate.update(sql, location.getAddress(), location.getCity(), location.getProvince().getId(), location.getSupermarket().getId());
        logger.info("Inserted location. Rows affected: {}", rowsAffected);
    }

    /**
     * Actualiza una provincia existente en la base de datos.
     * @param location Provincia a actualizar
     */
    @Override
    public void updateLocation(Location location) {
        logger.info("Updating location with id: {}", location.getId());
        String sql = "UPDATE locations SET address = ?, city = ?, province_id = ?, supermarket_id = ? WHERE id = ?"; //
        int rowsAffected = jdbcTemplate.update(sql, location.getAddress(), location.getCity(), location.getProvince().getId(), location.getSupermarket().getId(), location.getId());
        logger.info("Updated location. Rows affected: {}", rowsAffected);
    }

    /**
     * Elimina una localizacion de la base de datos.
     * @param id ID de la localizacion a eliminar
     */
    @Override
    public void deleteLocation(int id) {
        logger.info("Deleting location with id: {}", id);
        String sql = "DELETE FROM locations WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        logger.info("Deleted location. Rows affected: {}", rowsAffected);
    }

    /**
     * Obtiene una Localizacion por su ID.
     * @param id ID de la localizacion
     * @return Localizacion correspondiente al ID
     */
    @Override
    public Location getLocationById(int id) {
        logger.info("Retrieving location by id: {}", id);
        String sql = "SELECT l.*, p.id AS province_id, p.code AS province_code, p.name AS province_name, " +
                "s.id AS supermarket_id, s.name AS supermarket_name " +
                "FROM locations l " +
                "JOIN provinces p ON l.province_id = p.id " +
                "JOIN supermarkets s ON l.supermarket_id = s.id " +
                "WHERE l.id = ?";
        try {
            Location location = jdbcTemplate.queryForObject(sql, new LocationRowMapper(), id);
            logger.info("Location retrieved: {} - {}", location.getAddress(), location.getCity());
            return location;
        } catch (Exception e) {
            logger.warn("No location found with id: {}", id);
            return null;
        }
    }

    /**
     * Verifica si una Localizacion con la direccion especificado ya existe en la base de datos.
     * @param address la direccion de la localizacion a verificar.
     * @return true si una localizacion con la direccion ya existe, false de lo contrario.
     */
    @Override
    public boolean existsLocationByCode(String address) {
        logger.info("Checking if location with address: {} exists", address);
        String sql = "SELECT COUNT(*) FROM locations WHERE UPPER(address) = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, address.toUpperCase());
        boolean exists = count != null && count > 0;
        logger.info("Location with address: {} exists: {}", address, exists);
        return exists;
    }

    /**
     * Verifica si una localizacion con la direccion especificado ya existe en la base de datos,
     * excluyendo una localizacion con un ID específico.
     * @param address la direccion de la localizacion a verificar.
     * @param id   el ID de la localizacion a excluir de la verificación.
     * @return true si una localizacion con la direccion ya existe (y no es la Localizacion con el ID dado),
     *         false de lo contrario.
     */
    @Override
    public boolean existsLocationByCodeAndNotId(String address, int id) {
        logger.info("Checking if location with address: {} exists excluding id: {}", address, id);
        String sql = "SELECT COUNT(*) FROM locations WHERE UPPER(address) = ? AND id != ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, address.toUpperCase(), id);
        boolean exists = count != null && count > 0;
        logger.info("Location with address: {} exists excluding id {}: {}", address, id, exists);
        return exists;
    }
    /**
     * Clase interna que implementa RowMapper para mapear los resultados de la consulta SQL a la entidad Location.
     */

    private static class LocationRowMapper implements RowMapper<Location> {
        @Override
        public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
            Location location = new Location();
            location.setId(rs.getInt("id"));
            location.setAddress(rs.getString("address"));
            location.setCity(rs.getString("city"));


            // Mapeo de Province
            Province province = new Province();
            province.setId(rs.getInt("province_id"));
            province.setCode(rs.getString("province_code"));
            province.setName(rs.getString("province_name"));
            location.setProvince(province);


            // Mapeo de Supermercado
            Supermarket supermarket = new Supermarket();
            supermarket.setId(rs.getInt("supermarket_id"));
            supermarket.setName(rs.getString("supermarket_name"));
            location.setSupermarket(supermarket);


            return location;
        }
    }
}
