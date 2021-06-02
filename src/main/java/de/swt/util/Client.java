package de.swt.util;

import de.swt.database.AsyncMySQL;
import de.swt.database.DBManager;
import de.swt.gui.GUIManager;
import de.swt.logic.course.CourseManager;
import de.swt.logic.group.Group;
import de.swt.logic.group.GroupManager;
import de.swt.logic.session.Session;
import de.swt.logic.session.SessionManager;
import de.swt.logic.user.User;
import de.swt.logic.user.UserManager;
import de.swt.rmi.RMIClient;
import de.swt.rmi.RMIServerInterface;
import lombok.Getter;
import lombok.Setter;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Timer;

@Setter
@Getter
public class Client {

    private static Client instance;
    private boolean loggedIn;

    private AsyncMySQL mySQL;
    private int userId;
    private DBManager dbManager;

    private CourseManager courseManager;
    private UserManager userManager;
    private GroupManager groupManager;
    private SessionManager sessionManager;
    private RMIServerInterface server;
    private GUIManager guiManager;

    private Session currentSession;
    private Group currentGroup;

    public void onStart() {

        instance = this;
        loggedIn = false;
        dbManager = new DBManager();
        mySQL = dbManager.connectToDB();

        RMIClient rmiClient = new RMIClient();
        server = rmiClient.initRMIClient();

        courseManager = new CourseManager(instance);
        userManager = new UserManager(instance);
        groupManager = new GroupManager(instance);
        sessionManager = new SessionManager(instance);

        new Thread(() -> {
            Timer timer = new Timer();
            timer.schedule(new Synchronizer(), 0, 1000);
        }).start();

        new Thread(() -> {
            Timer timer = new Timer();
            timer.schedule(new ReadCommandList(), 1000, 10);
        }).start();

        guiManager.getLoginGUI().loginButton.setEnabled(true);
        guiManager.getLoginGUI().loginButton.setBackground(guiManager.getLoginGUI().loginButton.getBackground().brighter());
    }

    public void onDisable() {
        try {
            User user = userManager.load(userId);
            user.setActive(false);
            server.updateUser(user);
            if (currentSession != null) {
                if (user.getAccountType().equals(AccountType.TEACHER)) {
                    currentSession.getMasterIds().remove(userId);
                } else {
                    currentSession.getUserIds().remove(userId);
                }
                server.updateSession(currentSession);
            }
        } catch (SQLException | RemoteException exception) {
            exception.printStackTrace();
        }
    }

    public static Client getInstance() {
        return instance;
    }

}
