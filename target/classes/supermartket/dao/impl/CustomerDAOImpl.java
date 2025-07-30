package supermartket.dao.impl;

import java.util.List;
import supermartket.dao.CustomerDAO;
import supermartket.entity.Customer;
import supermartket.util.XJdbc;
import supermartket.util.XQuery;

public class CustomerDAOImpl implements CustomerDAO {
    private final String insertSql = "INSERT INTO Customer (CustomerID, FullName, Phone, Email) VALUES (?, ?, ?, ?)";
    private final String updateSql = "UPDATE Customer SET FullName = ?, Phone = ?, Email = ? WHERE CustomerID = ?";
    private final String deleteSql = "DELETE FROM Customer WHERE CustomerID = ?";
    private final String findByIdSql = "SELECT * FROM Customer WHERE CustomerID = ?";
    private final String findAllSql = "SELECT * FROM Customer";
    private final String findByNameSql = "SELECT * FROM Customer WHERE FullName LIKE ?";

    @Override
    public Customer create(Customer entity) {
        Object[] values = {
            entity.getCustomerID(),
            entity.getFullName(),
            entity.getPhone(),
            entity.getEmail(),
        };
        XJdbc.executeUpdate(insertSql, values);
        return entity;
    }

    @Override
    public void update(Customer entity) {
        Object[] values = {
            entity.getFullName(),
            entity.getPhone(),
            entity.getEmail(),
            entity.getCustomerID()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public Customer findById(String id) {
        return XQuery.getSingleBean(Customer.class, findByIdSql, id);
    }

    @Override
    public List<Customer> findAll() {
        return XQuery.getBeanList(Customer.class, findAllSql);
    }
    
    @Override
    public List<Customer> findByName(String nameKeyword) {
        return XQuery.getBeanList(Customer.class, findByNameSql, "%" + nameKeyword + "%");
    }
}

