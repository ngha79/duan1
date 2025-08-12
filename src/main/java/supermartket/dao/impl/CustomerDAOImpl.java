package supermartket.dao.impl;

import java.util.ArrayList;
import java.util.List;
import supermartket.dao.CustomerDAO;
import supermartket.dao.dto.CustomerDTO;
import supermartket.dao.dto.CustomerInfoDTO;
import supermartket.dao.dto.PageDTO;
import supermartket.entity.Customer;
import supermartket.util.XJdbc;
import supermartket.util.XQuery;

public class CustomerDAOImpl implements CustomerDAO {

    private final String insertSql = "INSERT INTO Customer (CustomerID, FullName, Phone, Email) VALUES (?, ?, ?, ?)";
    private final String updateSql = "UPDATE Customer SET FullName = ?, Phone = ?, Email = ? WHERE CustomerID = ?";
    private final String deleteSql = "DELETE FROM Customer WHERE CustomerID = ?";
    private final String findByIdSql = "SELECT * FROM Customer WHERE CustomerID = ?";
    private final String findAllSql = "SELECT * FROM Customer";
    private final String findByNameSql = """
                                        SELECT * FROM Customer WHERE
                                            (? IS NULL OR FullName LIKE ?)
                                        ORDER BY CustomerID
                                        OFFSET (? - 1) * 10 ROWS
                                        FETCH NEXT 10 ROWS ONLY""";
     private final String getTotalItemSql = """
                                        SELECT count(*) as count FROM Customer WHERE
                                            (? IS NULL OR FullName LIKE ?)
                                        """;
    private final String customerDetailSql = """
                                             SELECT 
                                                 COUNT(*) AS totalCustomers,
                                                 SUM(CASE 
                                                         WHEN YEAR(createdAt) = YEAR(GETDATE()) 
                                                          AND MONTH(createdAt) = MONTH(GETDATE()) 
                                                         THEN 1 ELSE 0 
                                                     END) AS newCustomersThisMonth,
                                                 SUM(CASE 
                                                         WHEN createdAt >= DATEADD(DAY, 1 - DATEPART(WEEKDAY, GETDATE()), CAST(GETDATE() AS DATE))
                                                          AND createdAt <= GETDATE() 
                                                         THEN 1 ELSE 0 
                                                     END) AS newCustomersThisWeek
                                             FROM Customer;
                                             """;

    @Override
    public Customer create(Customer entity) {
        Object[] values = {
            entity.getCustomerID(),
            entity.getFullName(),
            entity.getPhone(),
            entity.getEmail(),};
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
    public CustomerInfoDTO customerDetail() {
        return XQuery.getSingleBean(CustomerInfoDTO.class, customerDetailSql);
    }

    @Override
    public List<Customer> findByName(CustomerDTO dto) {
        StringBuilder sql = new StringBuilder(findByNameSql);
        List<Object> params = new ArrayList<>();

        String likeSearch = dto.getSearch() == null ? null : "%" + dto.getSearch() + "%";
        params.add(likeSearch);
        params.add(likeSearch);

        params.add(dto.getPage());
        return XQuery.getBeanList(Customer.class, sql.toString(), params.toArray());
    }

    @Override
    public PageDTO getTotalItem(CustomerDTO dto) {
       StringBuilder sql = new StringBuilder(getTotalItemSql);
        List<Object> params = new ArrayList<>();

        String likeSearch = dto.getSearch() == null ? null : "%" + dto.getSearch() + "%";
        params.add(likeSearch);
        params.add(likeSearch);
        return XQuery.getSingleBean(PageDTO.class, sql.toString(), params.toArray());   
    }
}
