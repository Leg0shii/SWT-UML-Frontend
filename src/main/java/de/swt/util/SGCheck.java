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

        for (Session session : sessionManager.getSessionHashMap().values()) {
            if (session.getRemainingTime() <= System.currentTimeMillis()) {
                for (int userid : session.getParticipants()) {
                    // update user object further ?
                    if (userManager.getUserHashMap().get(userid).isOnline()) {
                        commandManager.getCommandHashMap().get(userid).add(
                                new CommandObject("LE:-1", null, null));
                    }
                }
            }
        }

        for (Group group : groupManager.getGroupHashMap().values()) {
            if (group.getTimeTillTermination() <= System.currentTimeMillis()) {
                for (int userid : group.getParticipants()) {
                    // update user object further ?
                    if (userManager.getUserHashMap().get(userid).isOnline()) {
                        commandManager.getCommandHashMap().get(userid).add(
                                new CommandObject("LE:" + group.getCourseID(), null, null));
                    }
                }
            }
        }
    }

}
