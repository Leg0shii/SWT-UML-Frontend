package de.swt.util;

import de.swt.database.AsyncMySQL;
import de.swt.database.DBManager;
import de.swt.logic.CourseManager;
import de.swt.logic.User;
import de.swt.logic.UserManager;
import de.swt.rmi.RMIClient;
import de.swt.rmi.RMIServerInterface;

public class Client {

    public AsyncMySQL mySQL;
    public int userid;
    public DBManager dbManager;
    public static Client instance;

    public CourseManager courseManager;
    public UserManager userManager;
    public RMIServerInterface server;

    public void onStart() {

        instance = this;
        dbManager = new DBManager();
        mySQL = dbManager.connectToDB();

        RMIClient rmiClient = new RMIClient();
        //server = rmiClient.initRMIClient();

        courseManager = new CourseManager(instance);
        userManager = new UserManager(instance);

        loadAllInformation();
    }

    public void onDisable() {

    }

    public static Client getInstance() {
        return instance;
    }

    public void loadAllInformation() {
        courseManager.cacheAllCourseData();
        userManager.cacheAllUserData();
    }

}
