package de.swt.server.util;

import de.swt.server.Server;

import java.util.TimerTask;


public class Synchronizer extends TimerTask {

    @Override
    public void run() {
        Server.getInstance().userManager.cacheAllUserData();
        Server.getInstance().courseManager.cacheAllCourseData();
    }

}
