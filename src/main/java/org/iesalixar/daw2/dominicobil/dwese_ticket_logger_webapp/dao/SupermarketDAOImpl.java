package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Supermarket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class SupermarketDAOImpl implements SupermarketDAO {


    // Logger para registrar eventos importantes en el DAO
    private static final Logger logger = LoggerFactory.getLogger(SupermarketDAOImpl.class);


    private final JdbcTemplate jdbcTemplate;


    // Inyección de JdbcTemplate
    public SupermarketDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    /**
     * Lista todos los supermercados de la base de datos.
     * @return Lista de supermercados
     */
    @Override
    public List<Supermarket> listAllSupermarkets() {
        logger.info("Listing all supermarkets from the database.");
        String sql = "SELECT * FROM supermarkets";
        List<Supermarket> supermarkets = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Supermarket.class));
        logger.info("Retrieved {} supermarkets from the database.", supermarkets.size());
        return supermarkets;
    }


    /**
     * Inserta un nuevo supermercado en la base de datos.
     * @param supermarket Supermercado a insertar
     */
    @Override
    public void insertSupermarket(Supermarket supermarket) {
        logger.info("Inserting supermarket with name: {}", supermarket.getName());
        String sql = "INSERT INTO supermarkets (name) VALUES (?)";
        int rowsAffected = jdbcTemplate.update(sql, supermarket.getName());
        logger.info("Inserted supermarket. Rows affected: {}", rowsAffected);
    }


    /**
     * Actualiza un supermercado existente en la base de datos.
     * @param supermarket Supermercado a actualizar
     */
    @Override
    public void updateSupermarket(Supermarket supermarket) {
        logger.info("Updating supermarket with id: {}", supermarket.getId());
        String sql = "UPDATE supermarkets SET name = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, supermarket.getName(), supermarket.getId());
        logger.info("Updated supermarket. Rows affected: {}", rowsAffected);
    }


    /**
     * Elimina un supermercado de la base de datos.
     * @param id ID del supermercado a eliminar
     */
    @Override
    public void deleteSupermarket(int id) {
        logger.info("Deleting supermarket with id: {}", id);
        String sql = "DELETE FROM supermarkets WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        logger.info("Deleted supermarket. Rows affected: {}", rowsAffected);
    }


    /**
     * Recupera un supermercado por su ID.
     * @param id ID del supermercado a recuperar
     * @return Supermercado encontrado o null si no existe
     */
    @Override
    public Supermarket getSupermarketById(int id) {
        logger.info("Retrieving supermarket by id: {}", id);
        String sql = "SELECT * FROM supermarkets WHERE id = ?";
        try {
            Supermarket supermarket = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Supermarket.class), id);
            logger.info("Supermarket retrieved: {}", supermarket.getName());
            return supermarket;
        } catch (Exception e) {
            logger.warn("No supermarket found with id: {}", id);
            return null;
        }
    }


    /**
     * Verifica si un supermercado con el nombre especificado ya existe en la base de datos.
     * @param name el nombre del supermercado a verificar.
     * @return true si un supermercado con el nombre ya existe, false de lo contrario.
     */
    @Override
    public boolean existsSupermarketByName(String name) {
        logger.info("Checking if supermarket with name: {} exists", name);
        String sql = "SELECT COUNT(*) FROM supermarkets WHERE UPPER(name) = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name.toUpperCase());
        boolean exists = count != null && count > 0;
        logger.info("Supermarket with name: {} exists: {}", name, exists);
        return exists;
    }


    /**
     * Verifica si un supermercado con el nombre especificado ya existe en la base de datos,
     * excluyendo un supermercado con un ID específico.
     * @param name el nombre del supermercado a verificar.
     * @param id   el ID del supermercado a excluir de la verificación.
     * @return true si un supermercado con el nombre ya existe (y no es el supermercado con el ID dado),
     *         false de lo contrario.
     */
    @Override
    public boolean existsSupermarketByNameAndNotId(String name, int id) {
        logger.info("Checking if supermarket with name: {} exists excluding id: {}", name, id);
        String sql = "SELECT COUNT(*) FROM supermarkets WHERE UPPER(name) = ? AND id != ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name.toUpperCase(), id);
        boolean exists = count != null && count > 0;
        logger.info("Supermarket with name: {} exists excluding id {}: {}", name, id, exists);
        return exists;
    }
}

