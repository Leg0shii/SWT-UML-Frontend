package de.swt.util;

import de.swt.Server;
import de.swt.logic.group.Group;
import de.swt.logic.group.GroupManager;
import de.swt.logic.session.Session;
import de.swt.logic.session.SessionManager;
import de.swt.logic.user.User;
import de.swt.logic.user.UserManager;
import de.swt.manager.CommandManager;
import de.swt.manager.CommandObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TimerTask;

public class SGCheck extends TimerTask {

    @Override
    public void run() {

        UserManager userManager = Server.getInstance().userManager;
        SessionManager sessionManager = Server.getInstance().sessionManager;
        GroupManager groupManager = Server.getInstance().groupManager;
        CommandManager commandManager = Server.getInstance().commandManager;
        Server server = Server.getInstance();

        ArrayList<Integer> sessionSavedUserId = new ArrayList<>();
        ArrayList<Integer> groupSavedUserId = new ArrayList<>();

        for (Session session : sessionManager.getSessionHashMap().values()) {
            if (session.getRemainingTime() <= System.currentTimeMillis()) {
                // update user object further ?
                sessionSavedUserId.addAll(session.getParticipants());
                server.dbManager.deleteSession(session.getId());
                System.out.println("DELETING SESSION " + session.getId() + " BECAUSE TIME");
                sessionManager.cacheAllSessionData();
            }
        }

        for (Group group : groupManager.getGroupHashMap().values()) {
            if (group.getTimeTillTermination() <= System.currentTimeMillis()) {
                Session session = new Session();
                try {
                    session = sessionManager.loadSession(group.getCourseID());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                // update user object further ?
                groupSavedUserId.addAll(session.getParticipants());
                server.dbManager.deleteGroup(group.getId());
                System.out.println("DELETING GROUP " + group.getId() + " BECAUSE TIME");
                groupManager.cacheAllGroupData();
            }
        }

        for (int id : sessionSavedUserId) {
            commandManager.getCommandHashMap().get(id).add(
                    new CommandObject("LE:-1", null, null));
        }

        for (int id : groupSavedUserId) {
            commandManager.getCommandHashMap().get(id).add(
                    new CommandObject("LE:0", null, null));
        }
    }

}
