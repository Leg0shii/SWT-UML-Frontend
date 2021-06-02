package de.swt.util;

import de.swt.Server;
import de.swt.database.DBManager;
import de.swt.logic.group.Group;
import de.swt.logic.group.GroupManager;
import de.swt.logic.session.Session;
import de.swt.logic.session.SessionManager;
import de.swt.logic.user.User;
import de.swt.logic.user.UserManager;
import de.swt.manager.UserCommandMananger;
import de.swt.manager.CommandObject;

import java.util.TimerTask;

public class SGCheck extends TimerTask {

    @Override
    public void run() {

        SessionManager sessionManager = Server.getInstance().getSessionManager();
        GroupManager groupManager = Server.getInstance().getGroupManager();
        DBManager dbManager = Server.getInstance().getDbManager();

        for (Session session : sessionManager.getHashMap().values()) {
            if (session.getRemainingTime() <= System.currentTimeMillis()) {
                dbManager.deleteSession(session.getSessionId());
            }
        }

        for (Group group : groupManager.getHashMap().values()) {
            if (group.getTimeTillTermination() <= System.currentTimeMillis()) {
                dbManager.deleteGroup(group.getGroupId());
            }
        }
    }

}
