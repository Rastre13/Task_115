package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private SessionFactory sessionFactory = Util.getSessionFactory();
    private Transaction transaction = null;
    private static String option;
    private static String errorMessage;

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        option = "create table UsersTable(" + "id bigint auto_increment primary key,"
                + "name varchar(30)," + "lastName varchar(30)," + "age tinyint)";
        errorMessage = "A table with the same name already exists";
        doOperation();
    }

    @Override
    public void dropUsersTable() {
        option = "drop table UsersTable";
        errorMessage = "The table you are trying to delete doesn't exist";
        doOperation();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        option = "insert into UsersTable(name, lastName, age) values('" +
                name + "', '" + lastName + "', " + age + ")";
        errorMessage = "User hasn't been saved to the database";
        doOperation();
    }

    @Override
    public void removeUserById(long id) {
        option = "delete from UsersTable where id = " + id;
        errorMessage = "User hasn't been deleted from the database";
        doOperation();
    }

    @Override
    public void cleanUsersTable() {
        option = "truncate UsersTable";
        errorMessage = "Database hasn't been cleaned";
        doOperation();
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        option = "select * from UsersTable";
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            list = session.createSQLQuery(option).addEntity(User.class).list();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }

    public void doOperation() {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(option).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            catchCase();
        }
    }

    public void catchCase() {
        System.out.println(errorMessage);
        try {
            transaction.rollback();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
