package de.swt.util;

import de.swt.Server;
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

        UserManager userManager = Server.getInstance().getUserManager();
        SessionManager sessionManager = Server.getInstance().getSessionManager();
        GroupManager groupManager = Server.getInstance().getGroupManager();
        UserCommandMananger userCommandMananger = Server.getInstance().getUserCommandMananger();
        Server server = Server.getInstance();

        for (Object object : sessionManager.getHashMap().values()) {
            Session session = (Session) object;
            if (session.getRemainingTime() <= System.currentTimeMillis()) {
                for (int userId : session.getUserIds()) {
                    User user = (User) userManager.getHashMap().get(userId);
                    if (user.isActive()) {
                        userCommandMananger.getUserCommandQueue().get(userId).add(
                                new CommandObject("LE:-1", null, null));
                    }
                }
            }
        }

        for (Object object : groupManager.getHashMap().values()) {
            Group group = (Group) object;
            if (group.getTimeTillTermination() <= System.currentTimeMillis()) {
                for (int userId : group.getUserIds()) {
                    User user = (User) userManager.getHashMap().get(userId);
                    if (user.isActive()) {
                        userCommandMananger.getUserCommandQueue().get(userId).add(
                                new CommandObject("LE:" + group.getSessionId(), null, null));
                    }
                }
            }
        }
    }

}
