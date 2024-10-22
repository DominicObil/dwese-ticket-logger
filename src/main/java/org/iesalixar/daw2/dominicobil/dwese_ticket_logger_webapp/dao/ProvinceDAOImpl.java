
package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Province;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Region;
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
public class ProvinceDAOImpl implements ProvinceDAO {

    // Logger para registrar eventos importantes en el DAO
    private static final Logger logger = LoggerFactory.getLogger(ProvinceDAOImpl.class);

    private final JdbcTemplate jdbcTemplate;

    // Inyección de JdbcTemplate
    public ProvinceDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Lista todas las provincias de la base de datos.
     * @return Lista de provincias
     */
    @Override
    public List<Province> listAllProvinces() {
        logger.info("Listing all provinces from the database.");
        String sql = "SELECT p.*, r.id AS region_id, r.code AS region_code, r.name AS region_name " +
                "FROM provinces p JOIN regions r ON p.id_region = r.id";
        List<Province> provinces = jdbcTemplate.query(sql, new ProvinceRowMapper());
        logger.info("Retrieved {} provinces from the database.", provinces.size());
        return provinces;
    }

    /**
     * Inserta una nueva provincia en la base de datos.
     * @param province Provincia a insertar
     */
    @Override
    public void insertProvince(Province province) {
        logger.info("Inserting province with code: {} and name: {}", province.getCode(), province.getName());
        String sql = "INSERT INTO provinces (code, name, id_region) VALUES (?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, province.getCode(), province.getName(), province.getRegion().getId());
        logger.info("Inserted province. Rows affected: {}", rowsAffected);
    }

    /**
     * Actualiza una provincia existente en la base de datos.
     * @param province Provincia a actualizar
     */
    @Override
    public void updateProvince(Province province) {
        logger.info("Updating province with id: {}", province.getId());
        String sql = "UPDATE provinces SET code = ?, name = ?, id_region = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, province.getCode(), province.getName(), province.getRegion().getId(), province.getId());
        logger.info("Updated province. Rows affected: {}", rowsAffected);
    }

    /**
     * Elimina una provincia de la base de datos.
     * @param id ID de la provincia a eliminar
     */
    @Override
    public void deleteProvince(int id) {
        logger.info("Deleting province with id: {}", id);
        String sql = "DELETE FROM provinces WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        logger.info("Deleted province. Rows affected: {}", rowsAffected);
    }

    /**
     * Obtiene una provincia por su ID.
     * @param id ID de la provincia
     * @return Provincia correspondiente al ID
     */
    @Override
    public Province getProvinceById(int id) {
        logger.info("Retrieving province by id: {}", id);
        String sql = "SELECT p.*, r.id AS region_id, r.code AS region_code, r.name AS region_name " +
                "FROM provinces p JOIN regions r ON p.id_region = r.id WHERE p.id = ?";
        try {
            Province province = jdbcTemplate.queryForObject(sql, new ProvinceRowMapper(), id);
            logger.info("Province retrieved: {} - {}", province.getCode(), province.getName());
            return province;
        } catch (Exception e) {
            logger.warn("No province found with id: {}", id);
            return null;
        }
    }

    /**
     * Verifica si una provincia con el código especificado ya existe en la base de datos.
     * @param code el código de la provincia a verificar.
     * @return true si una provincia con el código ya existe, false de lo contrario.
     */
    @Override
    public boolean existsProvinceByCode(String code) {
        logger.info("Checking if province with code: {} exists", code);
        String sql = "SELECT COUNT(*) FROM provinces WHERE UPPER(code) = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, code.toUpperCase());
        boolean exists = count != null && count > 0;
        logger.info("Province with code: {} exists: {}", code, exists);
        return exists;
    }

    /**
     * Verifica si una provincia con el código especificado ya existe en la base de datos,
     * excluyendo una provincia con un ID específico.
     * @param code el código de la provincia a verificar.
     * @param id   el ID de la provincia a excluir de la verificación.
     * @return true si una provincia con el código ya existe (y no es la provincia con el ID dado),
     *         false de lo contrario.
     */
    @Override
    public boolean existsProvinceByCodeAndNotId(String code, int id) {
        logger.info("Checking if province with code: {} exists excluding id: {}", code, id);
        String sql = "SELECT COUNT(*) FROM provinces WHERE UPPER(code) = ? AND id != ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, code.toUpperCase(), id);
        boolean exists = count != null && count > 0;
        logger.info("Province with code: {} exists excluding id {}: {}", code, id, exists);
        return exists;
    }

    /**
     * Clase interna que implementa RowMapper para mapear los resultados de la consulta SQL a la entidad Province.
     */
    private static class ProvinceRowMapper implements RowMapper<Province> {
        @Override
        public Province mapRow(ResultSet rs, int rowNum) throws SQLException {
            Province province = new Province();
            province.setId(rs.getInt("id"));
            province.setCode(rs.getString("code"));
            province.setName(rs.getString("name"));

            Region region = new Region();
            region.setId(rs.getInt("region_id"));
            region.setCode(rs.getString("region_code"));
            region.setName(rs.getString("region_name"));
            province.setRegion(region);

            return province;
        }
    }
}





