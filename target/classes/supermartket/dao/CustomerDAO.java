package supermartket.dao;

import java.util.List;
import supermartket.dao.dto.CustomerDTO;
import supermartket.dao.dto.CustomerInfoDTO;
import supermartket.dao.dto.PageDTO;
import supermartket.entity.Customer;

public interface CustomerDAO extends CrudDAO<Customer, String> {

    List<Customer> findByName(CustomerDTO dto);

    Customer findByDate();

    PageDTO getTotalItem(CustomerDTO dto);

    CustomerInfoDTO customerDetail();
}
