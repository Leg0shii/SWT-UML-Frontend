package de.swt.client.util;

import de.swt.client.database.AsyncMySQL;
import de.swt.client.database.DBManager;
import de.swt.client.clientlogic.CourseManager;
import de.swt.client.clientlogic.UserManager;
import de.swt.client.clientrmi.RMIClient;
import de.swt.client.clientrmi.RMIServerInterface;

import java.io.IOException;
import java.rmi.RemoteException;

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

        try {
            ServerConn serverConn = new ServerConn(instance, 50000);
            serverConn.startServer();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        RMIClient rmiClient = new RMIClient();
        server = rmiClient.initRMIClient();

        courseManager = new CourseManager(instance);
        userManager = new UserManager(instance);

        try { System.out.println(server.ping(17));
        } catch (RemoteException ignored) { }

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
