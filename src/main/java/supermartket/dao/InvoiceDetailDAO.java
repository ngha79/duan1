package supermartket.dao;

import java.sql.Connection;
import java.util.List;
import supermartket.entity.InvoiceDetail;

public interface InvoiceDetailDAO {

    InvoiceDetail create(InvoiceDetail entity, Connection conn);

    void update(InvoiceDetail entity);

    void deleteById(String invoiceID, String productID);

    List<InvoiceDetail> findAll();

    InvoiceDetail findById(String invoiceID, String productID);
    
    List<InvoiceDetail> findByInvoiceID(String invoiceID);
}
