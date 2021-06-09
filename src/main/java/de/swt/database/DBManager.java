package de.swt.database;

import java.sql.SQLException;

public class DBManager {

    public AsyncMySQL connectToDB() {
        AsyncMySQL mySQL;
        try {
            mySQL = new AsyncMySQL("5.196.174.213", 3306, "root", "qexGGHZfFzWyKYE", "swt-31");
        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
            return null;
        }
        System.out.println("Successfully connected to database!");
        return mySQL;
    }

}
