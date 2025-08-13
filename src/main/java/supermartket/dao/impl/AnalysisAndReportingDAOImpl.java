package supermartket.dao.impl;

import java.util.List;
import supermartket.dao.AnalysisAndReportingDAO;
import supermartket.dao.dto.AnalysisAndReportingDTO;
import supermartket.dao.dto.CustomerAnalysisAndReportingDTO;
import supermartket.dao.dto.ItemProductAnalysisAndReportingDTO;
import supermartket.util.XQuery;

public class AnalysisAndReportingDAOImpl implements AnalysisAndReportingDAO {

    private final String reportingSql = """
                                        SELECT 
                                           (SELECT 
                                                ISNULL(SUM(TotalAmount), 0) 
                                            FROM Invoice
                                            WHERE CAST(InvoiceDate AS DATE) = CAST(GETDATE() AS DATE)) AS TodayRevenue,
                                            (SELECT 
                                                ISNULL(SUM(TotalAmount), 0)
                                            FROM Invoice
                                            WHERE DATEPART(week, InvoiceDate) = DATEPART(week, GETDATE())
                                              AND YEAR(InvoiceDate) = YEAR(GETDATE()))  AS WeekRevenue,
                                            (SELECT 
                                                ISNULL(SUM(TotalAmount), 0) 
                                            FROM Invoice
                                            WHERE MONTH(InvoiceDate) = MONTH(GETDATE())
                                              AND YEAR(InvoiceDate) = YEAR(GETDATE())) AS MonthRevenue
                                        
                                        """;

    private final String topProductSql = """
                                        SELECT TOP 7
                                            p.ProductName,
                                          p.ProductImage,
                                            SUM(id.Quantity) AS QuantitySold,
                                            SUM(id.Quantity * id.UnitPrice) AS TotalRevenue
                                        FROM Invoice i
                                        JOIN InvoiceDetail id ON i.InvoiceID = id.InvoiceID
                                        JOIN Product p ON id.ProductID = p.ProductID
                                        WHERE i.InvoiceDate >= DATEADD(DAY, -6, CAST(GETDATE() AS DATE))
                                        GROUP BY p.ProductName, p.ProductImage
                                        ORDER BY QuantitySold DESC;
                                        """;

    private final String top5CustomerSql = """
                                           SELECT TOP 5
                                               c.CustomerID,
                                               c.FullName,
                                               c.Phone,
                                               c.Email,
                                               SUM(id.Quantity * id.UnitPrice) AS TotalRevenue
                                           FROM
                                               Customer c
                                               INNER JOIN Invoice i ON c.CustomerID = i.CustomerID
                                               INNER JOIN InvoiceDetail id ON i.InvoiceID = id.InvoiceID
                                           WHERE
                                               CAST(i.InvoiceDate AS DATETIME) + CAST(i.InvoiceTime AS DATETIME) >= DATEADD(DAY, -1, GETDATE())
                                               AND c.CustomerID NOT IN ('CUST0001')
                                           GROUP BY
                                               c.CustomerID,
                                               c.FullName,
                                               c.Phone,
                                               c.Email
                                           ORDER BY
                                               TotalRevenue DESC
                                           """;

    @Override
    public AnalysisAndReportingDTO getInfoAnalysis() {
        return XQuery.getSingleBean(AnalysisAndReportingDTO.class, reportingSql);
    }

    @Override
    public List<ItemProductAnalysisAndReportingDTO> getTopProductAnalysis() {
        return XQuery.getBeanList(ItemProductAnalysisAndReportingDTO.class, topProductSql);
    }

    @Override
    public List<CustomerAnalysisAndReportingDTO> getTop5Customer() {
        return XQuery.getBeanList(CustomerAnalysisAndReportingDTO.class, top5CustomerSql);
    }

}
