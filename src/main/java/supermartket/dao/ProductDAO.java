package supermartket.dao;

import java.util.List;
import supermartket.dao.dto.PageDTO;
import supermartket.dao.dto.SearchProductDTO;
import supermartket.dao.dto.SearchProductManagerDTO;
import supermartket.entity.Product;


public interface ProductDAO extends CrudDAO<Product, String>{
    List<Product> findByName(String nameKeyword);
    PageDTO getTotalItem(SearchProductDTO dto);
    PageDTO getTotalItemStatus(SearchProductManagerDTO dto);
    List<Product> findBySearchStatus(SearchProductManagerDTO dto);
    List<Product> findBySearch(SearchProductDTO dto);
}
