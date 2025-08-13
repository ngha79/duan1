
package supermartket.dao;

import java.util.List;
import supermartket.dao.dto.ChangePassworDTO;
import supermartket.dao.dto.PageDTO;
import supermartket.dao.dto.SearchEmployeeDTO;
import supermartket.entity.Employee;


public interface EmployeeDAO extends CrudDAO<Employee, String>{
    List<Employee> findAll(SearchEmployeeDTO dto);
    PageDTO getTotalItem(SearchEmployeeDTO dto);
    Employee findByUserName(String username);
    void changePassword(ChangePassworDTO dto);
}
