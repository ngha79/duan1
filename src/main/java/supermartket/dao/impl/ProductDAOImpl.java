package supermartket.dao.impl;

import java.util.List;
import supermartket.dao.ProductDAO;
import supermartket.entity.Product;
import supermartket.util.XJdbc;
import supermartket.util.XQuery;

public class ProductDAOImpl implements ProductDAO {
    private final String insertSql = "INSERT INTO Product (ProductID, ProductName, CategoryID, SupplierID, Quantity, Price, Status, Unit) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String updateSql = "UPDATE Product SET ProductName = ?, CategoryID = ?, SupplierID = ?, Quantity = ?, Price = ?, Status = ?, Unit = ? WHERE ProductID = ?";
    private final String deleteSql = "DELETE FROM Product WHERE ProductID = ?";
    private final String findByIdSql = "SELECT * FROM Product WHERE ProductID = ?";
    private final String findAllSql = "SELECT * FROM Product";
    private final String findByNameSql = "SELECT * FROM Product WHERE ProductName LIKE ?";

    @Override
    public Product create(Product entity) {
        Object[] values = {
            entity.getProductID(),
            entity.getProductName(),
            entity.getCategoryID(),
            entity.getSupplierID(),
            entity.getQuantity(),
            entity.getPrice(),
            entity.getStatus(),
            entity.getUnit()
        };
        XJdbc.executeUpdate(insertSql, values);
        return entity;
    }

    @Override
    public void update(Product entity) {
        Object[] values = {
            entity.getProductName(),
            entity.getCategoryID(),
            entity.getSupplierID(),
            entity.getQuantity(),
            entity.getPrice(),
            entity.getStatus(),
            entity.getUnit(),
            entity.getProductID()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public Product findById(String id) {
        return XQuery.getSingleBean(Product.class, findByIdSql, id);
    }

    @Override
    public List<Product> findAll() {
        return XQuery.getBeanList(Product.class, findAllSql);
    }

    public List<Product> findByName(String nameKeyword) {
        return XQuery.getBeanList(Product.class, findByNameSql, "%" + nameKeyword + "%");
    }
}

