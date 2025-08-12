package supermartket.dao;

import java.util.List;
import supermartket.dao.dto.ImportReceiptDTO;
import supermartket.dao.dto.InfoReceipt;
import supermartket.dao.dto.PageDTO;
import supermartket.dao.dto.SearchImportReceiptDTO;
import supermartket.entity.ImportReceipt;

public interface ImportReceiptDAO {

    ImportReceipt create(ImportReceipt entity);

    void update(ImportReceipt entity);

    void deleteById(String id);

    List<ImportReceiptDTO> findAll();
    
    PageDTO getTotalItem(SearchImportReceiptDTO dto);

    List<ImportReceiptDTO> findAllBySearch(SearchImportReceiptDTO dto);

    ImportReceiptDTO findById(String id);
    
    InfoReceipt getTotalByStatus();
}
