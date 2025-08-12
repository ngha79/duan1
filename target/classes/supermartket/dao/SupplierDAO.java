package supermartket.dao;

import java.util.List;
import supermartket.dao.dto.PageDTO;
import supermartket.dao.dto.SupplierDTO;
import supermartket.entity.Supplier;

public interface SupplierDAO extends CrudDAO<Supplier, String>{
    List<Supplier> findAllByName(SupplierDTO dto);
    PageDTO getTotalItem(SupplierDTO dto);
}
