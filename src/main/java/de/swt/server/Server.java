package de.swt.server;

import de.swt.server.database.AsyncMySQL;
import de.swt.server.database.DBManager;
import de.swt.server.logic.course.CourseManager;
import de.swt.server.logic.group.GroupManager;
import de.swt.server.logic.user.UserManager;
import de.swt.server.manager.IPAdressManager;
import de.swt.server.rmi.RMIServer;
import de.swt.server.util.ServerConn;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public ServerConn serverConn;
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
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("RMIServer", rmiServer);
            System.out.println("Registry provided");
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("RMI ERROR");
        }

    }

}
