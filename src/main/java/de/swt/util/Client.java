package de.swt.util;

import de.swt.database.AsyncMySQL;
import de.swt.database.DBManager;
import de.swt.gui.GUIManager;
import de.swt.logic.course.CourseManager;
import de.swt.logic.group.GroupManager;
import de.swt.logic.session.SessionManager;
import de.swt.logic.user.UserManager;
import de.swt.rmi.RMIClient;
import de.swt.rmi.RMIServerInterface;

import java.io.IOException;
import java.util.Timer;

public class Client {

    public static Client instance;
    public boolean loggedIn;

    public AsyncMySQL mySQL;
    public int userid;
    public DBManager dbManager;

    public CourseManager courseManager;
    public UserManager userManager;
    public GroupManager groupManager;
    public SessionManager sessionManager;
    public RMIServerInterface server;
    private ServerConn serverConn;
    public GUIManager guiManager;

    public void onStart() {

        instance = this;
        loggedIn = false;
        dbManager = new DBManager();
        mySQL = dbManager.connectToDB();

        try {
            serverConn = new ServerConn(instance, 50000);
            serverConn.startServer();
            System.out.println("TCP Server started, listening on port 50000");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        RMIClient rmiClient = new RMIClient();
        server = rmiClient.initRMIClient();

        Timer commandGetter = new Timer();
        commandGetter.schedule(new ReadCommandList(instance), 1000, 5000);

        courseManager = new CourseManager(instance);
        userManager = new UserManager(instance);
        groupManager = new GroupManager(instance);
        sessionManager = new SessionManager(instance);

        loadAllInformation();
    }

    public void onDisable() {
        try {
            serverConn.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Client getInstance() {
        return instance;
    }

    public void loadAllInformation() {
        courseManager.cacheAllCourseData();
        userManager.cacheAllUserData();
        sessionManager.cacheAllSessionData();
        groupManager.cacheAllGroupData();
    }

}
