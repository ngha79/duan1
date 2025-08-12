/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermartket.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import supermartket.dao.InvoiceDetailDAO;
import supermartket.entity.InvoiceDetail;
import supermartket.util.XJdbc;
import supermartket.util.XQuery;

public class InvoiceDetailDAOImpl implements InvoiceDetailDAO {

    private final String insertSql = "INSERT INTO InvoiceDetail (InvoiceID, ProductID, Quantity, UnitPrice) VALUES (?, ?, ?, ?)";
    private final String updateSql = "UPDATE InvoiceDetail SET Quantity = ?, UnitPrice = ? WHERE InvoiceID = ? AND ProductID = ?";
    private final String deleteSql = "DELETE FROM InvoiceDetail WHERE InvoiceID = ? AND ProductID = ?";
    private final String findByIdSql = "SELECT * FROM InvoiceDetail WHERE InvoiceID = ? AND ProductID = ?";
    private final String findAllSql = "SELECT * FROM InvoiceDetail";
    private final String findByInvoiceIDSql = "SELECT * FROM InvoiceDetail WHERE InvoiceID = ?";

    @Override
    public InvoiceDetail create(InvoiceDetail detail, Connection conn) {
        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setString(1, detail.getInvoiceID());
            ps.setString(2, detail.getProductID());
            ps.setInt(3, detail.getQuantity());
            ps.setBigDecimal(4, detail.getUnitPrice());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return detail;
    }

    @Override
    public void update(InvoiceDetail entity) {
        Object[] values = {
            entity.getQuantity(),
            entity.getUnitPrice(),
            entity.getInvoiceID(),
            entity.getProductID()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(String invoiceID, String productID) {
        XJdbc.executeUpdate(deleteSql, invoiceID, productID);
    }

    @Override
    public List<InvoiceDetail> findAll() {
        return XQuery.getBeanList(InvoiceDetail.class, findAllSql);
    }

    @Override
    public InvoiceDetail findById(String invoiceID, String productID) {
        return XQuery.getSingleBean(InvoiceDetail.class, findByIdSql, invoiceID, productID);
    }
    
    @Override
    public List<InvoiceDetail> findByInvoiceID(String invoiceID) {
        return XQuery.getBeanList(InvoiceDetail.class, findByInvoiceIDSql, invoiceID);
    }
}
