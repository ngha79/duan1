package supermartket.dao.impl;

import java.util.List;
import supermartket.dao.SupplierDAO;
import supermartket.entity.Supplier;
import supermartket.util.XJdbc;
import supermartket.util.XQuery;

public class SupplierDAOImpl implements SupplierDAO {

    private final String insertSql = "INSERT INTO Supplier (SupplierID, SupplierName, Address, Phone, Email, Status) VALUES (?, ?, ?, ?, ?, ?)";
    private final String updateSql = "UPDATE Supplier SET SupplierName = ?, Address = ?, Phone = ?, Email = ?, Status = ? WHERE SupplierID = ?";
    private final String deleteSql = "DELETE FROM Supplier WHERE SupplierID = ?";
    private final String findByIdSql = "SELECT * FROM Supplier WHERE SupplierID = ?";
    private final String findAllSql = "SELECT * FROM Supplier";

    @Override
    public Supplier create(Supplier entity) {
        Object[] values = {
            entity.getSupplierID(),
            entity.getSupplierName(),
            entity.getAddress(),
            entity.getPhone(),
            entity.getEmail(),
            entity.getStatus()
        };
        XJdbc.executeUpdate(insertSql, values);
        return entity;
    }

    @Override
    public void update(Supplier entity) {
        Object[] values = {
            entity.getSupplierName(),
            entity.getAddress(),
            entity.getPhone(),
            entity.getEmail(),
            entity.getStatus(),
            entity.getSupplierID()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public Supplier findById(String id) {
        return XQuery.getSingleBean(Supplier.class, findByIdSql, id);
    }

    @Override
    public List<Supplier> findAll() {
        return XQuery.getBeanList(Supplier.class, findAllSql);
    }
}

