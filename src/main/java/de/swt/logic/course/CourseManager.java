package de.swt.logic.course;

import de.swt.manager.Manager;
import de.swt.util.Client;
import lombok.Setter;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CourseManager extends Manager<Course> {

    public CourseManager(Client client) {
        super(client);
    }

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

    @Override
    public void cacheAllData() throws SQLException {
        getHashMap().clear();
        var hashMap = new HashMap<Integer, Course>();
        try {
            hashMap = getClient().getServer().getCourses();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        for (Integer key : hashMap.keySet()){
            getHashMap().put(key, hashMap.get(key));
        }
        /*
        getHashMap().clear();
        ResultSet resultSet = getMySQL().query("SELECT courseId FROM courses;");
        while (resultSet.next()){
            load(resultSet.getInt("courseId"));
        }
         */
    }

    private ArrayList<Date> getDates(ResultSet resultSet) throws SQLException {
        ArrayList<Date> dates = new ArrayList<>();
        while (resultSet.next()) {
            Date date = new Date(resultSet.getLong("date"));
            dates.add(date);
        }
        return dates;
    }
}


