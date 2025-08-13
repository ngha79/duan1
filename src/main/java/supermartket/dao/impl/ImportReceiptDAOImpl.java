/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermartket.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import supermartket.dao.ImportReceiptDAO;
import supermartket.dao.dto.ImportReceiptDTO;
import supermartket.dao.dto.InfoReceipt;
import supermartket.dao.dto.PageDTO;
import supermartket.dao.dto.SearchImportReceiptDTO;
import supermartket.entity.ImportReceipt;
import supermartket.util.XJdbc;
import supermartket.util.XQuery;

public class ImportReceiptDAOImpl implements ImportReceiptDAO {

    private final String insertSql = "INSERT INTO ImportReceipt (ReceiptID, ImportDate, SupplierID, Status) VALUES (?, ?, ?, ?)";
    private final String updateSql = "UPDATE ImportReceipt SET SupplierID = ?, Status = ? WHERE ReceiptID = ?";
    private final String deleteSql = "DELETE FROM ImportReceipt WHERE ReceiptID = ?";
    private final String findByIdSql = """
                                       SELECT 
                                           ir.ReceiptID,
                                           ir.ImportDate,
                                           ir.SupplierID,
                                           ir.Status,
                                           SUM(ird.Quantity) AS quantity,
                                           SUM(ird.Quantity * ird.UnitPrice) AS totalPrice
                                       FROM 
                                           ImportReceipt ir
                                       LEFT JOIN 
                                           ImportReceiptDetail ird ON ir.ReceiptID = ird.ReceiptID
                                       WHERE ir.ReceiptID = ?
                                       GROUP BY 
                                           ir.ReceiptID, ir.ImportDate, ir.SupplierID, ir.Status;""";
    private final String findAllSql = """
                                      SELECT 
                                        ir.ReceiptID,
                                        ir.ImportDate,
                                        ir.SupplierID,
                                        ir.Status,
                                        SUM(ird.Quantity) AS quantity,
                                        SUM(ird.Quantity * ird.UnitPrice) AS totalPrice
                                    FROM 
                                        ImportReceipt ir
                                    LEFT JOIN 
                                        ImportReceiptDetail ird ON ir.ReceiptID = ird.ReceiptID
                                    GROUP BY 
                                        ir.ReceiptID, ir.ImportDate, ir.SupplierID, ir.Status;""";
    private final String findBySearchSql = """
                                               SELECT
                                                ir.ReceiptID,
                                                ir.ImportDate,
                                                ir.SupplierID,
                                                ir.Status,
                                                SUM(ird.Quantity) AS quantity,
                                                SUM(ird.Quantity * ird.UnitPrice) AS totalPrice
                                                FROM 
                                                    ImportReceipt ir
                                                LEFT JOIN 
                                                    ImportReceiptDetail ird ON ir.ReceiptID = ird.ReceiptID
                                               WHERE 
                                                   (? IS NULL OR ir.ReceiptID LIKE ?) AND
                                                   (? IS NULL OR ir.SupplierID = ?) AND
                                                   (? IS NULL OR ir.Status = ?)
                                                GROUP BY 
                                                    ir.ReceiptID, ir.ImportDate, ir.SupplierID, ir.Status
                                                ORDER BY ir.ReceiptID
                                                OFFSET (? - 1) * 10 ROWS
                                                FETCH NEXT 10 ROWS ONLY
                                                   """;

