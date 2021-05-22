package de.swt.client.database;

import java.sql.SQLException;

public class DBManager {

    private AsyncMySQL mySQL;

    public AsyncMySQL connectToDB() {

        try {
            // usually should be imported from config file but bruh nah
            this.mySQL = new AsyncMySQL("5.196.174.213", 3306, "root", "qexGGHZfFzWyKYE", "serverpro_db");
            System.out.println("Successfully connected to database!");
        } catch (SQLException | ClassNotFoundException throwables) { throwables.printStackTrace(); }

        return mySQL;

    }

}
