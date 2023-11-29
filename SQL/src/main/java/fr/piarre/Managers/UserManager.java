package fr.piarre.Managers;

import fr.piarre.Exceptions.Auth.UserNotFoundException;
import fr.piarre.Exceptions.Auth.WrongPasswordException;
import fr.piarre.Models.LoginReturn;
import fr.piarre.Models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

public class UserManager {
    Statement statement;

    public UserManager(Statement statement) {
        this.statement = statement;
    }

    public User GetByEmail(String email) {
        try {
            ResultSet resultSet = this.statement.executeQuery("SELECT * FROM users WHERE email = '" + email + "'");

            if (resultSet.next()) {
                User user = new User(resultSet.getString("email"), resultSet.getString("password"));
                user.setId(resultSet.getInt("id"));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
            return null;
        }
    }


    public LoginReturn Login(User user) throws WrongPasswordException, UserNotFoundException {
        User foundedUser = GetByEmail(user.getEmail());

        if (foundedUser != null) {
            if (foundedUser.getPassword().equals(user.getPassword())) {
                return LoginReturn.SUCCESS;
            } else {
                return LoginReturn.WRONG_PASSWORD;
            }
        } else {
            return LoginReturn.USER_NOT_FOUND;
        }
    }

    public void AddUser(User user, User creator) {
        try {
            int res = this.statement.executeUpdate("INSERT INTO users(email, password, createdBy) VALUES ('" + user.getEmail() + "', '" + user.getPassword() + "', '" + creator.getId() + "')");
            User foundedUser = GetByEmail(user.getEmail());

            System.out.println("User added with id: " + foundedUser.getId());

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("User already exists with email : " + user.getEmail());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void DeleteUser(User user) {
        try {
            int res = this.statement.executeUpdate("DELETE FROM users WHERE id = " + user.getId());
            System.out.println("User deleted with id: " + user.getId());
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    public void UpdateUser(User user) {
        try {
            int res = this.statement.executeUpdate("UPDATE users SET email = '" + user.getEmail() + "', password = '" + user.getPassword() + "' WHERE id = " + user.getId());
            System.out.println("User updated with id: " + user.getId());
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    public void GetAllUsers() {
        try {
            ResultSet resultSet = this.statement.executeQuery("SELECT * FROM users");
            Integer count = 0;

            while (resultSet.next()) {
                System.out.println("User id: " + resultSet.getInt("id") + " | Email: " + resultSet.getString("email") + " | Password: " + resultSet.getString("password").replaceAll(".", "*") + " | Created by: " + resultSet.getInt("createdBy") + " | Created at: " + resultSet.getString("createdAt") + " | Updated at: " + resultSet.getString("updatedAt"));
                count++;
            }

            System.out.println("List of users retrieved successfully with " + count + " users.");
            System.out.println("\n");
        } catch (SQLException e) {
            System.out.println("Error retrieving users: " + e.getMessage());
        }
    }
}
