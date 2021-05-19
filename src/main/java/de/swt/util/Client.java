package de.swt.util;

import de.swt.database.AsyncMySQL;
import de.swt.database.DBManager;
import de.swt.logic.CourseManager;
import de.swt.logic.User;
import de.swt.logic.UserManager;

public class Client {

    public AsyncMySQL mySQL;
    public User user;
    public DBManager dbManager;
    public static Client instance;

    public CourseManager courseManager;
    public UserManager userManager;

    public void onStart() {

        instance = this;
        dbManager = new DBManager();
        mySQL = dbManager.connectToDB();

        courseManager = new CourseManager(mySQL);
        courseManager.cacheAllCourseData();

        userManager = new UserManager(mySQL);
        userManager.setCourseHashMap(courseManager.getCourseHashMap());
        userManager.cacheAllUserData();

        for(int ids : userManager.getUserHashMap().keySet()) {
            userManager.loadAllUserCourses(ids);
        }

    }

    public void onDisable() {

    }

    public static Client getInstance() {
        return instance;
    }

}
