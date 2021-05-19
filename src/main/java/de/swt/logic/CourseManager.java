package de.swt.logic;

import de.swt.database.AsyncMySQL;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class CourseManager {

    private final AsyncMySQL mySQL;
    private HashMap<Integer, Course> courseHashMap;

    public CourseManager(AsyncMySQL mySQL) {

        this.mySQL = mySQL;
        this.courseHashMap = new HashMap<>();
    }

    public void cacheAllCourseData() {
        AtomicInteger i = new AtomicInteger(); // bruh moment -> It basically waits until loop is done lol
        mySQL.query("SELECT * FROM courses;", resultSet -> i.set(queryUserData(resultSet)));
        while(i.get() != 1);
    }

    public ArrayList<Date> getDateFromString(String string) {
        String[] stringDates = string.split(";");
        ArrayList<Date> list = new ArrayList<>();
        for (String singleDate : stringDates) {
            list.add(new Date(Long.parseLong(singleDate)));
        }
        return list;
    }

    public int queryUserData(ResultSet resultSet) {
        try {
            while (resultSet.next()) {
                Course course = new Course();
                course.setDates(getDateFromString(resultSet.getString("date")));
                course.setGrade(resultSet.getInt("grade"));
                course.setId(resultSet.getInt("courseid"));
                course.setName(resultSet.getString("gradename"));
                //redo
                course.setTeacher(new User(10, "Teacher1" , "Teacher2"));
                // course.setStudents(); load in students and teacher based on their courses in db
                // course.setTeacher();
                courseHashMap.put(course.getId(), course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

}
