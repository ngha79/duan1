/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermartket.dao.impl;

import java.util.List;
import supermartket.dao.dto.CartProductDTO;
import supermartket.dao.dto.CartReceiptDTO;
import supermartket.dao.dto.DashBoardInfoDTO;
import supermartket.util.XQuery;


public class DashBoardImpl {
    String infoSql = """
                     SELECT 
                         (SELECT COALESCE(SUM(FinalAmount), 0) AS todayRevenue
                         FROM Invoice
                         WHERE invoiceDate >= CAST(GETDATE() AS DATE)
                           AND invoiceDate < DATEADD(DAY, 1, CAST(GETDATE() AS DATE))
                         ) AS todayRevenue,
                     
                         (SELECT COALESCE(COUNT(*), 0)
                          FROM Invoice 
                          WHERE CAST(invoiceDate AS DATE) = CAST(GETDATE() AS DATE)
                         ) AS todayInvoices,
                     
                         (SELECT COUNT(*) 
                          FROM Product
                         ) AS totalProducts,
                     
                         (SELECT COUNT(*) 
                          FROM Product 
                          WHERE quantity <= 5
                         ) AS lowStockProducts""";
    String top5InvoiceTodaySql = """
                                 SELECT TOP 5 i.InvoiceID, c.FullName as customerName, i.invoiceTime, i.totalAmount, i.paymentMethod
                                 FROM Invoice i
                                 JOIN Customer c
                                 ON i.CustomerID = c.CustomerID
                                 WHERE CAST(i.invoiceDate AS DATE) = CAST(GETDATE() AS DATE)
                                 ORDER BY i.invoiceDate DESC;""";
    
    String topReceiptSql = """
                                 SELECT TOP 5 i.ReceiptID, s.SupplierName, SUM(id.quantity * id.UnitPrice) as TotalAmount, i.status, i.ImportDate
                                 FROM ImportReceipt i
                                 JOIN Supplier s
                                 ON i.SupplierID = s.SupplierID
                                 JOIN ImportReceiptDetail id
                                 ON i.ReceiptID = id.ReceiptID
                                 WHERE CAST(i.ImportDate AS DATE) = CAST(GETDATE() AS DATE)
                                 GROUP BY  i.ReceiptID, s.SupplierName,  i.status, i.ImportDate
                                 ORDER BY i.ImportDate DESC;""";
    public DashBoardInfoDTO getInfo() {
        return XQuery.getSingleBean(DashBoardInfoDTO.class, infoSql);
    }
    
     public List<CartProductDTO> getTop5InvoiceToday() {
        return XQuery.getBeanList(CartProductDTO.class, top5InvoiceTodaySql);
    }
     
      public List<CartReceiptDTO> getTopReceipt() {
        return XQuery.getBeanList(CartReceiptDTO.class, topReceiptSql);
    }
}
