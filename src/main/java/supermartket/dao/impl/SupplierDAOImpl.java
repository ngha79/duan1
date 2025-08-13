package supermartket.dao.impl;

import java.util.ArrayList;
import java.util.List;
import supermartket.dao.SupplierDAO;
import supermartket.dao.dto.PageDTO;
import supermartket.dao.dto.SupplierDTO;
import supermartket.entity.Supplier;
import supermartket.util.XJdbc;
import supermartket.util.XQuery;

public class SupplierDAOImpl implements SupplierDAO {

    private final String insertSql = "INSERT INTO Supplier (SupplierName, Address, Phone, Email, Status) VALUES ( ?, ?, ?, ?, ?)";
    private final String updateSql = "UPDATE Supplier SET SupplierName = ?, Address = ?, Phone = ?, Email = ?, Status = ? WHERE SupplierID = ?";
    private final String deleteSql = "DELETE FROM Supplier WHERE SupplierID = ?";
    private final String findByIdSql = "SELECT * FROM Supplier WHERE SupplierID = ?";
    private final String findAllSql = "SELECT * FROM Supplier";
    private final String findAllByNameSql = """
                                            SELECT * FROM Supplier
                                            WHERE 
                                                (? IS NULL OR SupplierName LIKE ?) AND
                                                (? IS NULL OR Status = ?) 
                                            ORDER BY SupplierID
                                            OFFSET (? - 1) * 10 ROWS
                                            FETCH NEXT 10 ROWS ONLY;
                                            """;
    private final String getTotalItemSql = """
                                            SELECT Count(*) as count FROM Supplier
                                            WHERE 
                                                (? IS NULL OR SupplierName LIKE ?) AND
                                                (? IS NULL OR Status = ?) 
                                            """;

    @Override
    public Supplier create(Supplier entity) {
        Object[] values = {
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

    @Override
    public List<Supplier> findAllByName(SupplierDTO dto) {
        StringBuilder sql = new StringBuilder(findAllByNameSql);
        
        List<Object> params = new ArrayList<>();
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%"); // param 1
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%"); // param 2

        params.add(dto.getStatus());
        params.add(dto.getStatus());

        params.add(dto.getPage());
        return XQuery.getBeanList(Supplier.class, sql.toString(), params.toArray());
    }

    @Override
    public PageDTO getTotalItem(SupplierDTO dto) {
        StringBuilder sql = new StringBuilder(getTotalItemSql);
        
        List<Object> params = new ArrayList<>();
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%"); // param 1
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%"); // param 2

        params.add(dto.getStatus());
        params.add(dto.getStatus());

        return XQuery.getSingleBean(PageDTO.class, sql.toString(), params.toArray()); 
    }
}
