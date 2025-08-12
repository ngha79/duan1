package supermartket.dao;

import java.util.List;
import supermartket.entity.ImportReceiptDetail;

public interface ImportReceiptDetailDAO {
    ImportReceiptDetail create(ImportReceiptDetail entity);

    void update(ImportReceiptDetail entity);

    void deleteById(String id, String prodId);
    
    void deleteByIRId(String id);

    List<ImportReceiptDetail> findAll(String id);
}
