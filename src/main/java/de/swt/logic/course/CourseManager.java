package de.swt.logic.course;

import de.swt.Server;
import de.swt.manager.Manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class CourseManager extends Manager<Course> {

    public CourseManager(Server server) {
        super(server);
    }

    /**
     * Is used to load in the course object from the database and saves it if not saved into the HashMap.
     * @param id Course id.
     * @return Course object.
     * @throws SQLException Is thrown when there are complications with the database.
     */
    @Override
    public Course load(int id) throws SQLException {
        if (getHashMap().containsKey(id)) {
            return getHashMap().get(id);
        } else {
            ResultSet resultSet = getMySQL().query("SELECT * FROM courses WHERE courseId = " + id + ";");
            resultSet.next();
            Course newCourse = new Course();
            newCourse.setCourseId(id);
            newCourse.setGrade(resultSet.getInt("grade"));
            newCourse.setGradeName(resultSet.getString("gradeName"));
            newCourse.setTeacherId(resultSet.getInt("teacherId"));
            resultSet = getMySQL().query("SELECT date FROM dateInCourse WHERE courseId = " + id + ";");
            newCourse.setDates(getDates(resultSet));
            resultSet = getMySQL().query("SELECT userId FROM userInCourse WHERE courseId = " + id + ";");
            newCourse.setUserIds(getIds(resultSet, "userId"));

            getHashMap().put(id, newCourse);

            return newCourse;
        }
    }

    /**
     * Is used to load in all session objects from the database and to save them into the HashMap.
     * @throws SQLException Is thrown when there are complications with the database.
     */
    @Override
    public void cacheAllData() throws SQLException {
        getHashMap().clear();
        ResultSet resultSet = getMySQL().query("SELECT courseId FROM courses;");
        while (resultSet.next()) {
            load(resultSet.getInt("courseId"));
        }
    }

    /**
     * Is used to get all dates from the result set of the database.
     * @param resultSet Query result from database.
     * @return ArrayList of dates.
     * @throws SQLException Is thrown when there are complications with the database.
     */
    private ArrayList<Date> getDates(ResultSet resultSet) throws SQLException {
        ArrayList<Date> dates = new ArrayList<>();
        while (resultSet.next()) {
            Date date = new Date(resultSet.getLong("date"));
            dates.add(date);
        }
        return dates;
    }
}


