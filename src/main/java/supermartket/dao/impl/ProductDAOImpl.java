package supermartket.dao.impl;

import java.util.ArrayList;
import java.util.List;
import supermartket.dao.ProductDAO;
import supermartket.dao.dto.PageDTO;
import supermartket.dao.dto.SearchProductDTO;
import supermartket.dao.dto.SearchProductManagerDTO;
import supermartket.entity.Product;
import supermartket.util.XJdbc;
import supermartket.util.XQuery;

public class ProductDAOImpl implements ProductDAO {

    private final String insertSql = "INSERT INTO Product (ProductName, ProductImage, CategoryID, SupplierID, Quantity, Price, Status, Unit) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String updateSql = "UPDATE Product SET ProductName = ?, ProductImage = ?, CategoryID = ?, SupplierID = ?, Quantity = ?, Price = ?, Status = ?, Unit = ? WHERE ProductID = ?";
    private final String deleteSql = "DELETE FROM Product WHERE ProductID = ?";
    private final String findByIdSql = "SELECT * FROM Product WHERE ProductID = ?";
    private final String findAllSql = "SELECT * FROM Product";
    private final String findByNameSql = "SELECT * FROM Product WHERE ProductName LIKE ?";
    private final String findBySearchSql = """
                                               SELECT * FROM Product
                                               WHERE 
                                                   (? IS NULL OR ProductName LIKE ?) AND
                                                   (? IS NULL OR Status = ?) AND
                                                   (? IS NULL OR CategoryID = ?) AND
                                                   (? IS NULL OR SupplierID = ?)
                                                   """;
    private final String findBySearchStatusSql = """
                                               SELECT * FROM Product
                                               WHERE 
                                                   (? IS NULL OR ProductName LIKE ?) AND
                                                   (? IS NULL OR Status LIKE ?) AND
                                                   (? IS NULL OR CategoryID = ?) AND
                                                   (? IS NULL OR SupplierID = ?)
                                                   """;
    private final String getTotalItemStatusSql = """
                                               SELECT count(*) as count FROM Product
                                               WHERE 
                                                   (? IS NULL OR ProductName LIKE ?) AND
                                                   (? IS NULL OR Status LIKE ?) AND
                                                   (? IS NULL OR CategoryID = ?) AND
                                                   (? IS NULL OR SupplierID = ?)
                                                   """;
    private final String getTotalItemSql = """
                                               SELECT count(*) as count FROM Product
                                               WHERE 
                                                   (? IS NULL OR ProductName LIKE ?) AND
                                                   (? IS NULL OR Status LIKE ?) AND
                                                   (? IS NULL OR CategoryID = ?) AND
                                                   (? IS NULL OR SupplierID = ?)
                                                   """;
    private final String pagination = """
                                         ORDER BY ProductId
                                          OFFSET (? - 1) * 10 ROWS
                                          FETCH NEXT 10 ROWS ONLY;""";

    @Override
    public Product create(Product entity) {
        Object[] values = {
            entity.getProductName(),
            entity.getProductImage(),
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
            entity.getProductImage(),
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

    @Override
    public List<Product> findByName(String nameKeyword) {
        return XQuery.getBeanList(Product.class, findByNameSql, "%" + nameKeyword + "%");
    }

    @Override
    public List<Product> findBySearch(SearchProductDTO dto) {
        StringBuilder sql = new StringBuilder(findBySearchSql);
        if (dto.getProductIds() != null && !dto.getProductIds().isEmpty()) {
            sql.append(" AND ProductID NOT IN (");
            sql.append("?,".repeat(dto.getProductIds().size()));
            sql.setLength(sql.length() - 1); // Xóa dấu "," cuối
            sql.append(")");
        }
        sql.append(pagination);
        List<Object> params = new ArrayList<>();
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%"); // param 1
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%"); // param 2

        params.add(dto.getStatus());
        params.add(dto.getStatus());
        
        params.add(dto.getCategoryID());
        params.add(dto.getCategoryID());

        params.add(dto.getSupplierID());
        params.add(dto.getSupplierID());
        if (dto.getProductIds() != null) {
            params.addAll(dto.getProductIds());
        }

        params.add(dto.getPage());

        return XQuery.getBeanList(Product.class, sql.toString(), params.toArray());
    }

    @Override
    public List<Product> findBySearchStatus(SearchProductManagerDTO dto) {
        StringBuilder sql = new StringBuilder(findBySearchStatusSql);
        sql.append(pagination);
        List<Object> params = new ArrayList<>();
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%");
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%");

        params.add(dto.getStatus());
        params.add(dto.getStatus());

        params.add(dto.getCategoryID());
        params.add(dto.getCategoryID());

        params.add(dto.getSupplierID());
        params.add(dto.getSupplierID());
        
        params.add(dto.getPage());

        return XQuery.getBeanList(Product.class, sql.toString(), params.toArray());
    }

    @Override
    public PageDTO getTotalItem(SearchProductDTO dto) {
        StringBuilder sql = new StringBuilder(getTotalItemSql);

        List<Object> params = new ArrayList<>();
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%");
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%");

        params.add(dto.getStatus());
        params.add(dto.getStatus());

        params.add(dto.getCategoryID());
        params.add(dto.getCategoryID());

        params.add(dto.getSupplierID());
        params.add(dto.getSupplierID());

        return XQuery.getSingleBean(PageDTO.class, sql.toString(), params.toArray());
    }
    
    @Override
    public PageDTO getTotalItemStatus(SearchProductManagerDTO dto) {
        StringBuilder sql = new StringBuilder(getTotalItemStatusSql);

        List<Object> params = new ArrayList<>();
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%");
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%");

        params.add(dto.getStatus());
        params.add(dto.getStatus());

        params.add(dto.getCategoryID());
        params.add(dto.getCategoryID());

        params.add(dto.getSupplierID());
        params.add(dto.getSupplierID());

        return XQuery.getSingleBean(PageDTO.class, sql.toString(), params.toArray());
    }

}
