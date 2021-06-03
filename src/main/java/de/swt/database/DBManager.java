package de.swt.database;

import java.sql.SQLException;

public class DBManager {

    private AsyncMySQL mySQL;

    public AsyncMySQL connectToDB() {

        try {
            this.mySQL = new AsyncMySQL("5.196.174.213", 3306, "root", "qexGGHZfFzWyKYE", "testdb");
            System.out.println("Successfully connected to database!");
        } catch (SQLException | ClassNotFoundException throwables) { throwables.printStackTrace(); }

        return mySQL;

    }

}
