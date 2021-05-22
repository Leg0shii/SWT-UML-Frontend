package de.swt.database;

import de.swt.logic.course.Course;
import de.swt.logic.group.Group;
import de.swt.logic.user.User;
import de.swt.util.AccountType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class DBManager {

    private AsyncMySQL mySQL;

    private void connectToDB() {

        try {
            // usually should be imported from config file but bruh nah
            this.mySQL = new AsyncMySQL("5.196.174.213", 3306, "root", "qexGGHZfFzWyKYE", "serverpro_db");
            System.out.println("Successfully connected to database!");
        } catch (SQLException | ClassNotFoundException throwables) { throwables.printStackTrace(); }

    }

    public AsyncMySQL initTables() {

        connectToDB();

        // create table for userdata
        // decided to create two fields for courses instead of creating another table and saving all users that would be in a course in there
        mySQL.update("CREATE TABLE IF NOT EXISTS users (userid INT AUTO_INCREMENT" +
            ", prename VARCHAR(255), surname VARCHAR(255), usertype VARCHAR(255), mcourseid INT, scourseid INT, upassword VARCHAR(255), online BOOL, PRIMARY KEY(userid));");

        // create table for courseata
        // date format: STRING: "millis;millis;millis"
        mySQL.update("CREATE TABLE IF NOT EXISTS courses (courseid INT AUTO_INCREMENT, grade INT, gradename VARCHAR(10), date VARCHAR(500), teacherid INT, PRIMARY KEY(courseid));");

        //create table for groupdata
        // participants format: STRING: "id;id;id;id;id"
        mySQL.update("CREATE TABLE IF NOT EXISTS groups (groupid INT AUTO_INCREMENT, courseid INT, ttt INT, maxGS INT, participants VARCHAR(255), PRIMARY KEY(groupid));");

        return mySQL;
    }

    public void updateUser(User user) {

        int id = user.getId();
        AccountType accountType = user.getAccountType();
        String firstname = user.getFirstname();
        String surname = user.getSurname();
        int[] course = user.getCourse();
        int online = user.isOnline() ? 1 : 0;

        mySQL.update("UPDATE users SET usertype = '" + accountType.toString() +
            "',prename='" + firstname + "',surname='" + surname + "',mcourseid=" + course[0] + ",scourseid" +
            course[1] + ",online=" + online + " WHERE userid = " + id + ";");
    }

    public void updateCourse(Course course) {

        int id = course.getId();
        int grade = course.getGrade();
        String name = course.getName();
        String dates = datesToString(course.getDates());
        User teacher = course.getTeacher();

        mySQL.update("UPDATE courses SET grade = " + grade + ",gradename='" + name + "',date='" + dates
            + "',teacheri=" + teacher.getId() + " WHERE courseid = " + id + ";");
    }

    public void updateGroups(Group group) {

        int groupid = group.getGroupid();
        int courseid = group.getCourseid();
        int timeTillTermination = group.getTimeTillTermination();
        int maxGroupSize = group.getMaxGroupSize();
        String participants = participantsToString(group.getParticipants());

        mySQL.update("UPDATE groups SET courseid = " + courseid + ",ttt=" + timeTillTermination
            + ",maxGS=" + maxGroupSize + ",participants='" + participants + "' WHERE groupid = " + group + ";");
    }

    private String participantsToString(ArrayList<Integer> part) {
        String participants = "";
        for (int p : part) {
            participants = p + ";" + participants;
        }
        return participants;
    }

    private String datesToString(ArrayList<Date> dates) {
        String stringOfDates = "";
        for(Date date : dates) {
            stringOfDates = date.getTime() + ";" + stringOfDates;
        }
        return stringOfDates;
    }

}

