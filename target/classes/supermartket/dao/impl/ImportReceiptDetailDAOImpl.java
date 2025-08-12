/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermartket.dao.impl;

import java.util.List;
import supermartket.dao.ImportReceiptDetailDAO;
import supermartket.entity.ImportReceipt;
import supermartket.entity.ImportReceiptDetail;
import supermartket.util.XJdbc;
import supermartket.util.XQuery;

public class ImportReceiptDetailDAOImpl implements ImportReceiptDetailDAO {

    private final String insertSql = "INSERT INTO ImportReceiptDetail (ReceiptID, ProductID, Quantity, UnitPrice) VALUES (?, ?, ?, ?)";
    private final String updateSql = "UPDATE ImportReceiptDetail SET Quantity = ?, UnitPrice = ? WHERE ReceiptID = ? AND ProductID = ?";
    private final String deleteSql = "DELETE FROM ImportReceiptDetail WHERE ReceiptID = ? AND ProductID = ?";
    private final String deleteIRSql = "DELETE FROM ImportReceiptDetail WHERE ReceiptID = ?";
    private final String findByImportReceiptSql = "SELECT * FROM ImportReceiptDetail WHERE ReceiptID = ?";

    @Override
    public ImportReceiptDetail create(ImportReceiptDetail entity) {
        Object[] values = {
            entity.getReceiptID(),
            entity.getProductID(),
            entity.getQuantity(),
            entity.getUnitPrice(),};
        XJdbc.executeUpdate(insertSql, values);
        return entity;
    }

    @Override
    public void update(ImportReceiptDetail entity) {
        Object[] values = {
            entity.getQuantity(),
            entity.getUnitPrice(),
            entity.getReceiptID(),
            entity.getProductID(),};
        XJdbc.executeUpdate(insertSql, values);
    }

    @Override
    public void deleteById(String id, String prodId) {
        Object[] values = {
            id, prodId
        };
        XJdbc.executeUpdate(deleteSql, values);
    }

    @Override
    public List<ImportReceiptDetail> findAll(String id) {
        return XQuery.getBeanList(ImportReceiptDetail.class, findByImportReceiptSql, id);
    }

    @Override
    public void deleteByIRId(String id) {
        XJdbc.executeUpdate(deleteIRSql, id);
    }
}
