package supermartket.dao.impl;

import java.util.ArrayList;
import java.util.List;
import supermartket.dao.EmployeeDAO;
import supermartket.dao.dto.PageDTO;
import supermartket.dao.dto.SearchEmployeeDTO;
import supermartket.entity.Employee;
import supermartket.util.XJdbc;
import supermartket.util.XQuery;

public class EmployeeDAOImpl implements EmployeeDAO {

    private final String insertSql = "INSERT INTO Employee VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    private final String updateSql = "UPDATE Employee SET FullName = ?, Phone = ?, Email = ?, Gender = ?, StartDate = ?, Role = ?, Status = ? WHERE EmployeeID = ?";
    private final String deleteSql = "DELETE FROM Employee WHERE EmployeeID = ?";
    private final String findByNameSql = "SELECT * FROM Employee WHERE FullName LIKE ?";
    private final String findByIdSql = "SELECT * FROM Employee WHERE EmployeeID = ?";
    private final String findAllSql = "SELECT * FROM Employee;";
    private final String findSearchSql = """
                                    SELECT * FROM Employee
                                    WHERE 
                                      (? IS NULL OR FullName LIKE ?) AND
                                      (? IS NULL OR Status = ?)
                                    ORDER BY EmployeeID
                                    OFFSET (? - 1) * 10 ROWS
                                    FETCH NEXT 10 ROWS ONLY
                                    """;
    private final String getTotalItemSql = """
                                    SELECT count(*) as count FROM Employee
                                    WHERE 
                                      (? IS NULL OR FullName LIKE ?) AND
                                      (? IS NULL OR Status = ?)
                                    
                                    """;

    @Override
    public Employee create(Employee entity) {
        Object[] values = {
            entity.getEmployeeID(),
            entity.getFullName(),
            entity.getRole(),
            entity.getPhone(),
            entity.getEmail(),
            entity.getStartDate(),
            entity.getStatus(),
            entity.getGender(),};
        XJdbc.executeUpdate(insertSql, values);
        return entity;
    }

    @Override
    public void update(Employee entity) {
        Object[] values = {
            entity.getFullName(),
            entity.getPhone(),
            entity.getEmail(),
            entity.getGender(),
            entity.getStartDate(),
            entity.getRole(),
            entity.getStatus(),
            entity.getEmployeeID()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<Employee> findAll(SearchEmployeeDTO dto) {
        StringBuilder sql = new StringBuilder(findSearchSql);
        List<Object> params = new ArrayList<>();
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%"); // param 1
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%"); // param 2

        params.add(dto.getStatus());
        params.add(dto.getStatus());

        params.add(dto.getPage());
        return XQuery.getBeanList(Employee.class, sql.toString(), params.toArray());
    }

    @Override
    public Employee findById(String id) {
        return XQuery.getSingleBean(Employee.class, findByIdSql, id);
    }

    @Override
    public List<Employee> findAll() {
        return XQuery.getBeanList(Employee.class, findAllSql);
    }

    @Override
    public PageDTO getTotalItem(SearchEmployeeDTO dto) {
        StringBuilder sql = new StringBuilder(getTotalItemSql);
        List<Object> params = new ArrayList<>();
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%"); // param 1
        params.add(dto.getSearch() == null ? null : "%" + dto.getSearch() + "%"); // param 2

        params.add(dto.getStatus());
        params.add(dto.getStatus());
        return XQuery.getSingleBean(PageDTO.class, sql.toString(), params.toArray());
    }
}
