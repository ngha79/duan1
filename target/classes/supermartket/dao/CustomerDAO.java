package supermartket.dao;

import java.util.List;
import supermartket.entity.Customer;

public interface CustomerDAO extends CrudDAO<Customer, String>{
    List<Customer> findByName(String name);
}
