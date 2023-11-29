package fr.piarre.Managers;

import fr.piarre.Models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    public boolean userExists(User user) {

        return true;
    }

    public Statement connect() {
        String url = "jdbc:mysql://localhost:3306/lemoine";
        String username = "root";
        String passwd = "IDP34+1";

        try {
            Connection conn = DriverManager.getConnection(url, username, passwd);

            return conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
