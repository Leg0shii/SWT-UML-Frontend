package de.swt;

import de.swt.database.AsyncMySQL;
import de.swt.database.DBManager;
import de.swt.logic.course.CourseManager;
import de.swt.logic.group.GroupManager;
import de.swt.logic.user.UserManager;
import de.swt.manager.IPAdressManager;
import de.swt.rmi.RMIServer;
import de.swt.util.ServerConn;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public de.swt.util.ServerConn serverConn;
    public DBManager dbManager;
    public AsyncMySQL mySQL;
    public IPAdressManager ipAdressManager;

    public CourseManager courseManager;
    public UserManager userManager;
    public GroupManager groupManager;

    public static Server server;

    public void onEnable() {

        server = this;

        ipAdressManager = new IPAdressManager();

        // starts server listening for incoming tcp packets
        //serverConn = new ServerConn(ipAdressManager,50000);
        //serverConn.startServer();

        // connects to database and loads in tables
        dbManager = new DBManager();
        mySQL = dbManager.initTables();

        courseManager = new CourseManager(server);
        userManager = new UserManager(server);
        groupManager = new GroupManager(server);

        initRMIServer();

        courseManager.cacheAllCourseData();
        userManager.cacheAllUserData();
        groupManager.cacheAllGroupData();

    }

    public void onDisable() {

    }

    public static Server getInstance() {
        return server;
    }

    private void initRMIServer() {
        // K:\IdeaProjects\swt-31\src\main\java\de\swt
        System.setProperty("java.security.policy","file:./test.policy");
        // System.setProperty("java.rmi.server.hostname","192.168.1.2");
        File f = new File("./");
        System.out.println(f.getAbsolutePath());

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            RMIServer rmiServer = new RMIServer(getInstance());
            LocateRegistry.createRegistry(1099);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("RMIServer", rmiServer);
            System.out.println("Registry provided");
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("RMI ERROR");
        }

    }

}
