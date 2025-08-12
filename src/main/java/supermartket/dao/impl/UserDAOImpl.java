package supermartket.dao.impl;

import java.util.List;
import supermartket.dao.UserDAO;
import supermartket.entity.User;
import supermartket.util.XJdbc;
import supermartket.util.XQuery;

public class UserDAOImpl implements UserDAO {

    String createSql = "INSERT INTO Users(Username, Password,  Role, EmployeeCode) VALUES(?,?,?,?)";
    String updateSql = "UPDATE Users SET Password=?, Role=? WHERE Username=?";
    String deleteSql = "DELETE FROM Users WHERE Username=?";
    String findAllSql = "SELECT * FROM Users";
    String findByIdSql = "SELECT * FROM Users WHERE Username=?";
    String findByNameSql = "SELECT * FROM Users u LEFT JOIN Employee e ON u.EmployeeCode = e.EmployeeID WHERE u.Username LIKE ? OR e.FullName LIKE ?";

    @Override
    public User create(User entity) {
        Object[] values = {
            entity.getUsername(),
            entity.getPassword(),
            entity.getRole(),
            entity.getEmployeeCode(),};
        XJdbc.executeUpdate(createSql, values);
        return entity;
    }

    @Override
    public void update(User entity) {
        Object[] values = {
            entity.getPassword(),
            entity.getRole(),
            entity.getUsername(),};
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<User> findAll() {
        return XQuery.getBeanList(User.class, findAllSql);
    }

    @Override
    public User findById(String id) {

        return XQuery.getSingleBean(User.class, findByIdSql, id);
    }

    @Override
    public List<User> findByName(String name) {
        Object[] values = {
            "%" + name + "%",
            "%" + name + "%"
        };
        return XQuery.getBeanList(User.class, findByNameSql, values);
    }
}
