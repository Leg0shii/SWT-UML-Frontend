package de.swt.util;

import de.swt.database.AsyncMySQL;
import de.swt.database.DBManager;
import de.swt.logic.Course;
import de.swt.logic.CourseManager;
import de.swt.logic.User;
import de.swt.logic.UserManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Client {

    public AsyncMySQL mySQL;
    public User user;
    public DBManager dbManager;
    public static Client instance;

    public CourseManager courseManager;
    public UserManager userManager;

    public void onStart() throws SQLException {

        instance = this;
        dbManager = new DBManager();
        mySQL = dbManager.connectToDB();

        courseManager = new CourseManager(instance);
        userManager = new UserManager(instance);

        loadAllInformation();
    }

    public void onDisable() {

    }

    public static Client getInstance() {
        return instance;
    }

    public void loadAllInformation() throws SQLException {
        courseManager.cacheAllCourseData();
        userManager.cacheAllUserData();
        for(int key : userManager.getUserHashMap().keySet()) {
            userManager.getUserHashMap().get(key).setCourse(userManager.loadCourses(key));
        }
    }

}
