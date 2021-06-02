package de.swt;

import de.swt.database.AsyncMySQL;
import de.swt.database.DBManager;
import de.swt.logic.course.CourseManager;
import de.swt.logic.group.GroupManager;
import de.swt.logic.session.SessionManager;
import de.swt.logic.user.UserManager;
import de.swt.manager.ServerCommandManager;
import de.swt.manager.UserCommandMananger;
import de.swt.manager.CommandObject;
import de.swt.rmi.InitRMIServer;
import de.swt.util.SGCheck;
import de.swt.util.Synchronizer;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Timer;

@Getter
@Setter
public class Server {

    private static Server server;
    private DBManager dbManager;
    private AsyncMySQL mySQL;
    private UserCommandMananger userCommandMananger;
    private ServerCommandManager serverCommandManager;
    private CourseManager courseManager;
    private UserManager userManager;
    private GroupManager groupManager;
    private SessionManager sessionManager;

    public static Server getInstance() {
        return server;
    }

    public void onEnable() {

        server = this;

        userCommandMananger = new UserCommandMananger();
        serverCommandManager = new ServerCommandManager();

        dbManager = new DBManager();
        mySQL = dbManager.initTables();

        courseManager = new CourseManager(server);
        userManager = new UserManager(server);
        groupManager = new GroupManager(server);
        sessionManager = new SessionManager(server);

        InitRMIServer initRMIServer = new InitRMIServer();
        initRMIServer.initRMIServer();

        new Thread(() -> {
            Timer timer = new Timer();
            timer.schedule(new Synchronizer(), 0, 1000);
        }).start();

        new Thread(()->{
            Timer courseTimer = new Timer();
            courseTimer.schedule(new SGCheck(), 1000, 1000);
        }).start();

    }

    public void onDisable() {

    }

}
