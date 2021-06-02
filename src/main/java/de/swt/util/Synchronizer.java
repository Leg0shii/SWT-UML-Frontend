package de.swt.util;

import de.swt.Server;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.util.TimerTask;

@Getter
@Setter
public class Synchronizer extends TimerTask {
    Server server;

    public Synchronizer(){
        server = Server.getInstance();
    }

    @Override
    public void run() {
        try {
            server.getCourseManager().cacheAllData();
            server.getGroupManager().cacheAllData();
            server.getUserManager().cacheAllData();
            server.getSessionManager().cacheAllData();
        } catch (SQLException exception){
            System.out.println("Server failed to Synchronize with DataBase");
        }
    }

}
