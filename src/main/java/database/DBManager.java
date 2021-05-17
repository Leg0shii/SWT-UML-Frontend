package database;

import java.sql.SQLException;

public class DBManager {

    private AsyncMySQL mySQL;

    private void connectToDB() {

        try {
            this.mySQL = new AsyncMySQL("5.196.174.213", 3306, "root", "qexGGHZfFzWyKYE", "serverpro_db");
            System.out.println("Successfully connected to database!");
        } catch (SQLException | ClassNotFoundException throwables) { throwables.printStackTrace(); }

    }

    public AsyncMySQL initTables() {

        connectToDB();

        //create table for playerdata
        mySQL.update("CREATE TABLE IF NOT EXISTS users (userid INT AUTO_INCREMENT" +
            ", prename VARCHAR(255), surname VARCHAR(255), usertype VARCHAR(255), mcourseid INT, scourseid INT, PRIMARY KEY(userid));");
        //create table for mapdata
        mySQL.update("CREATE TABLE IF NOT EXISTS courses (courseid INT AUTO_INCREMENT, grade INT, gradename VARCHAR(10), date VARCHAR(255), PRIMARY KEY(courseid));");

        return mySQL;
    }

}
