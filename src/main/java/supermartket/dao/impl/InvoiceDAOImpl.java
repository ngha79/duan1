/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermartket.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import supermartket.dao.InvoiceDAO;
import supermartket.dao.dto.InvoiceDTO;
import supermartket.dao.dto.PageDTO;
import supermartket.dao.dto.SearchInvoiceDTO;
import supermartket.entity.Invoice;
import supermartket.util.XJdbc;
import supermartket.util.XQuery;

public class InvoiceDAOImpl {

    private final String insertSql = "INSERT INTO Invoice ( InvoiceDate, InvoiceTime, EmployeeID, CustomerID, TotalAmount, TotalQuantity, PaymentMethod, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String updateSql = "UPDATE Invoice SET InvoiceDate = ?, InvoiceTime = ?, EmployeeID = ?, CustomerID = ?, TotalAmount = ?, TotalQuantity = ?, PaymentMethod = ?, Status = ? WHERE InvoiceID = ?";
    private final String deleteSql = "DELETE FROM Invoice WHERE InvoiceID = ?";
    private final String findByIdSql = "SELECT * FROM Invoice WHERE InvoiceID = ?";
    private final String findByDateSql = """
                                        SELECT TOP 1 i.* FROM Invoice i
                                        LEFT JOIN Customer c ON i.CustomerID = c.CustomerID
                                        LEFT JOIN Employee e ON i.EmployeeID = e.EmployeeID
                                        ORDER BY 
                                        CAST(i.InvoiceDate AS DATETIME) + CAST(i.InvoiceTime AS DATETIME) DESC
                                         """;
    private final String findAllSql = "SELECT * FROM Invoice";
    private final String findInvoiceBySearchSql = """
    SELECT i.* FROM Invoice i
    LEFT JOIN Customer c ON i.CustomerID = c.CustomerID
    LEFT JOIN Employee e ON i.EmployeeID = e.EmployeeID
    WHERE
        (? IS NULL OR (c.FullName LIKE ? OR c.FullName IS NULL)) AND
        (? IS NULL OR i.EmployeeID = ?) AND
        (? IS NULL OR i.InvoiceDate >= ?) AND
        (? IS NULL OR i.InvoiceDate <= ?)
    ORDER BY i.InvoiceID
    OFFSET (? - 1) * 10 ROWS
    FETCH NEXT 10 ROWS ONLY;
""";
    private final String getTotalInvoiceBySearchSql = """
    SELECT count(*) as count FROM Invoice i
    LEFT JOIN Customer c ON i.CustomerID = c.CustomerID
    LEFT JOIN Employee e ON i.EmployeeID = e.EmployeeID
    WHERE
        (? IS NULL OR (c.FullName LIKE ? OR c.FullName IS NULL)) AND
        (? IS NULL OR i.EmployeeID = ?) AND
        (? IS NULL OR i.InvoiceDate >= ?) AND
        (? IS NULL OR i.InvoiceDate <= ?)
""";

    public Invoice create(Invoice invoice, Connection conn) throws SQLException {
        String sql = "INSERT INTO Invoice (InvoiceDate, InvoiceTime, EmployeeID, CustomerID, TotalAmount, TotalQuantity, PaymentMethod, Status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(invoice.getInvoiceDate().getTime()));
            ps.setDate(1, new java.sql.Date(invoice.getInvoiceDate().getTime()));
            ps.setTime(2, invoice.getInvoiceTime());
            ps.setString(3, invoice.getEmployeeID());

            if (invoice.getCustomerID() == null) {
                ps.setNull(4, Types.VARCHAR);
            } else {
                ps.setString(4, invoice.getCustomerID());
            }

            ps.setBigDecimal(5, invoice.getTotalAmount());
            ps.setInt(6, invoice.getTotalQuantity());
            ps.setString(7, invoice.getPaymentMethod());
            ps.setString(8, invoice.getStatus());
            ps.executeUpdate();
        }
        return invoice;
    }

    public void update(Invoice entity) {
        Object[] values = {
            entity.getInvoiceDate(),
            entity.getInvoiceTime(),
            entity.getEmployeeID(),
            entity.getCustomerID(),
            entity.getTotalAmount(),
            entity.getTotalQuantity(),
            entity.getPaymentMethod(),
            entity.getStatus(),
            entity.getInvoiceID()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    public void deleteById(String id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    public List<Invoice> findAll() {
        return XQuery.getBeanList(Invoice.class, findAllSql);
    }
    
    public Invoice findByDate() {
        return XQuery.getSingleBean(Invoice.class, findByDateSql);
    }

    public Invoice findById(String id) {
        return XQuery.getSingleBean(Invoice.class, findByIdSql, id);
    }
    
     public PageDTO getTotalItem(SearchInvoiceDTO dto) {
        StringBuilder sql = new StringBuilder(getTotalInvoiceBySearchSql);
        List<Object> params = new ArrayList<>();

        String likeSearch = dto.getSearch() == null ? null : "%" + dto.getSearch() + "%";
        params.add(likeSearch);  
        params.add(likeSearch); 

        params.add(dto.getEmployeeId());
        params.add(dto.getEmployeeId());

        params.add(dto.getFromDate());
        params.add(dto.getFromDate());

        params.add(dto.getToDate());
        params.add(dto.getToDate());
        return XQuery.getSingleBean(PageDTO.class, sql.toString(), params.toArray());
    }

    public List<Invoice> findBySearchEmployeeAndDate(SearchInvoiceDTO dto) {
        StringBuilder sql = new StringBuilder(findInvoiceBySearchSql);
        List<Object> params = new ArrayList<>();

        String likeSearch = dto.getSearch() == null ? null : "%" + dto.getSearch() + "%";
        params.add(likeSearch);  // ? IS NULL
        params.add(likeSearch);  // c.CustomerName LIKE ?

        params.add(dto.getEmployeeId());
        params.add(dto.getEmployeeId());

        params.add(dto.getFromDate());
        params.add(dto.getFromDate());

        params.add(dto.getToDate());
        params.add(dto.getToDate());
        
        params.add(dto.getPage());

        return XQuery.getBeanList(Invoice.class, sql.toString(), params.toArray());
    }
}