    private final String getTotalItemSql = """
                                               SELECT
                                                COUNT(DISTINCT ir.ReceiptID) as count
                                                FROM 
                                                    ImportReceipt ir
                                                LEFT JOIN 
                                                    ImportReceiptDetail ird ON ir.ReceiptID = ird.ReceiptID
                                               WHERE 
                                                   (? IS NULL OR ir.ReceiptID LIKE ?) AND
                                                   (? IS NULL OR ir.SupplierID = ?) AND
                                                   (? IS NULL OR ir.Status = ?)
                                                   """;
    private final String getTotalByStatusSql = """
                                               SELECT 
                                                   (SELECT COUNT(*) 
                                                    FROM ImportReceipt 
                                                    WHERE ImportDate = CAST(GETDATE() AS DATE)) AS SoPhieuNhapHomNay,
                                               
                                                   (SELECT ISNULL(FORMAT(SUM(CAST(Quantity * UnitPrice AS DECIMAL(18,2))), '#,##0'), '0') + N'đ' AS TongGiaTriHomNay
                                                    FROM ImportReceiptDetail d
                                                    LEFT JOIN ImportReceipt r ON r.ReceiptID = d.ReceiptID
                                                    WHERE r.ImportDate = CAST(GETDATE() AS DATE)) AS TongGiaTriHomNay,
                                               
                                                   (SELECT COUNT(*) 
                                                    FROM ImportReceipt 
                                                    WHERE Status = N'Chờ duyệt') AS ChoDuyet,
                                               
                                                   (SELECT COUNT(*) 
                                                    FROM ImportReceipt 
                                                    WHERE Status = N'Đã duyệt'
                                                      AND MONTH(ImportDate) = MONTH(GETDATE())
                                                      AND YEAR(ImportDate) = YEAR(GETDATE())) AS DaHoanThanh""";

    @Override
    public ImportReceipt create(ImportReceipt entity) {
        Object[] values = {
            entity.getReceiptID(),
            entity.getImportDate(),
            entity.getSupplierID(),
            entity.getStatus(),};
        XJdbc.executeUpdate(insertSql, values);
        return entity;
    }

    @Override
    public void update(ImportReceipt entity) {
        Object[] values = {
            entity.getSupplierID(),
            entity.getStatus(),
            entity.getReceiptID(),};
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(deleteSql, id);

    }

    @Override
    public List<ImportReceiptDTO> findAll() {
        return XQuery.getBeanList(ImportReceiptDTO.class, findAllSql);
    }

    @Override
    public ImportReceiptDTO findById(String id) {
        return XQuery.getSingleBean(ImportReceiptDTO.class, findByIdSql, id);
    }

    @Override
    public List<ImportReceiptDTO> findAllBySearch(SearchImportReceiptDTO dto) {
        StringBuilder sql = new StringBuilder(findBySearchSql);
        List<Object> params = new ArrayList<>();
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%"); // param 1
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%"); // param 2

        params.add(dto.getSupplierID());
        params.add(dto.getSupplierID());

        params.add(dto.getStatus());
        params.add(dto.getStatus());

        params.add(dto.getPage());

        return XQuery.getBeanList(ImportReceiptDTO.class, sql.toString(), params.toArray());
    }

    @Override
    public PageDTO getTotalItem(SearchImportReceiptDTO dto) {
        StringBuilder sql = new StringBuilder(getTotalItemSql);
        List<Object> params = new ArrayList<>();
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%"); // param 1
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%"); // param 2

        params.add(dto.getSupplierID());
        params.add(dto.getSupplierID());

        params.add(dto.getStatus());
        params.add(dto.getStatus());
        return XQuery.getSingleBean(PageDTO.class, sql.toString(), params.toArray());
    }

    @Override
    public InfoReceipt getTotalByStatus() {
        ResultSet resultSet = XJdbc.executeQuery(getTotalByStatusSql);
        try {
            if (resultSet.next()) {
                Integer SoPhieuNhapHomNay = resultSet.getInt("SoPhieuNhapHomNay");
                String TongGiaTriHomNay = resultSet.getString("TongGiaTriHomNay");
                Integer ChoDuyet = resultSet.getInt("ChoDuyet");
                Integer DaHoanThanh = resultSet.getInt("DaHoanThanh");
                return new InfoReceipt(SoPhieuNhapHomNay, TongGiaTriHomNay, ChoDuyet, DaHoanThanh);
            }
            return InfoReceipt.builder().build();
        } catch (SQLException ex) {
            return InfoReceipt.builder().build();
        }
    }
}
