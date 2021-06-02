package de.swt;

import de.swt.database.AsyncMySQL;
import de.swt.database.DBManager;
import de.swt.logic.course.CourseManager;
import de.swt.logic.group.GroupManager;
import de.swt.logic.session.SessionManager;
import de.swt.logic.user.UserManager;
import de.swt.manager.CommandManager;
import de.swt.rmi.InitRMIServer;
import de.swt.util.SGCheck;
import de.swt.util.ServerConn;

import java.io.IOException;
import java.util.Timer;

public class Server {

    public static Server server;
    public ServerConn serverConn;
    public DBManager dbManager;
    public AsyncMySQL mySQL;
    public CommandManager commandManager;
    public CourseManager courseManager;
    public UserManager userManager;
    public GroupManager groupManager;
    public SessionManager sessionManager;

    public static Server getInstance() {
        return server;
    }

    public void onEnable() {

        server = this;

        commandManager = new CommandManager();

        // starts server listening for incoming tcp packets
        /* try {
            serverConn = new ServerConn(commandManager, 50001);
            serverConn.startServer();
            System.out.println("TCP Server started, listening on port 50001");
        } catch (IOException e) { e.printStackTrace(); } */

        // connects to database and loads in tables
        dbManager = new DBManager();
        mySQL = dbManager.initTables();

        courseManager = new CourseManager(server);
        userManager = new UserManager(server);
        groupManager = new GroupManager(server);
        sessionManager = new SessionManager(server);

        InitRMIServer initRMIServer = new InitRMIServer();
        initRMIServer.initRMIServer();

        courseManager.cacheAllCourseData();
        userManager.cacheAllUserData();
        groupManager.cacheAllGroupData();
        sessionManager.cacheAllSessionData();

        // runs it ever minute
        Timer courseTimer = new Timer();
        courseTimer.schedule(new SGCheck(), 1000, 1000);

    }

    public void onDisable() {

    }

}
