package de.swt.util;

import de.swt.logic.group.Group;
import de.swt.logic.session.Session;

import java.sql.SQLException;
import java.util.TimerTask;

public class Synchronizer extends TimerTask {

    private final Client client;

    public Synchronizer() {
        client = Client.getInstance();
    }

    @Override
    public void run() {
        try {
            client.getCourseManager().cacheAllData();
            client.getGroupManager().cacheAllData();
            client.getSessionManager().cacheAllData();
            client.getUserManager().cacheAllData();
            if (!(client.getSessionManager().getHashMap().containsValue(client.getCurrentSession()))) {
                client.setCurrentSession(new Session());
            }
            for (Session session : client.getSessionManager().getHashMap().values()){
                if (session.getUserIds().contains(client.getUserId())){
                    client.setCurrentSession(session);
                }
            }
            if (client.getCurrentGroup() != null) {
                if (!(client.getGroupManager().getHashMap().containsValue(client.getCurrentGroup()))) {
                    client.setCurrentGroup(null);
                }
                for (Group group : client.getGroupManager().getHashMap().values()){
                    if (group.getUserIds().contains(client.getUserId())){
                        client.setCurrentGroup(group);
                    }
                }
            }
            client.getGuiManager().updateGUIS();
        } catch (SQLException exception) {
            System.out.println("Client failed to Synchronize with DataBase");
        }
    }
}
