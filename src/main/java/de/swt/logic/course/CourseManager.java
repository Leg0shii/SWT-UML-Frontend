package de.swt.logic.course;

import de.swt.Server;
import de.swt.manager.Manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class CourseManager extends Manager {

    public CourseManager(Server server) {
        super(server);
    }

    @Override
    public Object load(int id) throws SQLException {
        if (getHashMap().containsKey(id)) {
            return getHashMap().get(id);
        } else {
            ResultSet resultSet = getMySQL().query("SELECT * FROM courses WHERE courseId = " + id + ");");
            Course newCourse = new Course();
            newCourse.setCourseId(id);
            newCourse.setGrade(resultSet.getInt("grade"));
            newCourse.setGradeName(resultSet.getString("gradeName"));
            resultSet = getMySQL().query("SELECT * FROM dateInCourse WHERE courseId = " + id + ";");
            newCourse.setDates(getDates(resultSet));
            resultSet = getMySQL().query("SELECT * FROM ");
            newCourse.setTeacherId();

            getHashMap().put(id, newCourse);
        }
        return null;
    }

    @Override
    public void cacheAllData() throws SQLException {
        getHashMap().clear();
        ResultSet resultSet = getMySQL().query("SELECT courseId FROM courses;");
        while (resultSet.next()) {
            load(resultSet.getInt("courseId"));
        }
    }

    private ArrayList<Date> getDates(ResultSet resultSet) throws SQLException {
        ArrayList<Date> dates = new ArrayList<>();
        while (resultSet.next()){
            Date date = new Date(resultSet.getLong("date"));
            dates.add(date);
        }
        return dates;
    }
}


