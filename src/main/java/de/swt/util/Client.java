package de.swt.util;

import de.swt.database.AsyncMySQL;
import de.swt.database.DBManager;
import de.swt.logic.User;

public class Client {

    public AsyncMySQL mySQL;
    public User user;
    public DBManager dbManager;
    public static Client instance;

    public void onStart() {

        instance = this;
        dbManager = new DBManager();
        mySQL = dbManager.connectToDB();

    }

    public void onDisable() {

    }

    public static Client getInstance() {
        return instance;
    }

}
