package de.swt.util;

import de.swt.Server;

import java.util.TimerTask;


public class Synchronizer extends TimerTask {

    @Override
    public void run() {
        Server.getInstance().userManager.cacheAllUserData();
        Server.getInstance().courseManager.cacheAllCourseData();
    }

}
