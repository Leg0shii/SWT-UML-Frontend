package de.swt.util;

import de.swt.Server;
import de.swt.database.DBManager;
import de.swt.logic.group.Group;
import de.swt.logic.group.GroupManager;
import de.swt.logic.session.Session;
import de.swt.logic.session.SessionManager;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.TimerTask;

public class SGCheck extends TimerTask {

    /**
     * Checks if there are session-times or group-times that are about
     * to run out. If that's the case it will delete them.
     */
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
        ArrayList<Group> groupsToDelete = new ArrayList<>();
        ArrayList<Group> groupsCopy = SerializationUtils.clone(new ArrayList<>(groupManager.getHashMap().values()));
        for (Group group : groupsCopy) {
            if (group.getTimeTillTermination() <= System.currentTimeMillis()) {
                groupsToDelete.add(group);
                dbManager.deleteGroup(group.getGroupId());
            }
        }
        for (Group group: groupsToDelete){
            dbManager.deleteGroup(group.getGroupId());
        }
        for (Session session : sessionsToDelete){
            dbManager.deleteSession(session.getSessionId());
        }
    }

}
