package de.swt.logic.course;

import de.swt.database.AsyncMySQL;
import de.swt.util.Client;
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
    private final Client client;

    public CourseManager(Client client) {

        this.client = client;
        this.mySQL = client.mySQL;
        this.courseHashMap = new HashMap<>();
    }

    public Course loadCourse(int id) throws SQLException {
        Course course;
        if (!courseHashMap.containsKey(id)) {
            ResultSet resultSet = mySQL.query("SELECT * FROM courses WHERE courseid = " + id + ";");
            if (resultSet.next()) {
                course = new Course();
                course.setDates(getDateFromString(resultSet.getString("date")));
                course.setGrade(resultSet.getInt("grade"));
                course.setId(resultSet.getInt("courseid"));
                course.setName(resultSet.getString("gradename"));
                course.setTeacherID(resultSet.getInt("teacherid"));
                courseHashMap.put(course.getId(), course);
            } else {
                System.out.println("SOMETHING WENT WRONG WHILE LOADING COURSE!!!");
                return null;
            }
        } else course = courseHashMap.get(id);
        return course;
    }

    public void cacheAllCourseData() {
        this.courseHashMap.clear();
        ResultSet resultSet = mySQL.query("SELECT courseid FROM courses;");
        try {
            while (resultSet.next()) {
                loadCourse(resultSet.getInt("courseid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Date> getDateFromString(String string) {
        String[] stringDates = string.split(";");
        ArrayList<Date> list = new ArrayList<>();
        for (String singleDate : stringDates) {
            if (!singleDate.equals("")) {
                list.add(new Date(Long.parseLong(singleDate)));
            }
        }
        return list;
    }

}
