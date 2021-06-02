package de.swt.util;

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
            if (client.getCurrentSession() != null) {
                if (!(client.getSessionManager().getHashMap().containsValue(client.getCurrentSession()))){
                    client.setCurrentSession(null);
                }
            }
            if (client.getCurrentGroup() != null){
                if (!(client.getGroupManager().getHashMap().containsValue(client.getCurrentGroup()))){
                    client.setCurrentGroup(null);
                }
            }
            client.getGuiManager().updateGUIS();
        } catch (SQLException exception) {
            System.out.println("Client failed to Synchronize with DataBase");
        }
    }
}
