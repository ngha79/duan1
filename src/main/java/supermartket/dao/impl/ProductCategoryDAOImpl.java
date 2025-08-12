package supermartket.dao.impl;

import java.util.List;
import supermartket.dao.ProductCategoryDAO;
import supermartket.entity.ProductCategory;
import supermartket.util.XJdbc;
import supermartket.util.XQuery;

public class ProductCategoryDAOImpl implements ProductCategoryDAO {

    private String insertSql = "INSERT INTO ProductCategory VALUES(?,?,?)";
    private String updateSql = "UPDATE ProductCategory SET CategoryName = ?, CategoryDescription = ? WHERE CategoryID = ?";
    private String deleteSql = "DELETE FROM ProductCategory WHERE CategoryID = ?";
    private String findByNameSql = "SELECT * FROM ProductCategory WHERE CategoryName LIKE ? OR CategoryDescription LIKE ?";
    private String findByIdSql = "SELECT * FROM ProductCategory WHERE CategoryID = ?";
    private String findAllSql = "SELECT * FROM ProductCategory";

    @Override
    public ProductCategory create(ProductCategory entity) {
        Object[] values = {
            entity.getCategoryID(),
            entity.getCategoryName(),
            entity.getCategoryDescription(),};
        XJdbc.executeUpdate(insertSql, values);
        return entity;
    }

    @Override
    public void update(ProductCategory entity) {
        Object[] values = {
            entity.getCategoryName(),
            entity.getCategoryDescription(),
            entity.getCategoryID(),};
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<ProductCategory> findAll() {
        return XQuery.getBeanList(ProductCategory.class, findAllSql);
    }

    @Override
    public List<ProductCategory> findAllByName(String search) {
        Object[] values = {
            "%" + search + "%",
            "%" + search + "%",};
        return XQuery.getBeanList(ProductCategory.class, findByNameSql, values);
    }

    @Override
    public ProductCategory findById(String id) {
        return XQuery.getSingleBean(ProductCategory.class, findByIdSql, id);
    }
}
