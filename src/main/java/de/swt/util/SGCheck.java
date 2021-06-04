package de.swt.util;

import de.swt.Server;
import de.swt.database.DBManager;
import de.swt.logic.group.Group;
import de.swt.logic.group.GroupManager;
import de.swt.logic.session.Session;
import de.swt.logic.session.SessionManager;
import de.swt.logic.user.User;
import de.swt.logic.user.UserManager;
import de.swt.manager.UserCommandManager;
import de.swt.manager.CommandObject;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.TimerTask;

public class SGCheck extends TimerTask {

    @Override
    public void run() {

        SessionManager sessionManager = Server.getInstance().getSessionManager();
        GroupManager groupManager = Server.getInstance().getGroupManager();
        DBManager dbManager = Server.getInstance().getDbManager();

        ArrayList<Session> sessionsToDelete = new ArrayList<>();
        ArrayList<Session> sessionsCopy = SerializationUtils.clone(new ArrayList<>(sessionManager.getHashMap().values()));
        for (Session session : sessionsCopy) {
            if (session.getRemainingTime() <= System.currentTimeMillis()) {
                sessionsToDelete.add(session);
            }
        }
        for (Session session : sessionsToDelete){
            dbManager.deleteSession(session.getSessionId());
        }

        ArrayList<Group> groupsToDelete = new ArrayList<>();
        ArrayList<Group> groupsCopy = SerializationUtils.clone(new ArrayList<>(groupManager.getHashMap().values()));
        for (Group group : groupsCopy) {
            if (group.getTimeTillTermination() <= System.currentTimeMillis()) {
                groupsToDelete.add(group);dbManager.deleteGroup(group.getGroupId());
            }
        }
        for (Group group: groupsToDelete){
            dbManager.deleteGroup(group.getGroupId());
        }
    }

}
