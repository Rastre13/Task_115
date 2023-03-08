package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private static Connection connection = Util.getMyConnection();
    private static String option;
    private static String errorMessage;

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        option = "create table UsersTable(" + "id bigint auto_increment primary key,"
                + "name varchar(30)," + "lastName varchar(30)," + "age tinyint)";
        errorMessage = "A table with the same name already exists";
        doOperation();
    }

    public void dropUsersTable() {
        option = "drop table UsersTable";
        errorMessage = "The table you are trying to delete doesn't exist";
        doOperation();
    }

    public void saveUser(String name, String lastName, byte age) {
        option = "insert into UsersTable(name, lastName, age) values('" +
                name + "', '" + lastName + "', " + age + ")";
        errorMessage = "User hasn't been saved to the database";
        doOperation();
    }

    public void removeUserById(long id) {
        option = "delete from UsersTable where id = " + id;
        errorMessage = "User hasn't been deleted from the database";
        doOperation();
    }

    public void cleanUsersTable() {
        option = "truncate UsersTable";
        errorMessage = "Database hasn't been cleaned";
        doOperation();
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        option = "select * from UsersTable";
        try (Statement statement = connection.createStatement()){
            connection.setAutoCommit(false);
            ResultSet resultSet = statement.executeQuery(option);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getByte(4));
                System.out.println(user);
                list.add(user);
            }
            connection.commit();
        } catch (SQLException e) {
            catchCase();
        }
        return list;
    }

    public void doOperation() {
        try (Statement statement = connection.createStatement()){
            connection.setAutoCommit(false);
            statement.executeUpdate(option);
            connection.commit();
        } catch (SQLException e) {
            catchCase();
        }
    }

    public void catchCase() {
        System.out.println(errorMessage);
        try {
            connection.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
