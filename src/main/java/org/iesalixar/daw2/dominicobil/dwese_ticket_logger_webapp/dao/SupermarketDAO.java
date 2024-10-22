package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Supermarket;

import java.sql.SQLException;
import java.util.List;

public interface SupermarketDAO {
    List<Supermarket> listAllSupermarkets() throws SQLException;
    Supermarket getSupermarketById(int id) throws SQLException;
    void insertSupermarket(Supermarket supermarket) throws SQLException;
    void updateSupermarket(Supermarket supermarket) throws SQLException;
    void deleteSupermarket(int id) throws SQLException;
    boolean existsSupermarketByName(String name) throws SQLException;
    boolean existsSupermarketByNameAndNotId(String name, int id) throws SQLException;
}
