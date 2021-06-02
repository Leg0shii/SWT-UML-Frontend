package de.swt.logic.course;

import de.swt.Server;
import de.swt.database.AsyncMySQL;
import de.swt.logic.user.User;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Getter
public class CourseManager {

    private final AsyncMySQL mySQL;
    private final HashMap<Integer, Course> courseHashMap;
    private final Server server;

    public CourseManager(Server server) {
        this.server = server;
        this.mySQL = server.mySQL;
        this.courseHashMap = new HashMap<>();
    }

    public Course loadCourse(int id) {
    }

    public void cacheAllCourseData() {
        courseHashMap.clear();
        ResultSet resultSet = mySQL.query("SELECT courseid FROM courses;");
        try {
            while (resultSet.next()) {
                loadCourse(resultSet.getInt("courseid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


