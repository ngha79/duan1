package supermartket.dao;

import java.util.List;
import supermartket.entity.ProductCategory;

public interface ProductCategoryDAO extends CrudDAO<ProductCategory, String>{
    List<ProductCategory> findAllByName(String name);
}
